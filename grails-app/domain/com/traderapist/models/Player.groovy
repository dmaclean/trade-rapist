package com.traderapist.models

import com.traderapist.scoringsystem.IFantasyScoringSystem
import com.traderapist.scoringsystem.ESPNStandardScoringSystem

class Player {
    static hasMany = [stats: Stat, fantasyPoints: FantasyPoints]

    String name
    String position

    static constraints = {
        name blank:false
        position blank:false
    }

    static mapping = {
        cache true
        stats cache: 'read-only'
        table "players"
        version false
    }

    def computeFantasyPoints(IFantasyScoringSystem scoringSystem) {
        def points = [:]

        long start = System.currentTimeMillis();
        for(s in stats) {
            def seasonStr = String.valueOf(s.season)
            def weekStr = String.valueOf(s.week)
            def key = "${seasonStr}__${weekStr}"

            if(!points[key]) {
                points[key] = 0.0
            }
            points[key] += scoringSystem.calculateScore([s])
        }
        long end = System.currentTimeMillis();
        println("Distributed stats to season and week for ${name} in ${(end-start)/1000.0}")

        for(p in points) {
            String[] keyPieces = p.key.split("__")
            double pts = p.value

            FantasyPoints fp = FantasyPoints.findByPlayerAndSeasonAndWeekAndSystemAndPoints(
                    this,
                    keyPieces[0].toInteger(),
                    keyPieces[1].toInteger(),
                    scoringSystem.class.getName(),
                    pts)

            if (!fp) {
                fp = new FantasyPoints(player: this, season: keyPieces[0], week: keyPieces[1], system: scoringSystem.class.getName(), points:  p.value)
            }
            fp.save(flush: true)
        }
    }

    /**
     * Runs a query to fetch all players of a certain position for a certain season and sort
     * them by fantasy points scored (highest to lowest).
     *
     * @param position      The player position we're interested in.
     * @param season        The season to analyze
     * @return              A list of arrays, with each array containing [<Player object>, <FantasyPoints object>]
     */
    static def getDropoffData(String position, int season) {
        def results = Player.executeQuery("from Player p inner join p.fantasyPoints f " +
                "where p.position = ? and f.season = ? and f.week = -1 order by f.points desc", [position, season])

        return results
    }
}

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
     * them by fantasy points scored (highest to lowest).  The players are then broken up into
     * tiers, with separation of tiers determined by any two consecutive players separated
     * by 16 or more points.
     *
     * @param position      The player position we're interested in.
     * @param season        The season to analyze
     * @return              A list of arrays, with each array containing [<Player object>, <FantasyPoints object>]
     */
    static def getDropoffData(String position, int season) {
        def results = Player.executeQuery("from Player p inner join p.fantasyPoints f " +
                "where p.position = ? and f.season = ? and f.week = -1 order by f.points desc", [position, season])

        // Separate into tiers.
        def maxDiff = 16
        def tiers = []

        def currTier = null
        def r_prev = null
        for(r in results) {
            // First player in a new tier
            if (!currTier) {
                currTier = [r]
            }
            // Found two players separated by 16 or more points.  Add the
            // current tier to the list of tiers and put our current player
            // into a new tier.
            else if (Math.abs(r_prev[1].points - r[1].points) >= maxDiff) {
                tiers << currTier
                currTier = [r]
            }
            // Current player belongs in current tier.
            else {
                currTier << r
            }

            r_prev = r
        }

        // Current tier has players in it.  Add it to the list.
        if (currTier.size() > 0) {
            tiers << currTier
        }

        return tiers
    }

    static def calculateStandardDeviation(results) {
        // Calculate mean
        def mean = 0.0
        for(r in results) {
            mean += r[1].points
        }
        mean /= results.size()

        // Diff from mean, squared
        def diff_squared = []
        for(r in results) {
            diff_squared << (r[1].points - mean)**2
        }

        return Math.sqrt(diff_squared.sum()/results.size())
    }
}

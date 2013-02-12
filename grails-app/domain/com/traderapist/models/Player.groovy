package com.traderapist.models

import com.traderapist.scoringsystem.IFantasyScoringSystem

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

    /**
     * Retrieve the years that we have stats for this player.
     *
     * @return      An array of years.
     */
    def getStatYears() {
        return Stat.executeQuery("select distinct season from Stat s where s.player = ?", [this])
    }

    /**
     * Calculates the average of a player's scores for the specified season.
     *
     * @param season    The season to calculate average points for.
     * @return          The average number of points scored, or zero if there is no data.
     */
    def getScoringAverageForSeason(season) {
        def result = Player.executeQuery("select avg(f.points) from Player p inner join p.fantasyPoints f where p = ? and f.season = ?", [this, season])
        return (result[0] == null) ? 0 : result[0]
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
        def tiers = []

        // Calculate maximum difference between players i and i+1 that would keep them in the same tier.
        // This difference is currently two standard deviations from the average difference between players.
        def diffs = []
        for(int i=0; i<results.size()-1; i++) {
            diffs.add(Math.abs(results[i][1].points - results[i+1][1].points))
        }
        def maxDiff = Player.calculateStandardDeviation(diffs) * 2

        def currTier = []
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

    /**
     * Calculates the standard deviation of points scored each week over
     * the course of the specified season.
     *
     * @param season        The season to evaluate.
     * @return              The standard deviation, or -1 if season is invalid.
     */
    def calculatePointsStandardDeviation(season) {
        /*
        Make sure that the user has specified a valid year for this player.
         */
        if(!Stat.getStatYears().contains(season)) {
            return -1
        }

        def fantasyPoints = FantasyPoints.findAllByPlayerAndSeason(this, season)

        def points = []
        for(fp in fantasyPoints) {
            if (fp.week != -1) {
                points << fp.points
            }
        }

        return calculateStandardDeviation(points)
    }

    def calculateProjectedPoints(year) {
        def statYears = Stat.getStatYears()

        /*
        Make sure the year we want to project is supported with stats from the previous year.

        i.e. year = 2002, we need stats from 2001.
         */
        if (!statYears.contains(year-1))
            throw new Exception("Invalid year.")
    }

    static def getCorrelation(position, stat, IFantasyScoringSystem system, season1, season2) {
        // Returns all players at a position that have FantasyPoint records for both seasons.
        def results
        if (stat == null) {
            results = Player.executeQuery("from Player p inner join p.fantasyPoints f1 with f1.season = ? and f1.week = -1 " +
                    "inner join p.fantasyPoints f2 with f2.season = ? and f2.week = -1 where p.position = ?", [season1, season2, position])
        }
        else {
            results = Player.executeQuery("from Player p inner join p.stats s1 with s1.season = ? and s1.week = -1 and s1.statKey = ? " +
                    "inner join p.stats s2 with s2.season = ? and s2.week = -1 and s2.statKey = ? " +
                    "where p.position = ?", [season1, stat, season2, stat, position])
        }

        def x = 0
        def y = 0
        def xy = 0
        def x2 = 0
        def y2 = 0

        /*
        Iterate through the results to calculate xy, x**2, and y**2
         */
        for(r in results) {
            def points1, points2

            if (stat == null) {
                points1 = r[1].points
                points2 = r[2].points
            }
            else {
                points1 = system.calculateScore([r[1]])
                points2 = system.calculateScore([r[2]])
            }

            x += points1
            y += points2
            xy += points1 * points2
            x2 += points1**2
            y2 += points2**2
        }

        def correlation = (results.size()*xy - x*y)/
                Math.sqrt(Double.valueOf(results.size()*x2 - x**2)*Double.valueOf(results.size()*y2 - y**2))

        return correlation
    }

    static def calculateStandardDeviation(values) {
        // Calculate mean
        def mean = values.sum()/values.size()

        // Diff from mean, squared
        def diff_squared = []
        for(v in values) {
            diff_squared << (v - mean)**2
        }

        return Math.sqrt(diff_squared.sum()/values.size())
    }
}

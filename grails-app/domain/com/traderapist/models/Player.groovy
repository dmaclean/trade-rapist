package com.traderapist.models

import com.traderapist.scoringsystem.IFantasyScoringSystem
import com.traderapist.scoringsystem.ESPNStandardScoringSystem

class Player {
    static hasMany = [stats: Stat]

    static transients = ['seasonStats', 'seasonStatPoints', 'weeklyStats', 'weeklyStatPoints']

    String name
    String position
    Map seasonStats = [:]
    Map seasonStatPoints = [:]
    Map weeklyStats = [:]
    Map weeklyStatPoints = [:]

    static constraints = {
        name blank:false
        position blank:false
    }

    static mapping = {
        table "players"
        version false
    }

    /**
     * Determine how many points have been scored for each season and each week.
     *
     * @param system		The fantasy scoring system to use when calculating points.
     */
    def calculatePoints(IFantasyScoringSystem system) {
        /*
         * Season stats
         */
        for(s in seasonStats) {
            seasonStatPoints[s.key] = system.calculateScore(s.value)
        }

        /*
         * Week stats
         */
        for(s in weeklyStats) {
            for(s2 in weeklyStats[s.key]) {
                weeklyStatPoints[s.key] = [(s2.key): system.calculateScore(s2.value)]
            }
        }
    }

    def afterLoad() {
        for(s in stats) {
            def seasonStr = String.valueOf(s.season)
            def weekStr = String.valueOf(s.week)

            // Stat is season
            if(s.week == -1) {
                if(!seasonStats[seasonStr]) {
                    seasonStats[seasonStr] = []
                }
                seasonStats[seasonStr] << s
            }
            //Stat is week
            else {
                // Season entry doesn't exist yet --> create map for it
                if(!weeklyStats[seasonStr]) {
                    weeklyStats[seasonStr] = [weekStr: [s]]
                }
                // Season exists, but week doesn't --> create map for it
                else if(!weeklyStats[seasonStr][weekStr]) {
                    weeklyStats[seasonStr][weekStr] = [s]
                }
                // Both already exist, just append the stat
                else {
                    weeklyStats[seasonStr][weekStr] << s
                }
            }
        }

        calculatePoints(new ESPNStandardScoringSystem())
    }
}

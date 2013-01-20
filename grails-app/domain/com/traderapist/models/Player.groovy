package com.traderapist.models

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
    }
}

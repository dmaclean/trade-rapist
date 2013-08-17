package com.traderapist.models

import com.traderapist.constants.FantasyConstants

class Stat {
    static belongsTo = [player: Player]

    Integer season
    Integer week
    Integer statKey
    Integer statValue
    double points

    static transients = ['points']

    static constraints = {
        season nullable:false
        week nullable:false
        week range:-1..17
        statKey nullable:false
        statValue nullable:false
    }

    static mapping = {
        cache false
        table "stats"
        version false
    }

    def translateStatKey() {
        return FantasyConstants.statTranslation[statKey]
    }

    /**
     * Determines which years are available for player statistics
     *
     * @return      An array of integers representing the seasons available.
     */
    static def getStatYears() {
        return Stat.executeQuery("select distinct s.season from Stat s")
    }
}

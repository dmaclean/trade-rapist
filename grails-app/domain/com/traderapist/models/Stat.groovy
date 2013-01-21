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
        table "stats"
        version false
    }

    def translateStatKey() {
        return FantasyConstants.statTranslation[statKey]
    }
}

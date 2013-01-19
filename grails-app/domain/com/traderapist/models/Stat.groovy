package com.traderapist.models

class Stat {
    static belongsTo = [player: Player]

    Integer id
    Integer season
    Integer week
    Integer statKey
    Integer statValue

    static constraints = {
    }

    static mapping = {
        table "stats"
        version false
    }
}

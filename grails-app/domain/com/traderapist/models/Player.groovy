package com.traderapist.models

class Player {
    static hasMany = [stats: Stat]

    String name
    String position
    static constraints = {
        name blank:false
        position blank:false
    }

    static mapping = {
        table "players"
        version false
    }
}

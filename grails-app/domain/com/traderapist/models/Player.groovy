package com.traderapist.models

class Player {
    static hasMany = [stats: Stat]

    Integer id
    String name
    String position
    static constraints = {
    }
}

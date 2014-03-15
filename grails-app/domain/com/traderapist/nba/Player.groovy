package com.traderapist.nba

class Player {

    String name
    String position
    String rgPosition
    int height
    int weight
    String url

    static hasMany = [injuries: Injury]

    static constraints = {
        name nullable: false
        position nullable: false, inList: ["PG", "SG", "SF", "PF", "C", "G", "F"]
        rgPosition nullable: false, inList: ["PG", "SG", "SF", "PF", "C"]
        height min: 1
        weight min: 1
        url nullable: false
    }

    static mapping = {
        datasource 'nba'
    }
}

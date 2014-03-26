package com.traderapist.nba

class Injury {

    Date injuryDate

    Date returnDate

    String details

    static belongsTo = [player: Player]

    static constraints = {
        injuryDate nullable: false
        returnDate nullable: false
    }

    static mapping = {
        datasource 'nba'
        table 'injuries'
        version false
    }
}

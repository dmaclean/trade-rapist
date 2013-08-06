package com.traderapist.models

class FantasyPoints {

    Integer season
    Integer week
    Double points
	Boolean projection
	Integer numStartable
	Integer numOwners

    static belongsTo = [player: Player]

	static hasOne = [ scoringSystem : ScoringSystem]

    static constraints = {
        season nullable: false
        week nullable: false
        week range: -1..17
        points nullable: false
        scoringSystem nullable: false
	    projection nullable: true
	    numStartable nullable: true
	    numOwners nullable: true
    }

    static mapping = {
        cache true
        table "fantasy_points"
        version false
    }
}

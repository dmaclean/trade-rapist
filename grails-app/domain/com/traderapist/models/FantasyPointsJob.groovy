package com.traderapist.models

class FantasyPointsJob {

	Boolean completed
	Integer season
	Integer week
	Boolean projection

	static hasOne = [fantasyTeam : FantasyTeam]

	static constraints = {
		completed nullable: false
		season nullable: false
		week nullable: false
		projection nullable: false
	}

	static mapping = {
		table "fantasy_points_jobs"
	}
}

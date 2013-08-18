package com.traderapist.models

class FantasyPointsJob {

	static Integer NO_PROJECTION = 0
	static Integer TRADERAPIST_PROJECTION = 1
	static Integer YAHOO_STANDARD_PROJECTION = 2
	static Integer YAHOO_PPR_PROJECTION = 3

	Boolean completed
	Integer season
	Integer week
	Integer projection

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

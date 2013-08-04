package com.traderapist.models

class FantasyPointsJob {

	static hasOne = [fantasyTeam : FantasyTeam]

	static constraints = {

	}

	static mapping = {
		table "fantasy_points_jobs"
	}
}

package com.traderapist.models

class FantasyTeamStarter {

	String position
	Integer numStarters

	static hasOne = [fantasyTeam : FantasyTeam]

	static constraints = {
		position nullable: false, blank: false
		numStarters min: 0, nullable: false
	}

	static mapping = {
		table "fantasy_team_starters"
	}
}

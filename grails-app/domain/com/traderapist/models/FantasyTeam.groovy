package com.traderapist.models

import com.traderapist.security.User

class FantasyTeam {

	static hasOne = [scoringSystem : ScoringSystem]

	static hasMany = [fantasyTeamStarters : FantasyTeamStarter]

	static belongsTo = [user:User, fantasyLeagueType:FantasyLeagueType]

	String leagueId
	String name
	Integer season
	Integer numOwners

	static constraints = {
		leagueId nullable: true
		name nullable: false, blank: false
		season nullable: false
		scoringSystem nullable: true
		numOwners nullable: false, min: 1
	}

	static mapping = {
		table "fantasy_teams"
	}
}

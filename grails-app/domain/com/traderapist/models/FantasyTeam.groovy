package com.traderapist.models

import com.traderapist.security.User

class FantasyTeam {

	static hasOne = [scoringSystem : ScoringSystem]

	static belongsTo = [user:User, fantasyLeagueType:FantasyLeagueType]

	String leagueId
	String name
	Integer season

	static constraints = {
		leagueId nullable: true
		name nullable: false, blank: false
		season nullable: false
		scoringSystem nullable: true
	}

	static mapping = {
		table "fantasy_teams"
	}
}

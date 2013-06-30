package com.traderapist.models

import com.traderapist.security.User

class FantasyTeam {

	static belongsTo = [user:User, fantasyLeagueType:FantasyLeagueType]

	String leagueId
	String name
	Integer season

	static constraints = {
		leagueId nullable: true
		name nullable: false
		season nullable: false
	}

	static mapping = {
		table "fantasy_teams"
	}
}

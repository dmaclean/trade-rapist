package com.traderapist.models

class FantasyTeamPlayer {

	static belongsTo = [player:Player, fantasyTeam:FantasyTeam]

	static constraints = {
		player nullable: false
		fantasyTeam nullable: false
	}

	static mapping = {
		table "fantasy_team_players"
	}
}

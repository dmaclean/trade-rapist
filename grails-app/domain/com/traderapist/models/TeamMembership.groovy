package com.traderapist.models

/**
 * Model to represent the team that a player belongs to for a given season.
 */
class TeamMembership {

    static belongsTo = [player: Player, team: Team]

	/**
	 * The season of the team membership.
	 */
	Integer season

	static constraints = {
		season nullable: false, min: 2001
    }

	static mapping = {
		cache true
		table "team_memberships"
	}
}

package com.traderapist.models

class FantasyLeagueType {

	/**
	 * Shorthand code to describe the Fantasy League - ESPN, Yahoo!, CBS, etc.
	 */
	String code

	/**
	 * Longer text description of the league.
	 */
	String description

	static constraints = {
		code nullable: false
		description nullable: false
	}

	static mapping = {
		cache true
		table "fantasy_league_types"
	}
}

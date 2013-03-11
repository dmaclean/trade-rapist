package com.traderapist.models

/**
 * Model representing an NFL team.
 */
class Team {

	/**
	 * The city that the team is from.
	 */
	String city

	/**
	 * The full name of the team.
	 */
	String name

	/**
	 * The city abbreviation of the team.
	 */
	String abbreviation

    static constraints = {
	    city nullable: false, blank: false
	    name nullable: false, blank: false
	    abbreviation nullable: false, blank: false
    }

	static mapping = {
		cache true
		table "teams"
		version false
	}
}

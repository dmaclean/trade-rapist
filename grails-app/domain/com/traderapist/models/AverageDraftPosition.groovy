package com.traderapist.models

/**
 * Model that represents a Player's average draft position in mock drafts
 * for a given season.
 */
class AverageDraftPosition {

	static belongsTo = [player: Player]

	/**
	 * The average draft position.  The number itself doesn't take into
	 * consideration the size of the draft.
	 */
	Double adp

	/**
	 * The season for this ADP.
	 */
	Integer season

    static constraints = {
	    season nullable: false, min: 2001
	    adp nullable: false, min: new Double(1.0)
    }

	static mapping = {
		cache false
		table "average_draft_positions"
	}
}

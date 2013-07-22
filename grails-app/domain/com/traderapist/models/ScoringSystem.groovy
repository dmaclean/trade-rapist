package com.traderapist.models

class ScoringSystem {

	String name

	static belongsTo = [fantasyTeam : FantasyTeam]

	static hasMany = [scoringRules : ScoringRule]

	static constraints = {
		name nullable: false
		fantasyTeam nullable: true
	}

	static mapping = {
		table "scoring_systems"
		scoringRules joinTable: "scoring_system_rules", column: "scoring_rule_id"
	}
}

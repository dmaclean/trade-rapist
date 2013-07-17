package com.traderapist.models

class ScoringRule {

	Integer statKey
	Double multiplier

	static hasMany = [scoringSystems : ScoringSystem]

	static belongsTo = ScoringSystem

	static constraints = {
		statKey nullable: false, min: 1
		multiplier nullable: false
	}

	static mapping = {
		table "scoring_rules"
		scoringSystems joinTable: "scoring_system_rules", column: "scoring_system_id"
	}
}

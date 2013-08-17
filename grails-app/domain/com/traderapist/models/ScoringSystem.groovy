package com.traderapist.models

class ScoringSystem {

	String name

	static belongsTo = [fantasyTeam : FantasyTeam]

	static hasMany = [fantasyPoints : FantasyPoints, scoringRules : ScoringRule]

	static constraints = {
		name nullable: false
		fantasyPoints nullable: true
		fantasyTeam nullable: true
	}

	static mapping = {
		table "scoring_systems"
		scoringRules joinTable: "scoring_system_rules", column: "scoring_rule_id"
	}

	def calculateScore(List<Stat> stats) {
		def score = 0.0

		scoringRules.each {     scoringRule ->
			stats.each {    stat ->
				if(scoringRule.statKey == stat.statKey) {
					score += stat.statValue * scoringRule.multiplier
				}
			}
		}

		return score
	}
}

package com.traderapist.utils.projection

import groovy.sql.Sql

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 10/17/13
 * Time: 9:43 PM
 * To change this template use File | Settings | File Templates.
 */
class ProjectStats {

	def sql = Sql.newInstance("jdbc:mysql://localhost:3306/nfldatabase", "fantasy", "fantasy", "com.mysql.jdbc.Driver")

	def project() {
		sql.eachRow("select * from stats_weekly_player where season = ? and week = ?", [2012,7]) { row ->
			if(row["position"] != "QB") {
				return
			}

			def id = row["playerid"]
			def season = row["season"]
			def week = row["week"]
			def playerResult = sql.firstRow("select * from players where playerid = ?", [id])

			def opponent = getOpponent(row["teamcode"], season, week)
			def factor = getOpponentPassingYardsFactor(opponent, season, week)
			def baseline = getAveragePlayerStat(id, season, week, "passingyards")

			println "${playerResult.firstname} ${playerResult.lastname} has expected passing yards of ${baseline*factor} against ${opponent}."
		}
	}

	def getAveragePlayerStat(id, season, week, stat) {
		def result = sql.firstRow("select avg(${stat}) as \"average\" from stats_weekly_player p where playerid = ? and season = ? and week < ?",
				[id, season, week])

		return result["average"]
	}

	def getOpponentPassingYardsFactor(opponent, season, week) {
		def avgPassingYards = sql.firstRow("SELECT avg(passingyardsagainstpergame) FROM stats_weekly_team s where season=? and week=?",
				[season, week-1])[0]

		/*
		 * This is to normalize the opponent's passing yards factor based on strength of schedule.
		 * We're basically trying to find out how good the passing games of our opponent's previous
		 * opponents are (as compared to the league average).
		 */
		def opponentOffensiveFactors = []
		def totalFactors = 0
		def numFactors = 0
		sql.eachRow("SELECT offensivepassingyards, offensivepassingyards/? as \"factor\" FROM stats_weekly_team s inner join team_schedules t on s.teamcode = t.teamcode " +
				"where s.season = ? and t.season = s.season and s.week = t.week and s.week < ? and t.opponentcode = ?",
				[avgPassingYards, season, week, opponent]) { row ->
			opponentOffensiveFactors << row["factor"]
			totalFactors += row["factor"]
			numFactors++
		}

		return totalFactors/numFactors
	}

	def getOpponent(team, season, week) {
		def result = sql.firstRow("select * from team_schedules where teamcode = ? and season = ? and week = ?",
				[team, season, week])

		return result["opponentcode"]
	}

	public static void main(String[] args) {
		ProjectStats ps = new ProjectStats()
		ps.project()
	}
}

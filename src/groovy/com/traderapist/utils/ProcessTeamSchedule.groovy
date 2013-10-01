package com.traderapist.utils

import groovy.sql.Sql

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 9/28/13
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
class ProcessTeamSchedule {

	def process(teamName, season) {
		def sql = Sql.newInstance("jdbc:mysql://localhost:3306/fantasy_yahoo", "fantasy", "fantasy", "com.mysql.jdbc.Driver")
		def teams = sql.dataSet("teams")
		def teamSchedule = sql.dataSet("team_schedule")

		def teamEntry = teams.firstRow("select * from teams where name = ?", [teamName])

		def csv = ",,,,,,,,,Score,Score,Offense,Offense,Offense,Offense,Offense,Defense,Defense,Defense,Defense,Defense,Expected Points,Expected Points,Expected Points\n" +
				"Week,Day,Date,,,OT,Rec,,Opp,Tm,Opp,1stD,TotYd,PassY,RushY,TO,1stD,TotYd,PassY,RushY,TO,Offense,Defense,Sp. Tms\n" +
				"2,Sun,September 15,boxscore,L,OT,1-1,@,Houston Texans,24,30,14,248,129,119,,25,452,280,172,2,-10.58,-0.01,6.54\n"

		def m = csv =~ /(\d+),(\w+)?,(\w+ \d+)?,(boxscore)?,(\w)?,(\w+)?,(\d+-\d+)?,(@?),([\s\w\.]+),.*?\n?/

		for(curr in m) {
			println "${curr[1]}, ${curr[8]}, ${curr[9]}"
			if(curr[9] == "Bye Week") {
				continue
			}

			def home = curr[8] != "@"
			def opponentPieces = curr[9].split(" ")
			def opponentName = opponentPieces[opponentPieces.size()-1]
			def opponentEntry = teams.firstRow("select * from teams where name = ?", [opponentName])

			teamSchedule.add(team_id: teamEntry.id, opponent_id: opponentEntry.id, season: season, week: curr[1], home: home)
		}
	}

	public static void main(String[] args) {
		def pts = new ProcessTeamSchedule()
		pts.process("Titans", 2013)
	}
}

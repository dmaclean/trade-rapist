package com.traderapist.utils

import groovy.sql.Sql

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 10/9/13
 * Time: 8:59 PM
 * To change this template use File | Settings | File Templates.
 */
class ProcessTeamMembership {
	static def process() {
		File f = new File("/Users/dmaclean/Downloads/players.csv")

		def sql = Sql.newInstance("jdbc:mysql://localhost:3306/fantasy_yahoo", "fantasy", "fantasy", "com.mysql.jdbc.Driver")
		def membership = sql.dataSet("team_memberships")

		f.eachLine {
			def pieces = it.split(",")
			def name = pieces[0].replaceAll("\\*","").replaceAll("\\+", "").replaceAll("  ", " ")
			def teamId = Integer.parseInt(pieces[1])
			def season = Integer.parseInt(pieces[2])

			def found = false
			def duplicate = false
			def id = null
			sql.eachRow("select * from players where name = ?", [name]) { row ->
				if(found) {
					duplicate = true
					return
				}

				id = row["id"]

				found = true
			}

			if(found && !duplicate) {
				def exists = false
				sql.eachRow("select * from team_memberships where player_id = ? and season = ?", [id, season]) { row ->
					exists = true
				}

				if(!exists) {
					membership.add([player_id: id, season: season, team_id: teamId])
				}
				else {
					println "Entry for ${name} in ${season} already exists."
				}
			}
			else if(!found) {
				println "Could not find a player named ${name}"
			}
			else if(duplicate) {
				println "Found multiple entries for ${name}"
			}
		}
	}

	public static void main(String[] args) {
		ProcessTeamMembership.process()
	}
}

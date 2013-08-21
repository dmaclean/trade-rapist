package com.traderapist.models

import com.traderapist.projections.YahooPPRProjections2013
import com.traderapist.projections.YahooStandardProjections2013

class FantasyPoints {

    Integer season
    Integer week
    Double points
	Integer projection
	Integer numStartable
	Integer numOwners

    static belongsTo = [player: Player]

	static hasOne = [ scoringSystem : ScoringSystem]

    static constraints = {
        season nullable: false
        week nullable: false
        week range: -1..17
        points nullable: false
        scoringSystem nullable: false
	    projection nullable: true
	    numStartable nullable: true
	    numOwners nullable: true
    }

    static mapping = {
        cache false
        table "fantasy_points"
        version false
    }

    static def positions = [
            Player.POSITION_QB,
            Player.POSITION_RB,
            Player.POSITION_WR,
            Player.POSITION_TE,
            Player.POSITION_DEF,
            Player.POSITION_K
    ]

    /**
     * Iterates through ALL players and calculates their fantasy points for each week and season for
     * the specified FantasyTeam (and their scoring system).  These points are written to the database
     * as FantasyPoints.
     *
     * The scoring system is determined by the one registered to the FantasyTeam passed in as an id.
     */
    static def generatePoints(fantasyTeam, position, season, week) {
        long start = System.currentTimeMillis()

        // Grab all the players - either all or at a certain position
        def players
        if(position && season && week) {
	        players = Player.createCriteria().listDistinct {
                eq "position", position
                stats {
                    eq "season", season
                    eq "week", week
                }
            }
        }
        else if(position && (!season || !week)) {
            players = Player.findAllByPosition(position)
        }
        else if(!position && season && week) {
            players = Player.createCriteria().list {
                stats {
                    eq "season", season
                    eq "week", week
                }
            }
        }
        else {
            players = Player.findAll()
        }

        for(player in players) {
            player.computeFantasyPoints(fantasyTeam, season, week)
        }
        long end = System.currentTimeMillis()

        if(position) {
            println("\t\tGenerated fantasy points for all ${ position } in ${ (end-start)/1000.0 } seconds")
        }
        else {
            println("\t\tGenerated fantasy points for all players in ${ (end-start)/1000.0 } seconds")
        }

        return true
    }

	static def projectPoints(FantasyPointsJob job) {
		def starters = [:]
		job.fantasyTeam.fantasyTeamStarters.each {  starter ->
			starters[starter.position] = starter.numStarters
		}

		if(job.projection == FantasyPointsJob.TRADERAPIST_PROJECTION) {
			def fantasyTeam = job.fantasyTeam

			/*
	         * Grab all players who had stats in the previous season.
			 */
			def previousSeason = fantasyTeam.season-1
			def players = Player.createCriteria().listDistinct {
				'in'("position", [Player.POSITION_QB, Player.POSITION_RB, Player.POSITION_WR, Player.POSITION_TE, Player.POSITION_DEF, Player.POSITION_K])
				stats {
					eq("season", previousSeason)
					eq("week", -1)
				}
			}

			for(p in players) {
				long start2 = System.currentTimeMillis()

				def points = p.calculateProjectedPoints(
						fantasyTeam.season,
						starters[p.position],
						fantasyTeam.numOwners,
						fantasyTeam.scoringSystem)

				def playerFantasyProjection = new FantasyPoints(
						player: p,
						season: fantasyTeam.season,
						week: -1,
						points: points,
						scoringSystem: fantasyTeam.scoringSystem,
						projection: FantasyPointsJob.TRADERAPIST_PROJECTION,
						numOwners: fantasyTeam.numOwners,
						numStartable: starters[p.position]
				).save()
				long end2 = System.currentTimeMillis()
				println "Generated ${ fantasyTeam.season } projection for ${ p.name } in ${ (end2-start2)/1000.0 }"
			}
		}
		else if (job.projection == FantasyPointsJob.YAHOO_STANDARD_PROJECTION) {
			YahooStandardProjections2013.yahooStandardProjections2013.each {  projection ->
				def player = Player.get(projection[0])

				if(player) {
					def fp = new FantasyPoints(
							season: job.season,
							week: job.week,
							points: projection[2],
							projection: job.projection,
							numOwners: job.fantasyTeam.numOwners,
							numStartable: starters[player.position],
							player: player,
							scoringSystem: job.fantasyTeam.scoringSystem
					).save()
				}
				else {
					log.error "Could not find player with id ${ projection[0] }"
				}
			}
		}
		else if (job.projection == FantasyPointsJob.YAHOO_PPR_PROJECTION) {
			YahooPPRProjections2013.yahooPPRProjections2013.each {  projection ->
				def player = Player.get(projection[0])

				if(player) {
					def fp = new FantasyPoints(
							season: job.season,
							week: job.week,
							points: projection[2],
							projection: job.projection,
							numOwners: job.fantasyTeam.numOwners,
							numStartable: starters[player.position],
							player: player,
							scoringSystem: job.fantasyTeam.scoringSystem
					).save()
				}
				else {
					log.error "Could not find player with id ${ projection[0] }"
				}
			}
		}
	}

    /**
	 * Does a sweeping update of projected fantasy points for Yahoo! Standard and PPR types
	 * for all teams across the app.
	 *
	 * @param type
	 * @param season
	 * @param data
	 */
	static def updateProjections(type, season, data) {
		// Process the data first
		def players = data.split("\r?\n")
		def projections = [:]
		players.each {  player ->
			// id, name, points
			def pieces = player.split(",")
			projections[pieces[0]] = pieces[2].toDouble()
		}

		// Update the Fantasy Points.
		def fantasyPoints = FantasyPoints.findAllBySeasonAndWeekAndProjection(season, -1, type)

		fantasyPoints.each {        fp ->
			if(fp.points != projections[fp.player.id.toString()]) {
				def newPoints = projections[fp.player.id.toString()]
				if(newPoints) {
					fp.points = newPoints.toDouble()

					if(!fp.save()) {
						println "Not valid for ${ fp.player.name }"
					}
				}
				else {
					println "Found null entry for ${ fp.player.name }"
				}
			}
		}
	}
}

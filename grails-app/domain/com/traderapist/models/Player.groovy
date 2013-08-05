package com.traderapist.models

import com.traderapist.constants.FantasyConstants
import com.traderapist.scoringsystem.ESPNStandardScoringSystem
import com.traderapist.scoringsystem.IFantasyScoringSystem

class Player {
    static hasMany = [stats: Stat, fantasyPoints: FantasyPoints,
		    teamMemberships: TeamMembership, averageDraftPositions: AverageDraftPosition]

	static final String POSITION_QB = "QB"

	static final String POSITION_RB = "RB"

	static final String POSITION_WR = "WR"

	static final String POSITION_TE = "TE"

	static final String POSITION_DEF = "DEF"

	static final String POSITION_K = "K"

    String name
    String position

    static constraints = {
        name blank:false
        position blank:false
    }

    static mapping = {
        cache true
        table "players"
        version false
    }

    /**
     * Retrieve the years that we have stats for this player.
     *
     * @return      An array of years.
     */
    def getStatYears() {
        return Stat.executeQuery("select distinct season from Stat s where s.player = ?", [this])
    }

    /**
     * Calculates the average of a player's scores for the specified season.
     *
     * @param season    The season to calculate average points for.
     * @return          The average number of points scored, or zero if there is no data.
     */
    def getScoringAverageForSeason(season) {
        def result = Player.executeQuery("select avg(f.points) from Player p inner join p.fantasyPoints f where p = ? and f.season = ?", [this, season])
        return (result[0] == null) ? 0 : result[0]
    }

    /**
     * Calculates the average of the scores for all players at a position the specified season.
     *
     * @param season    The season to calculate average points for.
     * @return          The average number of points scored, or zero if there is no data.
     */
    static def getScoringAverageForPositionForSeason(position, season) {
        def result = Player.executeQuery("select avg(f.points) from Player p inner join p.fantasyPoints f where f.season = ? and f.week = -1", [season])
        return (result[0] == null) ? 0 : result[0]
    }

	/**
	 * Returns a list of players at the specified position for a given season.  This method clears
	 * out the fantasyPoints set (which contains all FantasyPoints objects for the player coming
	 * back from the database) and only puts the FantasyPoints object for the specified season in.
	 *
	 * The method is used to quickly get a sorted list of players for the draft Minimax tree.
	 *
	 * @param position      The position we're interested in
	 * @param season        The season we're interested in
	 * @param system        The fully-qualified class name of the scoring system we're using
	 * @return              A list of Player objects, sorted in order of Fantasy Points scored.
	 */
	static def getPlayersInPointsOrder(position, season, system) {
		def results = Player.executeQuery("from Player p inner join p.fantasyPoints f inner join p.averageDraftPositions adp " +
				"where p.position = ? and f.season = ? and f.week = -1 and f.projection = ? and f.system = ? and adp.season = ? " +
				"order by f.points desc", [position, season, true, system, season])

		def players = []
		for(int i=0; i<results.size(); i++) {
			players << results[i][0]
			players[i].fantasyPoints = new HashSet([results[i][1]])
			players[i].averageDraftPositions = new HashSet([results[i][2]])
		}

		return players
	}

    /**
     * Calculates the fantasy points for the player based on their statistics and
     * the provided scoring system.
     *
     * @param scoringSystem     The scoring system that will translate stats into points.
     */
    def computeFantasyPoints(ScoringSystem scoringSystem) {
        def points = [:]

        long start = System.currentTimeMillis();
        for(s in stats) {
	        boolean exists = false;
	        for(fp in fantasyPoints) {
		        if(fp.scoringSystem == scoringSystem && fp.season == s.season && fp.week == s.week) {
			        exists = true
			        break
		        }
	        }

	        if(exists) {
		        print("Fantasy points for ${name} for ${s.season}/${s.week} already exists.  Skipping...")
		        continue
	        }

            def seasonStr = String.valueOf(s.season)
            def weekStr = String.valueOf(s.week)
            def key = "${seasonStr}__${weekStr}"

            if(!points[key]) {
                points[key] = 0.0
            }
            points[key] += scoringSystem.calculateScore([s])
        }
        long end = System.currentTimeMillis();
        println("Distributed stats to season and week for ${name} in ${(end-start)/1000.0}")

	    def fantasyPointsList = []
        for(p in points) {
            String[] keyPieces = p.key.split("__")
            FantasyPoints fp = new FantasyPoints(player: this, season: keyPieces[0], week: keyPieces[1], system: scoringSystem.class.getName(), points:  p.value)
            fp.save()

	        fantasyPointsList << fp
        }
	    end = System.currentTimeMillis();
	    println("Created FantasyPoint entries for ${name} in ${(end-start)/1000.0}")

	    return fantasyPointsList
    }

    /**
     * Runs a query to fetch all players of a certain position for a certain season and sort
     * them by fantasy points scored (highest to lowest).  The players are then broken up into
     * tiers, with separation of tiers determined by any two consecutive players separated
     * by 16 or more points.
     *
     * @param position      The player position we're interested in.
     * @param season        The season to analyze
     * @return              A list of arrays, with each array containing [<Player object>, <FantasyPoints object>]
     */
    static def getDropoffData(String position, int season) {
        def results = Player.executeQuery("from Player p inner join p.fantasyPoints f " +
                "where p.position = ? and f.season = ? and f.week = -1 order by f.points desc", [position, season])

        // Separate into tiers.
        def tiers = []

        // Calculate maximum difference between players i and i+1 that would keep them in the same tier.
        // This difference is currently two standard deviations from the average difference between players.
        def diffs = []
        for(int i=0; i<results.size()-1; i++) {
            diffs.add(Math.abs(results[i][1].points - results[i+1][1].points))
        }
        def maxDiff = Player.calculateStandardDeviation(diffs) * 2

        def currTier = []
        def r_prev = null
        for(r in results) {
            // First player in a new tier
            if (!currTier) {
                currTier = [r]
            }
            // Found two players separated by 16 or more points.  Add the
            // current tier to the list of tiers and put our current player
            // into a new tier.
            else if (Math.abs(r_prev[1].points - r[1].points) >= maxDiff) {
                tiers << currTier
                currTier = [r]
            }
            // Current player belongs in current tier.
            else {
                currTier << r
            }

            r_prev = r
        }

        // Current tier has players in it.  Add it to the list.
        if (currTier.size() > 0) {
            tiers << currTier
        }

        return tiers
    }

    /**
     * Calculates the standard deviation of points scored each week over
     * the course of the specified season.
     *
     * @param season        The season to evaluate.
     * @return              The standard deviation, or -1 if season is invalid.
     */
    def calculatePointsStandardDeviation(season) {
        /*
        Make sure that the user has specified a valid year for this player.
         */
        if(!Stat.getStatYears().contains(season)) {
            return -1
        }

        def fantasyPoints = FantasyPoints.findAllByPlayerAndSeason(this, season)

        def points = []
        for(fp in fantasyPoints) {
            if (fp.week != -1) {
                points << fp.points
            }
        }

        return calculateStandardDeviation(points)
    }

    /**
     * Estimates how many points a player will score in the specified season.  This
     * currently requires that the previous two season be available because we
     * calculate the point correlation for the player's position on the fly.
     *
     * @param year      The season we want to estimate points for.
     * @return          The estimated number of fantasy points the player will score.
     */
    def calculateProjectedPoints(year) {
        def statYears = Stat.getStatYears()

        /*
        Make sure the year we want to project is supported with stats from the previous year.

        i.e. year = 2002, we need stats from 2001.
         */
        if (!statYears.contains(year-1))
            throw new Exception("Invalid year.")

        def correlation = getCorrelation(position, null, null, year-2, year-1)
        def positionAvg = getScoringAverageForPositionForSeason(position, year-1)
        def points
        for(f in fantasyPoints) {
            if(f.season == year-1 && f.week == -1) {
                points = f.points
                break
            }
        }

        return correlation*(points-positionAvg) + positionAvg
    }

	/**
	 * Estimates how many points a player will score in the specified season.  This
	 * currently requires that the previous two season be available because we
	 * calculate the point correlation for the player's position on the fly.
	 *
	 * @param year              The season we want to estimate points for.
	 * @param numStartable      The number of players at this position that are startable on a roster.
	 * @param numOwners         The number of owners in a league.
	 * @return                  The estimated number of fantasy points the player will score.
	 */
	def calculateProjectedPoints(year, numStartable, numOwners, system) {
		def hasValidYear = false

		def passingYardsLastYear = 0
		def passingTouchdownsLastYear = 0
		def interceptionsLastYear = 0

		def rushingYardsLastYear = 0
		def rushingTouchdownsLastYear = 0

		def receptionYardsLastYear = 0
		def receptionTouchdownsLastYear = 0
		def receptionsLastYear = 0

		if(position == Player.POSITION_QB) {
			for(s in stats) {
				// Set flag for valid year
				if(!hasValidYear && s.season == year-1) {
					hasValidYear = true
				}

				if(s.season == year-1 && s.week == -1 && s.statKey == FantasyConstants.STAT_PASSING_YARDS) {
					passingYardsLastYear = s.statValue
				}
				else if(s.season == year-1 && s.week == -1 && s.statKey == FantasyConstants.STAT_PASSING_TOUCHDOWNS) {
					passingTouchdownsLastYear = s.statValue
				}
				else if(s.season == year-1 && s.week == -1 && s.statKey == FantasyConstants.STAT_INTERCEPTIONS) {
					interceptionsLastYear = s.statValue
				}
				else if(s.season == year-1 && s.week == -1 && s.statKey == FantasyConstants.STAT_RUSHING_YARDS) {
					rushingYardsLastYear = s.statValue
				}
				else if(s.season == year-1 && s.week == -1 && s.statKey == FantasyConstants.STAT_RUSHING_TOUCHDOWNS) {
					rushingTouchdownsLastYear = s.statValue
				}
			}

			/*
			Make sure the year we want to project is supported with stats from the previous year.

			i.e. year = 2002, we need stats from 2001.
			 */
			if (!hasValidYear)
				throw new Exception("Invalid year.")

			def idx = numStartable*numOwners -1

			def passingYardsResult = Stat.createCriteria().listDistinct {
				eq("season", year-1)
				eq("statKey", FantasyConstants.STAT_PASSING_YARDS)
				player {
					eq("position", position)
				}
				maxResults numStartable*numOwners
				order "statValue", "desc"
				cache true
			}

			def passingTouchdownsResult = Stat.createCriteria().listDistinct {
				eq("season", year-1)
				eq("statKey", FantasyConstants.STAT_PASSING_TOUCHDOWNS)
				player {
					eq("position", position)
				}
				maxResults numStartable*numOwners
				order "statValue", "desc"
				cache true
			}

			def interceptionsResult = Stat.createCriteria().listDistinct {
				eq("season", year-1)
				eq("statKey", FantasyConstants.STAT_INTERCEPTIONS)
				player {
					eq("position", position)
				}
				maxResults numStartable*numOwners
				order "statValue", "asc"
				cache true
			}

			def rushingYardsResult = Stat.createCriteria().listDistinct {
				eq("season", year-1)
				eq("statKey", FantasyConstants.STAT_RUSHING_YARDS)
				player {
					eq("position", position)
				}
				maxResults numStartable*numOwners
				order "statValue", "desc"
				cache true
			}

			def rushingTouchdownsResult = Stat.createCriteria().listDistinct {
				eq("season", year-1)
				eq("statKey", FantasyConstants.STAT_RUSHING_TOUCHDOWNS)
				player {
					eq("position", position)
				}
				maxResults numStartable*numOwners
				order "statValue", "desc"
				cache true
			}

			def passingYardsCorrelation = getCorrelation(position, FantasyConstants.STAT_PASSING_YARDS)
			def passingTouchdownsCorrelation = getCorrelation(position, FantasyConstants.STAT_PASSING_TOUCHDOWNS)
			def interceptionsCorrelation = getCorrelation(position, FantasyConstants.STAT_INTERCEPTIONS)
			def rushingYardsCorrelation = getCorrelation(position, FantasyConstants.STAT_RUSHING_YARDS)
			def rushingTouchdownsCorrelation = getCorrelation(position, FantasyConstants.STAT_RUSHING_TOUCHDOWNS)

			def passingYardsProjected = (passingYardsCorrelation * passingYardsLastYear) + ( (1 - passingYardsCorrelation) * passingYardsResult[idx].statValue)
			def passingTouchdownsProjected = (passingTouchdownsCorrelation * passingTouchdownsLastYear) + ( (1 - passingTouchdownsCorrelation) * passingTouchdownsResult[idx].statValue)
			def interceptionsProjected = (interceptionsCorrelation * interceptionsLastYear) + ( (1 - interceptionsCorrelation) * interceptionsResult[idx].statValue)
			def rushingYardsProjected = (rushingYardsCorrelation * rushingYardsLastYear) + ( (1 - rushingYardsCorrelation) * rushingYardsResult[idx].statValue)
			def rushingTouchdownsProjected = (rushingTouchdownsCorrelation * rushingTouchdownsLastYear) + ( (1 - rushingTouchdownsCorrelation) * rushingTouchdownsResult[idx].statValue)

			return system.calculateScore(
					[
							new Stat(season: 2013, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: passingYardsProjected),
							new Stat(season: 2013, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: passingTouchdownsProjected),
							new Stat(season: 2013, week: -1, statKey: FantasyConstants.STAT_INTERCEPTIONS, statValue: interceptionsProjected),
							new Stat(season: 2013, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: rushingYardsProjected),
							new Stat(season: 2013, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: rushingTouchdownsProjected),
					])
		}
		else if(position == Player.POSITION_RB) {
			for(s in stats) {
				// Set flag for valid year
				if(!hasValidYear && s.season == year-1) {
					hasValidYear = true
				}

				if(s.season == year-1 && s.week == -1 && s.statKey == FantasyConstants.STAT_RECEPTION_YARDS) {
					receptionYardsLastYear = s.statValue
				}
				else if(s.season == year-1 && s.week == -1 && s.statKey == FantasyConstants.STAT_RECEPTION_TOUCHDOWNS) {
					receptionTouchdownsLastYear = s.statValue
				}
				else if(s.season == year-1 && s.week == -1 && s.statKey == FantasyConstants.STAT_RECEPTIONS) {
					receptionsLastYear = s.statValue
				}
				else if(s.season == year-1 && s.week == -1 && s.statKey == FantasyConstants.STAT_RUSHING_YARDS) {
					rushingYardsLastYear = s.statValue
				}
				else if(s.season == year-1 && s.week == -1 && s.statKey == FantasyConstants.STAT_RUSHING_TOUCHDOWNS) {
					rushingTouchdownsLastYear = s.statValue
				}
			}

			/*
			Make sure the year we want to project is supported with stats from the previous year.

			i.e. year = 2002, we need stats from 2001.
			 */
			if (!hasValidYear)
				throw new Exception("Invalid year.")

			def idx = numStartable*numOwners -1

			def receptionYardsResult = Stat.createCriteria().listDistinct {
				eq("season", year-1)
				eq("statKey", FantasyConstants.STAT_RECEPTION_YARDS)
				player {
					eq("position", position)
				}
				maxResults numStartable*numOwners
				order "statValue", "desc"
				cache true
			}

			def receptionTouchdownsResult = Stat.createCriteria().listDistinct {
				eq("season", year-1)
				eq("statKey", FantasyConstants.STAT_RECEPTION_TOUCHDOWNS)
				player {
					eq("position", position)
				}
				maxResults numStartable*numOwners
				order "statValue", "desc"
				cache true
			}

			def receptionsResult = Stat.createCriteria().listDistinct {
				eq("season", year-1)
				eq("statKey", FantasyConstants.STAT_RECEPTIONS)
				player {
					eq("position", position)
				}
				maxResults numStartable*numOwners
				order "statValue", "asc"
				cache true
			}

			def rushingYardsResult = Stat.createCriteria().listDistinct {
				eq("season", year-1)
				eq("statKey", FantasyConstants.STAT_RUSHING_YARDS)
				player {
					eq("position", position)
				}
				maxResults numStartable*numOwners
				order "statValue", "desc"
				cache true
			}

			def rushingTouchdownsResult = Stat.createCriteria().listDistinct {
				eq("season", year-1)
				eq("statKey", FantasyConstants.STAT_RUSHING_TOUCHDOWNS)
				player {
					eq("position", position)
				}
				maxResults numStartable*numOwners
				order "statValue", "desc"
				cache true
			}

			def receptionYardsCorrelation = getCorrelation(position, FantasyConstants.STAT_RECEPTION_YARDS)
			def receptionTouchdownsCorrelation = getCorrelation(position, FantasyConstants.STAT_RECEPTION_TOUCHDOWNS)
			def receptionsCorrelation = getCorrelation(position, FantasyConstants.STAT_RECEPTIONS)
			def rushingYardsCorrelation = getCorrelation(position, FantasyConstants.STAT_RUSHING_YARDS)
			def rushingTouchdownsCorrelation = getCorrelation(position, FantasyConstants.STAT_RUSHING_TOUCHDOWNS)

			def receptionYardsProjected = (receptionYardsCorrelation * receptionYardsLastYear) + ( (1 - receptionYardsCorrelation) * receptionYardsResult[idx].statValue)
			def receptionTouchdownsProjected = (receptionTouchdownsCorrelation * receptionTouchdownsLastYear) + ( (1 - receptionTouchdownsCorrelation) * receptionTouchdownsResult[idx].statValue)
			def receptionsProjected = (receptionsCorrelation * receptionsLastYear) + ( (1 - receptionsCorrelation) * receptionsResult[idx].statValue)
			def rushingYardsProjected = (rushingYardsCorrelation * rushingYardsLastYear) + ( (1 - rushingYardsCorrelation) * rushingYardsResult[idx].statValue)
			def rushingTouchdownsProjected = (rushingTouchdownsCorrelation * rushingTouchdownsLastYear) + ( (1 - rushingTouchdownsCorrelation) * rushingTouchdownsResult[idx].statValue)

			return system.calculateScore(
					[
							new Stat(season: 2013, week: -1, statKey: FantasyConstants.STAT_RECEPTION_YARDS, statValue: receptionYardsProjected),
							new Stat(season: 2013, week: -1, statKey: FantasyConstants.STAT_RECEPTION_TOUCHDOWNS, statValue: receptionTouchdownsProjected),
							new Stat(season: 2013, week: -1, statKey: FantasyConstants.STAT_RECEPTIONS, statValue: receptionsProjected),
							new Stat(season: 2013, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: rushingYardsProjected),
							new Stat(season: 2013, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: rushingTouchdownsProjected),
					])
		}
		else if(position == Player.POSITION_WR || position == Player.POSITION_TE) {
			for(s in stats) {
				// Set flag for valid year
				if(!hasValidYear && s.season == year-1) {
					hasValidYear = true
				}

				if(s.season == year-1 && s.week == -1 && s.statKey == FantasyConstants.STAT_RECEPTION_YARDS) {
					receptionYardsLastYear = s.statValue
				}
				else if(s.season == year-1 && s.week == -1 && s.statKey == FantasyConstants.STAT_RECEPTION_TOUCHDOWNS) {
					receptionTouchdownsLastYear = s.statValue
				}
				else if(s.season == year-1 && s.week == -1 && s.statKey == FantasyConstants.STAT_RECEPTIONS) {
					receptionsLastYear = s.statValue
				}
			}

			/*
			Make sure the year we want to project is supported with stats from the previous year.

			i.e. year = 2002, we need stats from 2001.
			 */
			if (!hasValidYear)
				throw new Exception("Invalid year.")

			def idx = numStartable*numOwners -1
			def receptionYardsResult = Stat.createCriteria().listDistinct {
				eq("season", year-1)
				eq("statKey", FantasyConstants.STAT_RECEPTION_YARDS)
				player {
					eq("position", position)
				}
				maxResults numStartable*numOwners
				order "statValue", "desc"
				cache true
			}

			def receptionTouchdownsResult = Stat.createCriteria().listDistinct {
				eq("season", year-1)
				eq("statKey", FantasyConstants.STAT_RECEPTION_TOUCHDOWNS)
				player {
					eq("position", position)
				}
				maxResults numStartable*numOwners
				order "statValue", "desc"
				cache true
			}

			def receptionsResult = Stat.createCriteria().listDistinct {
				eq("season", year-1)
				eq("statKey", FantasyConstants.STAT_RECEPTIONS)
				player {
					eq("position", position)
				}
				maxResults numStartable*numOwners
				order "statValue", "asc"
				cache true
			}

			def receptionYardsCorrelation = getCorrelation(position, FantasyConstants.STAT_RECEPTION_YARDS)
			def receptionTouchdownsCorrelation = getCorrelation(position, FantasyConstants.STAT_RECEPTION_TOUCHDOWNS)
			def receptionsCorrelation = getCorrelation(position, FantasyConstants.STAT_RECEPTIONS)

			def receptionYardsProjected = (receptionYardsCorrelation * receptionYardsLastYear) + ( (1 - receptionYardsCorrelation) * receptionYardsResult[idx].statValue)
			def receptionTouchdownsProjected = (receptionTouchdownsCorrelation * receptionTouchdownsLastYear) + ( (1 - receptionTouchdownsCorrelation) * receptionTouchdownsResult[idx].statValue)
			def receptionsProjected = (receptionsCorrelation * receptionsLastYear) + ( (1 - receptionsCorrelation) * receptionsResult[idx].statValue)

			return system.calculateScore(
					[
							new Stat(season: 2013, week: -1, statKey: FantasyConstants.STAT_RECEPTION_YARDS, statValue: receptionYardsProjected),
							new Stat(season: 2013, week: -1, statKey: FantasyConstants.STAT_RECEPTION_TOUCHDOWNS, statValue: receptionTouchdownsProjected),
							new Stat(season: 2013, week: -1, statKey: FantasyConstants.STAT_RECEPTIONS, statValue: receptionsProjected)
					])
		}
		else if(position == Player.POSITION_DEF || position == Player.POSITION_K) {
			// Figure out what this particular player had for fantasy points last season.
			def query = FantasyPoints.where {
				player == this && (projection == null || projection == false) && season == year-1 && week == -1
			}
			def fp = query.find()

			// Grab the top (numOwners * numStartable) players.  The last one in the
			// result set is our "average" player.
			def avgPlayerResult = FantasyPoints.createCriteria().listDistinct {
				eq("season", year-1)
				eq("week", -1)
				ne("projection", true)
				eq("scoringSystem", system)

				player {
					eq("position", position)
				}

				maxResults numOwners * numStartable
				order "points", "desc"
				cache true
			}

			// Fetch the correlation factor.
			def correlation = getCorrelation(position, null)

			// Figure out projected points
			return (fp.points * correlation) + (avgPlayerResult[ (numOwners * numStartable) - 1 ].points * (1-correlation))
		}

		log.info("Position ${ position } doesn't match anything, so we'll just return 0.")
		return 0
	}

    /**
     * Calculates the correlation of points for a position between two seasons.  Optionally, a stat
     * can be specified to calculate the correlation exclusively of the points yielded by that stat.
     * In that case, a scoring system needs to be specified.
     *
     * @param position      The position to determine correlation for.
     * @param stat          (optional) A stat for the specified position to calculate correlation for.
     * @param system        (optional) A scoring system that can translate the stat value to fantasy points.
     * @param season1       The first season of the correlation.
     * @param season2       The second season of the correlation.
     * @return              A correlation value.
     */
    static def getCorrelation(position, stat, IFantasyScoringSystem system, season1, season2) {
        // Returns all players at a position that have FantasyPoint records for both seasons.
        def results
        if (stat == null) {
            results = Player.executeQuery("from Player p inner join p.fantasyPoints f1 with f1.season = ? and f1.week = -1 " +
                    "inner join p.fantasyPoints f2 with f2.season = ? and f2.week = -1 where p.position = ?", [season1, season2, position])
        }
        else {
            results = Player.executeQuery("from Player p inner join p.stats s1 with s1.season = ? and s1.week = -1 and s1.statKey = ? " +
                    "inner join p.stats s2 with s2.season = ? and s2.week = -1 and s2.statKey = ? " +
                    "where p.position = ?", [season1, stat, season2, stat, position])
        }

        def x = 0
        def y = 0
        def xy = 0
        def x2 = 0
        def y2 = 0

        /*
        Iterate through the results to calculate xy, x**2, and y**2
         */
        for(r in results) {
            def points1, points2

            if (stat == null) {
                points1 = r[1].points
                points2 = r[2].points
            }
            else {
                points1 = system.calculateScore([r[1]])
                points2 = system.calculateScore([r[2]])
            }

            x += points1
            y += points2
            xy += points1 * points2
            x2 += points1**2
            y2 += points2**2
        }

        def correlation = (results.size()*xy - x*y)/
                Math.sqrt(Double.valueOf(results.size()*x2 - x**2)*Double.valueOf(results.size()*y2 - y**2))

        return correlation
    }

	/**
	 * Simple method to return a static correlation that is consistent for all players
	 * at the provided position.
	 *
	 * @param position
	 */
	static def getCorrelation(position, stat) {
		if(position == POSITION_QB) {
			if(stat == FantasyConstants.STAT_PASSING_YARDS) {
				return 0.5
			}
			else if(stat == FantasyConstants.STAT_PASSING_TOUCHDOWNS) {
				return 0.37
			}
			else if(stat == FantasyConstants.STAT_INTERCEPTIONS) {
				return 0.08
			}
			else if(stat == FantasyConstants.STAT_RUSHING_YARDS) {
				return 0.78
			}
			else if(stat == FantasyConstants.STAT_RUSHING_TOUCHDOWNS) {
				return 0.5
			}
		}
		else if(position == POSITION_RB) {
			if(stat == FantasyConstants.STAT_RUSHING_YARDS) {
				return 0.5
			}
			else if(stat == FantasyConstants.STAT_RUSHING_TOUCHDOWNS) {
				return 0.5
			}
			else if(stat == FantasyConstants.STAT_RECEPTIONS) {
				return 0.54
			}
			else if(stat == FantasyConstants.STAT_RECEPTION_YARDS) {
				return 0.51
			}
			else if(stat == FantasyConstants.STAT_RECEPTION_TOUCHDOWNS) {
				return 0.29
			}
		}
		else if(position == POSITION_WR) {
			if(stat == FantasyConstants.STAT_RECEPTION_YARDS) {
				return 0.58
			}
			else if(stat == FantasyConstants.STAT_RECEPTION_TOUCHDOWNS) {
				return 0.38
			}
			else if(stat == FantasyConstants.STAT_RECEPTIONS) {
				return 0.64
			}
		}
		else if(position == POSITION_TE) {
			if(stat == FantasyConstants.STAT_RECEPTION_YARDS) {
				return 0.74
			}
			else if(stat == FantasyConstants.STAT_RECEPTION_TOUCHDOWNS) {
				return 0.44
			}
			else if(stat == FantasyConstants.STAT_RECEPTIONS) {
				return 0.65
			}
		}
		else if(position == POSITION_DEF) {
			return 0.1
		}
		else if(position == POSITION_K) {
			return 0.1
		}
	}

    /**
     * Calculates standard deviation for a list of numbers
     *
     * @param values    The numbers to calculate standard deviation for.
     * @return          The standard deviation.
     */
    static def calculateStandardDeviation(values) {
        // Make sure there's something in there
        if (values.size == 0) {
            return 0
        }

        // Calculate mean
        def mean = values.sum()/values.size()

        // Diff from mean, squared
        def diff_squared = []
        for(v in values) {
            diff_squared << (v - mean)**2
        }

        return Math.sqrt(diff_squared.sum()/values.size())
    }
}

package com.traderapist.models

import org.springframework.context.ApplicationContext

class FantasyPoints {

    Integer season
    Integer week
    Double points
	Boolean projection
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
        cache true
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

    static def process() {
        def session = ApplicationContext.sessionFactory.currentSession

        def jobs = FantasyPointsJob.findAllByCompleted(false)

        jobs.each {     job ->
            /*
             * Get all the necessary attributes
             *
             * Generate Points
             * - Fantasy Team Id
             * - Position
             *
             * Project Points
             *- Fantasy Team Id
             */

            positions.each {    position ->
                log.info "Generating fantasy points for ${ job.fantasyTeam.name } for ${ position }"

                FantasyPoints.generatePoints(job.fantasyTeam, position, job.season, job.week)

//                // Do HTTP GETs to /fantasyPointsController/generatePoints
//                httpGenerate.request(Method.GET, JSON) {
//                    url.path = "/fantasyPointsController/generatePoints?fantasy_team_id=${ job.fantasyTeam.id }&position=${ position }&season=${ job.season }&week=${ job.week }"
//                    response.success = {    resp, json ->
//                        print "Successful - ${ json }"
//                    }
//                }
            }

            log.info "Projecting fantasy points for ${ job.fantasyTeam.name }"

            FantasyPoints.projectPoints(job.fantasyTeam)

            // Do HTTP GET to /fantasyPointsController/projectPoints
//            httpProject.request(Method.GET, JSON) {
//                url.path = "/fantasyPointsController/projectPoints?fantasy_team_id=${ job.fantasyTeam.id }"
//                response.success = {    resp, json ->
//                    print "Successful - ${ json }"
//                }
//            }

            job.completed = true
            job.save(flush: true)
        }
    }

    /**
     * Iterates through ALL players and calculates their fantasy points for each week and season for
     * the specified FantasyTeam (and their scoring system).  These points are written to the database
     * as FantasyPoints.
     *
     * The scoring system is determined by the one registered to the FantasyTeam passed in as an id.
     */
    static def generatePoints(fantasyTeam, position, season, week) {
        // Grab all the players - either all or at a certain position
        def players
        if(position) {
            players = Player.findAllByPosition(position)
        }
        else {
            players = Player.findAll()
        }

        long start = System.currentTimeMillis()
        for(player in players) {
            print("Calculating fantasy points for ${player.name}")
            player.computeFantasyPoints(fantasyTeam, season, week)
        }
        long end = System.currentTimeMillis()
        print("Generated fantasy points for all players in ${ (end-start)/1000.0 } seconds")

        return true
    }

    /**
     * Creates projections for players of a particular position for the upcoming (or specified) season.  This expects URL parameters as
     * inputs:
     *
     * fantasy_team_id - The id of the fantasy team we want to do projections for.
     *
     * @return    A success message if everything works, or a failure message explaining the problem.
     */
    static def projectPoints(fantasyTeam) {
        def numStarters = [:]
        fantasyTeam.fantasyTeamStarters.each {  starter ->
            numStarters[starter.position] = starter.numStarters
        }

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
            // Check for duplicates
            if(FantasyPoints.findByNumOwnersAndNumStartableAndPlayerAndProjectionAndSeasonAndScoringSystem(
                    fantasyTeam.numOwners,
                    numStarters[p.position],
                    p,
                    true,
                    fantasyTeam.season,
                    fantasyTeam.scoringSystem
            ) != null) {
                continue
            }

            def points = p.calculateProjectedPoints(
                    fantasyTeam.season,
                    numStarters[p.position],
                    fantasyTeam.numOwners,
                    fantasyTeam.scoringSystem)

            def playerFantasyProjection = new FantasyPoints(
                    player: p,
                    season: fantasyTeam.season,
                    week: -1,
                    points: points,
                    scoringSystem: fantasyTeam.scoringSystem,
                    projection: true,
                    numOwners: fantasyTeam.numOwners,
                    numStartable: numStarters[p.position]
            ).save()

            print("saved ${ fantasyTeam.season } projection for ${ p.name }")
        }

        print "Point projection for ${ fantasyTeam.season } completed"
    }
}

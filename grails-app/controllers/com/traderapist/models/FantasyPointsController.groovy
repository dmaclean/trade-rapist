package com.traderapist.models

import grails.plugins.springsecurity.Secured
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.access.annotation.Secured

@Secured(['ROLE_ADMIN'])
class FantasyPointsController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [fantasyPointsInstanceList: FantasyPoints.list(params), fantasyPointsInstanceTotal: FantasyPoints.count()]
    }

    def create() {
        [fantasyPointsInstance: new FantasyPoints(params)]
    }

    /**
     * Iterates through ALL players and calculates their fantasy points for each week and season for
     * the specified FantasyTeam (and their scoring system).  These points are written to the database
     * as FantasyPoints.
     *
     * The scoring system is determined by the one registered to the FantasyTeam passed in as an id.
     */
    def generatePoints() {
        def fantasyTeam = FantasyTeam.get(params.fantasy_team_id)
	    if(fantasyTeam == null) {
		    render "Invalid fantasy team id ${ params.fantasy_team_id }"
		    return
	    }

	    def players
	    if(params["position"]) {
		    players = Player.findAllByPosition(params["position"])
	    }
	    else {
		    players = Player.findAll()
	    }

	    long start = System.currentTimeMillis()
	    for(player in players) {
            log.info("Calculating fantasy points for ${player.name}")
            player.computeFantasyPoints(fantasyTeam)
        }
	    long end = System.currentTimeMillis()
	    print("Generated fantasy points for all players in ${ (end-start)/1000.0 } seconds")
    }

	/**
	 * Creates projections for players of a particular position for the upcoming (or specified) season.  This expects URL parameters as
	 * inputs:
	 *
	 * fantasy_team_id - The id of the fantasy team we want to do projections for.
	 *
	 * @return    A success message if everything works, or a failure message explaining the problem.
	 */
	def projectPoints() {
		def fantasyTeam = FantasyTeam.get(params.fantasy_team_id)
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
			render "saved ${ params["season"] } projection for ${ p.name }<br/>"
			log.info("saved ${ params["season"] } projection for ${ p.name }")
		}

		render "Point projection for ${params["season"]} completed"
	}

    def save() {
        def fantasyPointsInstance = new FantasyPoints(params)
	    fantasyPointsInstance.scoringSystem = ScoringSystem.get(params.scoring_system_id)
        if (!fantasyPointsInstance.save(flush: true)) {
            render(view: "create", model: [fantasyPointsInstance: fantasyPointsInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'fantasyPoints.label', default: 'FantasyPoints'), fantasyPointsInstance.id])
        redirect(action: "show", id: fantasyPointsInstance.id)
    }

    def show(Long id) {
        def fantasyPointsInstance = FantasyPoints.get(id)
        if (!fantasyPointsInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'fantasyPoints.label', default: 'FantasyPoints'), id])
            redirect(action: "list")
            return
        }

        [fantasyPointsInstance: fantasyPointsInstance]
    }

    def edit(Long id) {
        def fantasyPointsInstance = FantasyPoints.get(id)
        if (!fantasyPointsInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'fantasyPoints.label', default: 'FantasyPoints'), id])
            redirect(action: "list")
            return
        }

        [fantasyPointsInstance: fantasyPointsInstance]
    }

    def update(Long id, Long version) {
        def fantasyPointsInstance = FantasyPoints.get(id)
        if (!fantasyPointsInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'fantasyPoints.label', default: 'FantasyPoints'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (fantasyPointsInstance.version > version) {
                fantasyPointsInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'fantasyPoints.label', default: 'FantasyPoints')] as Object[],
                          "Another user has updated this FantasyPoints while you were editing")
                render(view: "edit", model: [fantasyPointsInstance: fantasyPointsInstance])
                return
            }
        }

        fantasyPointsInstance.properties = params

        if (!fantasyPointsInstance.save(flush: true)) {
            render(view: "edit", model: [fantasyPointsInstance: fantasyPointsInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'fantasyPoints.label', default: 'FantasyPoints'), fantasyPointsInstance.id])
        redirect(action: "show", id: fantasyPointsInstance.id)
    }

    def delete(Long id) {
        def fantasyPointsInstance = FantasyPoints.get(id)
        if (!fantasyPointsInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'fantasyPoints.label', default: 'FantasyPoints'), id])
            redirect(action: "list")
            return
        }

        try {
            fantasyPointsInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'fantasyPoints.label', default: 'FantasyPoints'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'fantasyPoints.label', default: 'FantasyPoints'), id])
            redirect(action: "show", id: id)
        }
    }
}

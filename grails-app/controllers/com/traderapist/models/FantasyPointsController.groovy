package com.traderapist.models

import grails.plugins.springsecurity.Secured
import org.springframework.dao.DataIntegrityViolationException

@Secured(["hasRole('admin')"])
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
     * Iterates through ALL players and calculates their fantasy points for each week and season.  These
     * points are written to the database as FantasyPoints.
     *
     * The scoring system is determined by the "system" request parameter that is passed in.
     */
    def generatePoints() {
        Class clazz = Class.forName("com.traderapist.scoringsystem.${params["system"]}", true, Thread.currentThread().contextClassLoader)
        def scoringSystem = clazz.newInstance()

	    def players = null
	    if(params["position"]) {
		    players = Player.findAllByPosition(params["position"])
	    }
	    else {
		    players = Player.findAll()
	    }

	    long start = System.currentTimeMillis()
	    for(player in players) {
            log.info("Calculating fantasy points for ${player.name}")
            player.computeFantasyPoints(scoringSystem)
        }
	    long end = System.currentTimeMillis()
	    print("Generated fantasy points for all players in ${ (end-start)/1000.0 } seconds")
    }

	def projectPoints() {
		/*
		 * Make sure the provided scoring system is valid.
		 */
		def scoringSystem
		try {
			Class clazz = Class.forName("com.traderapist.scoringsystem.${params["system"]}", true, Thread.currentThread().contextClassLoader)
			scoringSystem = clazz.newInstance()
		}
		catch(Exception e) {
			render "Invalid scoring system - ${params["system"]}"
			return
		}

		/*
		 * Make sure season, num_startable, num_owners, and position are valid
		 */
		try { Integer.parseInt(params["season"]) } catch(Exception e) { render "season must be an integer"; return }
		try { Integer.parseInt(params["num_startable"]) } catch(Exception e) { render "num_startable must be an integer"; return }
		try { Integer.parseInt(params["num_owners"]) } catch(Exception e) { render "num_owners must be an integer"; return }
		def positionRegex = "/${Player.POSITION_QB}|${Player.POSITION_RB}|${Player.POSITION_WR}|${Player.POSITION_TE}|${Player.POSITION_DEF}|${Player.POSITION_K}/"
		if(params["position"] == null || !(params["position"] =~ positionRegex)) {
			render "position must be one of ${Player.POSITION_QB}, ${Player.POSITION_RB}, ${Player.POSITION_WR}, ${Player.POSITION_TE}, ${Player.POSITION_DEF}, ${Player.POSITION_K}"
		}

		/*
		 * Grab all players who had stats in the previous season.
		 */
		def previousSeason = Integer.parseInt(params["season"]) - 1
		def positions = (params["position"]) ? params["position"] : [Player.POSITION_QB, Player.POSITION_RB, Player.POSITION_WR, Player.POSITION_TE]
		def players = Player.createCriteria().listDistinct {
			'in'("position", positions)
			stats {
				eq("season", previousSeason)
				eq("week", -1)
			}
		}


		for(p in players) {
			// Check for duplicates
			if(FantasyPoints.findByNumOwnersAndNumStartableAndPlayerAndProjectionAndSeasonAndSystem(
					Integer.parseInt(params["num_owners"]),
					Integer.parseInt(params["num_startable"]),
					p,
					true,
					Integer.parseInt(params["season"]),
					params["system"]
			) != null) {
				continue
			}

			def points = p.calculateProjectedPoints(
					Integer.parseInt(params["season"]),
					Integer.parseInt(params["num_startable"]),
					Integer.parseInt(params["num_owners"]),
					scoringSystem)

			def playerFantasyProjection = new FantasyPoints(
					player: p,
					season: Integer.parseInt(params["season"]),
					week: -1,
					points: points,
					system: params["system"],
					projection: true,
					numOwners: Integer.parseInt(params["num_owners"]),
					numStartable: Integer.parseInt(params["num_startable"])
			).save()
			render "saved ${ params["season"] } projection for ${ p.name }<br/>"
			log.info("saved ${ params["season"] } projection for ${ p.name }")
		}

		render "Point projection for ${params["season"]} completed"
	}

    def save() {
        def fantasyPointsInstance = new FantasyPoints(params)
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

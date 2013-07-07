package com.traderapist.models

import grails.converters.JSON
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.access.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_USER'])
class FantasyTeamPlayerController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		[fantasyTeamPlayerInstanceList: FantasyTeamPlayer.list(params), fantasyTeamPlayerInstanceTotal: FantasyTeamPlayer.count()]
	}

	def create() {
		[fantasyTeamPlayerInstance: new FantasyTeamPlayer(params)]
	}

	def save() {
		def fantasyTeamPlayerInstance = new FantasyTeamPlayer(params)
		if (!fantasyTeamPlayerInstance.save(flush: true)) {
			render(view: "create", model: [fantasyTeamPlayerInstance: fantasyTeamPlayerInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'fantasyTeamPlayer.label', default: 'FantasyTeamPlayer'), fantasyTeamPlayerInstance.id])
		redirect(action: "show", id: fantasyTeamPlayerInstance.id)
	}

	/**
	 * Takes in a JSON structure containing the fantasy team and list of player ids and creates a FantasyTeamPlayer instance.  This
	 * will get invoked when a user clicks "Save Roster" on the draft page.
	 *
	 * JSON should look like:
	 *
	 * {
	 *      "team" : <team id>,
	 *      "players" : [   { "id":1, <other stuff> },
	 *                      { "id":2, <other stuff> },
	 *                      ...,
	 *                      { "id":n-1, <other stuff> },
	 *                      { "id":n, <other stuff> }
	 *                  ]
	 * }
	 */
	def saveAllFromDraft() {
		// Grab the JSON out of the request, parse it into objects that we can use, and grab those objects.
		def data = JSON.parse(request)
		def fantasyTeam = FantasyTeam.get(data.team)

		// Create a list of player ids so we can query for the actual objects in a single query.
		def ids = []
		for(p in data.players) {
			ids << new Long(p.id)
		}
		def players = Player.findAllByIdInList(ids)

		// Wipe out our existing players for this fantasy team
		FantasyTeamPlayer.deleteAll(FantasyTeamPlayer.findAllByFantasyTeam(fantasyTeam))

		// Create a FantasyTeamPlayer entry for each of the players we've drafted.
		for(p in players) {
			new FantasyTeamPlayer(player: p, fantasyTeam: fantasyTeam).save()
		}

		render "success"
	}

	def show(Long id) {
		def fantasyTeamPlayerInstance = FantasyTeamPlayer.get(id)
		if (!fantasyTeamPlayerInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fantasyTeamPlayer.label', default: 'FantasyTeamPlayer'), id])
			redirect(action: "list")
			return
		}

		[fantasyTeamPlayerInstance: fantasyTeamPlayerInstance]
	}

	def edit(Long id) {
		def fantasyTeamPlayerInstance = FantasyTeamPlayer.get(id)
		if (!fantasyTeamPlayerInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fantasyTeamPlayer.label', default: 'FantasyTeamPlayer'), id])
			redirect(action: "list")
			return
		}

		[fantasyTeamPlayerInstance: fantasyTeamPlayerInstance]
	}

	def update(Long id, Long version) {
		def fantasyTeamPlayerInstance = FantasyTeamPlayer.get(id)
		if (!fantasyTeamPlayerInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fantasyTeamPlayer.label', default: 'FantasyTeamPlayer'), id])
			redirect(action: "list")
			return
		}

		if (version != null) {
			if (fantasyTeamPlayerInstance.version > version) {
				fantasyTeamPlayerInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[message(code: 'fantasyTeamPlayer.label', default: 'FantasyTeamPlayer')] as Object[],
						"Another user has updated this FantasyTeamPlayer while you were editing")
				render(view: "edit", model: [fantasyTeamPlayerInstance: fantasyTeamPlayerInstance])
				return
			}
		}

		fantasyTeamPlayerInstance.properties = params

		if (!fantasyTeamPlayerInstance.save(flush: true)) {
			render(view: "edit", model: [fantasyTeamPlayerInstance: fantasyTeamPlayerInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'fantasyTeamPlayer.label', default: 'FantasyTeamPlayer'), fantasyTeamPlayerInstance.id])
		redirect(action: "show", id: fantasyTeamPlayerInstance.id)
	}

	def delete(Long id) {
		def fantasyTeamPlayerInstance = FantasyTeamPlayer.get(id)
		if (!fantasyTeamPlayerInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fantasyTeamPlayer.label', default: 'FantasyTeamPlayer'), id])
			redirect(action: "list")
			return
		}

		try {
			fantasyTeamPlayerInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'fantasyTeamPlayer.label', default: 'FantasyTeamPlayer'), id])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'fantasyTeamPlayer.label', default: 'FantasyTeamPlayer'), id])
			redirect(action: "show", id: id)
		}
	}
}

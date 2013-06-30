package com.traderapist.models

import org.springframework.dao.DataIntegrityViolationException

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

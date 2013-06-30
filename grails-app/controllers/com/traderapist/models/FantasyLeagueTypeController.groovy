package com.traderapist.models

import org.springframework.dao.DataIntegrityViolationException

class FantasyLeagueTypeController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		[fantasyLeagueTypeInstanceList: FantasyLeagueType.list(params), fantasyLeagueTypeInstanceTotal: FantasyLeagueType.count()]
	}

	def create() {
		[fantasyLeagueTypeInstance: new FantasyLeagueType(params)]
	}

	def save() {
		def fantasyLeagueTypeInstance = new FantasyLeagueType(params)
		if (!fantasyLeagueTypeInstance.save(flush: true)) {
			render(view: "create", model: [fantasyLeagueTypeInstance: fantasyLeagueTypeInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'fantasyLeagueType.label', default: 'FantasyLeagueType'), fantasyLeagueTypeInstance.id])
		redirect(action: "show", id: fantasyLeagueTypeInstance.id)
	}

	def show(Long id) {
		def fantasyLeagueTypeInstance = FantasyLeagueType.get(id)
		if (!fantasyLeagueTypeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fantasyLeagueType.label', default: 'FantasyLeagueType'), id])
			redirect(action: "list")
			return
		}

		[fantasyLeagueTypeInstance: fantasyLeagueTypeInstance]
	}

	def edit(Long id) {
		def fantasyLeagueTypeInstance = FantasyLeagueType.get(id)
		if (!fantasyLeagueTypeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fantasyLeagueType.label', default: 'FantasyLeagueType'), id])
			redirect(action: "list")
			return
		}

		[fantasyLeagueTypeInstance: fantasyLeagueTypeInstance]
	}

	def update(Long id, Long version) {
		def fantasyLeagueTypeInstance = FantasyLeagueType.get(id)
		if (!fantasyLeagueTypeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fantasyLeagueType.label', default: 'FantasyLeagueType'), id])
			redirect(action: "list")
			return
		}

		if (version != null) {
			if (fantasyLeagueTypeInstance.version > version) {
				fantasyLeagueTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[message(code: 'fantasyLeagueType.label', default: 'FantasyLeagueType')] as Object[],
						"Another user has updated this FantasyLeagueType while you were editing")
				render(view: "edit", model: [fantasyLeagueTypeInstance: fantasyLeagueTypeInstance])
				return
			}
		}

		fantasyLeagueTypeInstance.properties = params

		if (!fantasyLeagueTypeInstance.save(flush: true)) {
			render(view: "edit", model: [fantasyLeagueTypeInstance: fantasyLeagueTypeInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'fantasyLeagueType.label', default: 'FantasyLeagueType'), fantasyLeagueTypeInstance.id])
		redirect(action: "show", id: fantasyLeagueTypeInstance.id)
	}

	def delete(Long id) {
		def fantasyLeagueTypeInstance = FantasyLeagueType.get(id)
		if (!fantasyLeagueTypeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fantasyLeagueType.label', default: 'FantasyLeagueType'), id])
			redirect(action: "list")
			return
		}

		try {
			fantasyLeagueTypeInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'fantasyLeagueType.label', default: 'FantasyLeagueType'), id])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'fantasyLeagueType.label', default: 'FantasyLeagueType'), id])
			redirect(action: "show", id: id)
		}
	}
}

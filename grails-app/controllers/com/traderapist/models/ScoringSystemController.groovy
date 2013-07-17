package com.traderapist.models

import org.springframework.dao.DataIntegrityViolationException

class ScoringSystemController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		[scoringSystemInstanceList: ScoringSystem.list(params), scoringSystemInstanceTotal: ScoringSystem.count()]
	}

	def create() {
		[scoringSystemInstance: new ScoringSystem(params)]
	}

	def save() {
		def scoringSystemInstance = new ScoringSystem(params)
		if (!scoringSystemInstance.save(flush: true)) {
			render(view: "create", model: [scoringSystemInstance: scoringSystemInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'scoringSystem.label', default: 'ScoringSystem'), scoringSystemInstance.id])
		redirect(action: "show", id: scoringSystemInstance.id)
	}

	def show(Long id) {
		def scoringSystemInstance = ScoringSystem.get(id)
		if (!scoringSystemInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'scoringSystem.label', default: 'ScoringSystem'), id])
			redirect(action: "list")
			return
		}

		[scoringSystemInstance: scoringSystemInstance]
	}

	def edit(Long id) {
		def scoringSystemInstance = ScoringSystem.get(id)
		if (!scoringSystemInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'scoringSystem.label', default: 'ScoringSystem'), id])
			redirect(action: "list")
			return
		}

		[scoringSystemInstance: scoringSystemInstance]
	}

	def update(Long id, Long version) {
		def scoringSystemInstance = ScoringSystem.get(id)
		if (!scoringSystemInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'scoringSystem.label', default: 'ScoringSystem'), id])
			redirect(action: "list")
			return
		}

		if (version != null) {
			if (scoringSystemInstance.version > version) {
				scoringSystemInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[message(code: 'scoringSystem.label', default: 'ScoringSystem')] as Object[],
						"Another user has updated this ScoringSystem while you were editing")
				render(view: "edit", model: [scoringSystemInstance: scoringSystemInstance])
				return
			}
		}

		scoringSystemInstance.properties = params

		if (!scoringSystemInstance.save(flush: true)) {
			render(view: "edit", model: [scoringSystemInstance: scoringSystemInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'scoringSystem.label', default: 'ScoringSystem'), scoringSystemInstance.id])
		redirect(action: "show", id: scoringSystemInstance.id)
	}

	def delete(Long id) {
		def scoringSystemInstance = ScoringSystem.get(id)
		if (!scoringSystemInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'scoringSystem.label', default: 'ScoringSystem'), id])
			redirect(action: "list")
			return
		}

		try {
			scoringSystemInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'scoringSystem.label', default: 'ScoringSystem'), id])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'scoringSystem.label', default: 'ScoringSystem'), id])
			redirect(action: "show", id: id)
		}
	}
}

package com.traderapist.models

import org.springframework.dao.DataIntegrityViolationException

class ScoringRuleController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		[scoringRuleInstanceList: ScoringRule.list(params), scoringRuleInstanceTotal: ScoringRule.count()]
	}

	def create() {
		[scoringRuleInstance: new ScoringRule(params)]
	}

	def save() {
		def scoringRuleInstance = new ScoringRule(params)
		if (!scoringRuleInstance.save(flush: true)) {
			render(view: "create", model: [scoringRuleInstance: scoringRuleInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'scoringRule.label', default: 'ScoringRule'), scoringRuleInstance.id])
		redirect(action: "show", id: scoringRuleInstance.id)
	}

	def show(Long id) {
		def scoringRuleInstance = ScoringRule.get(id)
		if (!scoringRuleInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'scoringRule.label', default: 'ScoringRule'), id])
			redirect(action: "list")
			return
		}

		[scoringRuleInstance: scoringRuleInstance]
	}

	def edit(Long id) {
		def scoringRuleInstance = ScoringRule.get(id)
		if (!scoringRuleInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'scoringRule.label', default: 'ScoringRule'), id])
			redirect(action: "list")
			return
		}

		[scoringRuleInstance: scoringRuleInstance]
	}

	def update(Long id, Long version) {
		def scoringRuleInstance = ScoringRule.get(id)
		if (!scoringRuleInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'scoringRule.label', default: 'ScoringRule'), id])
			redirect(action: "list")
			return
		}

		if (version != null) {
			if (scoringRuleInstance.version > version) {
				scoringRuleInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[message(code: 'scoringRule.label', default: 'ScoringRule')] as Object[],
						"Another user has updated this ScoringRule while you were editing")
				render(view: "edit", model: [scoringRuleInstance: scoringRuleInstance])
				return
			}
		}

		scoringRuleInstance.properties = params

		if (!scoringRuleInstance.save(flush: true)) {
			render(view: "edit", model: [scoringRuleInstance: scoringRuleInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'scoringRule.label', default: 'ScoringRule'), scoringRuleInstance.id])
		redirect(action: "show", id: scoringRuleInstance.id)
	}

	def delete(Long id) {
		def scoringRuleInstance = ScoringRule.get(id)
		if (!scoringRuleInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'scoringRule.label', default: 'ScoringRule'), id])
			redirect(action: "list")
			return
		}

		try {
			scoringRuleInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'scoringRule.label', default: 'ScoringRule'), id])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'scoringRule.label', default: 'ScoringRule'), id])
			redirect(action: "show", id: id)
		}
	}
}

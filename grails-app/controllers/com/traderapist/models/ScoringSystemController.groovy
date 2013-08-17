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

	/**
	 * This gets hit when a user creates a new fantasy team, and, as a result, a scoring
	 * system to go along with that team.
	 *
	 * Here we create the scoring system and check if any of the scoring rules already
	 * exist.  If not, we create a new one.  Either way, the rules get added to the scoring
	 * system.
	 *
	 * @return
	 */
	def createSystemAndRules() {
		def fantasyTeam = FantasyTeam.get(params.fantasy_team_id)
		def scoringSystem = new ScoringSystem(name: params["ss_name"], fantasyTeam: fantasyTeam, scoringRules: new HashSet<ScoringRule>())

		if(!scoringSystem.save()) {
			render "error: Unable to save scoring system."
			return
		}

		for(p in params) {
			if(p.key.contains("stat_multiplier_")) {
				def statKey = Integer.parseInt(p.key.split("_")[2])
				def multiplier = Double.parseDouble(p.value)
				def sr = ScoringRule.findByStatKeyAndMultiplier(statKey, multiplier)

				if(sr == null) {
					sr = new ScoringRule(statKey: statKey, multiplier: multiplier).save()
				}

				scoringSystem.scoringRules.add(sr)
			}
		}

		scoringSystem.save()

		def cal = Calendar.getInstance()
		def currentYear = cal.get(Calendar.YEAR)
		def seasons = 2001..currentYear

		def projection = new FantasyPointsJob(fantasyTeam: fantasyTeam, completed: false, season: currentYear, week: -1, projection: true).save()
		print "Successfully created Fantasy projections job for ${ fantasyTeam.name }"

		seasons.each {    season ->
			[-1,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17].each {   week ->
				def actual = new FantasyPointsJob(fantasyTeam: fantasyTeam, completed: false, season: season, week: week, projection: false).save()
			}
		}

		flash.info = "Scoring system ${ params.ss_name } successfully created!"

		render "success"
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

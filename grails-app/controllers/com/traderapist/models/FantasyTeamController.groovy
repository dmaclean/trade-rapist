package com.traderapist.models

import com.traderapist.security.Role
import com.traderapist.security.User
import grails.converters.JSON
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.access.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_USER'])
class FantasyTeamController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def springSecurityService

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		User user = springSecurityService.getCurrentUser()

		params.max = Math.min(max ?: 10, 100)
		if(params.json) {
			if(!springSecurityService.isLoggedIn()) {
				render ""
			}
			else {
				render FantasyTeam.findAllByUser(user) as JSON
			}
		}
		else {
			def fantasyTeams

			// For regular users, just grab their teams.  For admins, get every team.
			if(user.getAuthorities().contains(Role.findByAuthority(Role.ROLE_USER))) {
				fantasyTeams = FantasyTeam.findAllByUser(user)
			}
			else {
				fantasyTeams = FantasyTeam.list(params)
			}

			[fantasyTeamInstanceList: fantasyTeams, fantasyTeamInstanceTotal: fantasyTeams.size()]
		}
	}

	def create() {
		def startersMap = [:]
		params.each {     key, value ->
			if(key ==~ /(${Player.POSITION_QB}|${Player.POSITION_RB}|${Player.POSITION_WR}|${Player.POSITION_TE}|${Player.POSITION_DEF}|${Player.POSITION_K})/) {
				startersMap[key] = value
			}
		}

		[fantasyTeamInstance: new FantasyTeam(params), starters: startersMap]
	}

	def save() {
		def fantasyTeamInstance = new FantasyTeam(params)

		/*
		 * Save off the fantasy team with starters
		 */
		if (!fantasyTeamInstance.save(flush: true)) {
			render(view: "create", model: [fantasyTeamInstance: fantasyTeamInstance])
			return
		}

		params.each {     key, value ->
			if(key ==~ /(${Player.POSITION_QB}|${Player.POSITION_RB}|${Player.POSITION_WR}|${Player.POSITION_TE}|${Player.POSITION_DEF}|${Player.POSITION_K})/) {
				def starter = new FantasyTeamStarter(position: key, numStarters: value, fantasyTeam: fantasyTeamInstance).save()
				log.info("Created FantasyTeamStarter ${ starter.toString() }")
			}
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'fantasyTeam.label', default: 'FantasyTeam'), fantasyTeamInstance.id])
		redirect(action: "show", id: fantasyTeamInstance.id)
	}

	def saveAjax() {
		def fantasyTeamInstance = new FantasyTeam(params)
		if (!fantasyTeamInstance.save(flush: true)) {
			render text: "error", status: 500
			return
		}

		params.each {     key, value ->
			if(key ==~ /(${Player.POSITION_QB}|${Player.POSITION_RB}|${Player.POSITION_WR}|${Player.POSITION_TE}|${Player.POSITION_DEF}|${Player.POSITION_K})/) {
				def starter = new FantasyTeamStarter(position: key, numStarters: value, fantasyTeam: fantasyTeamInstance).save()
				log.info("Created FantasyTeamStarter ${ starter.toString() }")
			}
		}

		render fantasyTeamInstance.id
	}

	def show(Long id) {
		def fantasyTeamInstance = FantasyTeam.get(id)
		if (!fantasyTeamInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fantasyTeam.label', default: 'FantasyTeam'), id])
			redirect(action: "list")
			return
		}

		withFormat {
			html fantasyTeamInstance: fantasyTeamInstance
			all fantasyTeamInstance: fantasyTeamInstance
			json { render fantasyTeamInstance as JSON }
		}
	}

	def edit(Long id) {
		def fantasyTeamInstance = FantasyTeam.get(id)
		if (!fantasyTeamInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fantasyTeam.label', default: 'FantasyTeam'), id])
			redirect(action: "list")
			return
		}

		def startersMap = [:]
		fantasyTeamInstance.fantasyTeamStarters.each { s ->
			startersMap[s.position] = s.numStarters
		}

		[fantasyTeamInstance: fantasyTeamInstance, starters: startersMap]
	}

	def update(Long id, Long version) {
		def fantasyTeamInstance = FantasyTeam.get(id)
		if (!fantasyTeamInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fantasyTeam.label', default: 'FantasyTeam'), id])
			redirect(action: "list")
			return
		}

		if (version != null) {
			if (fantasyTeamInstance.version > version) {
				fantasyTeamInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[message(code: 'fantasyTeam.label', default: 'FantasyTeam')] as Object[],
						"Another user has updated this FantasyTeam while you were editing")
				render(view: "edit", model: [fantasyTeamInstance: fantasyTeamInstance])
				return
			}
		}

		fantasyTeamInstance.properties = params

		fantasyTeamInstance.fantasyTeamStarters.each {  starter ->
			starter.numStarters = params[starter.position].toInteger()
			starter.save()
		}

		if (!fantasyTeamInstance.save(flush: true)) {
			render(view: "edit", model: [fantasyTeamInstance: fantasyTeamInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'fantasyTeam.label', default: 'FantasyTeam'), fantasyTeamInstance.id])
		redirect(action: "show", id: fantasyTeamInstance.id)
	}

	def delete(Long id) {
		def fantasyTeamInstance = FantasyTeam.get(id)
		if (!fantasyTeamInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fantasyTeam.label', default: 'FantasyTeam'), id])
			redirect(action: "list")
			return
		}

		try {
			fantasyTeamInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'fantasyTeam.label', default: 'FantasyTeam'), id])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'fantasyTeam.label', default: 'FantasyTeam'), id])
			redirect(action: "show", id: id)
		}
	}
}

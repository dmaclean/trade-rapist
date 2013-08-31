package com.traderapist.models

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.access.annotation.Secured

class AverageDraftPositionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	@Secured(['ROLE_ADMIN'])
	def index() {
        redirect(action: "list", params: params)
    }

	@Secured(['ROLE_ADMIN'])
    def list(Integer max) {
//        params.max = Math.min(max ?: 10, 100)
		def adpList = (params.season) ? AverageDraftPosition.findAllBySeason(params.season) : AverageDraftPosition.list(sort: "season", order: "desc")
        [averageDraftPositionInstanceList: adpList, averageDraftPositionInstanceTotal: AverageDraftPosition.count()]
    }

	@Secured(['ROLE_ADMIN'])
    def create() {
        [averageDraftPositionInstance: new AverageDraftPosition(params)]
    }

	/**
	 * Endpoint that accepts the contents of a CSV as a parameter and parses it out.
	 * Once parsed, if the player being referenced exists, their ADP will be saved for
	 * that season, along with their team affiliation.  Since we're not using player ids,
	 * if there are multiple players with the same name (i.e. Steve Smith, Adrian Peterson)
	 * then they are not saved and the user will be alerted that those players need to
	 * be manually resolved.
	 *
	 * @return
	 */
	def saveFromCSV() {
		def dupes = []
		def lines = params.input.split("\\|")

		lines.each {    line ->
			def pieces = line.split(",")

			def season = params.season.toInteger()
			def player = Player.findAllByName(pieces[0])
			if(player.size() > 1) {
				println "Found duplicate entries for ${ pieces[0] }"
				dupes << pieces[0]
			}
			else {
				def teamAbbr = pieces[1]
				def adpValue = pieces[3].toDouble()

				def adp = new AverageDraftPosition(player: player[0], season: season, adp: adpValue).save()

				def team = Team.findByAbbreviation(teamAbbr)
				def teamMembership = new TeamMembership(player: player, season: season, team: team).save()
			}
		}

		if(dupes.empty)         render "success"
		else {
			def responseStr = "The following players were duplicates:\n"

			dupes.each {    dupe ->
				responseStr += "${dupe}\n"
			}

			render responseStr
		}
	}

	@Secured(['ROLE_ADMIN'])
    def save() {
        def averageDraftPositionInstance = new AverageDraftPosition(params)
        if (!averageDraftPositionInstance.save(flush: true)) {
            render(view: "create", model: [averageDraftPositionInstance: averageDraftPositionInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'averageDraftPosition.label', default: 'AverageDraftPosition'), averageDraftPositionInstance.id])
        redirect(action: "show", id: averageDraftPositionInstance.id)
    }

	@Secured(['ROLE_ADMIN'])
    def show(Long id) {
        def averageDraftPositionInstance = AverageDraftPosition.get(id)
        if (!averageDraftPositionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'averageDraftPosition.label', default: 'AverageDraftPosition'), id])
            redirect(action: "list")
            return
        }

        [averageDraftPositionInstance: averageDraftPositionInstance]
    }

	@Secured(['ROLE_ADMIN'])
    def edit(Long id) {
        def averageDraftPositionInstance = AverageDraftPosition.get(id)
        if (!averageDraftPositionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'averageDraftPosition.label', default: 'AverageDraftPosition'), id])
            redirect(action: "list")
            return
        }

        [averageDraftPositionInstance: averageDraftPositionInstance]
    }

	@Secured(['ROLE_ADMIN'])
    def update(Long id, Long version) {
        def averageDraftPositionInstance = AverageDraftPosition.get(id)
        if (!averageDraftPositionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'averageDraftPosition.label', default: 'AverageDraftPosition'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (averageDraftPositionInstance.version > version) {
                averageDraftPositionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'averageDraftPosition.label', default: 'AverageDraftPosition')] as Object[],
                          "Another user has updated this AverageDraftPosition while you were editing")
                render(view: "edit", model: [averageDraftPositionInstance: averageDraftPositionInstance])
                return
            }
        }

        averageDraftPositionInstance.properties = params

        if (!averageDraftPositionInstance.save(flush: true)) {
            render(view: "edit", model: [averageDraftPositionInstance: averageDraftPositionInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'averageDraftPosition.label', default: 'AverageDraftPosition'), averageDraftPositionInstance.id])
        redirect(action: "show", id: averageDraftPositionInstance.id)
    }

	@Secured(['ROLE_ADMIN'])
    def delete(Long id) {
        def averageDraftPositionInstance = AverageDraftPosition.get(id)
        if (!averageDraftPositionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'averageDraftPosition.label', default: 'AverageDraftPosition'), id])
            redirect(action: "list")
            return
        }

        try {
            averageDraftPositionInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'averageDraftPosition.label', default: 'AverageDraftPosition'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'averageDraftPosition.label', default: 'AverageDraftPosition'), id])
            redirect(action: "show", id: id)
        }
    }
}

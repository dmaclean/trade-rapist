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

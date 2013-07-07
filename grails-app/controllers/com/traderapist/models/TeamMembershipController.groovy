package com.traderapist.models

import grails.plugins.springsecurity.Secured
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.access.annotation.Secured

@Secured(['ROLE_ADMIN'])
class TeamMembershipController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [teamMembershipInstanceList: TeamMembership.list(params), teamMembershipInstanceTotal: TeamMembership.count()]
    }

    def create() {
        [teamMembershipInstance: new TeamMembership(params)]
    }

    def save() {
        def teamMembershipInstance = new TeamMembership(params)
        if (!teamMembershipInstance.save(flush: true)) {
            render(view: "create", model: [teamMembershipInstance: teamMembershipInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'teamMembership.label', default: 'TeamMembership'), teamMembershipInstance.id])
        redirect(action: "show", id: teamMembershipInstance.id)
    }

    def show(Long id) {
        def teamMembershipInstance = TeamMembership.get(id)
        if (!teamMembershipInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'teamMembership.label', default: 'TeamMembership'), id])
            redirect(action: "list")
            return
        }

        [teamMembershipInstance: teamMembershipInstance]
    }

    def edit(Long id) {
        def teamMembershipInstance = TeamMembership.get(id)
        if (!teamMembershipInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'teamMembership.label', default: 'TeamMembership'), id])
            redirect(action: "list")
            return
        }

        [teamMembershipInstance: teamMembershipInstance]
    }

    def update(Long id, Long version) {
        def teamMembershipInstance = TeamMembership.get(id)
        if (!teamMembershipInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'teamMembership.label', default: 'TeamMembership'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (teamMembershipInstance.version > version) {
                teamMembershipInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'teamMembership.label', default: 'TeamMembership')] as Object[],
                          "Another user has updated this TeamMembership while you were editing")
                render(view: "edit", model: [teamMembershipInstance: teamMembershipInstance])
                return
            }
        }

        teamMembershipInstance.properties = params

        if (!teamMembershipInstance.save(flush: true)) {
            render(view: "edit", model: [teamMembershipInstance: teamMembershipInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'teamMembership.label', default: 'TeamMembership'), teamMembershipInstance.id])
        redirect(action: "show", id: teamMembershipInstance.id)
    }

    def delete(Long id) {
        def teamMembershipInstance = TeamMembership.get(id)
        if (!teamMembershipInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'teamMembership.label', default: 'TeamMembership'), id])
            redirect(action: "list")
            return
        }

        try {
            teamMembershipInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'teamMembership.label', default: 'TeamMembership'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'teamMembership.label', default: 'TeamMembership'), id])
            redirect(action: "show", id: id)
        }
    }
}

package com.traderapist.models

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.access.annotation.Secured

@Secured(['ROLE_ADMIN'])
class AverageDraftPositionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [averageDraftPositionInstanceList: AverageDraftPosition.list(params), averageDraftPositionInstanceTotal: AverageDraftPosition.count()]
    }

    def create() {
        [averageDraftPositionInstance: new AverageDraftPosition(params)]
    }

    def save() {
        def averageDraftPositionInstance = new AverageDraftPosition(params)
        if (!averageDraftPositionInstance.save(flush: true)) {
            render(view: "create", model: [averageDraftPositionInstance: averageDraftPositionInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'averageDraftPosition.label', default: 'AverageDraftPosition'), averageDraftPositionInstance.id])
        redirect(action: "show", id: averageDraftPositionInstance.id)
    }

    def show(Long id) {
        def averageDraftPositionInstance = AverageDraftPosition.get(id)
        if (!averageDraftPositionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'averageDraftPosition.label', default: 'AverageDraftPosition'), id])
            redirect(action: "list")
            return
        }

        [averageDraftPositionInstance: averageDraftPositionInstance]
    }

    def edit(Long id) {
        def averageDraftPositionInstance = AverageDraftPosition.get(id)
        if (!averageDraftPositionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'averageDraftPosition.label', default: 'AverageDraftPosition'), id])
            redirect(action: "list")
            return
        }

        [averageDraftPositionInstance: averageDraftPositionInstance]
    }

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

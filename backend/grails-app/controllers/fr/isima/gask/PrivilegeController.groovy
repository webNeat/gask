package fr.isima.gask



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class PrivilegeController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Privilege.list(params), model:[privilegeInstanceCount: Privilege.count()]
    }

    def show(Privilege privilegeInstance) {
        respond privilegeInstance
    }

    def create() {
        respond new Privilege(params)
    }

    @Transactional
    def save(Privilege privilegeInstance) {
        if (privilegeInstance == null) {
            notFound()
            return
        }

        if (privilegeInstance.hasErrors()) {
            respond privilegeInstance.errors, view:'create'
            return
        }

        privilegeInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'privilege.label', default: 'Privilege'), privilegeInstance.id])
                redirect privilegeInstance
            }
            '*' { respond privilegeInstance, [status: CREATED] }
        }
    }

    def edit(Privilege privilegeInstance) {
        respond privilegeInstance
    }

    @Transactional
    def update(Privilege privilegeInstance) {
        if (privilegeInstance == null) {
            notFound()
            return
        }

        if (privilegeInstance.hasErrors()) {
            respond privilegeInstance.errors, view:'edit'
            return
        }

        privilegeInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Privilege.label', default: 'Privilege'), privilegeInstance.id])
                redirect privilegeInstance
            }
            '*'{ respond privilegeInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Privilege privilegeInstance) {

        if (privilegeInstance == null) {
            notFound()
            return
        }

        privilegeInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Privilege.label', default: 'Privilege'), privilegeInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'privilege.label', default: 'Privilege'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}

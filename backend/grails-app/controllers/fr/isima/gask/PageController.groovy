package fr.isima.gask



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class PageController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Page.list(params), model:[pageInstanceCount: Page.count()]
    }

    def show(Page pageInstance) {
        respond pageInstance
    }

    def create() {
        respond new Page(params)
    }

    @Transactional
    def save(Page pageInstance) {
        if (pageInstance == null) {
            notFound()
            return
        }

        if (pageInstance.hasErrors()) {
            respond pageInstance.errors, view:'create'
            return
        }

        pageInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'page.label', default: 'Page'), pageInstance.id])
                redirect pageInstance
            }
            '*' { respond pageInstance, [status: CREATED] }
        }
    }

    def edit(Page pageInstance) {
        respond pageInstance
    }

    @Transactional
    def update(Page pageInstance) {
        if (pageInstance == null) {
            notFound()
            return
        }

        if (pageInstance.hasErrors()) {
            respond pageInstance.errors, view:'edit'
            return
        }

        pageInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Page.label', default: 'Page'), pageInstance.id])
                redirect pageInstance
            }
            '*'{ respond pageInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Page pageInstance) {

        if (pageInstance == null) {
            notFound()
            return
        }

        pageInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Page.label', default: 'Page'), pageInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'page.label', default: 'Page'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}

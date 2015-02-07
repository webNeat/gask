package fr.isima.gask



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
@Transactional(readOnly = true)
class NotificationController {

    static allowedMethods = [index : "GET"]
    def index(){
        render Notification.list() as JSON
    }
   
}

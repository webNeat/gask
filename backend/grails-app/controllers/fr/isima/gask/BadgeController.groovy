package fr.isima.gask



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import java.util.LinkedHashMap
@Transactional(readOnly = true)
class BadgeController {

    static allowedMethods = [index : "GET", get : "GET"]

    def index() {
        //JSON.registerObjectMarshaller(Route) {
        // return [name:it.name, checkPoints:it.checkPoints]
        render Badge.list() as JSON
    }
    def get(int id){
        def badge = Badge.get(id)
        def result = new LinkedHashMap()
        if(badge == null){
            result.id = -1
        }else{
            result = badge
        }
        render result as JSON
    }
}

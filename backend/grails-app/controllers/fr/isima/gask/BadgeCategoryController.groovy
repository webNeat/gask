package fr.isima.gask



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import java.util.LinkedHashMap
@Transactional(readOnly = true)
class BadgeCategoryController {
   static allowedMethods = [index : "GET", get : "GET"]

    def index() {
        render BadgeCategory.list() as JSON

        //JSON.registerObjectMarshaller(Route) {
        // return [name:it.name, checkPoints:it.checkPoints]
    }
    def get(int id){
        def badgeCat = BadgeCategory.get(id)
        def result = new LinkedHashMap()
        if(badge == null){
            result.id = -1
        }else{
            result = badgeCat
        }
        render result as JSON
    }
    
}

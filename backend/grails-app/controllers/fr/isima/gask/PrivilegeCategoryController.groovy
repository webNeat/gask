package fr.isima.gask



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import java.util.LinkedHashMap
@Transactional(readOnly = true)
class PrivilegeCategoryController {
    static allowedMethods = [index : "GET", get : "GET"]

    def index() {
           render PrivilegeCategory.list() as JSON
    }
    def get(int id){
        def privilegeCat = PrivilegeCategory.get(id)
        def result = new LinkedHashMap()
        if(privilegeCat == null){
            result.id = -1
        }else{
            result = privilegeCat
        }
        render result as JSON
    }
    
}

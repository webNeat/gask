package fr.isima.gask



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import java.util.LinkedHashMap

@Transactional(readOnly = true)
class PrivilegeController {
  static allowedMethods = [update : "PUT", index : "GET", get : "GET"]

    def index() {
        render Privilege.list() as JSON
    }
    def get(int id){
        def privilege = Privilege.get(id)
        def result = new LinkedHashMap()
        if(privilege == null){
            result.id = -1
        }else{
            result = privilege
        }
        render result as JSON
    }
    @Transactional
    def update(int id){
        def privilegeInstance = Privilege.get(id)
        def result = new LinkedHashMap()
        def user = User.get(session.user.id)        
        if (privilegeInstance == null) {
            result.done = false
            result.errs = 16
        }
        if(user == null){
            result.done = false
            result.errs = 1
        }else if(user.id == request.JSON.adminId && user.password == request.JSON.adminPass){
            if(user.isAdmin == true){
                privilegeInstance.reputation = request.JSON.reputation
                if(privilegeInstance.save(flush:true)){
                    result.done = true
                    result.errs = null
                }else{
                    result.done = false
                    result.errs = 19
                }
            }else{
                result.done = false
                result.errs = 6
            }
        }else{
            result.done = false
            result.errs = 3   
        }
        render result as JSON
    }
 
}

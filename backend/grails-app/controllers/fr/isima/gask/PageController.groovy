package fr.isima.gask



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import java.util.LinkedHashMap
@Transactional(readOnly = true)
class PageController {
    static allowedMethods = [create : "POST", index : "GET", get : "GET", titles : "GET", update: "PUT", delete: "DELETE", byTitle : "GET"]
    def index(){
        render Page.list() as JSON
    }

    def get(int id){
        def page = Page.get(id)
        def result = new LinkedHashMap()
        if(page == null){
            result.id = -1
        }else{
            result = page
        }
        render result as JSON
    }
    def titles(){
        def titles = Page.withCriteria {
            projections {
                property 'id', 'id'
                property 'title', 'title'
            }
        }
        render titles as JSON
    }
    @Transactional
    def create(){
        def pageInstance = new Page(request.JSON)
        def result = new LinkedHashMap()
        def user = User.get(session.user.id)        
        if (pageInstance == null) {
            result.done = false
            result.errs = 'page not found'
        }
        if(user == null){
            result.done = false
            result.errs = 'no user connected'
        }else if(user.id == request.JSON.adminId && user.password == request.JSON.adminPass){
            if(user.isAdmin == true){
                if(pageInstance.save(flush:true)){
                    result.done = true    
                    result.errs = null
                }else{
                    result.done = false
                    result.errs = 'not updated'
                }
            }else{
                result.done = false
                result.errs = 'User Not admin'
            }
        }else{
            result.done = false
            result.errs = 'params not corresponding to Current User'   
        }
        render result as JSON

    }
    @Transactional
    def update(int id){
        def pageInstance = Page.get(id)
        def result = new LinkedHashMap()
        def user = User.get(session.user.id)        
        if (pageInstance == null) {
            result.done = false
            result.errs = 'page not found'
        }
        if(user == null){
            result.done = false
            result.errs = 'no user connected'
        }else if(user.id == request.JSON.adminId && user.password == request.JSON.adminPass){
            if(user.isAdmin == true){
                pageInstance.title = request.JSON.title
                pageInstance.content = request.JSON.content
                if(pageInstance.save(flush:true)){
                    result.done = true    
                    result.errs = null
                }else{
                    result.done = false
                    result.errs = 'not updated'
                }
            }else{
                result.done = false
                result.errs = 'User Not admin'
            }
        }else{
            result.done = false
            result.errs = 'params not corresponding to Current User'   
        }
        render result as JSON
    }
     //DONE
    @Transactional
    def delete(int id) {
        def pageInstance = Page.get(id)
        def user = User.get(session.user.id) 
        def result = new LinkedHashMap()
        if (pageInstance == null) {
            result.done = false
            result.errs = 'page not found'
        }else if(user == null){
            result.done = false
            result.errs = 'no user connected'
        }else if(user.id == request.JSON.adminId && user.password == request.JSON.adminPass){
            if(user.isAdmin == true){
                if(pageInstance.delete(flush:true)){
                    result.done = true
                    result.errs = null
                }else{
                    result.done = false
                    result.errs = 'page no deleted'
                }
            }else{
                result.done = false
                result.errs = 'User Not admin'
            }
        }else{
            result.done = false
            result.errs = 'params not corresponding to Current User'   
        }
        render result as JSON
    }   

}

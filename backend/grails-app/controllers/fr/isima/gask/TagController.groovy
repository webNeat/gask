package fr.isima.gask



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import java.util.LinkedHashMap

@Transactional(readOnly = true)
class TagController {

    static allowedMethods = [create : "POST", removeFromFavorites  : "POST", addToFavorites : "POST", index : "GET", get : "GET", questions : "GET", users : "GET", save: "POST", update: "PUT", delete: "DELETE"]
    //DONE
    def index(){
        render Tag.list() as JSON
    }
    //DONE
    def get(int id){
        def tag = Tag.get(id)
        def result = new LinkedHashMap()
        if(tag == null){
            result.id = -1
        }else{
            result = tag
        }
        render result as JSON
    }
    def questions(int id){
        def tag = Tag.get(id)
        render tag.questions.asList() as JSON
    }
    def users(int id){
        def tag = Tag.get(id)
        render tag.users.asList() as JSON
    }
    //DONE
    @Transactional
    def addToFavorites(int id){
        def user = User.get(session.user.id)
        def result = new LinkedHashMap()

        if(user != null){
            def tag = Tag.get(id)
            if(!user.addToTags(tag).save(flush:true)){
                result.done = false
                result.errs = user.errors
            }else{
                result.done = true
                result.errs = null
            }
        }else{
            result.done = false
            result.errs = 'no user connected'
        }
        render result as JSON
    }
    //Done
    @Transactional
    def removeFromFavorites(int id){
        def user = User.get(session.user.id)
        def tag = Tag.get(id)
        def result = new LinkedHashMap()
        if(!user.removeFromTags(tag)){
            result.done = false
            result.errs = user.errors
            render result as JSON
            return 
        }
        result.done = true
        result.errs = null
        render result as JSON
   

    }
    //Done
    @Transactional
    def update(int id){
        def tagInstance = Tag.get(id)
        def result = new LinkedHashMap()
        def user = User.get(session.user.id)        
        if (tagInstance == null) {
            result.done = false
            result.errs = 'tag not found'
        }
        if(user == null){
            result.done = false
            result.errs = 'no user connected'
        }else if(user.id == request.JSON.adminId && user.password == request.JSON.adminPass){
            if(user.isAdmin == true){
                tagInstance.content = request.JSON.content
                if(tagInstance.save(flush:true)){
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
        def tagInstance = Tag.get(id)
        def user = User.get(session.user.id) 
        def result = new LinkedHashMap()
        if (tagInstance == null) {
            result.done = false
            result.errs = 'tag not found'
        }else if(user == null){
            result.done = false
            result.errs = 'no user connected'
        }else if(user.id == request.JSON.adminId && user.password == request.JSON.adminPass){
            if(user.isAdmin == true){
                removeFromFavorites(id)
                tagInstance.delete flush:true
                result.done = true
                result.errs = null
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
    def create(){
        def tagInstance = new Tag(request.JSON)
        def result = new LinkedHashMap()
        if (tagInstance == null) {
            result.done = false
            result.errs = null
            render result as JSON
            return
        }
        if (!tagInstance.save(flush:true)) {
            result.done = false
            result.errs = tagInstance.errors
            render result as JSON
            return
        }

        result.done = true
        result.errs = null
        render result as JSON

    }
}

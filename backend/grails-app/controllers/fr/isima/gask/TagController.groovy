package fr.isima.gask



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import java.util.LinkedHashMap

@Transactional(readOnly = true)
class TagController {

    static allowedMethods = [create : "POST", removeFromFavorites  : "POST", addToFavorites : "POST", index : "GET", get : "GET", questions : "GET", users : "GET", save: "POST", update: "PUT", delete: "DELETE"]
    def index(){
        render Tag.list() as JSON
    }
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
        render tag.questions as JSON
    }
    def users(int id){
        def tag = Tag.get(id)
        render tag.users as JSON
    }

    def addToFavorites(int id){
        def user = session.user
        def result = new LinkedHashMap()
        if(user != null){
            def tag = Tag.get(id)
            if(!user.addToTags(tag).save()){
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

    def removeFromFavorites(int id){
        def user = session.user
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
    @Transactional
    def update(int id){
        def tagInstance = Tag.get(id)
        def result = new LinkedHashMap()
        if (tagInstance == null) {
            result.done = false
            result.errs = null
            render result as JSON
           return
        }
        def user = session.user
        if(user.isAdmin == true && user.password == request.JSON.adminPass && user.id == request.JSON.adminId){
           tagInstance.content = request.JSON.content
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
    @Transactional
    def delete(int id) {
        def tagInstance = Tag.get(id)
        def user = session.user
        def result = new LinkedHashMap()
        if (tagInstance == null) {
            result.done = false
            result.errs = tagInstance.errors
        }else if(user.isAdmin == true && user.password == request.JSON.adminPass && user.id == request.JSON.adminId){
            tagInstance.delete flush:true
            result.done = true
            result.errs = null
        }

        render result as JSON
    }    
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

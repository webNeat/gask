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
    def get(int tagId){
        def tag = Tag.get(tagId)
        def result = new LinkedHashMap()
        if(tag == null){
            result.id = -1
        }else{
            result = tag
        }
        render result as JSON
    }
    def questions(int tagId){
        def tag = Tag.get(tagId)
        render tag.questions as JSON
    }
    def users(int tagId){
        def tag = Tag.get(tagId)
        render tag.users as JSON
    }
    def addToFavorites(int tagId){
        def user = session.user
        def tag = Tag.get(tagId)
        def result = new LinkedHashMap()
        if(!user.addToTags(tag).save()){
            result.done = false
            result.errs = user.errors
            render result as JSON
            return 
        }
        result.done = true
        result.errs = null
        render result as JSON
   

    }
    def removeFromFavorites(int tagId){
        def user = session.user
        def tag = Tag.get(tagId)
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
    def update(int tagId){
        def tagInstance = Tag.get(tagId)
        def result = new LinkedHashMap()
        if (tagInstance == null) {
            result.done = false
            result.errs = null
            render result as JSON
           return
        }
        def user = session.user
        if(user.isAdmin == true && user.password == params.adminPass && user.id == params.adminId){
           tagInstance.content = params.content
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
    def delete(int tagId) {
        def tagInstance = Tag.get(tagId)
        def user = session.user
        def result = new LinkedHashMap()
        if (tagInstance == null) {
            result.done = false
            result.errs = tagInstance.errors
        }else if(user.isAdmin == true && user.password == params.adminPass && user.id == params.adminId){
            tagInstance.delete flush:true
            result.done = true
            result.errs = null
        }

        render result as JSON
    }    
    @Transactional
    def create(){
        def tagInstance = new Tag(params)
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

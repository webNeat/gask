package fr.isima.gask



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import java.util.LinkedHashMap
@Transactional(readOnly = true)
class UserController {

    static allowedMethods = [index : "GET", get : "GET", names : "GET", current : "GET", password : "POST", priviliges : "GET", badges : "GET", questions : "GET", answers : "GET", comments : "GET", tags : "GET", notifications : "GET", votes : "GET", create : "POST", save: "POST", makeAdmin : "PUT", login : "POST", logout : "POST", update: "PUT", delete: "DELETE"]
    //Done
    def index() {
        render User.list() as JSON
    }
    //Done
    def get(int id){
        def user = User.get(id)
        def result = new LinkedHashMap()   
        if(user == null){
            result.id = -1
        }else{
            result = user
        }
        render result as JSON
    }
    //Done
    def names(){
        def users = User.withCriteria {
            projections {
                property 'id', 'id'
                property 'name', 'name'
            }
        }
        render users as JSON

    }
    //Done
    def current(){
        def result = new LinkedHashMap()
        if(session.user != null){
            def user = User.get(session.user.id)
            if( user == null){
                result.id = -1
            }else{
                result = user
            }
        }else{
            result.id = -1
        }
        render result as JSON
    }

    def privileges(int id){
        def user = User.get(id)
        render user.privileges.asList() as JSON   
        
    }

    def badges(int id){
        def user = User.get(id)
        render user.badges.asList() as JSON 
    }

    def questions(int id){
        def user = User.get(id)
        render user.questions.asList() as JSON   
    
    }
    
    def answers(int id){
        def user = User.get(id)
        render user.answers.asList() as JSON   
    
    } 

    def comments(int id){
        def user = User.get(id)
        render user.comments.asList() as JSON   
    
    }

    def tags(int id){
        def user = User.get(id)
        if(user != null){
            render user.tags.asList() as JSON   
        }
    }

    def notifications(int id){
        def user = User.get(id)
        render user.notifications.asList() as JSON   
    
    }

    def votes(int id){
        def user = User.get(id)
        render user.votes.asList() as JSON   
    
    }
    //Done
    @Transactional
    def create(){
        def userInstance = new User()
        userInstance.properties = request.JSON
        def result = new LinkedHashMap()
        if (userInstance == null) {
            result.done = false
            result.errs = null
            render result as JSON
            return
        }
        if (!userInstance.save(flush:true)) {
            result.done = false
            result.errs = userInstance.errors
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
        if(!id) id = request.JSON.id
        def userInstance = User.get(id)
        def result = new LinkedHashMap()
        if (userInstance == null) {
            result.done = false
            result.errs = 'User not found !'
            render result as JSON
           return
        }


        userInstance.properties = request.JSON
        
        if (!userInstance.save(flush:true)) {
            result.done = false
            result.errs = userInstance.errors
            render result as JSON
            return
        }

        result.done = true
        result.errs = null
        render result as JSON
    }
    //Done
    @Transactional
    def makeAdmin(int id){
        if(!id) id = request.JSON.id
        def result = new LinkedHashMap()
        if(session.user == null){
            result.done = false
            result.errs = 1
        }else if(session.user.id == request.JSON.adminId && session.user.password == request.JSON.adminPass){
            if(session.user.isAdmin == true){
                def userInstance = User.get(id)
                userInstance.isAdmin = true
                result.done = true
                result.errs = null
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
    //Done
    @Transactional
    def delete(int id) {
        if(!id) id = request.JSON.id
        def result = new LinkedHashMap()
        if(session.user == null){
            result.done = false
            result.errs = 1
        }else if(session.user.id == request.JSON.adminId && session.user.password == request.JSON.adminPass){
            if(session.user.isAdmin == true){
                def userInstance = User.get(id)
                userInstance.delete flush:true
                result.done = true
                result.errs = null
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
    //Done
    def login() {
        def user = User.findByEmailAndPassword(request.JSON.email, request.JSON.password)
        def result = new LinkedHashMap()
        if(session.user != null){
            result.done = false
            result.errs = 8    
        }else if (user) {
            session.user = user
            result.done = true
            result.errs = null
        }else {
            result.done = false
            result.errs = 2
        }
        render result as JSON
    }
    //Done
    def logout(){
        //Result ?
        def result = new LinkedHashMap()   
        session.invalidate()
        result.done = true
        result.errs = null
        render result as JSON
    }
}

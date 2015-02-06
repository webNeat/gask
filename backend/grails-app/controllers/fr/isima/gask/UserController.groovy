package fr.isima.gask



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import java.util.LinkedHashMap
import java.security.MessageDigest
@Transactional(readOnly = true)
class UserController {

    static allowedMethods = [index : "GET", get : "GET", names : "GET", current : "GET", password : "POST", priviliges : "GET", badges : "GET", questions : "GET", answers : "GET", comments : "GET", tags : "GET", notifications : "GET", votes : "GET", create : "POST", save: "POST", makeAdmin : "PUT", login : "PSOT", logout : "POST", update: "PUT", delete: "DELETE"]
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
        /*
        def result = users.collect { user->
            ["id": user.id, "userName": user.name]
        }*/
        render users as JSON

    }

    def current(){
        def result = new LinkedHashMap()
        def user = session.user
        if( user == null){
            result.id = -1
        }else{
            result = user
        }
        render result as JSON
    }

    def privileges(int id){
        def user = User.get(params.id)
        render user.privileges as JSON   
        
    }

    def badges(int id){
        def user = User.get(params.id)
        render user.badges as JSON 
    }

    def questions(int id){
        def user = User.get(params.id)
        render user.questions as JSON   
    
    }
    
    def answers(int id){
        def user = User.get(params.id)
        render user.answers as JSON   
    
    } 

    def comments(int id){
        def user = User.get(params.id)
        render user.comments as JSON   
    
    }

    def tags(int id){
        def user = User.get(params.id)
        render user.tags as JSON   
    
    }

    def notifications(int id){
        def user = User.get(params.id)
        render user.notifications as JSON   
    
    }

    def votes(int id){
        def user = User.get(params.id)
        render user.votes as JSON   
    
    }
    //Done
    @Transactional
    def create(){
        def userInstance = new User()
        userInstance.properties = params
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

    @Transactional
    def update(int id){
        def userInstance = User.get(id)
        println params.name
        
        def result = new LinkedHashMap()
        if (userInstance == null) {
            result.done = false
            result.errs = null
            render result as JSON
           return
        }


        userInstance.properties = params
        println userInstance.name
        
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
    @Transactional
    def makeAdmin(int id){
        def userInstance = User.get(id)
        def result = new LinkedHashMap()
        if (userInstance == null || userInstance.isAdmin == true ) {
            result.done = false
            result.errs = null
            render result as JSON
            return
        }

        userInstance.isAdmin = true
        userInstance.id = params.adminId
        userInstance.password = params.adminPass

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

    @Transactional
    def delete(int id) {
        def admin = User.get(id)
        def result = new LinkedHashMap()
        if (userInstance == null) {
            result.done = false
            result.errs = admin.errors

        }else if(admin.id == params.adminId && admin.password == params.adminPass){
            userInstance.delete flush:true
            result.done = true
            result.errs = null
        }

        render result as JSON
    }

    def login() {
        def user = User.findByEmailAndPassword(params.email, params.password)
        def result = new LinkedHashMap()
        if (user) {
            session.user = user
            result.done = true
            result.errs = null
        }else {
            result.done = false
            result.errs = "User not found"
        }
        render result as JSON
    }
 
    def logout(){
        //Result ?
        def result = new LinkedHashMap()   
        session.invalidate()
        result.done = true
        result.errs = null
        render result as JSON
    }
}

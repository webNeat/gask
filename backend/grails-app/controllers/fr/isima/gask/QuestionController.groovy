package fr.isima.gask



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import java.util.LinkedHashMap

@Transactional(readOnly = true)
class QuestionController {

    static allowedMethods = [index : "GET", get : "GET", titles : "GET", answers : "GET", comments : "GET", tags : "GET", votes : "GET", create : "POST", upVote : "PUT", downVote : "PUT", hide : "DELETE", update: "PUT", delete: "DELETE"]
    def index(){
        render Question.list() as JSON
    }
    def get(int questionId){
        def question = Question.get(questionId)
        def result = new LinkedHashMap()
        if(question == null){
            result.id = -1
        }else{
            result = question
        }
        render result as JSON
    }
    def titles(){
        def questions = Question.withCriteria {
            projections {
                property 'id', 'id'
                property 'title', 'title'
            }
        }
        render questions as JSON
    }
    def answers(int questionId){
        def question = Question.get(questionId)
        render question.answer as JSON
    }
    def comments(int questionId){
        def question = Question.get(questionId)
        render question.comment as JSON
    }
    def tags(int questionId){
        def question = Question.get(questionId)
        render question.tag as JSON
    }
    def votes(int questionId){
        def question = Question.get(questionId)
        render question.vote as JSON
    }
    @Transactional
    def create(){
        def listTags = Tag.getAll(params.tags)
        def questionInstance = new Question(title : params.title, content : params.content, tags : listTags)
        questionInstance.user = session.user
        def result = new LinkedHashMap()
        if (questionInstance == null) {
            result.done = false
            result.errs = null
            render result as JSON
            return
        }
        if (!questionInstance.save(flush:true)) {
            result.done = false
            result.errs = questionInstance.errs
            render result as JSON
            return
        }

        result.done = true
        result.errs = null
        render result as JSON

    }
    
   /*
    create: POST
        params : {title, content, tags:array of ids}
        // the author is the current user
        {done: bool, errs: array}
    update(questionId): PUT
        params : {title, content, tags:array, userId, password(hashed)}
        // check if the user is the author or an admin
        // and check the password
        {done: bool, errs: array}
    upVote(questionId): PUT
        params: {userId, userPass(hashed)}
        // check if user have the privilege
        // check if the user didn't already voted this question
        // check the password
        {done: bool, errs: array}
    downVote(questionId): PUT
        params: {userId, userPass(hashed)}
        // check if user have the privilege
        // check if the user didn't already voted this question
        // check the password
        {done: bool, errs: array}
    hide(questionId): DELETE
        params: {userId, userPass(hashed)}
        // check if user is the owner or an admin
        // set hidden to true
        {done: bool, errs: array}
    delete(questionId): DELETE
        params: {adminId, adminPass(hashed)}
        {done: bool, errs: array}

*/
}

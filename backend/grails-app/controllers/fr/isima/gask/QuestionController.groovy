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
    def get(int id){
        def question = Question.get(id)
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
    def answers(int id){
        def question = Question.get(id)
        render question.answers.asList() as JSON
    }
    def comments(int id){
        def question = Question.get(id)
        render question.comments.asList() as JSON
    }
    def tags(int id){
        def question = Question.get(id)
        render question.tags.asList() as JSON
    }
    def votes(int id){
        def question = Question.get(id)
        render question.votes.asList() as JSON
    }
    @Transactional
    def create(){
        def questionInstance = new Question()
        def user = User.get(session.user.id)
        questionInstance.properties = request.JSON
        def result = new LinkedHashMap()
        if(user != null){
            questionInstance.author = user
            for(t in questionInstance.tags){ 
                t.save(flush:true)
            }
            if (!questionInstance.save(flush:true)) {
                result.done = false
                result.errs = questionInstance.errors
            }else{
                // Add question to current user
                user.addToQuestions(questionInstance).save(flus:true)
                result.done = true
                result.errs = null
            }
        }else{
            result.done = false
            result.errs = 1
        }
        render result as JSON
    }
    @Transactional
    def update(int id){
        if(!id) id = request.JSON.id
        def questionInstance = Question.get(id)
        def result = new LinkedHashMap()
        def user = User.get(session.user.id)        
        if (questionInstance == null) {
            result.done = false
            result.errs = 11
        }
        if(user == null){
            result.done = false
            result.errs = 1
        }else if((questionInstance.author.id == request.JSON.userId && questionInstance.author.password == request.JSON.password) || user.isAdmin == true ){
            //user ==  admin
                questionInstance.content = request.JSON.content
                if(questionInstance.save(flush:true)){
                    result.done = true    
                    result.errs = null
                }else{
                    result.done = false
                    result.errs = 12
                }
        }else{
            result.done = false
            result.errs = 3   
        }
        render result as JSON
    }
    @Transactional
    def upVote(int id){
        if(!id) id = request.JSON.id
        def user = User.get(session.user.id)        
        def privileges = user.privileges.asList()
        def votesUser = user.votes.asList()
        def votesQuestion = Question.get(id).votes.asList()
        def questionInstance = Question.get(id)
        def result = new LinkedHashMap() 
        // if(privileges != null){
        if(true){
            def votesCommons = votesUser.intersect(votesQuestion)
            if(votesCommons == null || votesCommons.isEmpty()){
                def vote = new Vote()
                vote.value  = 1
                user.addToVotes(vote).save(flus:true)
                questionInstance.addToVotes(vote).save(flus:true)
                result.done = true
                result.errs = null
            }else if(votesCommons[0].value == -1){
                votesCommons[0].value = 1
                votesCommons[0].save(flus:true)
                result.done = true
                result.errs = null
            }else{
                result.done = false
                result.errs = 4
            }
        }else{
                result.done = false
                result.errs = 5
        }
        render result as JSON
    }
    @Transactional
    def downVote(int id){
        if(!id) id = request.JSON.id
        def user = User.get(session.user.id)        
        def privileges = user.privileges.asList()
        def votesUser = user.votes.asList()
        def votesQuestion = Question.get(id).votes.asList()
        def questionInstance = Question.get(id)
        def result = new LinkedHashMap()
        //if(privileges != null){
        if(true){
            def votesCommons = votesUser.intersect(votesQuestion)
            if(votesCommons == null || votesCommons.isEmpty()){
                def vote = new Vote()
                vote.value  = -1
                user.addToVotes(vote).save(flus:true)
                questionInstance.addToVotes(vote).save(flus:true)
                result.done = true
                result.errs = null
            }else if(votesCommons[0].value == 1){
                votesCommons[0].value = -1
                votesCommons[0].save(flus:true)
                result.done = true
                result.errs = null
            }else{
                result.done = false
                result.errs = 4
            }
        }else{
                result.done = false
                result.errs = 5
        }
        render result as JSON 

    }
    @Transactional
    def hide(int id){
        if(!id) id = request.JSON.id
        def questionInstance = Question.get(id)
        def result = new LinkedHashMap()
        def user = User.get(session.user.id)        
        if (questionInstance == null) {
            result.done = false
            result.errs = 11
        }
        if(user == null){
            result.done = false
            result.errs = 1
        }else if((questionInstance.author.id == request.JSON.userId && questionInstance.author.password == request.JSON.userPass) || user.isAdmin == true ){
                questionInstance.hidden = true
                if(questionInstance.save(flush:true)){
                    result.done = true    
                    result.errs = null
                }else{
                    result.done = false
                    result.errs = 12
                }
            }else{
            result.done = false
            result.errs = 3   
        }
        render result as JSON   
    }
    @Transactional
    def delete(int id) {
        if(!id) id = request.JSON.id
        def questionInstance = question.get(id)
        def user = User.get(session.user.id) 
        def result = new LinkedHashMap()
        if (questionInstance == null) {
            result.done = false
            result.errs = 11
        }else if(user == null){
            result.done = false
            result.errs = 1
        }else if(user.id == request.JSON.adminId && user.password == request.JSON.adminPass){
            if(user.isAdmin == true){
                questionInstance.author.removeFromQuestions(questionInstance)
                questionInstance.delete flush:true
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
}

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
        if (questionInstance == null) {
            result.done = false
            result.errs = null
        }else if(user != null){
            if (!questionInstance.save(flush:true)) {
                result.done = false
                result.errs = questionInstance.errors
            }else{
                //Ajouter au owner
                questionInstance.author.addToQuestions(questionInstance).save(flus:true)
                result.done = true
                result.errs = null
            }
        }else{
            result.done = false
            result.errs = 'no user connected'
        }
        render result as JSON
    }
    @Transactional
    def update(int id){
        def questionInstance = Question.get(id)
        def result = new LinkedHashMap()
        def user = User.get(session.user.id)        
        if (questionInstance == null) {
            result.done = false
            result.errs = 'question not found'
        }
        if(user == null){
            result.done = false
            result.errs = 'no user connected'
        }else if(user.id == request.JSON.userId && user.password == request.JSON.password){
            //user ==  admin
                questionInstance.content = request.JSON.content
                if(questionInstance.save(flush:true)){
                    result.done = true    
                    result.errs = null
                }else{
                    result.done = false
                    result.errs = 'not updated'
                }
        }else{
            result.done = false
            result.errs = 'params not corresponding to Current User'   
        }
        render result as JSON
    }
    def upVote(int id){
        def user = User.get(session.user.id)        
        def privilege = user.privileges.asList()
        def votesUser = user.votes.asList()
        def votesQuestion = Question.get(id).votes.asList()
        def result = new LinkedHashMap()
         if(privileges != null){
            def votesCommons = votesUser.intersect(votesQuestion)
            if(votesCommons == null){
                def vote = new Vote()
                vote.value  = 1
                user.addToVotes(vote).save(flus:true)
                questionInstance.addToVotes(vote).save(flus:true)
                result.done = true
                result.errs = null
            }else if(votesCommons.value == -1){
                votesCommons.value = 1
                votesCommons.save(flus:true)
                result.done = true
                result.errs = null
            }else{
                result.done = false
                result.errs = 'user have already upVoted'
            }
        }else{
                result.done = false
                result.errs = 'user don t have privilege to upVote'
        }
        render result as JSON
    }
    def downVote(int id){
        def user = User.get(session.user.id)        
        def privilege = user.privileges.asList()
        def votesUser = user.votes.asList()
        def votesQuestion = Question.get(id).votes.asList()
        def result = new LinkedHashMap()
         if(privileges != null){
            def votesCommons = votesUser.intersect(votesQuestion)
            if(votesCommons == null){
                def vote = new Vote()
                vote.value  = -1
                user.addToVotes(vote).save(flus:true)
                questionInstance.addToVotes(vote).save(flus:true)
                result.done = true
                result.errs = null
            }else if(votesCommons.value == 1){
                votesCommons.value = -1
                votesCommons.save(flus:true)
                result.done = true
                result.errs = null
            }else{
                result.done = false
                result.errs = 'user have already upVoted'
            }
        }else{
                result.done = false
                result.errs = 'user don t have privilege to upVote'
        }
        render result as JSON 

    }
    def hide(int id){
        def questionInstance = Question.get(id)
        def result = new LinkedHashMap()
        def user = User.get(session.user.id)        
        if (questionInstance == null) {
            result.done = false
            result.errs = 'question not found'
        }
        if(user == null){
            result.done = false
            result.errs = 'no user connected'
        }else if(user.id == request.JSON.userId && user.password == request.JSON.userPass){
                questionInstance.hidden = true
                if(questionInstance.save(flush:true)){
                    result.done = true    
                    result.errs = null
                }else{
                    result.done = false
                    result.errs = 'not updated'
                }
            }else{
            result.done = false
            result.errs = 'params not corresponding to Current User'   
        }
        render result as JSON   
    }
    @Transactional
    def delete(int id) {
        def questionInstance = question.get(id)
        def user = User.get(session.user.id) 
        def result = new LinkedHashMap()
        if (questionInstance == null) {
            result.done = false
            result.errs = 'question not found'
        }else if(user == null){
            result.done = false
            result.errs = 'no user connected'
        }else if(user.id == request.JSON.adminId && user.password == request.JSON.adminPass){
            if(user.isAdmin == true){
                questionInstance.author.removeFromQuestions(questionInstance)
                questionInstance.delete flush:true
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
}

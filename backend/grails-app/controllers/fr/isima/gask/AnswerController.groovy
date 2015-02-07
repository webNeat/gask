package fr.isima.gask



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import java.util.LinkedHashMap

@Transactional(readOnly = true)
class AnswerController {
    static allowedMethods = [index : "GET", get : "GET", comments : "GET", votes : "GET", create : "POST", update : "PUT", upVote : "PUT", downVote : "PUT", markAsBest : "PUT", hide : "DELETE", delete : "DELETE"]
    def index(){
        render Answer.list() as JSON
    }
    def get(int id){
        def answer = Answer.get(id)
        def result = new LinkedHashMap()
        if(answer == null){
            result.id = -1
        }else{
            result = answer
        }
        render result as JSON
    }
    def comments(int id){
        def answer = Answer.get(id)
        render answer.comments.asList() as JSON
    }
    def votes(int id){
        def answer = Answer.get(id)
        render answer.votes.asList() as JSON
    }
    @Transactional
    def create(){
        def answerInstance = new Answer()
        def user = User.get(session.user.id) 
        answerInstance.properties = request.JSON
        def result = new LinkedHashMap()
        if (answerInstance == null) {
            result.done = false
            result.errs = null
        }else if(user != null){
            if (!answerInstance.save(flush:true)) {
                result.done = false
                result.errs = answerInstance.errors
            }else{
                //Ajouter au owner
                answerInstance.author.addToAnswers(answerInstance).save(flus:true)
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
        def answerInstance = Answer.get(id)
        def result = new LinkedHashMap()
        def user = User.get(session.user.id)        
        if (answerInstance == null) {
            result.done = false
            result.errs = 'answer not found'
        }
        if(user == null){
            result.done = false
            result.errs = 'no user connected'
        }else if(user.id == request.JSON.userId && user.password == request.JSON.password){
            //user ==  admin
                answerInstance.content = request.JSON.content
                if(answerInstance.save(flush:true)){
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
    def upVote(int id){
        def user = User.get(session.user.id)        
        def privilege = user.privileges.asList()
        def votesUser = user.votes.asList()
        def votesAnswer = Answer.get(id).votes.asList()
        def result = new LinkedHashMap()
         if(privileges != null){
            def votesCommons = votesUser.intersect(votesAnswer)
            if(votesCommons == null){
                def vote = new Vote()
                vote.value  = 1
                user.addToVotes(vote).save(flus:true)
                answerInstance.addToVotes(vote).save(flus:true)
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
    @Transactional
    def downVote(int id){
        def user = User.get(session.user.id)        
        def privilege = user.privileges.asList()
        def votesUser = user.votes.asList()
        def votesAnswer = Answer.get(id).votes.asList()
        def result = new LinkedHashMap()
         if(privileges != null){
            def votesCommons = votesUser.intersect(votesAnswer)
            if(votesCommons == null){
                def vote = new Vote()
                vote.value  = -1
                user.addToVotes(vote).save(flus:true)
                answerInstance.addToVotes(vote).save(flus:true)
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
    @Transactional
    def hide(int id){
        def answerInstance = Answer.get(id)
        def result = new LinkedHashMap()
        def user = User.get(session.user.id)        
        if (answerInstance == null) {
            result.done = false
            result.errs = 'answer not found'
        }
        if(user == null){
            result.done = false
            result.errs = 'no user connected'
        }else if((answerInstance.author.id == request.JSON.userId && answerInstance.author.password == request.JSON.userPass) || user.isAdmin == true){
                answerInstance.hidden = true
                if(answerInstance.save(flush:true)){
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
        def answerInstance = Answer.get(id)
        def user = User.get(session.user.id) 
        def result = new LinkedHashMap()
        if (answerInstance == null) {
            result.done = false
            result.errs = 'answer not found'
        }else if(user == null){
            result.done = false
            result.errs = 'no user connected'
        }else if(user.id == request.JSON.adminId && user.password == request.JSON.adminPass){
            if(user.isAdmin == true){
                answerInstance.author.removeFromAnswers(answerInstance)
                answerInstance.delete flush:true
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
    def markAsBest(int id){
        def result = new LinkedHashMap()
        def user = User.get(session.user.id)
        def answer = Answer.get(id)
        def question = answer.question
        if(user != null){
            if(question.author.id == request.JSON.userId && question.author.password == request.JSON.userPass){
                question.bestAnswer = answer
                //Save ou pas ??
                if(!question.save(flush:true)){
                    result.done = false
                    result.errs = 'best Answer not updated'   
                }else{
                    result.done = true
                    result.errs = null          
                }
            }else{
                result.done = false
                result.errs = 'user no owner of this question'
            }
        }else{
            result.done = false
            result.errs = 'no user connected'
        }
        render result as JSON
    }
   
}

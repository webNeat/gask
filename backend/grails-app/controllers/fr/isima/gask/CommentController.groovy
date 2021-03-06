package fr.isima.gask



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import java.util.LinkedHashMap

@Transactional(readOnly = true)
class CommentController {
    static allowedMethods = [index : "GET", get : "GET", votes : "GET", create : "POST", upVote : "PUT", downVote : "PUT", hide : "DELETE", update: "PUT", delete: "DELETE"]
    def index(){
        render Comment.list() as JSON
    }
    def get(int id){
        def comment = Comment.get(id)
        def result = new LinkedHashMap()
        if(comment == null){
            result.id = -1
        }else{
            result = comment
        }
        render result as JSON
    }
    def votes(int id){
        def comment = Comment.get(id)
        render comment.votes.asList() as JSON
    }
    @Transactional
    def create(){
        def commentInstance = new Comment()
        def user = User.get(session.user.id)
        def question = Question.get(request.JSON.qId)
        commentInstance.content = request.JSON.content
        commentInstance.author = user
        commentInstance.question = question
        def result = new LinkedHashMap()
        if (commentInstance == null) {
            result.done = false
            result.errs = null
        }else if(user != null){
            if (!commentInstance.save(flush:true)) {
                result.done = false
                result.errs = commentInstance.errors
            }else{               
                user.addToComments(commentInstance).save(flus:true)
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
        def commentInstance = Comment.get(id)
        def result = new LinkedHashMap()
        def user = User.get(session.user.id)        
        if (commentInstance == null) {
            result.done = false
            result.errs = 13
        }
        if(user == null){
            result.done = false
            result.errs = 1
        }else if((commentInstance.author.id == request.JSON.userId && commentInstance.author.password == request.JSON.password) || user.isAdmin == true ){
                commentInstance.content = request.JSON.content
                if(commentInstance.save(flush:true)){
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
        def privilege = user.privileges.asList()
        def votesUser = user.votes.asList()
        def votesComment = Comment.get(id).votes.asList()
        def commentInstance = Comment.get(id)
        def result = new LinkedHashMap()
         if(privileges != null){
            def votesCommons = votesUser.intersect(votesComment)
            if(votesCommons == null){
                def vote = new Vote()
                vote.value  = 1
                user.addToVotes(vote).save(flus:true)
                commentInstance.addToVotes(vote).save(flus:true)
                result.done = true
                result.errs = null
            }else if(votesCommons.value == -1){
                votesCommons.value = 1
                votesCommons.save(flus:true)
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
        def privilege = user.privileges.asList()
        def votesUser = user.votes.asList()
        def votesComment = Comment.get(id).votes.asList()
        def commentInstance = Comment.get(id)
        def result = new LinkedHashMap()
         if(privileges != null){
            def votesCommons = votesUser.intersect(votesComment)
            if(votesCommons == null){
                def vote = new Vote()
                vote.value  = -1
                user.addToVotes(vote).save(flus:true)
                commentInstance.addToVotes(vote).save(flus:true)
                result.done = true
                result.errs = null
            }else if(votesCommons.value == 1){
                votesCommons.value = -1
                votesCommons.save(flus:true)
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
        def commentInstance = Comment.get(id)
        def result = new LinkedHashMap()
        def user = User.get(session.user.id)        
        if (commentInstance == null) {
            result.done = false
            result.errs = 13
        }
        if(user == null){
            result.done = false
            result.errs = 1
        }else if((commentInstance.author.id == request.JSON.userId && commentInstance.author.password == request.JSON.userPass) || user.isAdmin == true ){
                commentInstance.hidden = true
                if(commentInstance.save(flush:true)){
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
        def commentInstance = comment.get(id)
        def user = User.get(session.user.id) 
        def result = new LinkedHashMap()
        if (commentInstance == null) {
            result.done = false
            result.errs = 13
        }else if(user == null){
            result.done = false
            result.errs = 1
        }else if(user.id == request.JSON.adminId && user.password == request.JSON.adminPass){
            if(user.isAdmin == true){
                commentInstance.author.removeFromComments(commentInstance)
                commentInstance.delete flush:true
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

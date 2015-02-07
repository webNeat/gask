package fr.isima.gask

class Comment {
    String content
	boolean hidden = false

	static hasMany = [ votes : Vote ]
	static belongsTo = [ question : Question, answer : Answer, author : User ]
	static mapping = {
		content type : "text"
	}
    static constraints = {
    	content blank : false, nullable : false
    }
}

package fr.isima.gask

class Answer {
	String content
	boolean hidden = false

	static hasMany = [ votes : Vote, comments : Comment ]
	static belongsTo = [ question : Question, author : User ]
	static mapping = {
		content type : "text"
	}
    static constraints = {
    	content blank : false, nullable : false
    }
}

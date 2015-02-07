package fr.isima.gask

class Tag {
	String name
	String content

	//Question questionOwner
	//User userOwner
	static belongsTo = [ Question, User]
	static hasMany = [ questions : Question, users : User ]
	
	static mapping = {
		content type : "text"
		//users cascade: 'all-delete-orphan'

	}
    static constraints = {
    	name blank : false, nullable : false
    	content blank : false, nullable : false

    }
}

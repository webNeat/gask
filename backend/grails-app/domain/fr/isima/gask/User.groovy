package fr.isima.gask
import  java.util.Date

class User {
	String name
	String realName
	String password
	String email
	String photo
	String bio
	Date birthday = new Date()
	String website
	String location
	Integer reputation
	Date createdAt = new Date()
	Date lastLogin = new Date()
	Integer profileViews
	boolean isAdmin

	static hasMany = [questions : Question, answers : Answer, comments : Comment, 
					notifications : Notification, badges : Badge, privileges : Privilege,
					tags : Tag, votes : Vote ]
	static mapping = {
		bio type : "text"
		reputation defaultValue : 0
		profileViews defaultValue : 0
		isAdmin defaultValue : false
	}

    static constraints = {
	     name blank : false, nullable : false

		 realName blank : false, nullable : true

		 password blank : false, nullable : false

		 email blank : false, nullable : false

		 photo blank : false, nullable : true

		 bio blank : false, nullable : true

		 birthday blank : false, nullable : true

		 website blank : false, nullable : true

		 location blank : false, nullable : true

		 reputation blank : false, nullable : false

		 createdAt blank : false, nullable : false

		 lastLogin blank : false, nullable : false

		 profileViews blank : false, nullable : false

		 isAdmin blank : false, nullable : false
    }
}

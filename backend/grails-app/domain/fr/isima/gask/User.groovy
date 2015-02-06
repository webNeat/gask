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
	Integer reputation = 0
	Date createdAt = new Date()
	Date lastLogin = new Date()
	Integer profileViews = 0
	boolean isAdmin = false

	static hasMany = [questions : Question, answers : Answer, comments : Comment, 
					notifications : Notification, badges : Badge, privileges : Privilege,
					tags : Tag, votes : Vote ]
	static mapping = {
		bio type : "text"
	}

    static constraints = {
	     name blank : false, nullable : false

		 realName blank : false, nullable : true

		 password blank : false, nullable : false

		 email blank : false, nullable : false

		 photo blank : false, nullable : true

		 bio blank : false, nullable : true

		 website blank : false, nullable : true

		 location blank : false, nullable : true
    }
}

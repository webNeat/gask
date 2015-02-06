package fr.isima.gask
import  java.util.Date

class Question {
	String title
	String content
	Date createdAt = new Date()
	Date modifiedAt = new Date()
	boolean hidden
	Integer views

	static hasMany = [ tags : Tag, answers : Answer, votes : Vote, comments : Comment ]
	static hasOne = [ bestAnswer : Answer ]
	static belongsTo = [ author : User ]

	static mapping = {
		content type : "text"
	}
    static constraints = {
		title blank : false, nullable : false
		content blank : false, nullable : false
		views blank : false, nullable : false
		hidden blank : false, nullable : false
		createdAt blank : false, nullable : false
		modifiedAt blank : false, nullable : false
    }
}

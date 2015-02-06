package fr.isima.gask
import  java.util.Date
class Notification {
	String type //: {comment, answer, badge, privilege, ...}
	String  name //(the name of the badge, privilege or question)
	String  url
	boolean isRead
	Date createdAt = new Date()

	static belongsTo = [ users : User ]
    static constraints = {
    	type blank : false, inList : ["comment", "answer", "badge", "privilege"]
		createdAt blank : false, nullable : false
	    name blank : false, nullable : false
	    url blank : false, nullable : false
	    isRead blank : false, nullable : false
    }
}

package fr.isima.gask
import  java.util.Date
class Notification {
	String type 
	String  name
	String  url
	boolean isRead = false
	Date createdAt = new Date()

	static belongsTo = [ user : User ]
    static constraints = {
    	type blank : false, inList : ["comment", "answer", "badge", "privilege"]
	    name blank : false, nullable : false
	    url blank : false, nullable : false
    }
}

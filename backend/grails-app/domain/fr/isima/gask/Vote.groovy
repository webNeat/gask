package fr.isima.gask
import java.util.Date
class Vote {
	Integer value //: {-1, 1}
	Date createdAt = new Date()

	static belongsTo = [ user : User ]
    static constraints = {
    	value blank : false, nullable : false
    	createdAt blank : false, nullable : false

    }
}

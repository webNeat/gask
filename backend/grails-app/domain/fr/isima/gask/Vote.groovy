package fr.isima.gask
import java.util.Date
class Vote {
	Integer value = 0
	Date createdAt = new Date()

	static belongsTo = [ user : User ]
    static constraints = {
    	createdAt blank : false, nullable : false

    }
}

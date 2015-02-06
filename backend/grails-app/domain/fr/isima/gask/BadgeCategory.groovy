package fr.isima.gask

class BadgeCategory {

	String name
	String icon
	static hasMany = [ badges : Badge ]
    static constraints = {
    	name blank : false, nullable : false
    	icon blank : false, nullable : false
    }
}

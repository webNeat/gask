package fr.isima.gask

class Privilege {
	String name
	String description
	Integer reputation
	User userOwner
	static 	hasMany = [ users : User ]
	static belongsTo = [ User , PrivilegeCategory ]
    static constraints = {
    	name blank : false, nullable : false
    	description blank : false, nullable : false
    	reputation blank : false, nullable : false

    }
}

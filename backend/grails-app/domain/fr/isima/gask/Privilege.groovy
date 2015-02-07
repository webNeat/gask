package fr.isima.gask

class Privilege {
	String name
	String description
	Integer reputation = 0
	static 	hasMany = [ users : User ]
	static belongsTo = [ User , PrivilegeCategory ]
    static constraints = {
    	name blank : false, nullable : false
    	description blank : false, nullable : false
    }
}

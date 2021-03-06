package fr.isima.gask

class Badge {
	String name
	String description
	
	static belongsTo = [ BadgeCategory, User]
	static hasMany = [ users : User ]
   
    static constraints = {
	 name blank : false, nullable : false
	 description blank : false, nullable : false
    }
}

package fr.isima.gask

class PrivilegeCategory {
	String name
	String icon
	static hasMany = [ privileges : Privilege ]

    static constraints = {
    	name blank : false, nullable : false
    	icon blank : false, nullable : false
    }
}

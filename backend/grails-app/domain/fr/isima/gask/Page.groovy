package fr.isima.gask

class Page {
	String title
	String content
	
	static mapping = {
		content type : "text"
	}
    static constraints = {
    	title blank : false, nullable : false
    	content blank : false, nullable : false

    }
}

package com.traderapist.security

class Role {

	String authority

	static final String ROLE_ADMIN = "admin"
	static final String ROLE_USER = "user"

	static mapping = {
		table "roles"
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
}

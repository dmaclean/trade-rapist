package com.traderapist.security

class Role {

	String authority

	static final String ROLE_ADMIN = "ROLE_ADMIN"
	static final String ROLE_USER = "ROLE_USER"

	static mapping = {
		table "roles"
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
}

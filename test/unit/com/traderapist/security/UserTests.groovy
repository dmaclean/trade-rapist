package com.traderapist.security

import grails.test.mixin.TestFor
import org.junit.After
import org.junit.Before

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 8/14/13
 * Time: 9:32 AM
 * To change this template use File | Settings | File Templates.
 */
@TestFor(User)
class UserTests {

	@Before
	void setUp() {
		User.metaClass.encodePassword = { -> "password" }

		mockForConstraintsTests(User)
	}

	@After
	void tearDown() {

	}

	void testUsernameNotBlank() {
		def user = new User(username: "", password: "password")

		assertFalse "Should have failed validation", user.validate()

		assert user.errors["username"] == "blank"
	}

	void testUsernameUnique() {
		def user = new User(username: "dan@traderapist.com", password: "password")
		assert "Should have passed validation", user.validate()
		user.save(flush: true)

		def dupe = new User(username: "dan@traderapist.com", password: "password2")
		assertFalse "Should have failed validation", dupe.validate()

		assert dupe.errors["username"] == "unique"
	}

	void testUsernameIsValidEmail() {
		def user = new User(username: "dan", password: "password")
		assertFalse "Should have failed validation", user.validate()

		user.username = "dan@"
		assertFalse "Should have failed validation", user.validate()

		user.username = "@traderapist.com"
		assertFalse "Should have failed validation", user.validate()

		user.username = "dan@traderapist"
		assertFalse "Should have failed validation", user.validate()

		user.username = "dan@traderapist."
		assertFalse "Should have failed validation", user.validate()

		user.username = "dan@traderapist.com"
		assert "Should have passed validation", user.validate()
	}
}

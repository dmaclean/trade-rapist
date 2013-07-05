package com.traderapist.models

import com.traderapist.security.User
import grails.converters.JSON
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 7/2/13
 * Time: 9:57 AM
 * To change this template use File | Settings | File Templates.
 */
class FantasyTeamControllerIntegrationTests {
	def controller
	def params

	@Before
	void setUp() {
		controller = new FantasyTeamController()

		params = controller.request.getParameterMap()
	}

	@After
	void tearDown() {

	}

	@Test
	void testListWithJSON_LoggedIn() {

		def user = new User(username: "newuser", password: "password").save(flush: true)
		def flt = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
		def fantasyTeam = new FantasyTeam(user: user, fantasyLeagueType: flt, season: 2013, leagueId: "1111", name: "Dan Mac").save(flush: true)

		controller.springSecurityService = [
				encodePassword : "password",
				isLoggedIn : { -> true},
				getCurrentUser : { -> user }
		]

		controller.request.addParameter("json", "true")
		controller.list()

		assert controller.response.text == "[${(fantasyTeam as JSON)}]"
	}

	@Test
	void testListWithJSON_NotLoggedIn() {

		def user = new User(username: "newuser", password: "password").save(flush: true)
		def flt = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
		def fantasyTeam = new FantasyTeam(user: user, fantasyLeagueType: flt, season: 2013, leagueId: "1111", name: "Dan Mac").save(flush: true)

		controller.springSecurityService = [
				encodePassword : "password",
				isLoggedIn : { -> false},
				getCurrentUser : { -> user }
		]

		controller.request.addParameter("json", "true")
		controller.list()

		assert controller.response.text == ""
	}
}

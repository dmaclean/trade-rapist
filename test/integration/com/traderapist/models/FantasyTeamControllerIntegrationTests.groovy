package com.traderapist.models

import com.traderapist.security.Role
import com.traderapist.security.User
import com.traderapist.security.UserRole
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
	FantasyTeamController controller
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

	@Test
	void testList_UserLoggedIn() {
		def roleUser = new Role(authority: Role.ROLE_USER).save(flush: true)
		def roleAdmin = new Role(authority: Role.ROLE_ADMIN).save(flush: true)
		def user = new User(username: "newuser", password: "password").save(flush: true)
		def admin = new User(username: "adminuser", password: "password").save(flush: true)
		def userrole = new UserRole(user: user, role: roleUser).save(flush: true)
		def adminrole = new UserRole(user: admin, role: roleAdmin).save(flush: true)
		def flt = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
		def flt2 = new FantasyLeagueType(code: "Yahoo", description: "Yahoo").save(flush: true)
		def fantasyTeam = new FantasyTeam(user: user, fantasyLeagueType: flt, season: 2013, leagueId: "1111", name: "Dan Mac").save(flush: true)
		def fantasyTeam2 = new FantasyTeam(user: admin, fantasyLeagueType: flt2, season: 2013, leagueId: "1111", name: "Terror Squid").save(flush: true)

		controller.springSecurityService = [
				encodePassword : "password",
				isLoggedIn : { -> true},
				getCurrentUser : { -> user }
		]

		def result = controller.list()

		assert result["fantasyTeamInstanceList"][0] == fantasyTeam
		assert result["fantasyTeamInstanceTotal"] == 1
	}

	@Test
	void testList_NotLoggedIn() {

		def role = new Role(authority: Role.ROLE_USER).save(flush: true)
		def user = new User(username: "newuser", password: "password").save(flush: true)
		def userrole = new UserRole(user: user, role: role).save(flush: true)
		def flt = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
		def fantasyTeam = new FantasyTeam(user: user, fantasyLeagueType: flt, season: 2013, leagueId: "1111", name: "Dan Mac").save(flush: true)

		controller.springSecurityService = [
				encodePassword : "password",
				isLoggedIn : { -> false},
				getCurrentUser : { -> null }
		]

		controller.request.addParameter("json", "true")
		controller.list()

		assert controller.response.text == ""
	}

	@Test
	void testList_AdminLoggedIn() {
		def roleUser = new Role(authority: Role.ROLE_USER).save(flush: true)
		def roleAdmin = new Role(authority: Role.ROLE_ADMIN).save(flush: true)
		def user = new User(username: "newuser", password: "password").save(flush: true)
		def admin = new User(username: "adminuser", password: "password").save(flush: true)
		def userrole = new UserRole(user: user, role: roleUser).save(flush: true)
		def adminrole = new UserRole(user: admin, role: roleAdmin).save(flush: true)
		def flt = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
		def flt2 = new FantasyLeagueType(code: "Yahoo", description: "Yahoo").save(flush: true)
		def fantasyTeam = new FantasyTeam(user: user, fantasyLeagueType: flt, season: 2013, leagueId: "1111", name: "Dan Mac").save(flush: true)
		def fantasyTeam2 = new FantasyTeam(user: admin, fantasyLeagueType: flt2, season: 2013, leagueId: "1111", name: "Terror Squid").save(flush: true)

		controller.springSecurityService = [
				encodePassword : "password",
				isLoggedIn : { -> true},
				getCurrentUser : { -> admin }
		]

		def result = controller.list()

		assert result["fantasyTeamInstanceList"][0] == fantasyTeam
		assert result["fantasyTeamInstanceList"][1] == fantasyTeam2
		assert result["fantasyTeamInstanceTotal"] == 2
	}
}

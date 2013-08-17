package com.traderapist.models

import com.traderapist.security.User
import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(FantasyTeamStarter)
@Mock([FantasyLeagueType, FantasyTeam, User])
class FantasyTeamStarterTests {

	FantasyTeam fantasyTeam
	User user
	FantasyLeagueType flt

	@Before
	void setUp() {
		User.metaClass.encodePassword = { -> "password"}
		user = new User(username: "dmaclean", password: "password").save(flush: true)
		flt = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
		fantasyTeam = new FantasyTeam(user: user, fantasyLeagueType: flt, leagueId: "1234", name: "Terror Squid", season: 2013).save(flush: true)

		mockForConstraintsTests(FantasyTeamStarter)
	}

	@After
	void tearDown() {

	}

	void testPositionNotNullable() {
		def starter = new FantasyTeamStarter(numStarters: 10)

		assertFalse "Should have faild validation", starter.validate()

		assertTrue starter.errors["position"] == "nullable"
	}

	void testPositionNotBlank() {
		def starter = new FantasyTeamStarter(position: "", numStarters: 10)

		assertFalse "Should have faild validation", starter.validate()

		assertTrue starter.errors["position"] == "blank"
	}

	void testNumStartersNotNullable() {
		def starter = new FantasyTeamStarter(position: Player.POSITION_QB)

		assertFalse "Should have faild validation", starter.validate()

		assertTrue starter.errors["numStarters"] == "nullable"
	}

	void testNumStartersMin0() {
		def starter = new FantasyTeamStarter(position: Player.POSITION_QB, numStarters: -1)

		assertFalse "Should have faild validation", starter.validate()

		assertTrue starter.errors["numStarters"] == "min"
	}
}

package com.traderapist.models



import grails.test.mixin.*
import org.junit.*

import com.traderapist.security.User

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(FantasyTeam)
@Mock([FantasyLeagueType, User])
class FantasyTeamTests {
	User user
	FantasyLeagueType flt

	@Before
	void setUp() {
		User.metaClass.encodePassword = { -> "password"}
		user = new User(username: "dmaclean", password: "password").save(flush: true)
		flt = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)

		mockForConstraintsTests(FantasyTeam)
	}

	@After
	void tearDown() {
		user = null
		flt = null
	}

	void testGoodFantasyTeam() {
		def ft = new FantasyTeam(user: user, fantasyLeagueType: flt, season: 2013, name: "ESPN")

		assertTrue ft.validate()

		assertEquals 0, ft.errors.allErrors.size()
	}

	void testNameNotNullable() {
		FantasyTeam ft = new FantasyTeam(user: user, fantasyLeagueType: flt, season: 2013)

		assertFalse "Should have failed validation.", ft.validate()

		assert "nullable" == ft.errors["name"]
	}

	void testNameNotBlank() {
		FantasyTeam ft = new FantasyTeam(user: user, name: "", fantasyLeagueType: flt, season: 2013)

		assertFalse "Should have failed validation.", ft.validate()

		assert "blank" == ft.errors["name"]
	}

	void testSeasonNotNullable() {
		FantasyTeam ft = new FantasyTeam(user: user, fantasyLeagueType: flt, name: "Dan Mac")

		assertFalse "Should have failed validation.", ft.validate()

		assert "nullable" == ft.errors["season"]
	}
}

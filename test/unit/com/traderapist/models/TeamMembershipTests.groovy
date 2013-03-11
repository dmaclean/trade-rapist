package com.traderapist.models



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(TeamMembership)
@Mock([Team, Player])
class TeamMembershipTests {

	void setUp() {
		mockForConstraintsTests(TeamMembership)
	}

	void tearDown() {

	}

    void testNotNullable() {
	    def tm = new TeamMembership(season: null)

	    assertFalse("TeamMembership should not be valid.", tm.validate())

	    assert "nullable" == tm.errors["season"]
    }

	void testMinSeason() {
		def tm = new TeamMembership(season: 2000)

		assertFalse("TeamMembership should not be valid.", tm.validate())

		assert "min" == tm.errors["season"]
	}

	void testSuccess() {
		def player = new Player(name: "Dan MacLean", position: "QB").save()
		def team = new Team(city: "New England", name: "Patriots", abbreviation: "NE").save()
		def tm = new TeamMembership(season: 2001, player: player, team: team)

		assertTrue("TeamMembership should be valid.", tm.validate())
	}
}

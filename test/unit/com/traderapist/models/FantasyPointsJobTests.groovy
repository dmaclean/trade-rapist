package com.traderapist.models

import com.traderapist.security.User
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.junit.After
import org.junit.Before

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(FantasyPointsJob)
@Mock([FantasyLeagueType, FantasyTeam, User])
class FantasyPointsJobTests {

	User user
	FantasyLeagueType flt
	FantasyTeam fantasyTeam

	@Before
	void setUp() {
		User.metaClass.encodePassword = { -> "password"}
		user = new User(username: "testuser@gmail.com", password: "password").save(flush: true)
		flt = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
		fantasyTeam = new FantasyTeam(name: "Test team", user: user, fantasyLeagueType: flt, season: 2013, leagueId: "111", numOwners: 10, fantasyTeamStarters: new HashSet<FantasyTeamStarter>()).save(flush: true)

		mockForConstraintsTests(FantasyPointsJob)
	}

	@After
	void tearDown() {
		user = null
		flt = null
		fantasyTeam = null
	}

	void testGoodJob() {
		def job = new FantasyPointsJob(fantasyTeam: fantasyTeam, completed: false, season: 2013, week: -1, projection: FantasyPointsJob.TRADERAPIST_PROJECTION)

		assertTrue "Validation should not have failed", job.validate()

		assert job.errors.getAllErrors().size() == 0
	}

	void testFantasyTeamNotNullable() {
		def job = new FantasyPointsJob(fantasyTeam: null)

		assertFalse "Validation should have failed.", job.validate()

		assert job.errors["fantasyTeam"] == "nullable"
	}

	void testCompletedNotNullable() {
		def job = new FantasyPointsJob(fantasyTeam: fantasyTeam, completed: null)

		assertFalse "Validation should have failed.", job.validate()

		assert job.errors["completed"] == "nullable"
	}

	void testSeasonNotNullable() {
		def job = new FantasyPointsJob(fantasyTeam: fantasyTeam, completed: false, week: -1, projection: FantasyPointsJob.TRADERAPIST_PROJECTION)

		assertFalse "Validation should have failed.", job.validate()

		assert job.errors["season"] == "nullable"
	}

	void testWeekNotNullable() {
		def job = new FantasyPointsJob(fantasyTeam: fantasyTeam, completed: false, season: 2013, projection: FantasyPointsJob.TRADERAPIST_PROJECTION)

		assertFalse "Validation should have failed.", job.validate()

		assert job.errors["week"] == "nullable"
	}

	void testProjectionNotNullable() {
		def job = new FantasyPointsJob(fantasyTeam: fantasyTeam, completed: false, season: 2013, week: -1, projection: null)

		assertFalse "Validation should have failed.", job.validate()

		assert job.errors["projection"] == "nullable"
	}
}

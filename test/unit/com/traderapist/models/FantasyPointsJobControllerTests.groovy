package com.traderapist.models

import com.traderapist.constants.FantasyConstants
import com.traderapist.security.User
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.junit.After
import org.junit.Before

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(FantasyPointsJobController)
@Mock([FantasyLeagueType, FantasyPoints, FantasyPointsJob, FantasyTeam, FantasyTeamStarter, Player, ScoringSystem, Stat, User])
class FantasyPointsJobControllerTests {
	def passingYards
	def passingTouchdowns
	def interceptions
	def rushingYards
	def rushingTouchdowns

	Player player
	User user
	FantasyLeagueType flt
	FantasyTeam fantasyTeam
	ScoringSystem scoringSystem

	@Before
	void setUp() {
		User.metaClass.encodePassword = { -> "password"}
		user = new User(username: "dmaclean@gmail.com", password: "password").save(flush: true)
		flt = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
		fantasyTeam = new FantasyTeam(name: "Test team", user: user, fantasyLeagueType: flt, season: 2013, leagueId: "111", numOwners: 1, fantasyTeamStarters: []).save(flush: true)
		scoringSystem = new ScoringSystem(name: "Test SS", fantasyTeam: fantasyTeam, scoringRules: []).save(flush: true)

		player = new Player(name: "Dan MacLean", position: Player.POSITION_QB, stats: []).save(flush: true)

		passingYards = new Stat(player: player, season: 2012, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 1).save(flush: true)
		passingTouchdowns = new Stat(player: player, season: 2012, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 1).save(flush: true)
		interceptions = new Stat(player: player, season: 2012, week: -1, statKey: FantasyConstants.STAT_INTERCEPTIONS, statValue: 1).save(flush: true)
		rushingYards = new Stat(player: player, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 1).save(flush: true)
		rushingTouchdowns = new Stat(player: player, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 1).save(flush: true)

		fantasyTeam.fantasyTeamStarters.add(new FantasyTeamStarter(fantasyTeam: fantasyTeam, position: Player.POSITION_QB, numStarters: 1).save(flush: true))
	}

	@After
	void tearDown() {
		user = null
		flt = null
		fantasyTeam = null
		scoringSystem = null
		player = null

		passingYards = null
		passingTouchdowns = null
		interceptions = null
		rushingYards = null
		rushingTouchdowns = null
	}

    void testProcess_OneGenerationOneProjection() {
	    def gen = new FantasyPointsJob(fantasyTeam: fantasyTeam, season: 2012, week: -1, projection: FantasyPointsJob.NO_PROJECTION, completed: false).save(flush: true)
	    def proj = new FantasyPointsJob(fantasyTeam: fantasyTeam, season: 2013, week: -1, projection: FantasyPointsJob.TRADERAPIST_PROJECTION, completed: false).save(flush: true)

	    controller.params.fantasy_points_job_id = gen.id
	    controller.process()

	    def genResults = FantasyPoints.findAllByProjection(false)
	    assert "Expected 1 FantasyPoint non-projection, got ${ genResults.size() }", genResults.size() == 1
	    assert gen.completed
	    assert !proj.completed

	    controller.params.fantasy_points_job_id = proj.id
	    controller.process()

	    def projResults = FantasyPoints.findAllByProjection(true)
        assert "Expected 1 FantasyPoint projection, got ${ projResults.size() }", projResults.size() == 1
	    assert proj.completed
    }

	void testProcess_OneGeneration() {
		def gen = new FantasyPointsJob(fantasyTeam: fantasyTeam, season: 2012, week: -1, projection: FantasyPointsJob.NO_PROJECTION, completed: false).save(flush: true)

		controller.params.fantasy_points_job_id = gen.id
		controller.process()

		def genResults = FantasyPoints.findAllByProjection(false)
		def projResults = FantasyPoints.findAllByProjection(true)
		assert "Expected 1 FantasyPoint non-projection, got ${ genResults.size() }", genResults.size() == 1
		assert "Expected 0 FantasyPoint projection, got ${ projResults.size() }", projResults.size() == 0

		assert gen.completed
	}

	void testProcess_OneProjection() {
		def proj = new FantasyPointsJob(fantasyTeam: fantasyTeam, season: 2013, week: -1, projection: FantasyPointsJob.TRADERAPIST_PROJECTION, completed: false).save(flush: true)

		controller.params.fantasy_points_job_id = proj.id
		controller.process()

		def genResults = FantasyPoints.findAllByProjection(false)
		def projResults = FantasyPoints.findAllByProjection(true)
		assert "Expected 0 FantasyPoint non-projection, got ${ genResults.size() }", genResults.size() == 0
		assert "Expected 1 FantasyPoint projection, got ${ projResults.size() }", projResults.size() == 1

		assert proj.completed
	}

	void testProcess_YahooPPR() {
		def proj = new FantasyPointsJob(fantasyTeam: fantasyTeam, season: 2013, week: -1, projection: FantasyPointsJob.YAHOO_PPR_PROJECTION, completed: false).save(flush: true)

		def rodgers = new Player(id: 7200, name: "Aaron Rodgers", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
		rodgers.id = 7200
		rodgers.save(flush: true)

		def brees = new Player(id: 5479, name: "Drew Brees", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
		brees.id = 5479
		brees.save(flush: true)

		controller.params.fantasy_points_job_id = proj.id
		controller.process()

		def genResults = FantasyPoints.findAllByProjection(false)
		def projResults = FantasyPoints.findAllByProjection(true)
		assert "Expected 0 FantasyPoint non-projection, got ${ genResults.size() }", genResults.size() == 0
		assert "Expected 2 FantasyPoint projection, got ${ projResults.size() }", projResults.size() == 2

		def fps = FantasyPoints.list()
		assert fps.size() == 2

		assert fps[0].player == rodgers
		assert fps[0].points == 345.74

		assert fps[1].player == brees
		assert fps[1].points == 339.32

		assert proj.completed
	}

	void testProcess_YahooStandard() {
		fail("TBD")
	}
}

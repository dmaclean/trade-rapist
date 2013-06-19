package com.traderapist.models

import com.traderapist.constants.FantasyConstants
import org.junit.After
import org.junit.Before
import org.junit.Test

import static junit.framework.Assert.assertTrue

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 6/18/13
 * Time: 5:47 AM
 * To change this template use File | Settings | File Templates.
 */
class FantasyPointsControllerIntegrationTests {
	def controller
	def params
	def player

	@Before
	void setUp() {
		controller = new FantasyPointsController()
		params = [:]

		player = new Player(name: "Dan MacLean", position: "QB").save(flush: true)
	}

	@After
	void tearDown() {
		controller = null
		params = null

		player = null
	}

	@Test
	void testProjectPoints_Season() {
		controller.request.addParameter("system", "ESPNStandardScoringSystem")
		controller.request.addParameter("season", "2002")
		controller.request.addParameter("position", Player.POSITION_QB)
		controller.request.addParameter("num_startable", "1")
		controller.request.addParameter("num_owners", "1")

		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s3 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_INTERCEPTIONS, statValue: 2).save(flush: true)
		Stat s4 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s5 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 2).save(flush: true)

		player.stats = new HashSet<Stat>([s1,s2,s3,s4,s5])

		controller.projectPoints()

		def fps = FantasyPoints.findAllBySeason(2002)

		assertTrue "Should have found one FantasyPoints object for 2002", fps.size() == 1

		def fp = fps[0]

		assertTrue "Season is not 2002", fp.season == 2002
		assertTrue "Week is not -1", fp.week == -1
		assertTrue "Points is not 12", fp.points == -1
		assertTrue "Projection is not true", fp.projection
	}

	@Test
	void testProjectPoints_Season_Existing() {
		controller.request.addParameter("system", "ESPNStandardScoringSystem")
		controller.request.addParameter("season", "2002")
		controller.request.addParameter("position", Player.POSITION_QB)
		controller.request.addParameter("num_startable", "1")
		controller.request.addParameter("num_owners", "1")

		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s3 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_INTERCEPTIONS, statValue: 2).save(flush: true)
		Stat s4 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s5 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 2).save(flush: true)

		player.stats = new HashSet<Stat>([s1,s2,s3,s4,s5])

		controller.projectPoints()

		def fps = FantasyPoints.findAllBySeason(2002)

		assertTrue "Should have found one FantasyPoints object for 2002", fps.size() == 1

		def fp = fps[0]

		assertTrue "Season is not 2002", fp.season == 2002
		assertTrue "Week is not -1", fp.week == -1
		assertTrue "Points is not 12", fp.points == -1
		assertTrue "Projection is not true", fp.projection

		/*
		Make sure we didn't write a duplicate
		 */
		controller.projectPoints()

		fps = FantasyPoints.findAllBySeason(2002)

		assertTrue "Should have found one FantasyPoints object for 2002", fps.size() == 1

		assertTrue "Season is not 2002", fps[0].season == 2002
		assertTrue "Week is not -1", fps[0].week == -1
		assertTrue "Points is not 12", fps[0].points == -1
		assertTrue "Projection is not true", fp.projection
	}

	@Test
	void testProjectPoints_PositionQB_Season() {
		controller.request.addParameter("system", "ESPNStandardScoringSystem")
		controller.request.addParameter("season", "2002")
		controller.request.addParameter("position", Player.POSITION_QB)
		controller.request.addParameter("num_startable", "1")
		controller.request.addParameter("num_owners", "1")

		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s3 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_INTERCEPTIONS, statValue: 2).save(flush: true)
		Stat s4 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s5 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 2).save(flush: true)

		player.stats = new HashSet<Stat>([s1,s2,s3,s4,s5])

		controller.projectPoints()

		def fps = FantasyPoints.findAllBySeason(2002)

		assertTrue "Should have found one FantasyPoints object for 2002", fps.size() == 1

		def fp = fps[0]

		assertTrue "Season is not 2002", fp.season == 2002
		assertTrue "Week is not -1", fp.week == -1
		assertTrue "Points is not 12", fp.points == -1
		assertTrue "Projection is not true", fp.projection
	}

	@Test
	void testProjectPoints_PositionRB_Season() {
		controller.request.addParameter("system", "ESPNStandardScoringSystem")
		controller.request.addParameter("season", "2002")
		controller.request.addParameter("position", Player.POSITION_RB)
		controller.request.addParameter("num_startable", "1")
		controller.request.addParameter("num_owners", "1")

		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s3 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_INTERCEPTIONS, statValue: 2).save(flush: true)
		Stat s4 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s5 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 2).save(flush: true)

		player.stats = new HashSet<Stat>([s1,s2,s3,s4,s5])

		controller.projectPoints()

		def fps = FantasyPoints.findAllBySeason(2002)

		assertTrue "Should have found zero FantasyPoints object for 2002", fps.size() == 0
	}
}

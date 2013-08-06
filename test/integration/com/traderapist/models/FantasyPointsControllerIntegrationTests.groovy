package com.traderapist.models

import com.traderapist.constants.FantasyConstants
import com.traderapist.security.User
import org.junit.After
import org.junit.Before
import org.junit.Test

import static junit.framework.Assert.assertTrue

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 8/3/13
 * Time: 10:44 PM
 * To change this template use File | Settings | File Templates.
 */
class FantasyPointsControllerIntegrationTests {
	FantasyPointsController controller
	Player player
	User user
	FantasyLeagueType flt
	FantasyTeam fantasyTeam
	ScoringSystem scoringSystem

	def passingYardsRule
	def passingTouchdownRule
	def interceptionsRule
	def rushingYardsRule
	def rushingTouchdownsRule

	def sessionFactory

	@Before
	void setUp() {
		controller = new FantasyPointsController()
		User.metaClass.encodePassword = { -> "password"}
		user = new User(username: "test user", password: "password").save(flush: true)
		flt = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
		fantasyTeam = new FantasyTeam(name: "Test team", user: user, fantasyLeagueType: flt, season: 2013, leagueId: "111", numOwners: 10, fantasyTeamStarters: new HashSet<FantasyTeamStarter>()).save(flush: true)
		scoringSystem = new ScoringSystem(name: "Test SS", fantasyTeam: fantasyTeam, scoringRules: new HashSet<ScoringRule>()).save(flush: true)

		player = new Player(name: "Dan MacLean", position: "QB").save(flush: true)

		passingYardsRule = new ScoringRule(statKey: FantasyConstants.STAT_PASSING_YARDS, multiplier: 0.04).save(flush: true)
		passingTouchdownRule = new ScoringRule(statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, multiplier: 4).save(flush: true)
		interceptionsRule = new ScoringRule(statKey: FantasyConstants.STAT_INTERCEPTIONS, multiplier: -2).save(flush: true)
		rushingYardsRule = new ScoringRule(statKey: FantasyConstants.STAT_RUSHING_YARDS, multiplier: 0.1).save(flush: true)
		rushingTouchdownsRule = new ScoringRule(statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, multiplier: 6).save(flush: true)

		scoringSystem.scoringRules.add(passingYardsRule)
		scoringSystem.scoringRules.add(passingTouchdownRule)
		scoringSystem.scoringRules.add(interceptionsRule)
		scoringSystem.scoringRules.add(rushingYardsRule)
		scoringSystem.scoringRules.add(rushingTouchdownsRule)
	}

	@After
	void tearDown() {
		controller = null
		player = null
	}

	@Test
	void testProjectPoints_Season_Duplicates() {
		def qbStarters = new FantasyTeamStarter(position: Player.POSITION_QB, numStarters: 1, fantasyTeam: fantasyTeam).save(flush: true)
		def rbStarters = new FantasyTeamStarter(position: Player.POSITION_RB, numStarters: 2, fantasyTeam: fantasyTeam).save(flush: true)
		def wrStarters = new FantasyTeamStarter(position: Player.POSITION_WR, numStarters: 3, fantasyTeam: fantasyTeam).save(flush: true)
		def teStarters = new FantasyTeamStarter(position: Player.POSITION_TE, numStarters: 1, fantasyTeam: fantasyTeam).save(flush: true)
		def dStarters = new FantasyTeamStarter(position: Player.POSITION_DEF, numStarters: 1, fantasyTeam: fantasyTeam).save(flush: true)
		def kStarters = new FantasyTeamStarter(position: Player.POSITION_K, numStarters: 1, fantasyTeam: fantasyTeam).save(flush: true)

		fantasyTeam.fantasyTeamStarters.add(qbStarters)
		fantasyTeam.fantasyTeamStarters.add(rbStarters)
		fantasyTeam.fantasyTeamStarters.add(wrStarters)
		fantasyTeam.fantasyTeamStarters.add(teStarters)
		fantasyTeam.fantasyTeamStarters.add(dStarters)
		fantasyTeam.fantasyTeamStarters.add(kStarters)

		fantasyTeam.season = 2002

		fantasyTeam.numOwners = 1

		controller.request.addParameter("fantasy_team_id", fantasyTeam.id.toString())

		def dupeFP = new FantasyPoints(
				numOwners: 1,
				numStartable: 1,
				player: player,
				projection: true,
				season: fantasyTeam.season,
				week: -1,
				scoringSystem: scoringSystem,
				points: 20.2
		).save(flush: true)

		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 12).save(flush: true)
		Stat s3 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_INTERCEPTIONS, statValue: 2).save(flush: true)
		Stat s4 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s5 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 2).save(flush: true)

		controller.projectPoints()

		def fps = FantasyPoints.findAllBySeason(2002)

		assert "Should have found one FantasyPoints object for 2002, found ${ fps.size() }", fps.size() == 1

		def fp = fps[0]

		assert "Season is not 2002", fp.season == 2002
		assert "Week is not -1", fp.week == -1
		assert "Points is not 20.2, instead got ${ fp.points }", fp.points == 20.2
		assert "Projection is not true", fp.projection
	}

	@Test
	void testGeneratePoints_Season() {
		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)
		player.stats = new HashSet<>([s1,s2])
		player.save(flush: true)

		controller.request.setParameter("fantasy_team_id", fantasyTeam.id.toString())

		controller.generatePoints()

		sessionFactory.currentSession.flush()
		sessionFactory.currentSession.clear()

		def fps = FantasyPoints.findAllBySeason(2001)

		assertTrue "Should have found one FantasyPoints object for 2001, found ${ fps.size() }", fps.size() == 1

		def fp = fps[0]

		assertTrue "Season is not 2001, is ${ fp.season }", fp.season == 2001
		assertTrue "Week is not -1, is ${ fp.week }", fp.week == -1
		assertTrue "Points is not 12, ${ fp.points }", fp.points == 12
	}
}

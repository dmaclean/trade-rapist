package com.traderapist.models

import com.traderapist.constants.FantasyConstants
import com.traderapist.security.User
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.junit.Before

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(FantasyPoints)
@Mock([FantasyLeagueType, FantasyPointsJob, FantasyTeam, FantasyTeamStarter, Player, ScoringRule, ScoringSystem, Stat, User])
class FantasyPointsTests {

    Player player
	ScoringSystem scoringSystem

	User user
	FantasyLeagueType flt
	FantasyTeam fantasyTeam

	def passingYardsRule
	def passingTouchdownRule
	def interceptionsRule
	def rushingYardsRule
	def rushingTouchdownsRule

    @Before
    void setUp() {
        player = new Player(name: "Dan", position: Player.POSITION_QB)
        player.save(flush: true)

	    User.metaClass.encodePassword = { -> "password"}
	    user = new User(username: "dmaclean@gmail.com", password: "password").save(flush: true)
	    flt = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
	    fantasyTeam = new FantasyTeam(name: "Test team", user: user, fantasyLeagueType: flt, season: 2013, leagueId: "111", numOwners: 10, fantasyTeamStarters: []).save(flush: true)
	    fantasyTeam.fantasyTeamStarters.add(new FantasyTeamStarter(fantasyTeam: fantasyTeam, position: Player.POSITION_QB, numStarters: 1).save(flush: true))
	    fantasyTeam.fantasyTeamStarters.add(new FantasyTeamStarter(fantasyTeam: fantasyTeam, position: Player.POSITION_RB, numStarters: 2).save(flush: true))
	    fantasyTeam.fantasyTeamStarters.add(new FantasyTeamStarter(fantasyTeam: fantasyTeam, position: Player.POSITION_WR, numStarters: 3).save(flush: true))
	    fantasyTeam.fantasyTeamStarters.add(new FantasyTeamStarter(fantasyTeam: fantasyTeam, position: Player.POSITION_TE, numStarters: 1).save(flush: true))
	    fantasyTeam.fantasyTeamStarters.add(new FantasyTeamStarter(fantasyTeam: fantasyTeam, position: Player.POSITION_DEF, numStarters: 1).save(flush: true))
	    fantasyTeam.fantasyTeamStarters.add(new FantasyTeamStarter(fantasyTeam: fantasyTeam, position: Player.POSITION_K, numStarters: 1).save(flush: true))

	    scoringSystem = new ScoringSystem(name: "My Scoring System", fantasyTeam: fantasyTeam, scoringRules: []).save(flush: true)

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

	    mockForConstraintsTests(FantasyPoints)
    }

    void testSeasonNotNull() {
        FantasyPoints fp = new FantasyPoints(week: 1, points: 1, scoringSystem: scoringSystem)

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "nullable" == fp.errors["season"]
    }

    void testWeekNotNull() {
        FantasyPoints fp = new FantasyPoints(season: 2001, points: 1, scoringSystem: scoringSystem)

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "nullable" == fp.errors["week"]
    }

    void testWeekInRange() {
        FantasyPoints fp = new FantasyPoints(season: 2001, week: -2, points: 1, scoringSystem: scoringSystem)

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "range" == fp.errors["week"]

        fp.week = 18

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "range" == fp.errors["week"]
    }

    void testPointsNotNull() {
        FantasyPoints fp = new FantasyPoints(week: 1, season: 2001, scoringSystem: scoringSystem)

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "nullable" == fp.errors["points"]
    }

    void testScoringSystemNotNull() {
        FantasyPoints fp = new FantasyPoints(week: 1, season: 2001, points: 10)

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "nullable" == fp.errors["scoringSystem"]
    }

	void testProjectionDefaultFalse() {
		def player = new Player(id: 1, name: "Dan", position: "QB")
		FantasyPoints fp = new FantasyPoints(player: player, season: 2013, week: -1, scoringSystem: scoringSystem, points: 100)

		assertTrue fp.validate()
	}

	void testNumStartableNullable() {
		def player = new Player(id: 1, name: "Dan", position: "QB")
		FantasyPoints fp = new FantasyPoints(
				player: player,
				season: 2013,
				week: -1,
				points: 100,
				scoringSystem: scoringSystem,
				projection: true,
				numOwners: 10
		)

		assertTrue fp.validate()
	}

	void testNumOwnersNullable() {
		def player = new Player(id: 1, name: "Dan", position: "QB")
		FantasyPoints fp = new FantasyPoints(
				player: player,
				season: 2013,
				week: -1,
				points: 100,
				scoringSystem: scoringSystem,
				projection: true,
				numStartable: 1
		)

		assertTrue fp.validate()
	}

	void testGeneratePoints_Season() {
		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)

		FantasyPoints.generatePoints(fantasyTeam, player.position, 2001, -1)

		def fps = FantasyPoints.findAllBySeason(2001)

		assertTrue "Should have found one FantasyPoints object for 2001, found ${ fps.size() }", fps.size() == 1

		def fp = fps[0]

		assertTrue "Season is not 2001, is ${ fp.season }", fp.season == 2001
		assertTrue "Week is not -1, is ${ fp.week }", fp.week == -1
		assertTrue "Points is not 12, ${ fp.points }", fp.points == 12
	}

	void testGeneratePoints_SeasonWeekParams_Season() {
		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)

		FantasyPoints.generatePoints(fantasyTeam, player.position, 2001, -1)

		def fps = FantasyPoints.findAllBySeason(2001)

		assertTrue "Should have found one FantasyPoints object for 2001, found ${ fps.size() }", fps.size() == 1

		def fp = fps[0]

		assertTrue "Season is not 2001, is ${ fp.season }", fp.season == 2001
		assertTrue "Week is not -1, is ${ fp.week }", fp.week == -1
		assertTrue "Points is not 12, ${ fp.points }", fp.points == 12
	}

	void testGeneratePoints_PositionQB_Season() {
		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)

		FantasyPoints.generatePoints(fantasyTeam, player.position, 2001, -1)

		def fps = FantasyPoints.findAllBySeason(2001)

		assertTrue "Should have found one FantasyPoints object for 2001, found ${ fps.size() }", fps.size() == 1

		def fp = fps[0]

		assertTrue "Season is not 2001", fp.season == 2001
		assertTrue "Week is not -1", fp.week == -1
		assertTrue "Points is not 12", fp.points == 12
	}

	void testGeneratePoints_SeasonWeekParams_PositionQB_Season() {
		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)

		FantasyPoints.generatePoints(fantasyTeam, player.position, 2001, -1)

		def fps = FantasyPoints.findAllBySeason(2001)

		assertTrue "Should have found one FantasyPoints object for 2001, found ${ fps.size() }", fps.size() == 1

		def fp = fps[0]

		assertTrue "Season is not 2001", fp.season == 2001
		assertTrue "Week is not -1", fp.week == -1
		assertTrue "Points is not 12", fp.points == 12
	}

	void testGeneratePoints_PositionRB_Season() {
		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)

		FantasyPoints.generatePoints(fantasyTeam, Player.POSITION_RB, 2001, -1)

		def fps = FantasyPoints.findAllBySeason(2001)

		assertTrue "Should have found zero FantasyPoints object for 2001, found ${ fps.size() }", fps.size() == 0
	}

	void testGeneratePoints_SeasonWeekParams_PositionRB_Season() {
		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)

		FantasyPoints.generatePoints(fantasyTeam, Player.POSITION_RB, 2001, -1)

		def fps = FantasyPoints.findAllBySeason(2001)

		assertTrue "Should have found zero FantasyPoints object for 2001, found ${ fps.size() }", fps.size() == 0
	}

	void testProjectPoints_Season() {
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

		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s3 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_INTERCEPTIONS, statValue: 2).save(flush: true)
		Stat s4 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s5 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 2).save(flush: true)

		FantasyPoints.projectPoints(fantasyTeam)

		def fps = FantasyPoints.findAllBySeason(2002)

		assertTrue "Should have found one FantasyPoints object for 2002, found ${ fps.size() }", fps.size() == 1

		def fp = fps[0]

		assertTrue "Season is not 2002", fp.season == 2002
		assertTrue "Week is not -1", fp.week == -1
		assertTrue "Points is not 20.2, instead got ${ fp.points }", fp.points == 20.2
		assertTrue "Projection is not true", fp.projection
	}

	void testProjectPoints_YahooPPR2013() {
		def rodgers = new Player(id: 7200, name: "Aaron Rodgers", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
		rodgers.id = 7200
		rodgers.save(flush: true)

		def brees = new Player(id: 5479, name: "Drew Brees", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
		brees.id = 5479
		brees.save(flush: true)

		def peterson = new Player(id: 8261, name: "Adrian Peterson", position: Player.POSITION_RB, stats: [], averageDraftPositions: []).save(flush: true)
		peterson.id = 8261
		peterson.save(flush: true)

		def job = new FantasyPointsJob(fantasyTeam: fantasyTeam, completed: false, season: 2013, week: -1, projection: FantasyPointsJob.YAHOO_PPR_PROJECTION).save(flush: true)

		FantasyPoints.projectPoints(job)

		def fps = FantasyPoints.list()
		assert fps.size() == 3

		assert fps[0].player == rodgers
		assert fps[0].points == 345.74

		assert fps[1].player == brees
		assert fps[1].points == 339.32

		assert fps[2].player == peterson
		assert fps[2].points == 307.83
	}

	void testProjectPoints_YahooStandard2013() {
		def rodgers = new Player(id: 7200, name: "Aaron Rodgers", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
		rodgers.id = 7200
		rodgers.save(flush: true)

		def brees = new Player(id: 5479, name: "Drew Brees", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
		brees.id = 5479
		brees.save(flush: true)

		def peterson = new Player(id: 8261, name: "Adrian Peterson", position: Player.POSITION_RB, stats: [], averageDraftPositions: []).save(flush: true)
		peterson.id = 8261
		peterson.save(flush: true)

		def job = new FantasyPointsJob(fantasyTeam: fantasyTeam, completed: false, season: 2013, week: -1, projection: FantasyPointsJob.YAHOO_STANDARD_PROJECTION).save(flush: true)

		FantasyPoints.projectPoints(job)

		def fps = FantasyPoints.list()
		assert fps.size() == 3

		assert fps[0].player == rodgers
		assert fps[0].points == 345.74

		assert fps[1].player == brees
		assert fps[1].points == 339.32

		assert fps[2].player == peterson
		assert fps[2].points == 269.89
	}
}

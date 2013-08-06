package com.traderapist.models

import com.traderapist.security.User
import grails.test.mixin.*
import org.junit.*

import com.traderapist.constants.FantasyConstants

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Player)
@Mock([FantasyLeagueType,FantasyTeam,ScoringSystem,ScoringRule,Stat,FantasyPoints,User])
class PlayerTests {

	User user
	FantasyLeagueType flt
	FantasyTeam fantasyTeam
	ScoringSystem scoringSystem

	/*
	 * Scoring Rules
	 */
	def passingYardsRule
	def passingTouchdownRule
	def interceptionsRule
	def rushingYardsRule
	def rushingTouchdownsRule
	def receptionsRule
	def receptionYardsRule
	def receptionTouchdownsRule

	@Before
	void setUp() {
		User.metaClass.encodePassword = { -> "password"}
		user = new User(username: "dmaclean", password: "password").save(flush: true)
		flt = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
		fantasyTeam = new FantasyTeam(name: "Test team", user: user, fantasyLeagueType: flt, season: 2013, leagueId: "111", numOwners: 10, fantasyTeamStarters: new HashSet<FantasyTeamStarter>()).save(flush: true)

		scoringSystem = new ScoringSystem(name: "Test SS", fantasyTeam: fantasyTeam, scoringRules: new HashSet<ScoringRule>()).save(flush: true)
		passingYardsRule = new ScoringRule(statKey: FantasyConstants.STAT_PASSING_YARDS, multiplier: 0.04).save(flush: true)
		passingTouchdownRule = new ScoringRule(statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, multiplier: 4).save(flush: true)
		interceptionsRule = new ScoringRule(statKey: FantasyConstants.STAT_INTERCEPTIONS, multiplier: -2).save(flush: true)
		rushingYardsRule = new ScoringRule(statKey: FantasyConstants.STAT_RUSHING_YARDS, multiplier: 0.1).save(flush: true)
		rushingTouchdownsRule = new ScoringRule(statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, multiplier: 6).save(flush: true)
		receptionsRule = new ScoringRule(statKey: FantasyConstants.STAT_RECEPTIONS, multiplier: 0).save(flush: true)
		receptionYardsRule = new ScoringRule(statKey: FantasyConstants.STAT_RECEPTION_YARDS, multiplier: 0.1).save(flush: true)
		receptionTouchdownsRule = new ScoringRule(statKey: FantasyConstants.STAT_RECEPTION_TOUCHDOWNS, multiplier: 6).save(flush: true)

		scoringSystem.scoringRules.add(passingYardsRule)
		scoringSystem.scoringRules.add(passingTouchdownRule)
		scoringSystem.scoringRules.add(interceptionsRule)
		scoringSystem.scoringRules.add(rushingYardsRule)
		scoringSystem.scoringRules.add(rushingTouchdownsRule)
		scoringSystem.scoringRules.add(receptionsRule)
		scoringSystem.scoringRules.add(receptionYardsRule)
		scoringSystem.scoringRules.add(receptionTouchdownsRule)
	}

	@After
	void tearDown() {
		scoringSystem = null
		passingYardsRule = null
		passingTouchdownRule = null

		fantasyTeam = null
	}

   void testNameNotBlank() {
        def player = new Player(name:"")

        mockForConstraintsTests(Player, [player])

        assertFalse "The player validation should have failed", player.validate()

        assert "blank" == player.errors["name"]
    }

    void testPositionNotBlank() {
        def player = new Player(position:"")

        mockForConstraintsTests(Player, [player])

        assertFalse "The player validation should have failed", player.validate()

        assert "blank" == player.errors["position"]
    }

    void testCalculatePoints_SeasonStats() {
        Player player = new Player(name: "Dan", position: Player.POSITION_QB, stats: []).save(flush: true)
        Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 200).save(flush: true)
        Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)

		player.computeFantasyPoints(fantasyTeam)

	    def fantasyPoints = FantasyPoints.findAllBySeason(2001)
        assert "Expecting 1 entry for FantasyPoints", fantasyPoints.size() == 1

        assert fantasyPoints[0].season == 2001
        assert fantasyPoints[0].week == -1
        assert fantasyPoints[0].points == 16
    }

	void testCalculatePoints_SeasonStats_ESPNStandardScoring_AlreadyExist() {
		Player player = new Player(name: "Dan", position: Player.POSITION_QB, stats: []).save(flush: true)
		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 200).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)

		fantasyTeam.fantasyTeamStarters.add(new FantasyTeamStarter(position: Player.POSITION_QB, numStarters: 1))

		// Create existing FantasyPoints entry for 2001 season
		FantasyPoints fp2001 = new FantasyPoints(
				player: player,
				points: 16,
				season: 2001,
				week: -1,
				scoringSystem: scoringSystem,
				numOwners: fantasyTeam.numOwners,
				numStartable: 1
		).save(flush: true)

		player.computeFantasyPoints(fantasyTeam)

		def fantasyPoints = FantasyPoints.findAllBySeason(2001)
		assertTrue "Expecting 1 entry for FantasyPoints", fantasyPoints.size() == 1

		assert fantasyPoints[0].season == 2001
		assert fantasyPoints[0].week == -1
		assert fantasyPoints[0].points == 16
	}

    void testCalculatePoints_WeekStats_ESPNStandardScoring() {
        Player player = new Player(name: "Dan", position: Player.POSITION_QB, stats: []).save(flush: true)
        Stat s1 = new Stat(player: player, season: 2001, week: 1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 200).save(flush: true)
        Stat s2 = new Stat(player: player, season: 2001, week: 1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)

        player.computeFantasyPoints(fantasyTeam)
	    def fantasyPoints = FantasyPoints.findAllBySeason(2001)
	    assertTrue "Expecting 1 entry for FantasyPoints, got ${ fantasyPoints.size() }", fantasyPoints.size() == 1

        assert fantasyPoints[0].season == 2001
        assert fantasyPoints[0].week == 1
        assert fantasyPoints[0].points == 16
    }

	void testCalculatePoints_WeekStats_ESPNStandardScoring_AlreadyExists() {
		Player player = new Player(name: "Dan", position: Player.POSITION_QB, stats: []).save(flush: true)
		Stat s1 = new Stat(player: player, season: 2001, week: 1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 200).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: 1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)

		// Create existing FantasyPoints entry for 2001 season
		FantasyPoints fp2001 = new FantasyPoints(player: player, points: 16, season: 2001, week: 1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: 1).save(flush: true)

		player.computeFantasyPoints(fantasyTeam)
		def fantasyPoints = FantasyPoints.findAllBySeason(2001)
		assertTrue "Expecting 1 entry for FantasyPoints, got ${ fantasyPoints.size() }", fantasyPoints.size() == 1

		assert fantasyPoints[0].season == 2001
		assert fantasyPoints[0].week == 1
		assert fantasyPoints[0].points == 16
	}

	void testGetCorrelation_Position_Stat() {
		assert Player.getCorrelation(Player.POSITION_QB, FantasyConstants.STAT_PASSING_YARDS) == 0.5
		assert Player.getCorrelation(Player.POSITION_QB, FantasyConstants.STAT_PASSING_TOUCHDOWNS) == 0.37
		assert Player.getCorrelation(Player.POSITION_QB, FantasyConstants.STAT_INTERCEPTIONS) == 0.08
		assert Player.getCorrelation(Player.POSITION_QB, FantasyConstants.STAT_RUSHING_YARDS) == 0.78
		assert Player.getCorrelation(Player.POSITION_QB, FantasyConstants.STAT_RUSHING_TOUCHDOWNS) == 0.5

		assert Player.getCorrelation(Player.POSITION_RB, FantasyConstants.STAT_RUSHING_YARDS) == 0.5
		assert Player.getCorrelation(Player.POSITION_RB, FantasyConstants.STAT_RUSHING_TOUCHDOWNS) == 0.5
		assert Player.getCorrelation(Player.POSITION_RB, FantasyConstants.STAT_RECEPTIONS) == 0.54
		assert Player.getCorrelation(Player.POSITION_RB, FantasyConstants.STAT_RECEPTION_YARDS) == 0.51
		assert Player.getCorrelation(Player.POSITION_RB, FantasyConstants.STAT_RECEPTION_TOUCHDOWNS) == 0.29

		assert Player.getCorrelation(Player.POSITION_WR, FantasyConstants.STAT_RECEPTION_YARDS) == 0.58
		assert Player.getCorrelation(Player.POSITION_WR, FantasyConstants.STAT_RECEPTION_TOUCHDOWNS) == 0.38
		assert Player.getCorrelation(Player.POSITION_WR, FantasyConstants.STAT_RECEPTIONS) == 0.64

		assert Player.getCorrelation(Player.POSITION_TE, FantasyConstants.STAT_RECEPTION_YARDS) == 0.74
		assert Player.getCorrelation(Player.POSITION_TE, FantasyConstants.STAT_RECEPTION_TOUCHDOWNS) == 0.44
		assert Player.getCorrelation(Player.POSITION_TE, FantasyConstants.STAT_RECEPTIONS) == 0.65

		assert Player.getCorrelation(Player.POSITION_DEF, null) == 0.1

		assert Player.getCorrelation(Player.POSITION_K, null) == 0.1
	}

	void testCalculateProjectedPointsQB_season_numStartable1_numOwners3() {
		def numStartable = 1
		def numOwners = 3

		// Create three quarterbacks
		def q1 = new Player(name: "Quarterback 1", position: Player.POSITION_QB, stats: []).save(flush: true)
		def q2 = new Player(name: "Quarterback 2", position: Player.POSITION_QB, stats: []).save(flush: true)
		def q3 = new Player(name: "Quarterback 3", position: Player.POSITION_QB, stats: []).save(flush: true)
		def q4 = new Player(name: "Quarterback 4", position: Player.POSITION_QB, stats: []).save(flush: true)
		def r1 = new Player(name: "Running back 1", position: Player.POSITION_RB, stats: []).save(flush: true)

		// Create stats for passing yards, passing touchdowns, interceptions, rushing yards, and rushing touchdowns
		// for each quarterback for the previous season.
		def py1 = new Stat(player: q1, season: 2012, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		def py2 = new Stat(player: q2, season: 2012, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 90).save(flush: true)
		def py3 = new Stat(player: q3, season: 2012, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 80).save(flush: true)         // our avg player
		def py4 = new Stat(player: q4, season: 2012, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 70).save(flush: true)

		def pt1 = new Stat(player: q1, season: 2012, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 8).save(flush: true)     // our avg player
		def pt2 = new Stat(player: q2, season: 2012, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 9).save(flush: true)
		def pt3 = new Stat(player: q3, season: 2012, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 10).save(flush: true)
		def pt4 = new Stat(player: q4, season: 2012, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 7).save(flush: true)

		def i1 = new Stat(player: q1, season: 2012, week: -1, statKey: FantasyConstants.STAT_INTERCEPTIONS, statValue: 1).save(flush: true)
		def i2 = new Stat(player: q2, season: 2012, week: -1, statKey: FantasyConstants.STAT_INTERCEPTIONS, statValue: 2).save(flush: true)
		def i3 = new Stat(player: q3, season: 2012, week: -1, statKey: FantasyConstants.STAT_INTERCEPTIONS, statValue: 3).save(flush: true)     // our avg player
		def i4 = new Stat(player: q4, season: 2012, week: -1, statKey: FantasyConstants.STAT_INTERCEPTIONS, statValue: 4).save(flush: true)

		def ry1 = new Stat(player: q1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 8).save(flush: true)
		def ry2 = new Stat(player: q2, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 9).save(flush: true)      // our avg player
		def ry3 = new Stat(player: q3, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 10).save(flush: true)
		def ry4 = new Stat(player: q4, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 11).save(flush: true)
		def ry5 = new Stat(player: r1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 100).save(flush: true)

		def rt1 = new Stat(player: q1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 3).save(flush: true)
		def rt2 = new Stat(player: q2, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 2).save(flush: true)
		def rt3 = new Stat(player: q3, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 1).save(flush: true)  // our avg player
		def rt4 = new Stat(player: q4, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 0).save(flush: true)
		def rt5 = new Stat(player: r1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 10).save(flush: true)

		/*
		 * Do projections
		 *
		 * Quarterback 1
		 *
		 * 2013 passing yards =         (100 * 0.5 ) + (80 * 0.5) = 90 --> 90 / 25 = 3.6
		 * 2013 passing touchdowns =    (8 * 0.37) + (8 * 0.63) = 2.96 + 5.04 = 8 --> 8 * 4 = 32
		 * 2013 interceptions =         (1 * 0.08) + (3 * 0.92) = 0.08 + 2.76 = 2.84 --> 2 * -2 = -4
		 * 2013 rushing yards =         (8 * 0.78) + (9 * 0.22) = 6.24 + 1.98 = 8.22 --> 8 /10 = .8
		 * 2013 rushing touchdowns =    (3 * 0.5) + (1 * 0.5) = 1.5 + 0.5 = 2 --> 2 * 6 = 12
		 */
		assert q1.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 44.4

		/*
		 * Quarterback 2
		 *
		 * 2013 passing yards =         (90 * 0.5 ) + (80 * 0.5) = 85 --> 85 / 25 = 3.4
		 * 2013 passing touchdowns =    (9 * 0.37) + (8 * 0.63) = 3.33 + 5.04 = 8.37 --> 8 * 4 = 32
		 * 2013 interceptions =         (2 * 0.08) + (3 * 0.92) = 0.16 + 2.76 = 2.92 --> 2 * -2 = -4
		 * 2013 rushing yards =         (9 * 0.78) + (9 * 0.22) = 7.02 + 1.98 = 9 --> 9 /10 = .9
		 * 2013 rushing touchdowns =    (2 * 0.5) + (1 * 0.5) = 1 + 0.5 = 1.5 --> 1 * 6 = 6
		 */
		assert q2.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 38.3

		/*
		 * Quarterback 3
		 *
		 * 2013 passing yards =         (80 * 0.5 ) + (80 * 0.5) = 80 --> 80 / 25 = 3.2
		 * 2013 passing touchdowns =    (10 * 0.37) + (8 * 0.63) = 3.7 + 5.04 = 8.74 --> 8 * 4 = 32
		 * 2013 interceptions =         (3 * 0.08) + (3 * 0.92) = 0.24 + 2.76 = 3 --> 3 * -2 = -6
		 * 2013 rushing yards =         (10 * 0.78) + (9 * 0.22) = 7.8 + 1.98 = 9.78 --> 9 /10 = .9
		 * 2013 rushing touchdowns =    (1 * 0.5) + (1 * 0.5) = 0.5 + 0.5 = 1 --> 1 * 6 = 6
		 */
		def q3Result = q3.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem)
		assert q3Result > 36.01 && q3Result < 36.2

		/*
		 * Quarterback 4
		 *
		 * 2013 passing yards =         (70 * 0.5 ) + (80 * 0.5) = 75 --> 75 / 25 = 3
		 * 2013 passing touchdowns =    (7 * 0.37) + (8 * 0.63) = 2.59 + 5.04 = 7.63 --> 7 * 4 = 28
		 * 2013 interceptions =         (4 * 0.08) + (3 * 0.92) = 0.32 + 2.76 = 3.08 --> 3 * -2 = -6
		 * 2013 rushing yards =         (11 * 0.78) + (9 * 0.22) = 8.58 + 1.98 = 10.56 --> 10 /10 = 1
		 * 2013 rushing touchdowns =    (0 * 0.5) + (1 * 0.5) = 0 + 0.5 = 0.5 --> 0 * 6 = 0
		 */
		assert q4.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 26
	}

	void testCalculateProjectedPointsRB_season_numStartable1_numOwners3() {
		def numStartable = 1
		def numOwners = 3

		// Create three quarterbacks
		def r1 = new Player(name: "Running back 1", position: Player.POSITION_RB, stats: [], averageDraftPositions: []).save(flush: true)
		def r2 = new Player(name: "Running back 2", position: Player.POSITION_RB, stats: [], averageDraftPositions: []).save(flush: true)
		def r3 = new Player(name: "Running back 3", position: Player.POSITION_RB, stats: [], averageDraftPositions: []).save(flush: true)
		def r4 = new Player(name: "Running back 4", position: Player.POSITION_RB, stats: [], averageDraftPositions: []).save(flush: true)
		def q1 = new Player(name: "Quarterback 1", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)

		// Create stats for passing yards, passing touchdowns, interceptions, rushing yards, and rushing touchdowns
		// for each quarterback for the previous season.
		def ry1 = new Stat(player: r1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 8).save(flush: true)
		def ry2 = new Stat(player: r2, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 9).save(flush: true)      // our avg player
		def ry3 = new Stat(player: r3, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 10).save(flush: true)
		def ry4 = new Stat(player: r4, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 11).save(flush: true)
		def ry5 = new Stat(player: q1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 100).save(flush: true)

		def rt1 = new Stat(player: r1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 3).save(flush: true)
		def rt2 = new Stat(player: r2, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 2).save(flush: true)
		def rt3 = new Stat(player: r3, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 1).save(flush: true)  // our avg player
		def rt4 = new Stat(player: r4, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 0).save(flush: true)
		def rt5 = new Stat(player: q1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 10).save(flush: true)

		def rcy1 = new Stat(player: r1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_YARDS, statValue: 100).save(flush: true)
		def rcy2 = new Stat(player: r2, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_YARDS, statValue: 90).save(flush: true)
		def rcy3 = new Stat(player: r3, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_YARDS, statValue: 80).save(flush: true)         // our avg player
		def rcy4 = new Stat(player: r4, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_YARDS, statValue: 70).save(flush: true)

		def rct1 = new Stat(player: r1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_TOUCHDOWNS, statValue: 8).save(flush: true)     // our avg player
		def rct2 = new Stat(player: r2, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_TOUCHDOWNS, statValue: 9).save(flush: true)
		def rct3 = new Stat(player: r3, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_TOUCHDOWNS, statValue: 10).save(flush: true)
		def rct4 = new Stat(player: r4, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_TOUCHDOWNS, statValue: 7).save(flush: true)

		def rc1 = new Stat(player: r1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTIONS, statValue: 1).save(flush: true)
		def rc2 = new Stat(player: r2, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTIONS, statValue: 2).save(flush: true)     // our avg player
		def rc3 = new Stat(player: r3, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTIONS, statValue: 3).save(flush: true)
		def rc4 = new Stat(player: r4, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTIONS, statValue: 4).save(flush: true)

		/*
		 * Do projections
		 *
		 * RB 1
		 *
		 * 2013 rushing yards =         (8 * 0.5) + (9 * 0.5) = 4 + 4.5 = 8.5 --> 8/10 = .8
		 * 2013 rushing touchdowns =    (3 * 0.5) + (1 * 0.5) = 1.5 + 0.5 = 2 --> 2 * 6 = 12
		 * 2013 reception yards =       (100 * 0.51) + (80 * 0.49) = 51 + 39.2 = 90.2 --> 90 / 10 = 9
		 * 2013 reception touchdowns =  (8 * 0.29) + (8 * 0.71) = 2.32 + 5.68 = 8 --> 8 * 6 = 48
		 * 2013 receptions =            (1 * 0.54) + (2 * 0.46) = 0.54 + 0.92 = 1.46 --> 1 * 0 = 0
		 */
		assert r1.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 69.8

		/*
		 * RB 2
		 *
		 * 2013 rushing yards =         (9 * 0.5) + (9 * 0.5) = 4.5 + 4.5 = 8.5 --> 9 /10 = .9
		 * 2013 rushing touchdowns =    (2 * 0.5) + (1 * 0.5) = 1 + 0.5 = 1.5 --> 1 * 6 = 6
		 * 2013 reception yards =       (90 * 0.51) + (80 * 0.49) = 45.9 + 39.2 = 85.1 --> 85 / 10 = 8.5
		 * 2013 reception touchdowns =  (9 * 0.29) + (8 * 0.71) = 2.61 + 5.68 = 8.29 --> 8 * 6 = 48
		 * 2013 receptions =            (2 * 0.54) + (2 * 0.46) = 1.08 + 0.92 = 2 --> 2 * 0 = 0
		 */
		assert r2.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 63.4

		/*
		 * RB 3
		 *
		 * 2013 rushing yards =         (10 * 0.5) + (9 * 0.5) = 5 + 4.5 = 9.5 --> 9 /10 = .9
		 * 2013 rushing touchdowns =    (1 * 0.5) + (1 * 0.5) = 0.5 + 0.5 = 1 --> 1 * 6 = 6
		 * 2013 reception yards =       (80 * 0.51) + (80 * 0.49) = 40.8 + 39.2 = 80 --> 80 / 10 = 8
		 * 2013 reception touchdowns =  (10 * 0.29) + (8 * 0.71) = 2.9 + 5.68 = 8.58 --> 8 * 6 = 48
		 * 2013 receptions =            (3 * 0.54) + (2 * 0.46) = 1.62 + 0.92 = 2.54 --> 2 * 0 = 0
		 */
		assert r3.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 62.9

		/*
		 * RB 4
		 *
		 * 2013 rushing yards =         (11 * 0.5) + (9 * 0.5) = 5.5 + 4.5 = 10 --> 10 /10 = 1
		 * 2013 rushing touchdowns =    (0 * 0.5) + (1 * 0.5) = 0 + 0.5 = 0.5 --> 0 * 6 = 0
		 * 2013 reception yards =       (70 * 0.51) + (80 * 0.49) = 35/7 + 39.2 = 74.9 --> 74 / 10 = 7.4
		 * 2013 reception touchdowns =  (7 * 0.29) + (8 * 0.71) = 2.03 + 5.68 = 7.71 --> 7 * 6 = 42
		 * 2013 receptions =            (4 * 0.54) + (2 * 0.46) = 2.16 + 0.92 = 3.08 --> 3 * 0 = 0
		 */
		assert r4.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 50.4
	}

	void testCalculateProjectedPointsWR_season_numStartable1_numOwners3() {
		def numStartable = 1
		def numOwners = 3

		// Create three quarterbacks
		def w1 = new Player(name: "Wide Receiver 1", position: Player.POSITION_WR, stats: [], averageDraftPositions: []).save(flush: true)
		def w2 = new Player(name: "Wide Receiver 2", position: Player.POSITION_WR, stats: [], averageDraftPositions: []).save(flush: true)
		def w3 = new Player(name: "Wide Receiver 3", position: Player.POSITION_WR, stats: [], averageDraftPositions: []).save(flush: true)
		def w4 = new Player(name: "Wide Receiver 4", position: Player.POSITION_WR, stats: [], averageDraftPositions: []).save(flush: true)
		def r1 = new Player(name: "Running back 1", position: Player.POSITION_RB, stats: [], averageDraftPositions: []).save(flush: true)

		// Create stats for passing yards, passing touchdowns, interceptions, rushing yards, and rushing touchdowns
		// for each quarterback for the previous season.
		def rcy1 = new Stat(player: w1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_YARDS, statValue: 100).save(flush: true)
		def rcy2 = new Stat(player: w2, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_YARDS, statValue: 90).save(flush: true)
		def rcy3 = new Stat(player: w3, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_YARDS, statValue: 80).save(flush: true)         // our avg player
		def rcy4 = new Stat(player: w4, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_YARDS, statValue: 70).save(flush: true)
		def rcy5 = new Stat(player: r1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_YARDS, statValue: 170).save(flush: true)

		def rct1 = new Stat(player: w1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_TOUCHDOWNS, statValue: 8).save(flush: true)     // our avg player
		def rct2 = new Stat(player: w2, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_TOUCHDOWNS, statValue: 9).save(flush: true)
		def rct3 = new Stat(player: w3, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_TOUCHDOWNS, statValue: 10).save(flush: true)
		def rct4 = new Stat(player: w4, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_TOUCHDOWNS, statValue: 7).save(flush: true)

		def rc1 = new Stat(player: w1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTIONS, statValue: 1).save(flush: true)
		def rc2 = new Stat(player: w2, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTIONS, statValue: 2).save(flush: true)     // our avg player
		def rc3 = new Stat(player: w3, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTIONS, statValue: 3).save(flush: true)
		def rc4 = new Stat(player: w4, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTIONS, statValue: 4).save(flush: true)

		/*
		 * Do projections
		 *
		 * WR 1
		 *
		 * 2013 reception yards =       (100 * 0.58) + (80 * 0.42) = 58 + 33.6 = 91.6 --> 91 / 10 = 9.1
		 * 2013 reception touchdowns =  (8 * 0.38) + (8 * 0.62) = 3.04 + 4.96 = 8 --> 8 * 6 = 48
		 * 2013 receptions =            (1 * 0.64) + (2 * 0.36) = 0.64 + 0.72 = 1.36 --> 1 * 0 = 0
		 */
		assert w1.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 57.1

		/*
		 * WR 2
		 *
		 * 2013 reception yards =       (90 * 0.58) + (80 * 0.42) = 52.2 + 33.6 = 85.8 --> 85 / 10 = 8.5
		 * 2013 reception touchdowns =  (9 * 0.38) + (8 * 0.62) = 3.42 + 4.96 = 8.38 --> 8 * 6 = 48
		 * 2013 receptions =            (2 * 0.64) + (2 * 0.36) = 1.28 + 0.72 = 2 --> 2 * 0 = 0
		 */
		assert w2.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 56.5

		/*
		 * WR 3
		 *
		 * 2013 reception yards =       (80 * 0.58) + (80 * 0.42) = 46.4 + 33.6 = 80 --> 80 / 10 = 8
		 * 2013 reception touchdowns =  (10 * 0.38) + (8 * 0.62) = 3.8 + 4.96 = 8.76 --> 8 * 6 = 48
		 * 2013 receptions =            (3 * 0.64) + (2 * 0.36) = 1.92 + 0.72 = 3.64 --> 3 * 0 = 0
		 */
		assert w3.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 56

		/*
		 * WR 4
		 *
		 * 2013 reception yards =       (70 * 0.58) + (80 * 0.42) = 40.6 + 33.6 = 74.2 --> 74 / 10 = 7.4
		 * 2013 reception touchdowns =  (7 * 0.38) + (8 * 0.62) = 2.66 + 4.96 = 7.62 --> 7 * 6 = 42
		 * 2013 receptions =            (4 * 0.64) + (2 * 0.36) = 2.56 + 0.72 = 3.28 --> 3 * 0 = 0
		 */
		assert w4.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 49.4
	}

	void testCalculateProjectedPointsTE_season_numStartable1_numOwners3() {
		def numStartable = 1
		def numOwners = 3

		// Create three quarterbacks
		def t1 = new Player(name: "Tight End 1", position: Player.POSITION_TE, stats: [], averageDraftPositions: []).save(flush: true)
		def t2 = new Player(name: "Tight End 2", position: Player.POSITION_TE, stats: [], averageDraftPositions: []).save(flush: true)
		def t3 = new Player(name: "Tight End 3", position: Player.POSITION_TE, stats: [], averageDraftPositions: []).save(flush: true)
		def t4 = new Player(name: "Tight End 4", position: Player.POSITION_TE, stats: [], averageDraftPositions: []).save(flush: true)
		def r1 = new Player(name: "Running back 1", position: Player.POSITION_RB, stats: [], averageDraftPositions: []).save(flush: true)

		// Create stats for passing yards, passing touchdowns, interceptions, rushing yards, and rushing touchdowns
		// for each quarterback for the previous season.
		def rcy1 = new Stat(player: t1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_YARDS, statValue: 100).save(flush: true)
		def rcy2 = new Stat(player: t2, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_YARDS, statValue: 90).save(flush: true)
		def rcy3 = new Stat(player: t3, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_YARDS, statValue: 80).save(flush: true)         // our avg player
		def rcy4 = new Stat(player: t4, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_YARDS, statValue: 70).save(flush: true)
		def rcy5 = new Stat(player: r1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_YARDS, statValue: 170).save(flush: true)

		def rct1 = new Stat(player: t1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_TOUCHDOWNS, statValue: 8).save(flush: true)     // our avg player
		def rct2 = new Stat(player: t2, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_TOUCHDOWNS, statValue: 9).save(flush: true)
		def rct3 = new Stat(player: t3, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_TOUCHDOWNS, statValue: 10).save(flush: true)
		def rct4 = new Stat(player: t4, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTION_TOUCHDOWNS, statValue: 7).save(flush: true)

		def rc1 = new Stat(player: t1, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTIONS, statValue: 1).save(flush: true)
		def rc2 = new Stat(player: t2, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTIONS, statValue: 2).save(flush: true)     // our avg player
		def rc3 = new Stat(player: t3, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTIONS, statValue: 3).save(flush: true)
		def rc4 = new Stat(player: t4, season: 2012, week: -1, statKey: FantasyConstants.STAT_RECEPTIONS, statValue: 4).save(flush: true)

		/*
		 * Do projections
		 *
		 * TE 1
		 *
		 * 2013 reception yards =       (100 * 0.74) + (80 * 0.26) = 74 + 20.8 = 94.8 --> 94 / 10 = 9.4
		 * 2013 reception touchdowns =  (8 * 0.44) + (8 * 0.66) = 3.04 + 5.28 = 8.32 --> 8 * 6 = 48
		 * 2013 receptions =            (1 * 0.65) + (2 * 0.35) = 0.65 + 0.70 = 1.35 --> 1 * 0 = 0
		 */
		assert t1.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 57.4

		/*
		 * TE 2
		 *
		 * 2013 reception yards =       (90 * 0.74) + (80 * 0.26) = 66.6 + 20.8 = 87.4 --> 87 / 10 = 8.7
		 * 2013 reception touchdowns =  (9 * 0.44) + (8 * 0.56) = 3.96 + 4.48 = 8.44 --> 8 * 6 = 48
		 * 2013 receptions =            (2 * 0.65) + (2 * 0.35) = 1.3 + 0.7 = 2 --> 2 * 0 = 0
		 */
		assert t2.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 56.7

		/*
		 * TE 3
		 *
		 * 2013 reception yards =       (80 * 0.74) + (80 * 0.26) = 59.2 + 20.8 = 80 --> 80 / 10 = 8
		 * 2013 reception touchdowns =  (10 * 0.44) + (8 * 0.56) = 4.4 + 4.48 = 8.88 --> 8 * 6 = 48
		 * 2013 receptions =            (3 * 0.65) + (2 * 0.35) = 1.95 + 0.7 = 2.65 --> 2 * 0 = 0
		 */
		assert t3.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 56

		/*
		 * TE 4
		 *
		 * 2013 reception yards =       (70 * 0.74) + (80 * 0.26) = 51.8 + 20.8 = 72.6 --> 72 / 10 = 7.2
		 * 2013 reception touchdowns =  (7 * 0.44) + (8 * 0.56) = 3.08 + 4.48 = 7.56 --> 7 * 6 = 42
		 * 2013 receptions =            (4 * 0.65) + (2 * 0.35) = 2.6 + 0.7 = 3.3 --> 3 * 0 = 0
		 */
		assert t4.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 49.2
	}

	void testCalculateProjectedPointsDEF_season_numStartable1_numOwners3() {
		def numStartable = 1
		def numOwners = 3

		// Create four defenses
		def d1 = new Player(name: "Defense 1", position: Player.POSITION_DEF, stats: [], averageDraftPositions: []).save(flush: true)
		def d2 = new Player(name: "Defense 2", position: Player.POSITION_DEF, stats: [], averageDraftPositions: []).save(flush: true)
		def d3 = new Player(name: "Defense 3", position: Player.POSITION_DEF, stats: [], averageDraftPositions: []).save(flush: true)
		def d4 = new Player(name: "Defense 4", position: Player.POSITION_DEF, stats: [], averageDraftPositions: []).save(flush: true)

		def fp2012_1 = new FantasyPoints(player: d1, season: 2012, week: -1, points: 100, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: 1).save(flush: true)
		def fp2012_2 = new FantasyPoints(player: d2, season: 2012, week: -1, points: 90, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: 1).save(flush: true)
		def fp2012_3 = new FantasyPoints(player: d3, season: 2012, week: -1, points: 80, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: 1).save(flush: true)  // Average player
		def fp2012_4 = new FantasyPoints(player: d4, season: 2012, week: -1, points: 70, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: 1).save(flush: true)

		/*
		 * Do projections
		 *
		 * DEF 1
		 *
		 * 2013 points allowed 14-20 =       (100 * 0.1) + (80 * 0.9) = 10 + 72 = 82 --> 82 * 1 = 82
		 */
		assert d1.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 82

		/*
		 * DEF 2
		 *
		 * 2013 points allowed 14-20 =       (90 * 0.1) + (80 * 0.9) = 9 + 72 = 81 --> 81 * 1 = 81
		 */
		assert d2.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 81

		/*
		 * DEF 3
		 *
		 * 2013 points allowed 14-20 =       (80 * 0.1) + (80 * 0.9) = 8 + 72 = 80 --> 80 * 1 = 80
		 */
		assert d3.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 80

		/*
		 * DEF 4
		 *
		 * 2013 points allowed 14-20 =       (70 * 0.1) + (80 * 0.9) = 7 + 72 = 79 --> 79 * 1 = 79
		 */
		assert d4.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 79
	}

	void testCalculateProjectedPointsK_season_numStartable1_numOwners3() {
		def numStartable = 1
		def numOwners = 3

		// Create four kickers
		def k1 = new Player(name: "Kicker 1", position: Player.POSITION_K, stats: [], averageDraftPositions: []).save(flush: true)
		def k2 = new Player(name: "Kicker 2", position: Player.POSITION_K, stats: [], averageDraftPositions: []).save(flush: true)
		def k3 = new Player(name: "Kicker 3", position: Player.POSITION_K, stats: [], averageDraftPositions: []).save(flush: true)
		def k4 = new Player(name: "Kicker 4", position: Player.POSITION_K, stats: [], averageDraftPositions: []).save(flush: true)

		def fp2012_1 = new FantasyPoints(player: k1, season: 2012, week: -1, points: 100, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: 1).save(flush: true)
		def fp2012_2 = new FantasyPoints(player: k2, season: 2012, week: -1, points: 90, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: 1).save(flush: true)
		def fp2012_3 = new FantasyPoints(player: k3, season: 2012, week: -1, points: 80, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: 1).save(flush: true)  // Average player
		def fp2012_4 = new FantasyPoints(player: k4, season: 2012, week: -1, points: 70, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: 1).save(flush: true)

		/*
		 * Do projections
		 *
		 * K 1
		 *
		 * 2013 points =       (100 * 0.1) + (80 * 0.9) = 10 + 72 = 82 --> 82 * 1 = 82
		 */
		assert k1.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 82

		/*
		 * K 2
		 *
		 * 2013 points =       (90 * 0.1) + (80 * 0.9) = 9 + 72 = 81 --> 81 * 1 = 81
		 */
		assert k2.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 81

		/*
		 * K 3
		 *
		 * 2013 points =       (80 * 0.1) + (80 * 0.9) = 8 + 72 = 80 --> 80 * 1 = 80
		 */
		assert k3.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 80

		/*
		 * K 4
		 *
		 * 2013 points =       (70 * 0.1) + (80 * 0.9) = 7 + 72 = 79 --> 79 * 1 = 79
		 */
		assert k4.calculateProjectedPoints(2013, numStartable, numOwners, scoringSystem) == 79
	}

	void testComputeFantasyPoints_Season() {
		def player = new Player(name: "Dan", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)

		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 200).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)

		player.computeFantasyPoints(fantasyTeam)
		def fantasyPoints = FantasyPoints.findAllBySeason(2001)
		assert fantasyPoints.size() == 1
		assert fantasyPoints[0].season == 2001
		assert fantasyPoints[0].week == -1
		assert fantasyPoints[0].points == 16
	}

	void testComputeFantasyPoints_Season_Duplicates() {
		def player = new Player(name: "Dan", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)

		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 200)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2)

		// Create existing FantasyPoints
		def fp = new FantasyPoints(
				season: 2001,
				week: -1,
				points: 100,
				projection: false,
				numOwners: 10,
				numStartable: 1,
				player: player,
				scoringSystem: scoringSystem
		).save(flush: true)

		assert FantasyPoints.list().size() == 1

		player.computeFantasyPoints(fantasyTeam)

		assert FantasyPoints.list().size() == 1
	}
}

package com.traderapist.models

import com.traderapist.constants.FantasyConstants
import com.traderapist.security.User
import org.junit.After
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.fail

class PlayerIntegrationTests {

	User user
	FantasyLeagueType flt
	FantasyTeam fantasyTeam
	ScoringSystem scoringSystem

	def startable

    @Before
    void setUp() {
	    User.metaClass.encodePassword = { -> "password"}
	    user = new User(username: "testuser@gmail.com", password: "password").save(flush: true)
	    flt = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
	    fantasyTeam = new FantasyTeam(name: "Test team", user: user, fantasyLeagueType: flt, season: 2013, leagueId: "111", numOwners: 10, fantasyTeamStarters: new HashSet<FantasyTeamStarter>()).save(flush: true)
	    scoringSystem = new ScoringSystem(name: "Test SS", fantasyTeam: fantasyTeam, scoringRules: new HashSet<ScoringRule>()).save(flush: true)

	    fantasyTeam.fantasyTeamStarters.add(new FantasyTeamStarter(position: Player.POSITION_QB, numStarters: 1, fantasyTeam: fantasyTeam).save(flush: true))
	    fantasyTeam.fantasyTeamStarters.add(new FantasyTeamStarter(position: Player.POSITION_RB, numStarters: 1, fantasyTeam: fantasyTeam).save(flush: true))
	    fantasyTeam.fantasyTeamStarters.add(new FantasyTeamStarter(position: Player.POSITION_WR, numStarters: 1, fantasyTeam: fantasyTeam).save(flush: true))
	    fantasyTeam.fantasyTeamStarters.add(new FantasyTeamStarter(position: Player.POSITION_TE, numStarters: 1, fantasyTeam: fantasyTeam).save(flush: true))
	    fantasyTeam.fantasyTeamStarters.add(new FantasyTeamStarter(position: Player.POSITION_DEF, numStarters: 1, fantasyTeam: fantasyTeam).save(flush: true))
	    fantasyTeam.fantasyTeamStarters.add(new FantasyTeamStarter(position: Player.POSITION_K, numStarters: 1, fantasyTeam: fantasyTeam).save(flush: true))

	    startable = [:]
	    fantasyTeam.fantasyTeamStarters.each {  starter ->
		    startable[starter.position] = starter.numStarters
	    }
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testGetDropoffData_2001_QB_1() {
        def p1 = new Player(name: "Player 1", position: Player.POSITION_QB).save(flush: true)
        def p2 = new Player(name: "Player 2", position: Player.POSITION_QB).save(flush: true)
        def fp1 = new FantasyPoints(player: p1, season: 2001, week: -1, points: 25.0, scoringSystem: scoringSystem).save(flush: true)
        def fp2 = new FantasyPoints(player: p2, season: 2001, week: -1, points: 9.0, scoringSystem: scoringSystem).save(flush: true)

        def tiers = Player.getDropoffData("QB", 2001)

        assert tiers.size() == 2

        /*
        First index is tier #
        Second index is place within list on that tier
        Third index is Player object (0) or FantasyPoints object (1)
         */
        assert tiers[0][0][0].name == "Player 1" && tiers[0][0][0].position == "QB"
        assert tiers[1][0][0].name == "Player 2" && tiers[1][0][0].position == "QB"
        assert tiers[0][0][1].points == 25.0
        assert tiers[1][0][1].points == 9.0
    }

    @Test
    void testGetDropoffData_2001_QB_2() {
        def q1 = new Player(name: "Quarterback 1", position: "QB").save(flush: true)
        def q2 = new Player(name: "Quarterback 2", position: "QB").save(flush: true)
        def q3 = new Player(name: "Quarterback 3", position: "QB").save(flush: true)
        def q4 = new Player(name: "Quarterback 4", position: "QB").save(flush: true)
        def fp1 = new FantasyPoints(player: q1, season: 2001, week: -1, points: 80.0, scoringSystem: scoringSystem).save(flush: true)
        def fp2 = new FantasyPoints(player: q2, season: 2001, week: -1, points: 61.0, scoringSystem: scoringSystem).save(flush: true)
        def fp3 = new FantasyPoints(player: q3, season: 2001, week: -1, points: 84.0, scoringSystem: scoringSystem).save(flush: true)
        def fp4 = new FantasyPoints(player: q4, season: 2001, week: -1, points: 100.0, scoringSystem: scoringSystem).save(flush: true)

        def tiers = Player.getDropoffData("QB", 2001)

        assert tiers.size() == 3

        /*
        First index is tier #
        Second index is place within list on that tier
        Third index is Player object (0) or FantasyPoints object (1)
         */
        assert tiers[0][0][0].name == "Quarterback 4" && tiers[0][0][0].position == "QB"
        assert tiers[1][0][0].name == "Quarterback 3" && tiers[1][0][0].position == "QB"
        assert tiers[1][1][0].name == "Quarterback 1" && tiers[1][1][0].position == "QB"
        assert tiers[2][0][0].name == "Quarterback 2" && tiers[2][0][0].position == "QB"
        assert tiers[0][0][1].points == 100.0
        assert tiers[1][0][1].points == 84.0
        assert tiers[1][1][1].points == 80.0
        assert tiers[2][0][1].points == 61.0
    }

	@Test
    void testGetProjectedPoints_BadYear() {
        def q1 = new Player(name: "Quarterback", position: "QB").save(flush: true)
        def s1 = new Stat(player: q1, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 10).save(flush: true)

        try {
            q1.calculateProjectedPoints(2001, scoringSystem)
            fail("calculateProjectedPoints() Should have thrown exception.")
        }
        catch(Exception e) {
            assert e.getMessage().equals("Invalid year.")
        }
    }

	@Test
	void testGetProjectedPoints_QB() {
        def qb1 = new Player(name: "Quarterback 1", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
        def qb2 = new Player(name: "Quarterback 2", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
        def qb3 = new Player(name: "Quarterback 3", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
        def qb4 = new Player(name: "Quarterback 4", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
        def qb5 = new Player(name: "Quarterback 5", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
        def qb6 = new Player(name: "Quarterback 6", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)

        // create some stats so the call to getStatYears() doesn't fail.
        def qb1_s2001 = new Stat(player: qb1, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 1).save(flush: true)
        def qb1_s2002 = new Stat(player: qb1, season: 2002, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 1).save(flush: true)
        def qb2_s2001 = new Stat(player: qb2, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 1).save(flush: true)
        def qb2_s2002 = new Stat(player: qb2, season: 2002, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 1).save(flush: true)
        def qb3_s2001 = new Stat(player: qb3, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 1).save(flush: true)
        def qb3_s2002 = new Stat(player: qb3, season: 2002, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 1).save(flush: true)
        def qb4_s2001 = new Stat(player: qb4, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 1).save(flush: true)
        def qb4_s2002 = new Stat(player: qb4, season: 2002, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 1).save(flush: true)
        def qb5_s2001 = new Stat(player: qb5, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 1).save(flush: true)
        def qb5_s2002 = new Stat(player: qb5, season: 2002, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 1).save(flush: true)
        def qb6_s2001 = new Stat(player: qb6, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 1).save(flush: true)
        def qb6_s2002 = new Stat(player: qb6, season: 2002, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 1).save(flush: true)

        def fp1_2001 = new FantasyPoints(player: qb1, season: 2001, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb1.position], points: 43.0).save(flush: true)
        def fp1_2002 = new FantasyPoints(player: qb1, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb1.position], points: 99.0).save(flush: true)
		qb1.fantasyPoints = [fp1_2001, fp1_2002]

        def fp2_2001 = new FantasyPoints(player: qb2, season: 2001, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb2.position], points: 21.0).save(flush: true)
        def fp2_2002 = new FantasyPoints(player: qb2, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb2.position], points: 65.0).save(flush: true)
		qb2.fantasyPoints = [fp2_2001, fp2_2002]

        def fp3_2001 = new FantasyPoints(player: qb3, season: 2001, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb3.position], points: 25.0).save(flush: true)
        def fp3_2002 = new FantasyPoints(player: qb3, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb3.position], points: 79.0).save(flush: true)
		qb3.fantasyPoints = [fp3_2001, fp3_2002]

        def fp4_2001 = new FantasyPoints(player: qb4, season: 2001, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb4.position], points: 42.0).save(flush: true)
        def fp4_2002 = new FantasyPoints(player: qb4, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb4.position], points: 75.0).save(flush: true)
		qb4.fantasyPoints = [fp4_2001, fp4_2002]

        def fp5_2001 = new FantasyPoints(player: qb5, season: 2001, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb5.position], points: 57.0).save(flush: true)
        def fp5_2002 = new FantasyPoints(player: qb5, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb5.position], points: 87.0).save(flush: true)
		qb5.fantasyPoints = [fp5_2001, fp5_2002]

        def fp6_2001 = new FantasyPoints(player: qb6, season: 2001, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb6.position], points: 59.0).save(flush: true)
        def fp6_2002 = new FantasyPoints(player: qb6, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb6.position], points: 81.0).save(flush: true)
		qb6.fantasyPoints = [fp6_2001, fp6_2002]

        assert Double.valueOf(qb1.calculateProjectedPoints(2003, scoringSystem)).trunc(2) == 90.53
        assert Double.valueOf(qb2.calculateProjectedPoints(2003, scoringSystem)).trunc(2) == 72.52
        assert Double.valueOf(qb3.calculateProjectedPoints(2003, scoringSystem)).trunc(2) == 79.94
        assert Double.valueOf(qb4.calculateProjectedPoints(2003, scoringSystem)).trunc(2) == 77.82
        assert Double.valueOf(qb5.calculateProjectedPoints(2003, scoringSystem)).trunc(2) == 84.17
        assert Double.valueOf(qb6.calculateProjectedPoints(2003, scoringSystem)).trunc(2) == 81
    }

	@Test
	void testCalculatePointsStandardDeviation_BadYear() {
        def player = new Player(name: "Quarterback", position: Player.POSITION_QB).save(flush: true)
        def fp1 = new FantasyPoints(player: player, season: 2001, week: 1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[player.position], points: 10).save(flush: true)

        assert player.calculatePointsStandardDeviation(2002) == -1
    }

	@Test
	void testCalculatePointsStandardDeviation_2001() {
        def player = new Player(name: "Quarterback", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)

        // Set up Stat object to satisfy Stat.getStatYears() check.
        def s1 = new Stat(player: player, season: 2001, week: 1, statKey: FantasyConstants.STAT_TOUCHDOWN, statValue: 1).save(flush: true)

        // Register FantasyPoints for the weeks.
        def fp1 = new FantasyPoints(player: player, season: 2001, week: 1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[player.position], points: 10.0).save(flush: true)
        def fp2 = new FantasyPoints(player: player, season: 2001, week: 2, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[player.position], points: 9.0).save(flush: true)
        def fp3 = new FantasyPoints(player: player, season: 2001, week: 3, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[player.position], points: 11.0).save(flush: true)
        def fp4 = new FantasyPoints(player: player, season: 2001, week: 4, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[player.position], points: 20.0).save(flush: true)

        assert player.calculatePointsStandardDeviation(2001).doubleValue().trunc(3) == 4.387
    }

	@Test
	void testGetStatYears_2001() {
        def player = new Player(name: "Quarterback", position: "QB").save(flush: true)

        // Set up Stat object to satisfy Stat.getStatYears() check.
        def s1 = new Stat(player: player, season: 2001, week: 1, statKey: FantasyConstants.STAT_TOUCHDOWN, statValue: 1).save(flush: true)

        def statYears = player.getStatYears()
        assert statYears.contains(2001) && statYears.size() == 1
    }

	@Test
	void testGetStatYears_2001_2002() {
        def player = new Player(name: "Quarterback", position: "QB").save(flush: true)

        // Set up Stat object to satisfy Stat.getStatYears() check.
        def s1 = new Stat(player: player, season: 2001, week: 1, statKey: FantasyConstants.STAT_TOUCHDOWN, statValue: 1).save(flush: true)
        def s2 = new Stat(player: player, season: 2002, week: 1, statKey: FantasyConstants.STAT_TOUCHDOWN, statValue: 1).save(flush: true)

        def statYears = player.getStatYears()

        assert statYears.size() == 2
        assert statYears.contains(2001)
        assert statYears.contains(2002)
    }

	@Test
	void testGetScoringAverageForSeason_BadSeason() {
        def player = new Player(name: "Quarterback", position: Player.POSITION_QB).save(flush: true)

        assert player.getScoringAverageForSeason(2001) == 0
    }

	@Test
	void testGetScoringAverageForSeason_GoodSeason() {
        def player = new Player(name: "Quarterback", position: Player.POSITION_QB).save(flush: true)
        def player2 = new Player(name: "Running Back", position: Player.POSITION_RB).save(flush: true)

        // Register FantasyPoints for the weeks.
        def fp1 = new FantasyPoints(player: player, season: 2001, week: 1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[player.position], points: 10.0).save(flush: true)
        def fp2 = new FantasyPoints(player: player, season: 2001, week: 2, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[player.position], points: 9.0).save(flush: true)
        def fp3 = new FantasyPoints(player: player, season: 2001, week: 3, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[player.position], points: 11.0).save(flush: true)
        def fp4 = new FantasyPoints(player: player, season: 2001, week: 4, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[player.position], points: 20.0).save(flush: true)

        def fp5 = new FantasyPoints(player: player2, season: 2001, week: 1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[player.position], points: 20.0).save(flush: true)
        def fp6 = new FantasyPoints(player: player2, season: 2001, week: 2, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[player.position], points: 19.0).save(flush: true)
        def fp7 = new FantasyPoints(player: player2, season: 2001, week: 3, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[player.position], points: 21.0).save(flush: true)
        def fp8 = new FantasyPoints(player: player2, season: 2001, week: 4, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[player.position], points: 30.0).save(flush: true)

        assert player.getScoringAverageForSeason(2001) == 12.5
    }

	@Test
	void testGetScoringAverageForPositionForSeason_GoodSeason() {
        def player = new Player(name: "Quarterback", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
        def player2 = new Player(name: "Quarterback 2", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)

        // Register FantasyPoints for the weeks.
        def fp1 = new FantasyPoints(player: player, season: 2001, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[player.position], points: 10.0).save(flush: true)
        def fp5 = new FantasyPoints(player: player2, season: 2001, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[player.position], points: 20.0).save(flush: true)

        assert Player.getScoringAverageForPositionForSeason(Player.POSITION_QB, 2001) == 15
    }

	@Test
	void testGetCorrelation_OverallPoints_2001_2002_QB() {
        def qb1 = new Player(name: "Quarterback 1", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
        def qb2 = new Player(name: "Quarterback 2", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
        def qb3 = new Player(name: "Quarterback 3", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
        def qb4 = new Player(name: "Quarterback 4", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
        def qb5 = new Player(name: "Quarterback 5", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
        def qb6 = new Player(name: "Quarterback 6", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
        def qb_1season = new Player(name: "Quarterback 7", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)

        def fp1_2001 = new FantasyPoints(player: qb1, season: 2001, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb1.position], points: 43.0).save(flush: true)
        def fp1_2002 = new FantasyPoints(player: qb1, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb1.position], points: 99.0).save(flush: true)

        def fp2_2001 = new FantasyPoints(player: qb2, season: 2001, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb2.position], points: 21.0).save(flush: true)
        def fp2_2002 = new FantasyPoints(player: qb2, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb2.position], points: 65.0).save(flush: true)

        def fp3_2001 = new FantasyPoints(player: qb3, season: 2001, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb3.position], points: 25.0).save(flush: true)
        def fp3_2002 = new FantasyPoints(player: qb3, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb3.position], points: 79.0).save(flush: true)

        def fp4_2001 = new FantasyPoints(player: qb4, season: 2001, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb4.position], points: 42.0).save(flush: true)
        def fp4_2002 = new FantasyPoints(player: qb4, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb4.position], points: 75.0).save(flush: true)

        def fp5_2001 = new FantasyPoints(player: qb5, season: 2001, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb5.position], points: 57.0).save(flush: true)
        def fp5_2002 = new FantasyPoints(player: qb5, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb5.position], points: 87.0).save(flush: true)

        def fp6_2001 = new FantasyPoints(player: qb6, season: 2001, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb6.position], points: 59.0).save(flush: true)
        def fp6_2002 = new FantasyPoints(player: qb6, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb6.position], points: 81.0).save(flush: true)

        def fp_1season_2001 = new FantasyPoints(player: qb_1season, season: 2001, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[qb_1season.position], points: 59.0).save(flush: true)

        assert Double.valueOf(Player.getCorrelation(Player.POSITION_QB, null, scoringSystem, 2001, 2002)).round(6) == 0.529809
    }

    /**
     *      x       y       xy      x2      y2
     * q1   10      15      150     100     225
     * q2   12      17      204     144     289
     * q3   30      45      1350    900     2025
     * sum  52      77      1704    1144    2539
     *
     * c = (3*1704 - 52*77)/sqrt((3*1144 - 52^2)*(3*2539 - 77^2))
     *   = (5112 - 4004)/sqrt((3432-2704)*(7617-5929))
     *   = 1108/sqrt(728*1688)
     *   = 1108/sqrt(1228864)
     *   = 1108/1108.5413
     *   = .999
     */
	@Test
	void testGetCorrelation_PassingTouchdowns_2001_2002_QB() {
        def qb1 = new Player(name: "Quarterback 1", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
        def qb2 = new Player(name: "Quarterback 2", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
        def qb3 = new Player(name: "Quarterback 3", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
        def qb_1season = new Player(name: "Quarterback 7", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)

        def s1_2001 = new Stat(player: qb1, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 10).save(flush: true)
        def s1_2002 = new Stat(player: qb1, season: 2002, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 15).save(flush: true)

        def s2_2001 = new Stat(player: qb2, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 12).save(flush: true)
        def s2_2002 = new Stat(player: qb2, season: 2002, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 17).save(flush: true)

        def s3_2001 = new Stat(player: qb3, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 30).save(flush: true)
        def s3_2002 = new Stat(player: qb3, season: 2002, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 45).save(flush: true)

        def s_1season_2001 = new Stat(player: qb_1season, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 12).save(flush: true)

		def passingTouchdownsRule = new ScoringRule(scoringSystems: scoringSystem, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, multiplier: 4).save(flush: true)
		scoringSystem.scoringRules << passingTouchdownsRule

        assert Double.valueOf(Player.getCorrelation(Player.POSITION_QB, FantasyConstants.STAT_PASSING_TOUCHDOWNS, scoringSystem, 2001, 2002)).trunc(3) == 0.999
    }

	@Test
	void testCalculateStandardDeviation() {
        def scores = [10.0, 9.0, 11.0, 20.0]

        // Mean = (10.0 + 9.0 + 11.0 + 20.0)/4 = 12.5
        // DIFF FROM MEAN, SQUARED
        // q1 = (10.0 - 12.5)^2 = -2.5^2 = 6.25
        // q2 = (9.0 - 12.5)^2 = -3.5^2 = 12.25
        // q3 = (11.0 - 12.5)^2 = -1.5^2 = 2.25
        // q4 = (20.0 - 12.5)^2 = 7.5^2 = 56.25

        // sqrt( (6.25 + 12.25 + 2.25 + 56.25)/4 ) = sqrt(77/4) = sqrt(19.25) = 4.387
        assert Player.calculateStandardDeviation(scores).trunc(3) == 4.387
    }

	@Test
	void testGetPlayersInPointsOrder() {
		/*
		 * Define players
		 */
		def q1 = new Player(name: "Quarterback 1", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
		def q2 = new Player(name: "Quarterback 2", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)

		def r1 = new Player(name: "Running back 1", position: Player.POSITION_RB, stats: [], averageDraftPositions: []).save(flush: true)
		def r2 = new Player(name: "Running back 2", position: Player.POSITION_RB, stats: [], averageDraftPositions: []).save(flush: true)

		/*
		 * Define fantasy points for players - non projections should be ignored.
		 */
		def q1_stats = new FantasyPoints(player: q1, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[q1.position], points: 305).save(flush: true)
		def q2_stats = new FantasyPoints(player: q2, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[q2.position], points: 310).save(flush: true)
		def q1p_stats = new FantasyPoints(player: q1, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[q1.position], points: 300, projection: true).save(flush: true)
		def q2p_stats = new FantasyPoints(player: q2, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[q2.position], points: 295, projection: true).save(flush: true)

		def r1_stats = new FantasyPoints(player: r1, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[r1.position], points: 245).save(flush: true)
		def r2_stats = new FantasyPoints(player: r2, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[r2.position], points: 250).save(flush: true)
		def r1p_stats = new FantasyPoints(player: r1, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[r1.position], points: 250, projection: true).save(flush: true)
		def r2p_stats = new FantasyPoints(player: r2, season: 2002, week: -1, scoringSystem: scoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[r2.position], points: 245, projection: true).save(flush: true)

		/*
		 * Define ADP for players
		 */
		def q1_adp_2002 = new AverageDraftPosition(player: q1, adp: 1, season: 2002).save(flush: true)
		def q1_adp_2001 = new AverageDraftPosition(player: q1, adp: 4, season: 2001).save(flush: true)
		def q2_adp_2002 = new AverageDraftPosition(player: q2, adp: 5, season: 2002).save(flush: true)
		def q2_adp_2001 = new AverageDraftPosition(player: q2, adp: 10, season: 2001).save(flush: true)

		def r1_adp_2002 = new AverageDraftPosition(player: r1, adp: 2, season: 2002).save(flush: true)
		def r1_adp_2001 = new AverageDraftPosition(player: r1, adp: 8, season: 2001).save(flush: true)
		def r2_adp_2002 = new AverageDraftPosition(player: r2, adp: 8, season: 2002).save(flush: true)
		def r2_adp_2001 = new AverageDraftPosition(player: r2, adp: 50, season: 2001).save(flush: true)


		/*
		 * Quarterback results
		 */
		def qb_results = Player.getPlayersInPointsOrder(Player.POSITION_QB, 2002, scoringSystem)
		assert qb_results.size() == 2
		assert qb_results[0].name == "Quarterback 1"
		assert qb_results[0].position == Player.POSITION_QB
		assert qb_results[0].fantasyPoints.toArray()[0].points == 300
		assert qb_results[0].averageDraftPositions.size() == 1
		assert qb_results[0].averageDraftPositions.toArray()[0].adp == 1

		assert qb_results[1].name == "Quarterback 2"
		assert qb_results[1].position == Player.POSITION_QB
		assert qb_results[1].fantasyPoints.toArray()[0].points == 295
		assert qb_results[1].averageDraftPositions.size() == 1
		assert qb_results[1].averageDraftPositions.toArray()[0].adp == 5

		/*
		 * Running back results
		 */
		def rb_results = Player.getPlayersInPointsOrder(Player.POSITION_RB, 2002, scoringSystem)
		assert rb_results.size() == 2
		assert rb_results[0].name == "Running back 1"
		assert rb_results[0].position == Player.POSITION_RB
		assert rb_results[0].fantasyPoints.toArray()[0].points == 250
		assert rb_results[0].averageDraftPositions.size() == 1
		assert rb_results[0].averageDraftPositions.toArray()[0].adp == 2

		assert rb_results[1].name == "Running back 2"
		assert rb_results[1].position == Player.POSITION_RB
		assert rb_results[1].fantasyPoints.toArray()[0].points == 245
		assert rb_results[1].averageDraftPositions.size() == 1
		assert rb_results[1].averageDraftPositions.toArray()[0].adp == 8
	}

	@Test
	void testGetPlayersInPointsOrder_NonExistentScoringSystem() {
		def differentFantasyTeam = new FantasyTeam(name: "Test team", user: user, fantasyLeagueType: flt, season: 2013, leagueId: "111", numOwners: 10, fantasyTeamStarters: []).save(flush: true)
		def differentScoringSystem = new ScoringSystem(name: "Test SS", fantasyTeam: differentFantasyTeam, scoringRules: []).save(flush: true)

		/*
		 * Define players
		 */
		def q1 = new Player(name: "Quarterback 1", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
		def q2 = new Player(name: "Quarterback 2", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)

		def r1 = new Player(name: "Running back 1", position: Player.POSITION_RB, stats: [], averageDraftPositions: []).save(flush: true)
		def r2 = new Player(name: "Running back 2", position: Player.POSITION_RB, stats: [], averageDraftPositions: []).save(flush: true)

		/*
		 * Define fantasy points for players - non projections should be ignored.
		 */
		def q1_stats = new FantasyPoints(player: q1, season: 2002, week: -1, scoringSystem: differentScoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[r1.position], points: 305).save(flush: true)
		def q2_stats = new FantasyPoints(player: q2, season: 2002, week: -1, scoringSystem: differentScoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[r1.position], points: 310).save(flush: true)
		def q1p_stats = new FantasyPoints(player: q1, season: 2002, week: -1, scoringSystem: differentScoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[r1.position], points: 300, projection: true).save(flush: true)
		def q2p_stats = new FantasyPoints(player: q2, season: 2002, week: -1, scoringSystem: differentScoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[r1.position], points: 295, projection: true).save(flush: true)

		def r1_stats = new FantasyPoints(player: r1, season: 2002, week: -1, scoringSystem: differentScoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[r1.position], points: 245).save(flush: true)
		def r2_stats = new FantasyPoints(player: r2, season: 2002, week: -1, scoringSystem: differentScoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[r1.position], points: 250).save(flush: true)
		def r1p_stats = new FantasyPoints(player: r1, season: 2002, week: -1, scoringSystem: differentScoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[r1.position], points: 250, projection: true).save(flush: true)
		def r2p_stats = new FantasyPoints(player: r2, season: 2002, week: -1, scoringSystem: differentScoringSystem, numOwners: fantasyTeam.numOwners, numStartable: startable[r1.position], points: 245, projection: true).save(flush: true)

		/*
		 * Define ADP for players
		 */
		def q1_adp_2002 = new AverageDraftPosition(player: q1, adp: 1, season: 2002).save(flush: true)
		def q1_adp_2001 = new AverageDraftPosition(player: q1, adp: 4, season: 2001).save(flush: true)
		def q2_adp_2002 = new AverageDraftPosition(player: q2, adp: 5, season: 2002).save(flush: true)
		def q2_adp_2001 = new AverageDraftPosition(player: q2, adp: 10, season: 2001).save(flush: true)

		def r1_adp_2002 = new AverageDraftPosition(player: r1, adp: 2, season: 2002).save(flush: true)
		def r1_adp_2001 = new AverageDraftPosition(player: r1, adp: 8, season: 2001).save(flush: true)
		def r2_adp_2002 = new AverageDraftPosition(player: r2, adp: 8, season: 2002).save(flush: true)
		def r2_adp_2001 = new AverageDraftPosition(player: r2, adp: 50, season: 2001).save(flush: true)


		/*
		 * Quarterback results
		 */
		def qb_results = Player.getPlayersInPointsOrder(Player.POSITION_QB, 2002, scoringSystem)
		assert "Expected to get back 0 Quarterbacks, got ${ qb_results.size() }", qb_results.size() == 0
//		assert qb_results[0].name == "Quarterback 1"
//		assert qb_results[0].position == Player.POSITION_QB
//		assert qb_results[0].fantasyPoints.toArray()[0].points == 300
//		assert qb_results[0].averageDraftPositions.size() == 1
//		assert qb_results[0].averageDraftPositions.toArray()[0].adp == 1
//
//		assert qb_results[1].name == "Quarterback 2"
//		assert qb_results[1].position == Player.POSITION_QB
//		assert qb_results[1].fantasyPoints.toArray()[0].points == 295
//		assert qb_results[1].averageDraftPositions.size() == 1
//		assert qb_results[1].averageDraftPositions.toArray()[0].adp == 5

		/*
		 * Running back results
		 */
		def rb_results = Player.getPlayersInPointsOrder(Player.POSITION_RB, 2002, scoringSystem)
		assert rb_results.size() == 0
//		assert rb_results[0].name == "Running back 1"
//		assert rb_results[0].position == Player.POSITION_RB
//		assert rb_results[0].fantasyPoints.toArray()[0].points == 250
//		assert rb_results[0].averageDraftPositions.size() == 1
//		assert rb_results[0].averageDraftPositions.toArray()[0].adp == 2
//
//		assert rb_results[1].name == "Running back 2"
//		assert rb_results[1].position == Player.POSITION_RB
//		assert rb_results[1].fantasyPoints.toArray()[0].points == 245
//		assert rb_results[1].averageDraftPositions.size() == 1
//		assert rb_results[1].averageDraftPositions.toArray()[0].adp == 8
	}
}

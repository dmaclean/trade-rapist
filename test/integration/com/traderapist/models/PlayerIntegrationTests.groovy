package com.traderapist.models

import com.traderapist.constants.FantasyConstants
import com.traderapist.scoringsystem.ESPNStandardScoringSystem
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

import static org.junit.Assert.*
import org.junit.*

class PlayerIntegrationTests {

    @Before
    void setUp() {

    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testGetDropoffData_2001_QB_1() {
        def p1 = new Player(name: "Player 1", position: "QB").save(flush: true)
        def p2 = new Player(name: "Player 2", position: "QB").save(flush: true)
        def fp1 = new FantasyPoints(player: p1, season: 2001, week: -1, points: 25.0, system: "ESPNStandardScoringSystem").save(flush: true)
        def fp2 = new FantasyPoints(player: p2, season: 2001, week: -1, points: 9.0, system: "ESPNStandardScoringSystem").save(flush: true)

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
        def fp1 = new FantasyPoints(player: q1, season: 2001, week: -1, points: 80.0, system: "ESPNStandardScoringSystem").save(flush: true)
        def fp2 = new FantasyPoints(player: q2, season: 2001, week: -1, points: 61.0, system: "ESPNStandardScoringSystem").save(flush: true)
        def fp3 = new FantasyPoints(player: q3, season: 2001, week: -1, points: 84.0, system: "ESPNStandardScoringSystem").save(flush: true)
        def fp4 = new FantasyPoints(player: q4, season: 2001, week: -1, points: 100.0, system: "ESPNStandardScoringSystem").save(flush: true)

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
            q1.calculateProjectedPoints(2001)
            fail("calculateProjectedPoints() Should have thrown exception.")
        }
        catch(Exception e) {
            assert e.getMessage().equals("Invalid year.")
        }
    }

	@Test
	void testGetProjectedPoints_QB() {
        def qb1 = new Player(name: "Quarterback 1", position: "QB").save(flush: true)
        def qb2 = new Player(name: "Quarterback 2", position: "QB").save(flush: true)
        def qb3 = new Player(name: "Quarterback 3", position: "QB").save(flush: true)
        def qb4 = new Player(name: "Quarterback 4", position: "QB").save(flush: true)
        def qb5 = new Player(name: "Quarterback 5", position: "QB").save(flush: true)
        def qb6 = new Player(name: "Quarterback 6", position: "QB").save(flush: true)

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

        def fp1_2001 = new FantasyPoints(player: qb1, season: 2001, week: -1, system: "ESPNStandardScoringSystem", points: 43.0).save(flush: true)
        def fp1_2002 = new FantasyPoints(player: qb1, season: 2002, week: -1, system: "ESPNStandardScoringSystem", points: 99.0).save(flush: true)
        qb1.fantasyPoints = new HashSet()
        qb1.fantasyPoints.add(fp1_2001)
        qb1.fantasyPoints.add(fp1_2002)

        def fp2_2001 = new FantasyPoints(player: qb2, season: 2001, week: -1, system: "ESPNStandardScoringSystem", points: 21.0).save(flush: true)
        def fp2_2002 = new FantasyPoints(player: qb2, season: 2002, week: -1, system: "ESPNStandardScoringSystem", points: 65.0).save(flush: true)
        qb2.fantasyPoints = new HashSet()
        qb2.fantasyPoints.add(fp2_2001)
        qb2.fantasyPoints.add(fp2_2002)

        def fp3_2001 = new FantasyPoints(player: qb3, season: 2001, week: -1, system: "ESPNStandardScoringSystem", points: 25.0).save(flush: true)
        def fp3_2002 = new FantasyPoints(player: qb3, season: 2002, week: -1, system: "ESPNStandardScoringSystem", points: 79.0).save(flush: true)
        qb3.fantasyPoints = new HashSet()
        qb3.fantasyPoints.add(fp3_2001)
        qb3.fantasyPoints.add(fp3_2002)

        def fp4_2001 = new FantasyPoints(player: qb4, season: 2001, week: -1, system: "ESPNStandardScoringSystem", points: 42.0).save(flush: true)
        def fp4_2002 = new FantasyPoints(player: qb4, season: 2002, week: -1, system: "ESPNStandardScoringSystem", points: 75.0).save(flush: true)
        qb4.fantasyPoints = new HashSet()
        qb4.fantasyPoints.add(fp4_2001)
        qb4.fantasyPoints.add(fp4_2002)

        def fp5_2001 = new FantasyPoints(player: qb5, season: 2001, week: -1, system: "ESPNStandardScoringSystem", points: 57.0).save(flush: true)
        def fp5_2002 = new FantasyPoints(player: qb5, season: 2002, week: -1, system: "ESPNStandardScoringSystem", points: 87.0).save(flush: true)
        qb5.fantasyPoints = new HashSet()
        qb5.fantasyPoints.add(fp5_2001)
        qb5.fantasyPoints.add(fp5_2002)

        def fp6_2001 = new FantasyPoints(player: qb6, season: 2001, week: -1, system: "ESPNStandardScoringSystem", points: 59.0).save(flush: true)
        def fp6_2002 = new FantasyPoints(player: qb6, season: 2002, week: -1, system: "ESPNStandardScoringSystem", points: 81.0).save(flush: true)
        qb6.fantasyPoints = new HashSet()
        qb6.fantasyPoints.add(fp6_2001)
        qb6.fantasyPoints.add(fp6_2002)

        try {
            assert Double.valueOf(qb1.calculateProjectedPoints(2003)).trunc(2) == 90.53
            assert Double.valueOf(qb2.calculateProjectedPoints(2003)).trunc(2) == 72.52
            assert Double.valueOf(qb3.calculateProjectedPoints(2003)).trunc(2) == 79.94
            assert Double.valueOf(qb4.calculateProjectedPoints(2003)).trunc(2) == 77.82
            assert Double.valueOf(qb5.calculateProjectedPoints(2003)).trunc(2) == 84.17
            assert Double.valueOf(qb6.calculateProjectedPoints(2003)).trunc(2) == 81
        }
        catch(Exception e) {
            fail(e.getMessage())
        }
    }

	@Test
	void testCalculateProjectedPointsQB_season_numStartable1_numOwners3() {
		def numStartable = 1
		def numOwners = 3

		// Create three quarterbacks
		def q1 = new Player(name: "Quarterback 1", position: Player.POSITION_QB).save(flush: true)
		def q2 = new Player(name: "Quarterback 2", position: Player.POSITION_QB).save(flush: true)
		def q3 = new Player(name: "Quarterback 3", position: Player.POSITION_QB).save(flush: true)
		def q4 = new Player(name: "Quarterback 4", position: Player.POSITION_QB).save(flush: true)
		def r1 = new Player(name: "Running back 1", position: Player.POSITION_RB).save(flush: true)

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

		// Cannot figure out why re-querying the quarterbacks doesn't pull in stats.
		q1.stats = new HashSet<Stat>([py1, pt1, i1, ry1, rt1])
		q2.stats = new HashSet<Stat>([py2, pt2, i2, ry2, rt2])
		q3.stats = new HashSet<Stat>([py3, pt3, i3, ry3, rt3])
		q4.stats = new HashSet<Stat>([py4, pt4, i4, ry4, rt4])
		r1.stats = new HashSet<Stat>([ry5, rt5])


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
		assert q1.calculateProjectedPoints(2013, numStartable, numOwners, new ESPNStandardScoringSystem()) == 44.4

		/*
		 * Quarterback 2
		 *
		 * 2013 passing yards =         (90 * 0.5 ) + (80 * 0.5) = 85 --> 85 / 25 = 3.4
		 * 2013 passing touchdowns =    (9 * 0.37) + (8 * 0.63) = 3.33 + 5.04 = 8.37 --> 8 * 4 = 32
		 * 2013 interceptions =         (2 * 0.08) + (3 * 0.92) = 0.16 + 2.76 = 2.92 --> 2 * -2 = -4
		 * 2013 rushing yards =         (9 * 0.78) + (9 * 0.22) = 7.02 + 1.98 = 9 --> 9 /10 = .9
		 * 2013 rushing touchdowns =    (2 * 0.5) + (1 * 0.5) = 1 + 0.5 = 1.5 --> 1 * 6 = 6
		 */
		assert q2.calculateProjectedPoints(2013, numStartable, numOwners, new ESPNStandardScoringSystem()) == 38.3

		/*
		 * Quarterback 3
		 *
		 * 2013 passing yards =         (80 * 0.5 ) + (80 * 0.5) = 80 --> 80 / 25 = 3.2
		 * 2013 passing touchdowns =    (10 * 0.37) + (8 * 0.63) = 3.7 + 5.04 = 8.74 --> 8 * 4 = 32
		 * 2013 interceptions =         (3 * 0.08) + (3 * 0.92) = 0.24 + 2.76 = 3 --> 3 * -2 = -6
		 * 2013 rushing yards =         (10 * 0.78) + (9 * 0.22) = 7.8 + 1.98 = 9.78 --> 9 /10 = .9
		 * 2013 rushing touchdowns =    (1 * 0.5) + (1 * 0.5) = 0.5 + 0.5 = 1 --> 1 * 6 = 6
		 */
		assert q3.calculateProjectedPoints(2013, numStartable, numOwners, new ESPNStandardScoringSystem()) == 36.1

		/*
		 * Quarterback 4
		 *
		 * 2013 passing yards =         (70 * 0.5 ) + (80 * 0.5) = 75 --> 75 / 25 = 3
		 * 2013 passing touchdowns =    (7 * 0.37) + (8 * 0.63) = 2.59 + 5.04 = 7.63 --> 7 * 4 = 28
		 * 2013 interceptions =         (4 * 0.08) + (3 * 0.92) = 0.32 + 2.76 = 3.08 --> 3 * -2 = -6
		 * 2013 rushing yards =         (11 * 0.78) + (9 * 0.22) = 8.58 + 1.98 = 10.56 --> 10 /10 = 1
		 * 2013 rushing touchdowns =    (0 * 0.5) + (1 * 0.5) = 0 + 0.5 = 0.5 --> 0 * 6 = 0
		 */
		assert q4.calculateProjectedPoints(2013, numStartable, numOwners, new ESPNStandardScoringSystem()) == 26
	}

	@Test
	void testCalculateProjectedPointsRB_season_numStartable1_numOwners3() {
		def numStartable = 1
		def numOwners = 3

		// Create three quarterbacks
		def r1 = new Player(name: "Running back 1", position: Player.POSITION_RB).save(flush: true)
		def r2 = new Player(name: "Running back 2", position: Player.POSITION_RB).save(flush: true)
		def r3 = new Player(name: "Running back 3", position: Player.POSITION_RB).save(flush: true)
		def r4 = new Player(name: "Running back 4", position: Player.POSITION_RB).save(flush: true)
		def q1 = new Player(name: "Quarterback 1", position: Player.POSITION_QB).save(flush: true)

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

		// Cannot figure out why re-querying the quarterbacks doesn't pull in stats.
		r1.stats = new HashSet<Stat>([rcy1, rct1, rc1, ry1, rt1])
		r2.stats = new HashSet<Stat>([rcy2, rct2, rc2, ry2, rt2])
		r3.stats = new HashSet<Stat>([rcy3, rct3, rc3, ry3, rt3])
		r4.stats = new HashSet<Stat>([rcy4, rct4, rc4, ry4, rt4])
		q1.stats = new HashSet<Stat>([ry5, rt5])


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
		assert r1.calculateProjectedPoints(2013, numStartable, numOwners, new ESPNStandardScoringSystem()) == 69.8

		/*
		 * RB 2
		 *
		 * 2013 rushing yards =         (9 * 0.5) + (9 * 0.5) = 4.5 + 4.5 = 8.5 --> 9 /10 = .9
		 * 2013 rushing touchdowns =    (2 * 0.5) + (1 * 0.5) = 1 + 0.5 = 1.5 --> 1 * 6 = 6
		 * 2013 reception yards =       (90 * 0.51) + (80 * 0.49) = 45.9 + 39.2 = 85.1 --> 85 / 10 = 8.5
		 * 2013 reception touchdowns =  (9 * 0.29) + (8 * 0.71) = 2.61 + 5.68 = 8.29 --> 8 * 6 = 48
		 * 2013 receptions =            (2 * 0.54) + (2 * 0.46) = 1.08 + 0.92 = 2 --> 2 * 0 = 0
		 */
		assert r2.calculateProjectedPoints(2013, numStartable, numOwners, new ESPNStandardScoringSystem()) == 63.4

		/*
		 * RB 3
		 *
		 * 2013 rushing yards =         (10 * 0.5) + (9 * 0.5) = 5 + 4.5 = 9.5 --> 9 /10 = .9
		 * 2013 rushing touchdowns =    (1 * 0.5) + (1 * 0.5) = 0.5 + 0.5 = 1 --> 1 * 6 = 6
		 * 2013 reception yards =       (80 * 0.51) + (80 * 0.49) = 40.8 + 39.2 = 80 --> 80 / 10 = 8
		 * 2013 reception touchdowns =  (10 * 0.29) + (8 * 0.71) = 2.9 + 5.68 = 8.58 --> 8 * 6 = 48
		 * 2013 receptions =            (3 * 0.54) + (2 * 0.46) = 1.62 + 0.92 = 2.54 --> 2 * 0 = 0
		 */
		assert r3.calculateProjectedPoints(2013, numStartable, numOwners, new ESPNStandardScoringSystem()) == 62.9

		/*
		 * RB 4
		 *
		 * 2013 rushing yards =         (11 * 0.5) + (9 * 0.5) = 5.5 + 4.5 = 10 --> 10 /10 = 1
		 * 2013 rushing touchdowns =    (0 * 0.5) + (1 * 0.5) = 0 + 0.5 = 0.5 --> 0 * 6 = 0
		 * 2013 reception yards =       (70 * 0.51) + (80 * 0.49) = 35/7 + 39.2 = 74.9 --> 74 / 10 = 7.4
		 * 2013 reception touchdowns =  (7 * 0.29) + (8 * 0.71) = 2.03 + 5.68 = 7.71 --> 7 * 6 = 42
		 * 2013 receptions =            (4 * 0.54) + (2 * 0.46) = 2.16 + 0.92 = 3.08 --> 3 * 0 = 0
		 */
		assert r4.calculateProjectedPoints(2013, numStartable, numOwners, new ESPNStandardScoringSystem()) == 50.4
	}

	@Test
	void testCalculateProjectedPointsWR_season_numStartable1_numOwners3() {
		def numStartable = 1
		def numOwners = 3

		// Create three quarterbacks
		def w1 = new Player(name: "Wide Receiver 1", position: Player.POSITION_WR).save(flush: true)
		def w2 = new Player(name: "Wide Receiver 2", position: Player.POSITION_WR).save(flush: true)
		def w3 = new Player(name: "Wide Receiver 3", position: Player.POSITION_WR).save(flush: true)
		def w4 = new Player(name: "Wide Receiver 4", position: Player.POSITION_WR).save(flush: true)
		def r1 = new Player(name: "Running back 1", position: Player.POSITION_RB).save(flush: true)

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

		// Cannot figure out why re-querying the quarterbacks doesn't pull in stats.
		w1.stats = new HashSet<Stat>([rcy1, rct1, rc1])
		w2.stats = new HashSet<Stat>([rcy2, rct2, rc2])
		w3.stats = new HashSet<Stat>([rcy3, rct3, rc3])
		w4.stats = new HashSet<Stat>([rcy4, rct4, rc4])
		r1.stats = new HashSet<Stat>([rcy5])


		/*
		 * Do projections
		 *
		 * WR 1
		 *
		 * 2013 reception yards =       (100 * 0.58) + (80 * 0.42) = 58 + 33.6 = 91.6 --> 91 / 10 = 9.1
		 * 2013 reception touchdowns =  (8 * 0.38) + (8 * 0.62) = 3.04 + 4.96 = 8 --> 8 * 6 = 48
		 * 2013 receptions =            (1 * 0.64) + (2 * 0.36) = 0.64 + 0.72 = 1.36 --> 1 * 0 = 0
		 */
		assert w1.calculateProjectedPoints(2013, numStartable, numOwners, new ESPNStandardScoringSystem()) == 57.1

		/*
		 * WR 2
		 *
		 * 2013 reception yards =       (90 * 0.58) + (80 * 0.42) = 52.2 + 33.6 = 85.8 --> 85 / 10 = 8.5
		 * 2013 reception touchdowns =  (9 * 0.38) + (8 * 0.62) = 3.42 + 4.96 = 8.38 --> 8 * 6 = 48
		 * 2013 receptions =            (2 * 0.64) + (2 * 0.36) = 1.28 + 0.72 = 2 --> 2 * 0 = 0
		 */
		assert w2.calculateProjectedPoints(2013, numStartable, numOwners, new ESPNStandardScoringSystem()) == 56.5

		/*
		 * WR 3
		 *
		 * 2013 reception yards =       (80 * 0.58) + (80 * 0.42) = 46.4 + 33.6 = 80 --> 80 / 10 = 8
		 * 2013 reception touchdowns =  (10 * 0.38) + (8 * 0.62) = 3.8 + 4.96 = 8.76 --> 8 * 6 = 48
		 * 2013 receptions =            (3 * 0.64) + (2 * 0.36) = 1.92 + 0.72 = 3.64 --> 3 * 0 = 0
		 */
		assert w3.calculateProjectedPoints(2013, numStartable, numOwners, new ESPNStandardScoringSystem()) == 56

		/*
		 * WR 4
		 *
		 * 2013 reception yards =       (70 * 0.58) + (80 * 0.42) = 40.6 + 33.6 = 74.2 --> 74 / 10 = 7.4
		 * 2013 reception touchdowns =  (7 * 0.38) + (8 * 0.62) = 2.66 + 4.96 = 7.62 --> 7 * 6 = 42
		 * 2013 receptions =            (4 * 0.64) + (2 * 0.36) = 2.56 + 0.72 = 3.28 --> 3 * 0 = 0
		 */
		assert w4.calculateProjectedPoints(2013, numStartable, numOwners, new ESPNStandardScoringSystem()) == 49.4
	}

	@Test
	void testCalculateProjectedPointsTE_season_numStartable1_numOwners3() {
		def numStartable = 1
		def numOwners = 3

		// Create three quarterbacks
		def t1 = new Player(name: "Tight End 1", position: Player.POSITION_TE).save(flush: true)
		def t2 = new Player(name: "Tight End 2", position: Player.POSITION_TE).save(flush: true)
		def t3 = new Player(name: "Tight End 3", position: Player.POSITION_TE).save(flush: true)
		def t4 = new Player(name: "Tight End 4", position: Player.POSITION_TE).save(flush: true)
		def r1 = new Player(name: "Running back 1", position: Player.POSITION_RB).save(flush: true)

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

		// Cannot figure out why re-querying the quarterbacks doesn't pull in stats.
		t1.stats = new HashSet<Stat>([rcy1, rct1, rc1])
		t2.stats = new HashSet<Stat>([rcy2, rct2, rc2])
		t3.stats = new HashSet<Stat>([rcy3, rct3, rc3])
		t4.stats = new HashSet<Stat>([rcy4, rct4, rc4])
		r1.stats = new HashSet<Stat>([rcy5])


		/*
		 * Do projections
		 *
		 * TE 1
		 *
		 * 2013 reception yards =       (100 * 0.74) + (80 * 0.26) = 74 + 20.8 = 94.8 --> 94 / 10 = 9.4
		 * 2013 reception touchdowns =  (8 * 0.44) + (8 * 0.66) = 3.04 + 5.28 = 8.32 --> 8 * 6 = 48
		 * 2013 receptions =            (1 * 0.65) + (2 * 0.35) = 0.65 + 0.70 = 1.35 --> 1 * 0 = 0
		 */
		assert t1.calculateProjectedPoints(2013, numStartable, numOwners, new ESPNStandardScoringSystem()) == 57.4

		/*
		 * TE 2
		 *
		 * 2013 reception yards =       (90 * 0.74) + (80 * 0.26) = 66.6 + 20.8 = 87.4 --> 87 / 10 = 8.7
		 * 2013 reception touchdowns =  (9 * 0.44) + (8 * 0.56) = 3.96 + 4.48 = 8.44 --> 8 * 6 = 48
		 * 2013 receptions =            (2 * 0.65) + (2 * 0.35) = 1.3 + 0.7 = 2 --> 2 * 0 = 0
		 */
		assert t2.calculateProjectedPoints(2013, numStartable, numOwners, new ESPNStandardScoringSystem()) == 56.7

		/*
		 * TE 3
		 *
		 * 2013 reception yards =       (80 * 0.74) + (80 * 0.26) = 59.2 + 20.8 = 80 --> 80 / 10 = 8
		 * 2013 reception touchdowns =  (10 * 0.44) + (8 * 0.56) = 4.4 + 4.48 = 8.88 --> 8 * 6 = 48
		 * 2013 receptions =            (3 * 0.65) + (2 * 0.35) = 1.95 + 0.7 = 2.65 --> 2 * 0 = 0
		 */
		assert t3.calculateProjectedPoints(2013, numStartable, numOwners, new ESPNStandardScoringSystem()) == 56

		/*
		 * TE 4
		 *
		 * 2013 reception yards =       (70 * 0.74) + (80 * 0.26) = 51.8 + 20.8 = 72.6 --> 72 / 10 = 7.2
		 * 2013 reception touchdowns =  (7 * 0.44) + (8 * 0.56) = 3.08 + 4.48 = 7.56 --> 7 * 6 = 42
		 * 2013 receptions =            (4 * 0.65) + (2 * 0.35) = 2.6 + 0.7 = 3.3 --> 3 * 0 = 0
		 */
		assert t4.calculateProjectedPoints(2013, numStartable, numOwners, new ESPNStandardScoringSystem()) == 49.2
	}

	@Test
	void testCalculatePointsStandardDeviation_BadYear() {
        def player = new Player(name: "Quarterback", position: "QB").save(flush: true)
        def fp1 = new FantasyPoints(player: player, season: 2001, week: 1, system: "ESPNStandardScoringSystem", points: 10).save(flush: true)

        assert player.calculatePointsStandardDeviation(2002) == -1
    }

	@Test
	void testCalculatePointsStandardDeviation_2001() {
        def player = new Player(name: "Quarterback", position: "QB").save(flush: true)

        // Set up Stat object to satisfy Stat.getStatYears() check.
        def s1 = new Stat(player: player, season: 2001, week: 1, statKey: FantasyConstants.STAT_TOUCHDOWN, statValue: 1).save(flush: true)

        // Register FantasyPoints for the weeks.
        def fp1 = new FantasyPoints(player: player, season: 2001, week: 1, system: "ESPNStandardScoringSystem", points: 10.0).save(flush: true)
        def fp2 = new FantasyPoints(player: player, season: 2001, week: 2, system: "ESPNStandardScoringSystem", points: 9.0).save(flush: true)
        def fp3 = new FantasyPoints(player: player, season: 2001, week: 3, system: "ESPNStandardScoringSystem", points: 11.0).save(flush: true)
        def fp4 = new FantasyPoints(player: player, season: 2001, week: 4, system: "ESPNStandardScoringSystem", points: 20.0).save(flush: true)

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
        def player = new Player(name: "Quarterback", position: "QB").save(flush: true)

        assert player.getScoringAverageForSeason(2001) == 0
    }

	@Test
	void testGetScoringAverageForSeason_GoodSeason() {
        def player = new Player(name: "Quarterback", position: "QB").save(flush: true)
        def player2 = new Player(name: "Running Back", position: "RB").save(flush: true)

        // Register FantasyPoints for the weeks.
        def fp1 = new FantasyPoints(player: player, season: 2001, week: 1, system: "ESPNStandardScoringSystem", points: 10.0).save(flush: true)
        def fp2 = new FantasyPoints(player: player, season: 2001, week: 2, system: "ESPNStandardScoringSystem", points: 9.0).save(flush: true)
        def fp3 = new FantasyPoints(player: player, season: 2001, week: 3, system: "ESPNStandardScoringSystem", points: 11.0).save(flush: true)
        def fp4 = new FantasyPoints(player: player, season: 2001, week: 4, system: "ESPNStandardScoringSystem", points: 20.0).save(flush: true)

        def fp5 = new FantasyPoints(player: player2, season: 2001, week: 1, system: "ESPNStandardScoringSystem", points: 20.0).save(flush: true)
        def fp6 = new FantasyPoints(player: player2, season: 2001, week: 2, system: "ESPNStandardScoringSystem", points: 19.0).save(flush: true)
        def fp7 = new FantasyPoints(player: player2, season: 2001, week: 3, system: "ESPNStandardScoringSystem", points: 21.0).save(flush: true)
        def fp8 = new FantasyPoints(player: player2, season: 2001, week: 4, system: "ESPNStandardScoringSystem", points: 30.0).save(flush: true)

        assert player.getScoringAverageForSeason(2001) == 12.5
    }

	@Test
	void testGetScoringAverageForPositionForSeason_GoodSeason() {
        def player = new Player(name: "Quarterback", position: "QB").save(flush: true)
        def player2 = new Player(name: "Quarterback 2", position: "QB").save(flush: true)

        // Register FantasyPoints for the weeks.
        def fp1 = new FantasyPoints(player: player, season: 2001, week: -1, system: "ESPNStandardScoringSystem", points: 10.0).save(flush: true)
        def fp5 = new FantasyPoints(player: player2, season: 2001, week: -1, system: "ESPNStandardScoringSystem", points: 20.0).save(flush: true)

        assert Player.getScoringAverageForPositionForSeason("QB", 2001) == 15
    }

	@Test
	void testGetCorrelation_OverallPoints_2001_2002_QB() {
        def qb1 = new Player(name: "Quarterback 1", position: "QB").save(flush: true)
        def qb2 = new Player(name: "Quarterback 2", position: "QB").save(flush: true)
        def qb3 = new Player(name: "Quarterback 3", position: "QB").save(flush: true)
        def qb4 = new Player(name: "Quarterback 4", position: "QB").save(flush: true)
        def qb5 = new Player(name: "Quarterback 5", position: "QB").save(flush: true)
        def qb6 = new Player(name: "Quarterback 6", position: "QB").save(flush: true)
        def qb_1season = new Player(name: "Quarterback 7", position: "QB").save(flush: true)

        def fp1_2001 = new FantasyPoints(player: qb1, season: 2001, week: -1, system: "ESPNStandardScoringSystem", points: 43.0).save(flush: true)
        def fp1_2002 = new FantasyPoints(player: qb1, season: 2002, week: -1, system: "ESPNStandardScoringSystem", points: 99.0).save(flush: true)

        def fp2_2001 = new FantasyPoints(player: qb2, season: 2001, week: -1, system: "ESPNStandardScoringSystem", points: 21.0).save(flush: true)
        def fp2_2002 = new FantasyPoints(player: qb2, season: 2002, week: -1, system: "ESPNStandardScoringSystem", points: 65.0).save(flush: true)

        def fp3_2001 = new FantasyPoints(player: qb3, season: 2001, week: -1, system: "ESPNStandardScoringSystem", points: 25.0).save(flush: true)
        def fp3_2002 = new FantasyPoints(player: qb3, season: 2002, week: -1, system: "ESPNStandardScoringSystem", points: 79.0).save(flush: true)

        def fp4_2001 = new FantasyPoints(player: qb4, season: 2001, week: -1, system: "ESPNStandardScoringSystem", points: 42.0).save(flush: true)
        def fp4_2002 = new FantasyPoints(player: qb4, season: 2002, week: -1, system: "ESPNStandardScoringSystem", points: 75.0).save(flush: true)

        def fp5_2001 = new FantasyPoints(player: qb5, season: 2001, week: -1, system: "ESPNStandardScoringSystem", points: 57.0).save(flush: true)
        def fp5_2002 = new FantasyPoints(player: qb5, season: 2002, week: -1, system: "ESPNStandardScoringSystem", points: 87.0).save(flush: true)

        def fp6_2001 = new FantasyPoints(player: qb6, season: 2001, week: -1, system: "ESPNStandardScoringSystem", points: 59.0).save(flush: true)
        def fp6_2002 = new FantasyPoints(player: qb6, season: 2002, week: -1, system: "ESPNStandardScoringSystem", points: 81.0).save(flush: true)

        def fp_1season_2001 = new FantasyPoints(player: qb_1season, season: 2001, week: -1, system: "ESPNStandardScoringSystem", points: 59.0).save(flush: true)

        assert Double.valueOf(Player.getCorrelation("QB", null, null, 2001, 2002)).round(6) == 0.529809
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
        def qb1 = new Player(name: "Quarterback 1", position: "QB").save(flush: true)
        def qb2 = new Player(name: "Quarterback 2", position: "QB").save(flush: true)
        def qb3 = new Player(name: "Quarterback 3", position: "QB").save(flush: true)
        def qb_1season = new Player(name: "Quarterback 7", position: "QB").save(flush: true)

        def s1_2001 = new Stat(player: qb1, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 10).save(flush: true)
        def s1_2002 = new Stat(player: qb1, season: 2002, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 15).save(flush: true)

        def s2_2001 = new Stat(player: qb2, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 12).save(flush: true)
        def s2_2002 = new Stat(player: qb2, season: 2002, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 17).save(flush: true)

        def s3_2001 = new Stat(player: qb3, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 30).save(flush: true)
        def s3_2002 = new Stat(player: qb3, season: 2002, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 45).save(flush: true)

        def s_1season_2001 = new Stat(player: qb_1season, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 12).save(flush: true)

        def espnStandard = new ESPNStandardScoringSystem()
        assert Double.valueOf(Player.getCorrelation("QB", FantasyConstants.STAT_PASSING_TOUCHDOWNS, espnStandard, 2001, 2002)).trunc(3) == 0.999
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
		def q1 = new Player(name: "Quarterback 1", position: Player.POSITION_QB).save(flush: true)
		def q2 = new Player(name: "Quarterback 2", position: Player.POSITION_QB).save(flush: true)

		def r1 = new Player(name: "Running back 1", position: Player.POSITION_RB).save(flush: true)
		def r2 = new Player(name: "Running back 2", position: Player.POSITION_RB).save(flush: true)

		/*
		 * Define fantasy points for players
		 */
		def q1_stats = new FantasyPoints(player: q1, season: 2002, week: -1, system: ESPNStandardScoringSystem.class.getName(), points: 300).save(flush: true)
		def q2_stats = new FantasyPoints(player: q2, season: 2002, week: -1, system: ESPNStandardScoringSystem.class.getName(), points: 295).save(flush: true)

		def r1_stats = new FantasyPoints(player: r1, season: 2002, week: -1, system: ESPNStandardScoringSystem.class.getName(), points: 250).save(flush: true)
		def r2_stats = new FantasyPoints(player: r2, season: 2002, week: -1, system: ESPNStandardScoringSystem.class.getName(), points: 245).save(flush: true)

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
		def qb_results = Player.getPlayersInPointsOrder(Player.POSITION_QB, 2002)
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
		def rb_results = Player.getPlayersInPointsOrder(Player.POSITION_RB, 2002)
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
}

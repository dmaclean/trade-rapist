package com.traderapist.models

import com.traderapist.constants.FantasyConstants

import static org.junit.Assert.*
import org.junit.*

class PlayerTests {

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testGetDropoffData_2001_QB_1() {
        def p1 = new Player(name: "Player 1", position: "QB")
        p1.save(flush: true)
        def p2 = new Player(name: "Player 2", position: "QB")
        p2.save(flush: true)
        def fp1 = new FantasyPoints(player: p1, season: 2001, week: -1, points: 25.0, system: "ESPNStandardScoringSystem")
        fp1.save(flush: true)
        def fp2 = new FantasyPoints(player: p2, season: 2001, week: -1, points: 9.0, system: "ESPNStandardScoringSystem")
        fp2.save(flush: true)

        def tiers = Player.getDropoffData("QB", 2001)

        assert tiers.size() == 2

        assert tiers[0][0][0].name == "Player 1" && tiers[0][0][0].position == "QB"
        assert tiers[1][0][0].name == "Player 2" && tiers[1][0][0].position == "QB"
        assert tiers[0][0][1].points == 25.0
        assert tiers[1][0][1].points == 9.0
    }

    @Test
    void testGetDropoffData_2001_QB_2() {
        def q1 = new Player(name: "Quarterback 1", position: "QB")
        q1.save(flush: true)
        def q2 = new Player(name: "Quarterback 2", position: "QB")
        q2.save(flush: true)
        def q3 = new Player(name: "Quarterback 3", position: "QB")
        q3.save(flush: true)
        def q4 = new Player(name: "Quarterback 4", position: "QB")
        q4.save(flush: true)
        def fp1 = new FantasyPoints(player: q1, season: 2001, week: -1, points: 80.0, system: "ESPNStandardScoringSystem")
        fp1.save(flush: true)
        def fp2 = new FantasyPoints(player: q2, season: 2001, week: -1, points: 61.0, system: "ESPNStandardScoringSystem")
        fp2.save(flush: true)
        def fp3 = new FantasyPoints(player: q3, season: 2001, week: -1, points: 84.0, system: "ESPNStandardScoringSystem")
        fp3.save(flush: true)
        def fp4 = new FantasyPoints(player: q4, season: 2001, week: -1, points: 100.0, system: "ESPNStandardScoringSystem")
        fp4.save(flush: true)

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

    def testGetProjectedPoints_BadYear() {
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

    def testGetProjectedPoints_QB() {
        def q1 = new Player(name: "Quarterback", position: "QB").save(flush: true)
        def s1 = new Stat(player: q1, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 10).save(flush: true)

        try {
            q1.calculateProjectedPoints(2002)

        }
        catch(Exception e) {
            fail(e.getMessage())
        }
    }

    def testCalculatePointsStandardDeviation_BadYear() {
        def player = new Player(name: "Quarterback", position: "QB").save(flush: true)
        def fp1 = new FantasyPoints(player: player, season: 2001, week: 1, system: "ESPNStandardScoringSystem", points: 10).save(flush: true)

        assert player.calculatePointsStandardDeviation(2002) == -1
    }

    def testCalculatePointsStandardDeviation_2001() {
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

    def testGetStatYears_2001() {
        def player = new Player(name: "Quarterback", position: "QB").save(flush: true)

        // Set up Stat object to satisfy Stat.getStatYears() check.
        def s1 = new Stat(player: player, season: 2001, week: 1, statKey: FantasyConstants.STAT_TOUCHDOWN, statValue: 1).save(flush: true)

        def statYears = player.getStatYears()
        assert statYears.contains(2001) && statYears.size() == 1
    }

    def testGetStatYears_2001_2002() {
        def player = new Player(name: "Quarterback", position: "QB").save(flush: true)

        // Set up Stat object to satisfy Stat.getStatYears() check.
        def s1 = new Stat(player: player, season: 2001, week: 1, statKey: FantasyConstants.STAT_TOUCHDOWN, statValue: 1).save(flush: true)
        def s2 = new Stat(player: player, season: 2002, week: 1, statKey: FantasyConstants.STAT_TOUCHDOWN, statValue: 1).save(flush: true)

        def statYears = player.getStatYears()

        assert statYears.size() == 2
        assert statYears.contains(2001)
        assert statYears.contains(2002)
    }

    def testGetScoringAverageForSeason_BadSeason() {
        def player = new Player(name: "Quarterback", position: "QB").save(flush: true)

        assert player.getScoringAverageForSeason(2001) == 0
    }

    def testGetScoringAverageForSeason_GoodSeason() {
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

    /*def testCalculateStandardDeviation() {
        def q1 = new Player(name: "Quarterback 1", position: "QB")
        def q2 = new Player(name: "Quarterback 2", position: "QB")
        def q3 = new Player(name: "Quarterback 3", position: "QB")
        def q4 = new Player(name: "Quarterback 4", position: "QB")
        def fp1 = new FantasyPoints(player: q1, season: 2001, week: -1, points: 10.0, system: "ESPNStandardScoringSystem")
        def fp2 = new FantasyPoints(player: q2, season: 2001, week: -1, points: 9.0, system: "ESPNStandardScoringSystem")
        def fp3 = new FantasyPoints(player: q3, season: 2001, week: -1, points: 11.0, system: "ESPNStandardScoringSystem")
        def fp4 = new FantasyPoints(player: q4, season: 2001, week: -1, points: 20.0, system: "ESPNStandardScoringSystem")

        def players = [[q1, fp1], [q2, fp2], [q3, fp3], [q4, fp4]]

        // Mean = (10.0 + 9.0 + 11.0 + 20.0)/4 = 12.5
        // DIFF FROM MEAN, SQUARED
        // q1 = (10.0 - 12.5)^2 = -2.5^2 = 6.25
        // q2 = (9.0 - 12.5)^2 = -3.5^2 = 12.25
        // q3 = (11.0 - 12.5)^2 = -1.5^2 = 2.25
        // q4 = (20.0 - 12.5)^2 = 7.5^2 = 56.25

        // sqrt( (6.25 + 12.25 + 2.25 + 56.25)/4 ) = sqrt(77/4) = sqrt(19.25) = 4.387
        assert Player.calculateStandardDeviation(players).trunc(3) == 4.387
    }*/

    def testCalculateStandardDeviation() {
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
}

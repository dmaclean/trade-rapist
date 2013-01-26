package com.traderapist.models

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
        def fp1 = new FantasyPoints(player: p1, season: 2001, week: -1, points: 10.0, system: "ESPNStandardScoringSystem")
        fp1.save(flush: true)
        def fp2 = new FantasyPoints(player: p2, season: 2001, week: -1, points: 9.0, system: "ESPNStandardScoringSystem")
        fp2.save(flush: true)

        def results = Player.getDropoffData("QB", 2001)

        assert results.size() == 2

        assert results[0][0].name == "Player 1" && results[0][0].position == "QB"
        assert results[1][0].name == "Player 2" && results[1][0].position == "QB"
        assert results[0][1].points == 10.0
        assert results[1][1].points == 9.0
    }

    @Test
    void testGetDropoffData_2001_QB_2() {
        def q1 = new Player(name: "Quarterback 1", position: "QB")
        q1.save(flush: true)
        def q2 = new Player(name: "Quarterback 2", position: "QB")
        q2.save(flush: true)
        def q3 = new Player(name: "Quarterback 3", position: "QB")
        q3.save(flush: true)
        def r1 = new Player(name: "Running back 1", position: "RB")
        r1.save(flush: true)
        def fp1 = new FantasyPoints(player: q1, season: 2001, week: -1, points: 10.0, system: "ESPNStandardScoringSystem")
        fp1.save(flush: true)
        def fp2 = new FantasyPoints(player: q2, season: 2001, week: -1, points: 9.0, system: "ESPNStandardScoringSystem")
        fp2.save(flush: true)
        def fp3 = new FantasyPoints(player: q3, season: 2001, week: -1, points: 11.0, system: "ESPNStandardScoringSystem")
        fp3.save(flush: true)
        def fp4 = new FantasyPoints(player: r1, season: 2001, week: -1, points: 20.0, system: "ESPNStandardScoringSystem")
        fp4.save(flush: true)

        def results = Player.getDropoffData("QB", 2001)

        assert results.size() == 3

        assert results[0][0].name == "Quarterback 3" && results[0][0].position == "QB"
        assert results[1][0].name == "Quarterback 1" && results[1][0].position == "QB"
        assert results[2][0].name == "Quarterback 2" && results[2][0].position == "QB"
        assert results[0][1].points == 11.0
        assert results[1][1].points == 10.0
        assert results[2][1].points == 9.0
    }
}

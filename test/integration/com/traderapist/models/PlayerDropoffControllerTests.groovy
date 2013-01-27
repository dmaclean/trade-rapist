package com.traderapist.models

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

import static org.junit.Assert.*
import org.junit.*

class PlayerDropoffControllerTests {

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testSomething() {
        def q1 = new Player(name: "Quarterback 1", position: "QB")
        q1.save(flush: true)
        def q2 = new Player(name: "Quarterback 2", position: "QB")
        q2.save(flush: true)
        def q3 = new Player(name: "Quarterback 3", position: "QB")
        q3.save(flush: true)
        def q4 = new Player(name: "Quarterback 4", position: "QB")
        q4.save(flush: true)
        def r1 = new Player(name: "Running back 1", position: "RB")
        r1.save(flush: true)
        def fp1 = new FantasyPoints(player: q1, season: 2001, week: -1, points: 64.0, system: "ESPNStandardScoringSystem")
        fp1.save(flush: true)
        def fp2 = new FantasyPoints(player: q2, season: 2001, week: -1, points: 84.0, system: "ESPNStandardScoringSystem")
        fp2.save(flush: true)
        def fp3 = new FantasyPoints(player: q3, season: 2001, week: -1, points: 81.0, system: "ESPNStandardScoringSystem")
        fp3.save(flush: true)
        def fp5 = new FantasyPoints(player: q4, season: 2001, week: -1, points: 100.0, system: "ESPNStandardScoringSystem")
        fp5.save(flush: true)
        def fp4 = new FantasyPoints(player: r1, season: 2001, week: -1, points: 20.0, system: "ESPNStandardScoringSystem")
        fp4.save(flush: true)

        def pdc = new PlayerDropoffController()

        pdc.index()

        assert pdc.response.contentAsString == "Tier 1<br/>Quarterback 4 - 100.0<br/><br/>Tier 2<br/>Quarterback 2 - 84.0<br/>Quarterback 3 - 81.0<br/><br/>Tier 3<br/>Quarterback 1 - 64.0<br/><br/>"
    }
}

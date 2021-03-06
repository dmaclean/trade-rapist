package com.traderapist.models

import com.traderapist.constants.FantasyConstants
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import groovy.mock.interceptor.MockFor
import org.junit.*

//@TestFor(PlayerDropoffController)
//@Mock([Player,FantasyPoints,Stat])
class PlayerDropoffControllerIntegrationTests {

	def controller

    @Before
    void setUp() {
	    controller = new PlayerDropoffController()

        def q1 = new Player(name: "Quarterback 1", position: "QB").save(flush: true)
        def q2 = new Player(name: "Quarterback 2", position: "QB").save(flush: true)
        def q3 = new Player(name: "Quarterback 3", position: "QB").save(flush: true)
        def q4 = new Player(name: "Quarterback 4", position: "QB").save(flush: true)
        def r1 = new Player(name: "Running back 1", position: "RB").save(flush: true)

	    def fp1 = new FantasyPoints(player: q1, season: 2001, week: -1, points: 64.0, system: "ESPNStandardScoringSystem").save(flush: true)
        def fp2 = new FantasyPoints(player: q2, season: 2001, week: -1, points: 84.0, system: "ESPNStandardScoringSystem").save(flush: true)
        def fp3 = new FantasyPoints(player: q3, season: 2001, week: -1, points: 81.0, system: "ESPNStandardScoringSystem").save(flush: true)
        def fp5 = new FantasyPoints(player: q4, season: 2001, week: -1, points: 100.0, system: "ESPNStandardScoringSystem").save(flush: true)
        def fp4 = new FantasyPoints(player: r1, season: 2001, week: -1, points: 20.0, system: "ESPNStandardScoringSystem").save(flush: true)

	    def stat1 = new Stat(player: q1, season: 2001, week: -1, statKey: FantasyConstants.STAT_COMPLETIONS, statValue: 100).save(flush: true)
        def stat2 = new Stat(player: q2, season: 2001, week: -1, statKey: FantasyConstants.STAT_COMPLETIONS, statValue: 100).save(flush: true)
        def stat3 = new Stat(player: q3, season: 2001, week: -1, statKey: FantasyConstants.STAT_COMPLETIONS, statValue: 100).save(flush: true)
        def stat4 = new Stat(player: q4, season: 2001, week: -1, statKey: FantasyConstants.STAT_COMPLETIONS, statValue: 100).save(flush: true)
        def stat5 = new Stat(player: r1, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 10).save(flush: true)
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testIndex() {
        controller.index()

        // Didn't specify which season.  Should default to 2001
        assert controller.modelAndView.model["chosenSeason"] == 2001
        assert controller.modelAndView.model["seasons"].contains(2001)
        assert controller.modelAndView.getViewName() == "/playerDropoff/index"
        //assert pdc.response.contentAsString.contains("<h2>Tier 1</h2><p>Quarterback 4 - 100.0</p><h2>Tier 2</h2><p>Quarterback 2 - 84.0</p><p>Quarterback 3 - 81.0</p><h2>Tier 3</h2><p>Quarterback 1 - 64.0</p>")
    }

    @Test
    void testIndex_YearInput() {
        controller.request.setParameter("seasons", "2002")

	    controller.index()

        // Specified the season.  Should get 2002
        assert controller.modelAndView.model["chosenSeason"] == 2002
        assert controller.modelAndView.model["seasons"].contains(2001)
        assert controller.modelAndView.getViewName() == "/playerDropoff/index"
        //assert pdc.response.contentAsString.contains("<h2>Tier 1</h2><p>Quarterback 4 - 100.0</p><h2>Tier 2</h2><p>Quarterback 2 - 84.0</p><p>Quarterback 3 - 81.0</p><h2>Tier 3</h2><p>Quarterback 1 - 64.0</p>")
    }
}

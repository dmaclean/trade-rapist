package com.traderapist.models

import com.traderapist.scoringsystem.ESPNStandardScoringSystem
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 6/7/13
 * Time: 8:50 AM
 * To change this template use File | Settings | File Templates.
 */
//@Mock([Player])
class FantasyPointsIntegrationTests {
	@Before
	void setUp() {

	}

	@After
	void tearDown() {

	}

	@Test
	void testProjectionDefaultsToFalse() {
		def player = new Player(name: "Dan MacLean", position: "QB").save(flush: true)

		def fp = new FantasyPoints(player: player, season: 2013, week: -1, points: 100, system: ESPNStandardScoringSystem.class.getName())
		fp.save(flush: true)

		def fp2 = FantasyPoints.get(fp.id)

		assert !fp2.projection
	}
}

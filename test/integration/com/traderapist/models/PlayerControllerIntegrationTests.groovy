package com.traderapist.models

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 6/8/13
 * Time: 6:55 PM
 * To change this template use File | Settings | File Templates.
 */
//@TestFor(PlayerController)
//@Mock([Player,FantasyPoints])
class PlayerControllerIntegrationTests {

	def controller
	def params

	@Before
	void setUp() {
		controller = new PlayerController()

//		Player.metaClass.getStatYears = { [2002] }
//		Player.metaClass.getScoringAverageForSeason = { season -> 0 }
//		Stat.metaClass.static.getStatYears = { [2002] }

		controller.request.addParameter("name", "Dan")
		controller.request.addParameter("position", "QB")
		params = controller.request.getParameterMap()
	}

	@After
	void tearDown() {

	}

	@Test
	void testShow_NullId() {
		def player = new Player(params)

		assert player.save(flush: true) != null

		assert controller.show() == null
		assert controller.response.redirectedUrl == '/player/list'
	}

	@Test
	void testShow() {
		def player = new Player(params)

		assert player.save(flush: true) != null

//		params.id = player.id

		def model = controller.show(player.id)

		assert model.playerInstance == player
	}
}

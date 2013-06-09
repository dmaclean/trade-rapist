package com.traderapist.models

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
class PlayerControllerIntegrationTests extends GroovyTestCase {
	PlayerController controller
	def params

	@Before
	void setUp() {
		super.setUp()

		controller = new PlayerController()

		params = [:]
		params["name"] = "Dan"
		params["position"] = "QB"
	}

	@After
	void tearDown() {
		super.tearDown()

		controller = null
		params = null
	}

	@Test
	void testShow_NullId() {
//		controller.show()

//		assert controller.response.redirectedUrl == '/player/list'

		def player = new Player(params)

		assert player.save(flush: true) != null

		params.id = player.id

		assert controller.show() == null
		assert controller.response.redirectedUrl == '/player/list'
	}

	@Test
	void testShow() {
//		controller.show()

//		assert controller.response.redirectedUrl == '/player/list'

		def player = new Player(params)

		assert player.save(flush: true) != null

		params.id = player.id

		def model = controller.show(player.id)

		assert model.playerInstance == player
	}
}

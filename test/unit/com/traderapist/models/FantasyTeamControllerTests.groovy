package com.traderapist.models

import grails.converters.JSON
import org.junit.*
import grails.test.mixin.*

import com.traderapist.security.User

@TestFor(FantasyTeamController)
@Mock([FantasyLeagueType, FantasyTeam, FantasyTeamStarter, User])
class FantasyTeamControllerTests {
	User user
	FantasyLeagueType flt

	@Before
	void setUp() {
		User.metaClass.encodePassword = { -> "password"}
		user = new User(username: "dmaclean", password: "password").save(flush: true)
		flt = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
	}

	@After
	void tearDown() {
		user = null
		flt = null
	}

	def populateValidParams(params) {
		assert params != null
		// TODO: Populate valid properties like...
		params["league_id"] = "league id"
		params["name"] = "ESPN"
		params["season"] = 2013
		params["user"] = user
		params["fantasyLeagueType"] = flt
		params["numOwners"] = 10

		params[Player.POSITION_QB] = 1
		params[Player.POSITION_RB] = 2
		params[Player.POSITION_WR] = 3
		params[Player.POSITION_TE] = 1
		params[Player.POSITION_DEF] = 1
		params[Player.POSITION_K] = 1
	}

	void testIndex() {
		controller.index()
		assert "/fantasyTeam/list" == response.redirectedUrl
	}

	/**
	 * The list() method uses Spring Security, so the tests for it are in integration.
	 */
//	void testList() {
//		def model = controller.list()
//
//		assert model.fantasyTeamInstanceList.size() == 0
//		assert model.fantasyTeamInstanceTotal == 0
//	}

	void testCreate() {
		def model = controller.create()

		assert model.fantasyTeamInstance != null
	}

	void testSave() {
		controller.save()

		assert model.fantasyTeamInstance != null
		assert view == '/fantasyTeam/create'

		response.reset()

		populateValidParams(params)
		controller.save()

		assert response.redirectedUrl == '/fantasyTeam/show/1'
		assert controller.flash.message != null
		assert FantasyTeam.count() == 1
	}

	void testSaveWithStarters() {
		controller.save()

		assert model.fantasyTeamInstance != null
		assert view == '/fantasyTeam/create'

		response.reset()

		populateValidParams(params)
		controller.save()

		assert response.redirectedUrl == '/fantasyTeam/show/1'
		assert controller.flash.message != null
		assert FantasyTeam.count() == 1

		assert FantasyTeamStarter.count() == 6
	}

	void testSaveAjax() {
		controller.saveAjax()

		assert response.text == "error"

		response.reset()

		populateValidParams(params)
		controller.saveAjax()

		assert response.text =~ /\d+/
		assert FantasyTeam.count() == 1

		assert FantasyTeamStarter.count() == 6
	}

	void testShow() {
		controller.show()

		assert flash.message != null
		assert response.redirectedUrl == '/fantasyTeam/list'

		populateValidParams(params)
		def fantasyTeam = new FantasyTeam(params)

		assert fantasyTeam.save() != null

		params.id = fantasyTeam.id

		def model = controller.show()

		assert model.fantasyTeamInstance == fantasyTeam
	}

	void testEdit() {
		controller.edit()

		assert flash.message != null
		assert response.redirectedUrl == '/fantasyTeam/list'

		populateValidParams(params)
		def fantasyTeam = new FantasyTeam(params)

		assert fantasyTeam.save() != null

		params.id = fantasyTeam.id

		def model = controller.edit()

		assert model.fantasyTeamInstance == fantasyTeam
	}

	void testUpdate() {
		controller.update()

		assert flash.message != null
		assert response.redirectedUrl == '/fantasyTeam/list'

		response.reset()

		populateValidParams(params)
		def fantasyTeam = new FantasyTeam(params)

		assert fantasyTeam.save() != null

		// Create FantasyTeamStarter object
	    def fantasyTeamStarter = new FantasyTeamStarter(position: Player.POSITION_WR, numStarters: 3, fantasyTeam: fantasyTeam).save(flush: true)

		// test invalid parameters in update
		params.id = fantasyTeam.id
		//TODO: add invalid values to params object
		params["season"] = null
		params["name"] = null

		controller.update()

		assert view == "/fantasyTeam/edit"
		assert model.fantasyTeamInstance != null

		fantasyTeam.clearErrors()

		populateValidParams(params)

		// Change WR from 3 to 2
		params[Player.POSITION_WR] = 2

		controller.update()

		def starter = FantasyTeamStarter.findByFantasyTeamAndPosition(fantasyTeam, Player.POSITION_WR)
		assert starter != null && starter.numStarters == 2

		assert response.redirectedUrl == "/fantasyTeam/show/$fantasyTeam.id"
		assert flash.message != null

		//test outdated version number
		response.reset()
		fantasyTeam.clearErrors()

		populateValidParams(params)
		params.id = fantasyTeam.id
		params.version = -1
		controller.update()

		assert view == "/fantasyTeam/edit"
		assert model.fantasyTeamInstance != null
		assert model.fantasyTeamInstance.errors.getFieldError('version')
		assert flash.message != null
	}

	void testDelete() {
		controller.delete()
		assert flash.message != null
		assert response.redirectedUrl == '/fantasyTeam/list'

		response.reset()

		populateValidParams(params)
		def fantasyTeam = new FantasyTeam(params)

		assert fantasyTeam.save() != null
		assert FantasyTeam.count() == 1

		params.id = fantasyTeam.id

		controller.delete()

		assert FantasyTeam.count() == 0
		assert FantasyTeam.get(fantasyTeam.id) == null
		assert response.redirectedUrl == '/fantasyTeam/list'
	}
}

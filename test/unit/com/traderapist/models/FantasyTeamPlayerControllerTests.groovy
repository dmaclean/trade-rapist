package com.traderapist.models

import com.traderapist.security.User
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.junit.After
import org.junit.Before

@TestFor(FantasyTeamPlayerController)
@Mock([FantasyTeam, FantasyTeamPlayer, FantasyLeagueType, Player, User])
class FantasyTeamPlayerControllerTests {
	def fantasyLeagueType
	def fantasyTeam
	def player
	def user

	@Before
	void setUp() {
		User.metaClass.encodePassword = { -> "password" }

		user = new User(username: "Dan@gmail.com", password: "password").save(flush: true)
		player = new Player(name: "dan", position: Player.POSITION_QB).save(flush: true)
		fantasyLeagueType = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
		fantasyTeam = new FantasyTeam(user: user, name: "ESPN", fantasyLeagueType: fantasyLeagueType, season: 2013, numOwners: 10).save(flush: true)
	}

	@After
	void tearDown() {

	}

	def populateValidParams(params) {
		assert params != null
		// TODO: Populate valid properties like...
		params["player"] = player
		params["fantasyTeam"] = fantasyTeam
	}

	void testIndex() {
		controller.index()
		assert "/fantasyTeamPlayer/list" == response.redirectedUrl
	}

	void testList() {

		def model = controller.list()

		assert model.fantasyTeamPlayerInstanceList.size() == 0
		assert model.fantasyTeamPlayerInstanceTotal == 0
	}

	void testCreate() {
		def model = controller.create()

		assert model.fantasyTeamPlayerInstance != null
	}

	void testSave() {
		controller.save()

		assert model.fantasyTeamPlayerInstance != null
		assert view == '/fantasyTeamPlayer/create'

		response.reset()

		populateValidParams(params)
		controller.save()

		assert response.redirectedUrl == '/fantasyTeamPlayer/show/1'
		assert controller.flash.message != null
		assert FantasyTeamPlayer.count() == 1
	}

	void testShow() {
		controller.show()

		assert flash.message != null
		assert response.redirectedUrl == '/fantasyTeamPlayer/list'

		populateValidParams(params)
		def fantasyTeamPlayer = new FantasyTeamPlayer(params)

		assert fantasyTeamPlayer.save() != null

		params.id = fantasyTeamPlayer.id

		def model = controller.show()

		assert model.fantasyTeamPlayerInstance == fantasyTeamPlayer
	}

	void testEdit() {
		controller.edit()

		assert flash.message != null
		assert response.redirectedUrl == '/fantasyTeamPlayer/list'

		populateValidParams(params)
		def fantasyTeamPlayer = new FantasyTeamPlayer(params)

		assert fantasyTeamPlayer.save() != null

		params.id = fantasyTeamPlayer.id

		def model = controller.edit()

		assert model.fantasyTeamPlayerInstance == fantasyTeamPlayer
	}

	void testUpdate() {
		controller.update()

		assert flash.message != null
		assert response.redirectedUrl == '/fantasyTeamPlayer/list'

		response.reset()

		populateValidParams(params)
		def fantasyTeamPlayer = new FantasyTeamPlayer(params)

		assert fantasyTeamPlayer.save() != null

		// test invalid parameters in update
		params.id = fantasyTeamPlayer.id
		//TODO: add invalid values to params object
		params["player"] = null
		params["fantasyTeam"] = null

		controller.update()

		assert view == "/fantasyTeamPlayer/edit"
		assert model.fantasyTeamPlayerInstance != null

		fantasyTeamPlayer.clearErrors()

		populateValidParams(params)
		controller.update()

		assert response.redirectedUrl == "/fantasyTeamPlayer/show/$fantasyTeamPlayer.id"
		assert flash.message != null

		//test outdated version number
		response.reset()
		fantasyTeamPlayer.clearErrors()

		populateValidParams(params)
		params.id = fantasyTeamPlayer.id
		params.version = -1
		controller.update()

		assert view == "/fantasyTeamPlayer/edit"
		assert model.fantasyTeamPlayerInstance != null
		assert model.fantasyTeamPlayerInstance.errors.getFieldError('version')
		assert flash.message != null
	}

	void testDelete() {
		controller.delete()
		assert flash.message != null
		assert response.redirectedUrl == '/fantasyTeamPlayer/list'

		response.reset()

		populateValidParams(params)
		def fantasyTeamPlayer = new FantasyTeamPlayer(params)

		assert fantasyTeamPlayer.save() != null
		assert FantasyTeamPlayer.count() == 1

		params.id = fantasyTeamPlayer.id

		controller.delete()

		assert FantasyTeamPlayer.count() == 0
		assert FantasyTeamPlayer.get(fantasyTeamPlayer.id) == null
		assert response.redirectedUrl == '/fantasyTeamPlayer/list'
	}
}

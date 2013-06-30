package com.traderapist.models



import org.junit.*
import grails.test.mixin.*

@TestFor(FantasyLeagueTypeController)
@Mock(FantasyLeagueType)
class FantasyLeagueTypeControllerTests {

	def populateValidParams(params) {
		assert params != null
		// TODO: Populate valid properties like...
		params["code"] = "ESPN"
		params["description"] = "ESPN standard scoring fantasy football league"
	}

	void testIndex() {
		controller.index()
		assert "/fantasyLeagueType/list" == response.redirectedUrl
	}

	void testList() {

		def model = controller.list()

		assert model.fantasyLeagueTypeInstanceList.size() == 0
		assert model.fantasyLeagueTypeInstanceTotal == 0
	}

	void testCreate() {
		def model = controller.create()

		assert model.fantasyLeagueTypeInstance != null
	}

	void testSave() {
		controller.save()

		assert model.fantasyLeagueTypeInstance != null
		assert view == '/fantasyLeagueType/create'

		response.reset()

		populateValidParams(params)
		controller.save()

		assert response.redirectedUrl == '/fantasyLeagueType/show/1'
		assert controller.flash.message != null
		assert FantasyLeagueType.count() == 1
	}

	void testShow() {
		controller.show()

		assert flash.message != null
		assert response.redirectedUrl == '/fantasyLeagueType/list'

		populateValidParams(params)
		def fantasyLeagueType = new FantasyLeagueType(params)

		assert fantasyLeagueType.save() != null

		params.id = fantasyLeagueType.id

		def model = controller.show()

		assert model.fantasyLeagueTypeInstance == fantasyLeagueType
	}

	void testEdit() {
		controller.edit()

		assert flash.message != null
		assert response.redirectedUrl == '/fantasyLeagueType/list'

		populateValidParams(params)
		def fantasyLeagueType = new FantasyLeagueType(params)

		assert fantasyLeagueType.save() != null

		params.id = fantasyLeagueType.id

		def model = controller.edit()

		assert model.fantasyLeagueTypeInstance == fantasyLeagueType
	}

	void testUpdate() {
		controller.update()

		assert flash.message != null
		assert response.redirectedUrl == '/fantasyLeagueType/list'

		response.reset()

		populateValidParams(params)
		def fantasyLeagueType = new FantasyLeagueType(params)

		assert fantasyLeagueType.save() != null

		// test invalid parameters in update
		params.id = fantasyLeagueType.id
		//TODO: add invalid values to params object
		params["code"] = null
		params["description"] = null

		controller.update()

		assert view == "/fantasyLeagueType/edit"
		assert model.fantasyLeagueTypeInstance != null

		fantasyLeagueType.clearErrors()

		populateValidParams(params)
		controller.update()

		assert response.redirectedUrl == "/fantasyLeagueType/show/$fantasyLeagueType.id"
		assert flash.message != null

		//test outdated version number
		response.reset()
		fantasyLeagueType.clearErrors()

		populateValidParams(params)
		params.id = fantasyLeagueType.id
		params.version = -1
		controller.update()

		assert view == "/fantasyLeagueType/edit"
		assert model.fantasyLeagueTypeInstance != null
		assert model.fantasyLeagueTypeInstance.errors.getFieldError('version')
		assert flash.message != null
	}

	void testDelete() {
		controller.delete()
		assert flash.message != null
		assert response.redirectedUrl == '/fantasyLeagueType/list'

		response.reset()

		populateValidParams(params)
		def fantasyLeagueType = new FantasyLeagueType(params)

		assert fantasyLeagueType.save() != null
		assert FantasyLeagueType.count() == 1

		params.id = fantasyLeagueType.id

		controller.delete()

		assert FantasyLeagueType.count() == 0
		assert FantasyLeagueType.get(fantasyLeagueType.id) == null
		assert response.redirectedUrl == '/fantasyLeagueType/list'
	}
}

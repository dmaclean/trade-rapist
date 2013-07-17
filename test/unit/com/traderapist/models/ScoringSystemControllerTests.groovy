package com.traderapist.models



import org.junit.*
import grails.test.mixin.*

@TestFor(ScoringSystemController)
@Mock(ScoringSystem)
class ScoringSystemControllerTests {

	def populateValidParams(params) {
		assert params != null
		// TODO: Populate valid properties like...
		params["name"] = 'someValidName'
	}

	void testIndex() {
		controller.index()
		assert "/scoringSystem/list" == response.redirectedUrl
	}

	void testList() {

		def model = controller.list()

		assert model.scoringSystemInstanceList.size() == 0
		assert model.scoringSystemInstanceTotal == 0
	}

	void testCreate() {
		def model = controller.create()

		assert model.scoringSystemInstance != null
	}

	void testSave() {
		controller.save()

		assert model.scoringSystemInstance != null
		assert view == '/scoringSystem/create'

		response.reset()

		populateValidParams(params)
		controller.save()

		assert response.redirectedUrl == '/scoringSystem/show/1'
		assert controller.flash.message != null
		assert ScoringSystem.count() == 1
	}

	void testShow() {
		controller.show()

		assert flash.message != null
		assert response.redirectedUrl == '/scoringSystem/list'

		populateValidParams(params)
		def scoringSystem = new ScoringSystem(params)

		assert scoringSystem.save() != null

		params.id = scoringSystem.id

		def model = controller.show()

		assert model.scoringSystemInstance == scoringSystem
	}

	void testEdit() {
		controller.edit()

		assert flash.message != null
		assert response.redirectedUrl == '/scoringSystem/list'

		populateValidParams(params)
		def scoringSystem = new ScoringSystem(params)

		assert scoringSystem.save() != null

		params.id = scoringSystem.id

		def model = controller.edit()

		assert model.scoringSystemInstance == scoringSystem
	}

	void testUpdate() {
		controller.update()

		assert flash.message != null
		assert response.redirectedUrl == '/scoringSystem/list'

		response.reset()

		populateValidParams(params)
		def scoringSystem = new ScoringSystem(params)

		assert scoringSystem.save() != null

		// test invalid parameters in update
		params.id = scoringSystem.id
		//TODO: add invalid values to params object
		params.name = null

		controller.update()

		assert view == "/scoringSystem/edit"
		assert model.scoringSystemInstance != null

		scoringSystem.clearErrors()

		populateValidParams(params)
		controller.update()

		assert response.redirectedUrl == "/scoringSystem/show/$scoringSystem.id"
		assert flash.message != null

		//test outdated version number
		response.reset()
		scoringSystem.clearErrors()

		populateValidParams(params)
		params.id = scoringSystem.id
		params.version = -1
		controller.update()

		assert view == "/scoringSystem/edit"
		assert model.scoringSystemInstance != null
		assert model.scoringSystemInstance.errors.getFieldError('version')
		assert flash.message != null
	}

	void testDelete() {
		controller.delete()
		assert flash.message != null
		assert response.redirectedUrl == '/scoringSystem/list'

		response.reset()

		populateValidParams(params)
		def scoringSystem = new ScoringSystem(params)

		assert scoringSystem.save() != null
		assert ScoringSystem.count() == 1

		params.id = scoringSystem.id

		controller.delete()

		assert ScoringSystem.count() == 0
		assert ScoringSystem.get(scoringSystem.id) == null
		assert response.redirectedUrl == '/scoringSystem/list'
	}
}

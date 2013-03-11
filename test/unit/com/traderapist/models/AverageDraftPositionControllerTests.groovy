package com.traderapist.models



import org.junit.*
import grails.test.mixin.*

@TestFor(AverageDraftPositionController)
@Mock([AverageDraftPosition,Player])
class AverageDraftPositionControllerTests {

	Player player

	void setUp() {
		player = new Player(name: "Dan MacLean", position: "QB").save(flush: true)
	}

	void tearDown() {

	}

    def populateValidParams(params) {
        assert params != null
        params["adp"] = 1.0
	    params["season"] = 2001
	    params["player"] = player
    }

    void testIndex() {
        controller.index()
        assert "/averageDraftPosition/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.averageDraftPositionInstanceList.size() == 0
        assert model.averageDraftPositionInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.averageDraftPositionInstance != null
    }

    void testSave() {
        controller.save()

        assert model.averageDraftPositionInstance != null
        assert view == '/averageDraftPosition/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/averageDraftPosition/show/1'
        assert controller.flash.message != null
        assert AverageDraftPosition.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/averageDraftPosition/list'

        populateValidParams(params)
        def averageDraftPosition = new AverageDraftPosition(params)

        assert averageDraftPosition.save() != null

        params.id = averageDraftPosition.id

        def model = controller.show()

        assert model.averageDraftPositionInstance == averageDraftPosition
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/averageDraftPosition/list'

        populateValidParams(params)
        def averageDraftPosition = new AverageDraftPosition(params)

        assert averageDraftPosition.save() != null

        params.id = averageDraftPosition.id

        def model = controller.edit()

        assert model.averageDraftPositionInstance == averageDraftPosition
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/averageDraftPosition/list'

        response.reset()

        populateValidParams(params)
        def averageDraftPosition = new AverageDraftPosition(params)

        assert averageDraftPosition.save() != null

        // test invalid parameters in update
        params.id = averageDraftPosition.id
        //TODO: add invalid values to params object
	    params["adp"] = 0.9

        controller.update()

        assert view == "/averageDraftPosition/edit"
        assert model.averageDraftPositionInstance != null

        averageDraftPosition.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/averageDraftPosition/show/$averageDraftPosition.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        averageDraftPosition.clearErrors()

        populateValidParams(params)
        params.id = averageDraftPosition.id
        params.version = -1
        controller.update()

        assert view == "/averageDraftPosition/edit"
        assert model.averageDraftPositionInstance != null
        assert model.averageDraftPositionInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/averageDraftPosition/list'

        response.reset()

        populateValidParams(params)
        def averageDraftPosition = new AverageDraftPosition(params)

        assert averageDraftPosition.save() != null
        assert AverageDraftPosition.count() == 1

        params.id = averageDraftPosition.id

        controller.delete()

        assert AverageDraftPosition.count() == 0
        assert AverageDraftPosition.get(averageDraftPosition.id) == null
        assert response.redirectedUrl == '/averageDraftPosition/list'
    }
}

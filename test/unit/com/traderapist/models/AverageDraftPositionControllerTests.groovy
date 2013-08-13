package com.traderapist.models



import org.junit.*
import grails.test.mixin.*

@TestFor(AverageDraftPositionController)
@Mock([AverageDraftPosition,Player, Team, TeamMembership])
class AverageDraftPositionControllerTests {

	Player player

	void setUp() {
		player = new Player(name: "Dan MacLean", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
	}

	void tearDown() {
		player = null
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

	void testSaveFromCSV() {
		def rodgers = new Player(name: "Aaron Rodgers", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
		def brees = new Player(name: "Drew Brees", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
		def manning = new Player(name: "Peyton Manning", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)

		params.input = "Aaron Rodgers,GB,QB,9.2\n" +
				"Drew Brees,NO,QB,12.9\n" +
				"Peyton Manning,Den,QB,15.1"
		params.season = "2013"

		controller.saveFromCSV()

		def adp = AverageDraftPosition.list()

		assert adp[0].player.name == "Aaron Rodgers"
		assert adp[0].adp == 9.2
		assert adp[0].season == 2013

		assert adp[1].player.name == "Drew Brees"
		assert adp[1].adp == 12.9
		assert adp[1].season == 2013

		assert adp[2].player.name == "Peyton Manning"
		assert adp[2].adp == 15.1
		assert adp[2].season == 2013

		assert controller.response.text == "success"
	}

	void testSaveFromCSV_duplicate_name() {
		def rodgers = new Player(name: "Aaron Rodgers", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
		def rodgers_rb = new Player(name: "Aaron Rodgers", position: Player.POSITION_RB, stats: [], averageDraftPositions: []).save(flush: true)
		def brees = new Player(name: "Drew Brees", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)
		def manning = new Player(name: "Peyton Manning", position: Player.POSITION_QB, stats: [], averageDraftPositions: []).save(flush: true)

		params.input = "Aaron Rodgers,GB,QB,9.2\n" +
				"Drew Brees,NO,QB,12.9\n" +
				"Peyton Manning,Den,QB,15.1"
		params.season = "2013"

		controller.saveFromCSV()

		def adp = AverageDraftPosition.list()

		assert adp[0].player.name == "Drew Brees"
		assert adp[0].adp == 12.9
		assert adp[0].season == 2013

		assert adp[1].player.name == "Peyton Manning"
		assert adp[1].adp == 15.1
		assert adp[1].season == 2013

		assert controller.response.text == "The following players were duplicates:\nAaron Rodgers\n"
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

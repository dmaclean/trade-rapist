package com.traderapist.models

import com.traderapist.scoringsystem.IFantasyScoringSystem
import org.junit.*
import grails.test.mixin.*

@TestFor(PlayerController)
@Mock([Player, Stat])
class PlayerControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
        params["name"] = "Dan"
        params["position"] = "QB"
    }

    void testIndex() {
        controller.index()
        assert "/player/list" == response.redirectedUrl
    }

    void testList() {
	    Player.metaClass.static.getCorrelation = { position, stat, system, season1, season2 -> [] }

        def model = controller.list()

        assert model.playerInstanceList.size() == 0
        assert model.playerInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.playerInstance != null
    }

    void testSave() {
        controller.save()

        assert model.playerInstance != null
        assert view == '/player/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/player/show/1'
        assert controller.flash.message != null
        assert Player.count() == 1
    }

	/*
	 * Controller.show uses a stat query that uses executeQuery.  Therefore, we
	 * need to move this into an integration test for it to passed.
	 */
//    void testShow() {
//        controller.show()
//
//        assert flash.message != null
//        assert response.redirectedUrl == '/player/list'
//
//        populateValidParams(params)
//        def player = new Player(params)
//
//        assert player.save() != null
//
//        params.id = player.id
//
//        def model = controller.show()
//
//        assert model.playerInstance == player
//    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/player/list'

        populateValidParams(params)
        def player = new Player(params)

        assert player.save() != null

        params.id = player.id

        def model = controller.edit()

        assert model.playerInstance == player
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/player/list'

        response.reset()

        populateValidParams(params)
        def player = new Player(params)

        assert player.save() != null

        // test invalid parameters in update
        params.id = player.id
        //TODO: add invalid values to params object
        params.name = ""
        params.position = ""

        controller.update()

        assert view == "/player/edit"
        assert model.playerInstance != null

        player.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/player/show/$player.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        player.clearErrors()

        populateValidParams(params)
        params.id = player.id
        params.version = -1
        controller.update()

        assert view == "/player/edit"
        assert model.playerInstance != null
        //assert model.playerInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/player/list'

        response.reset()

        populateValidParams(params)
        def player = new Player(params)

        assert player.save() != null
        assert Player.count() == 1

        params.id = player.id

        controller.delete()

        assert Player.count() == 0
        assert Player.get(player.id) == null
        assert response.redirectedUrl == '/player/list'
    }
}

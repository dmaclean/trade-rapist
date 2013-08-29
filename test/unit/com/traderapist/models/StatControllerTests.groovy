package com.traderapist.models

import com.traderapist.constants.FantasyConstants
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.junit.After
import org.junit.Before

@TestFor(StatController)
@Mock([Player,Stat])
class StatControllerTests {

    Player player

    @Before
    void setUp() {
        player = new Player(name: "Dan MacLean", position: "QB")
        player.save(flush: true)
    }

    @After
    void tearDown() {
	    player = null
    }

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
        params["player"] = player
        params["season"] = 2001
        params["week"] = 1
        params["statKey"] = FantasyConstants.STAT_COMPLETIONS
        params["statValue"] = 30
    }

    void testIndex() {
        controller.index()
        assert "/stat/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.statInstanceList.size() == 0
        assert model.statInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.statInstance != null
    }

    void testSave() {
        controller.save()

        assert model.statInstance != null
        assert view == '/stat/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/stat/show/1'
        assert controller.flash.message != null
        assert Stat.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/stat/list'

        populateValidParams(params)
        def stat = new Stat(params)

        assert stat.save() != null

        params.id = stat.id

        def model = controller.show()

        assert model.statInstance == stat
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/stat/list'

        populateValidParams(params)
        def stat = new Stat(params)

        assert stat.save() != null

        params.id = stat.id

        def model = controller.edit()

        assert model.statInstance == stat
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/stat/list'

        response.reset()

        populateValidParams(params)
        def stat = new Stat(params)

        assert stat.save() != null

        // test invalid parameters in update
        params.id = stat.id
        //TODO: add invalid values to params object
        params.week = null
        params.season = null

        controller.update()

        assert view == "/stat/edit"
        assert model.statInstance != null

        stat.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/stat/show/$stat.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        stat.clearErrors()

        populateValidParams(params)
        params.id = stat.id
        params.version = -1
        controller.update()

        assert view == "/stat/edit"
        assert model.statInstance != null
       // assert model.statInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/stat/list'

        response.reset()

        populateValidParams(params)
        def stat = new Stat(params)

        assert stat.save() != null
        assert Stat.count() == 1

        params.id = stat.id

        controller.delete()

        assert Stat.count() == 0
        assert Stat.get(stat.id) == null
        assert response.redirectedUrl == '/stat/list'
    }

	void testDumpToCSV() {
		controller.dumpToCSV()
		assert response.text == "done"
	}
}

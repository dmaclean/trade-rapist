package com.traderapist.models



import org.junit.*
import grails.test.mixin.*

import com.traderapist.constants.FantasyConstants

@TestFor(FantasyPointsController)
@Mock([FantasyPoints,Player,Stat])
class FantasyPointsControllerTests {

    Player player

    @Before
    void setUp() {
        player = new Player(name: "Dan MacLean", position: "QB")
        player.save(flush: true)
    }

    @After
    void tearDown() {

    }

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
        params["season"] = 2001
        params["week"] = 4
        params["points"] = 100
        params["system"] = 'ESPNStandardScoringSystem'
        params["player"] = player
    }

    void testIndex() {
        controller.index()
        assert "/fantasyPoints/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.fantasyPointsInstanceList.size() == 0
        assert model.fantasyPointsInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.fantasyPointsInstance != null
    }

    void testGeneratePoints_Season() {
        params["system"] = "ESPNStandardScoringSystem"

        Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100)
        s1.save(flush: true)
        Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2)
        s2.save(flush: true)

        controller.generatePoints()

        FantasyPoints fp = FantasyPoints.findBySeason(2001)

        assertTrue "Season is not 2001", fp.season == 2001
        assertTrue "Week is not -1", fp.week == -1
        assertTrue "Points is not 12", fp.points == 12
    }

    void testSave() {
        controller.save()

        assert model.fantasyPointsInstance != null
        assert view == '/fantasyPoints/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/fantasyPoints/show/1'
        assert controller.flash.message != null
        assert FantasyPoints.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/fantasyPoints/list'

        populateValidParams(params)
        def fantasyPoints = new FantasyPoints(params)

        assert fantasyPoints.save() != null

        params.id = fantasyPoints.id

        def model = controller.show()

        assert model.fantasyPointsInstance == fantasyPoints
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/fantasyPoints/list'

        populateValidParams(params)
        def fantasyPoints = new FantasyPoints(params)

        assert fantasyPoints.save() != null

        params.id = fantasyPoints.id

        def model = controller.edit()

        assert model.fantasyPointsInstance == fantasyPoints
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/fantasyPoints/list'

        response.reset()

        populateValidParams(params)
        def fantasyPoints = new FantasyPoints(params)

        assert fantasyPoints.save() != null

        // test invalid parameters in update
        params.id = fantasyPoints.id
        //TODO: add invalid values to params object
        params.week = -10

        controller.update()

        assert view == "/fantasyPoints/edit"
        assert model.fantasyPointsInstance != null

        fantasyPoints.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/fantasyPoints/show/$fantasyPoints.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        fantasyPoints.clearErrors()

        populateValidParams(params)
        params.id = fantasyPoints.id
        params.version = -1
        controller.update()

        assert view == "/fantasyPoints/edit"
        assert model.fantasyPointsInstance != null
        //assert model.fantasyPointsInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/fantasyPoints/list'

        response.reset()

        populateValidParams(params)
        def fantasyPoints = new FantasyPoints(params)

        assert fantasyPoints.save() != null
        assert FantasyPoints.count() == 1

        params.id = fantasyPoints.id

        controller.delete()

        assert FantasyPoints.count() == 0
        assert FantasyPoints.get(fantasyPoints.id) == null
        assert response.redirectedUrl == '/fantasyPoints/list'
    }
}

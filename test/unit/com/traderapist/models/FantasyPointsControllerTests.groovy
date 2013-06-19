package com.traderapist.models



import org.junit.*
import grails.test.mixin.*

import com.traderapist.constants.FantasyConstants

@TestFor(FantasyPointsController)
@Mock([FantasyPoints,Player,Stat])
class FantasyPointsControllerTests {

    Player player

	def sessionFactory

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
	    params["projection"] = false
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

        def fps = FantasyPoints.findAllBySeason(2001)

        assertTrue "Should have found one FantasyPoints object for 2001", fps.size() == 1

        def fp = fps[0]

        assertTrue "Season is not 2001", fp.season == 2001
        assertTrue "Week is not -1", fp.week == -1
        assertTrue "Points is not 12", fp.points == 12
    }

    void testGeneratePoints_Season_Existing() {
        params["system"] = "ESPNStandardScoringSystem"

        Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100)
        s1.save(flush: true)
        Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2)
        s2.save(flush: true)

        controller.generatePoints()

        def fps = FantasyPoints.findAllBySeason(2001)

        assertTrue "Should have found one FantasyPoints object for 2001", fps.size() == 1

        def fp = fps[0]

        assertTrue "Season is not 2001", fp.season == 2001
        assertTrue "Week is not -1", fp.week == -1
        assertTrue "Points is not 12", fp.points == 12

        /*
        Make sure we didn't write a duplicate
         */
        controller.generatePoints()

        fps = FantasyPoints.findAllBySeason(2001)

        assertTrue "Should have found one FantasyPoints object for 2001", fps.size() == 1

        assertTrue "Season is not 2001", fps[0].season == 2001
        assertTrue "Week is not -1", fps[0].week == -1
        assertTrue "Points is not 12", fps[0].points == 12
    }

	void testGeneratePoints_PositionQB_Season() {
		params["system"] = "ESPNStandardScoringSystem"
		params["position"] = "QB"

		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100)
		s1.save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2)
		s2.save(flush: true)

		controller.generatePoints()

		def fps = FantasyPoints.findAllBySeason(2001)

		assertTrue "Should have found one FantasyPoints object for 2001", fps.size() == 1

		def fp = fps[0]

		assertTrue "Season is not 2001", fp.season == 2001
		assertTrue "Week is not -1", fp.week == -1
		assertTrue "Points is not 12", fp.points == 12
	}

	void testGeneratePoints_PositionRB_Season() {
		params["system"] = "ESPNStandardScoringSystem"
		params["position"] = "RB"

		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100)
		s1.save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2)
		s2.save(flush: true)

		controller.generatePoints()

		def fps = FantasyPoints.findAllBySeason(2001)

		assertTrue "Should have found zero FantasyPoints object for 2001", fps.size() == 0
	}

	void testProjectPoints_BadSystem() {
		params["system"] = "Nonsense"
		params["num_startable"] = "1"
		params["season"] = "2013"
		params["num_owners"] = "3"

		controller.projectPoints()
		assert response.text == "Invalid scoring system - Nonsense"
	}

	void testProjectPoints_BadSeason() {
		params["system"] = "ESPNStandardScoringSystem"
		params["num_startable"] = "1"
		params["num_owners"] = "3"

		controller.projectPoints()
		assert response.text == "season must be an integer"

		response.reset()

		params["season"] = "not an integer"
		controller.projectPoints()
		assert response.text == "season must be an integer"
	}

	void testProjectPoints_BadNumStartable() {
		params["system"] = "ESPNStandardScoringSystem"
		params["season"] = "2013"
		params["num_owners"] = "3"

		controller.projectPoints()
		assert response.text == "num_startable must be an integer"

		response.reset()

		params["num_startable"] = "not an integer"
		controller.projectPoints()
		assert response.text == "num_startable must be an integer"
	}

	void testProjectPoints_BadNumOwners() {
		params["system"] = "ESPNStandardScoringSystem"
		params["num_startable"] = "1"
		params["season"] = "2013"

		controller.projectPoints()
		assert response.text == "num_owners must be an integer"

		response.reset()

		params["num_owners"] = "not an integer"
		controller.projectPoints()
		assert response.text == "num_owners must be an integer"
	}

	void testProjectPoints_Season() {
		params["system"] = "ESPNStandardScoringSystem"
		params["season"] = "2002"
		params["position"] = "QB"
		params["num_startable"] = "1"
		params["num_owners"] = "1"

		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s3 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_INTERCEPTIONS, statValue: 2).save(flush: true)
		Stat s4 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s5 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 2).save(flush: true)

		controller.projectPoints()

		def fps = FantasyPoints.findAllBySeason(2002)

		assertTrue "Should have found one FantasyPoints object for 2002", fps.size() == 1

		def fp = fps[0]

		assertTrue "Season is not 2002", fp.season == 2002
		assertTrue "Week is not -1", fp.week == -1
		assertTrue "Points is not 20.2", fp.points == 20.2
		assertTrue "Projection is not true", fp.projection
	}

	void testProjectPoints_Season_Existing() {
		params["system"] = "ESPNStandardScoringSystem"
		params["season"] = "2002"
		params["position"] = "QB"
		params["num_startable"] = "1"
		params["num_owners"] = "1"

		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s3 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_INTERCEPTIONS, statValue: 2).save(flush: true)
		Stat s4 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s5 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 2).save(flush: true)

		controller.projectPoints()

		def fps = FantasyPoints.findAllBySeason(2002)

		assertTrue "Should have found one FantasyPoints object for 2002", fps.size() == 1

		def fp = fps[0]

		assertTrue "Season is not 2002", fp.season == 2002
		assertTrue "Week is not -1", fp.week == -1
		assertTrue "Points is not 20.2", fp.points == 20.2
		assertTrue "Projection is not true", fp.projection

		/*
		Make sure we didn't write a duplicate
		 */
		controller.projectPoints()

		fps = FantasyPoints.findAllBySeason(2002)

		assertTrue "Should have found one FantasyPoints object for 2002", fps.size() == 1

		assertTrue "Season is not 2002", fps[0].season == 2002
		assertTrue "Week is not -1", fps[0].week == -1
		assertTrue "Points is not 20.2", fps[0].points == 20.2
		assertTrue "Projection is not true", fp.projection
	}

	void testProjectPoints_PositionQB_Season() {
		params["system"] = "ESPNStandardScoringSystem"
		params["position"] = "QB"
		params["season"] = "2002"
		params["num_startable"] = "1"
		params["num_owners"] = "1"

		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s3 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_INTERCEPTIONS, statValue: 2).save(flush: true)
		Stat s4 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s5 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 2).save(flush: true)

		controller.projectPoints()

		def fps = FantasyPoints.findAllBySeason(2002)

		assertTrue "Should have found one FantasyPoints object for 2002", fps.size() == 1

		def fp = fps[0]

		assertTrue "Season is not 2002", fp.season == 2002
		assertTrue "Week is not -1", fp.week == -1
		assertTrue "Points is not 20.2", fp.points == 20.2
		assertTrue "Projection is not true", fp.projection
	}

	void testProjectPoints_PositionRB_Season() {
		params["system"] = "ESPNStandardScoringSystem"
		params["position"] = "RB"

		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 100).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s3 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_INTERCEPTIONS, statValue: 2).save(flush: true)
		Stat s4 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Stat s5 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_RUSHING_YARDS, statValue: 2).save(flush: true)

		controller.projectPoints()

		def fps = FantasyPoints.findAllBySeason(2002)

		assertTrue "Should have found zero FantasyPoints object for 2002", fps.size() == 0
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

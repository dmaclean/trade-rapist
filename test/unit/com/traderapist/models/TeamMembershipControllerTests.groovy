package com.traderapist.models



import org.junit.*
import grails.test.mixin.*

@TestFor(TeamMembershipController)
@Mock([TeamMembership, Player, Team])
class TeamMembershipControllerTests {

	Team team
	Player player

	void setUp() {
		player = new Player(name: "Dan MacLean", position: "QB").save()
		team = new Team(city: "New England", name: "Patriots", abbreviation: "NE").save()
	}

	void tearDown() {

	}

    def populateValidParams(params) {
        assert params != null
        params["player"] = player
	    params["team"] = team
	    params["season"] = 2001
    }

    void testIndex() {
        controller.index()
        assert "/teamMembership/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.teamMembershipInstanceList.size() == 0
        assert model.teamMembershipInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.teamMembershipInstance != null
    }

    void testSave() {
        controller.save()

        assert model.teamMembershipInstance != null
        assert view == '/teamMembership/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/teamMembership/show/1'
        assert controller.flash.message != null
        assert TeamMembership.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/teamMembership/list'

        populateValidParams(params)
        def teamMembership = new TeamMembership(params)

        assert teamMembership.save() != null

        params.id = teamMembership.id

        def model = controller.show()

        assert model.teamMembershipInstance == teamMembership
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/teamMembership/list'

        populateValidParams(params)
        def teamMembership = new TeamMembership(params)

        assert teamMembership.save() != null

        params.id = teamMembership.id

        def model = controller.edit()

        assert model.teamMembershipInstance == teamMembership
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/teamMembership/list'

        response.reset()

        populateValidParams(params)
        def teamMembership = new TeamMembership(params)

        assert teamMembership.save() != null

        // test invalid parameters in update
        params.id = teamMembership.id
        //TODO: add invalid values to params object
	    params["season"] = 2000

        controller.update()

        assert view == "/teamMembership/edit"
        assert model.teamMembershipInstance != null

        teamMembership.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/teamMembership/show/$teamMembership.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        teamMembership.clearErrors()

        populateValidParams(params)
        params.id = teamMembership.id
        params.version = -1
        controller.update()

        assert view == "/teamMembership/edit"
        assert model.teamMembershipInstance != null
        assert model.teamMembershipInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/teamMembership/list'

        response.reset()

        populateValidParams(params)
        def teamMembership = new TeamMembership(params)

        assert teamMembership.save() != null
        assert TeamMembership.count() == 1

        params.id = teamMembership.id

        controller.delete()

        assert TeamMembership.count() == 0
        assert TeamMembership.get(teamMembership.id) == null
        assert response.redirectedUrl == '/teamMembership/list'
    }
}

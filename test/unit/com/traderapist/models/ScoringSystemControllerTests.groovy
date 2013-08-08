package com.traderapist.models

import com.traderapist.constants.FantasyConstants
import com.traderapist.security.User
import org.junit.*
import grails.test.mixin.*

@TestFor(ScoringSystemController)
@Mock([FantasyPointsJob, FantasyTeam, ScoringSystem, ScoringRule, User, FantasyLeagueType])
class ScoringSystemControllerTests {

	def fantasyTeam
	def user
	def fantasyLeagueType

	@Before
	void setUp() {
		User.metaClass.encodePassword = { -> "password" }

		user = new User(username: "Dan", password: "password").save(flush: true)
		fantasyLeagueType = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
		fantasyTeam = new FantasyTeam(name: "MyFantasyTeam", leagueId: "111", season: 2013, user: user, fantasyLeagueType: fantasyLeagueType, numOwners: 10, fantasyTeamStarters: [])
		fantasyTeam.validate()
		fantasyTeam.save(flush: true)
	}

	@After
	void tearDown() {
		fantasyTeam = null
		user = null
	}

	def populateValidParams(params) {
		assert params != null
		// TODO: Populate valid properties like...
		params["name"] = 'someValidName'
		params["fantasyTeam"] = fantasyTeam
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

	void testCreateSystemAndRules_NewSystemAndRules() {
		controller.params.ss_name = "Test System"
		controller.params.fantasy_team_id = fantasyTeam.id.toString()
		controller.params.stat_multiplier_1 = "1"
		controller.params.stat_multiplier_2 = "2"
		controller.params.stat_multiplier_3 = "3"

		controller.createSystemAndRules()

		assert response.text == "success"
		assert ScoringRule.list().size() == 3
		assert ScoringSystem.list().size() == 1

		assert flash.info == "Scoring system ${ controller.params.ss_name } successfully created!"

		def jobs = FantasyPointsJob.list()
		def cal = Calendar.getInstance()
		def currentYear = cal.get(Calendar.YEAR)
		def seasons = [2001..currentYear]
		def results = [:]

		seasons.each {    season ->
			[-1,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17].each {   week ->
				results["${ season }_${ week }_false"] = false
			}
		}
		results["${ currentYear }_-1_true"] = false

		jobs.each {     job ->
			results["${ job.season }_${ job.week }_${ job.projection }"] = true
			assert "Expected job to be marked as incomplete", !job.completed
		}

		results.each {  key, value ->
			assert "Found entry ${ key } to be ungenerated", value
		}
	}

	void testCreateSystemAndRules_NewSystemExistingRules() {
		def rule1 = new ScoringRule(statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, multiplier: 6).save(flush: true)
		def rule2 = new ScoringRule(statKey: FantasyConstants.STAT_PASSING_YARDS, multiplier: 0.4).save(flush: true)

		controller.params.ss_name = "Test System"
        controller.params.fantasy_team_id = fantasyTeam.id.toString()
		controller.params.stat_multiplier_5 = "6"     // passing touchdowns
		controller.params.stat_multiplier_4 = "0.4"   // passing yards

		controller.createSystemAndRules()

		assert response.text == "success"
		assert ScoringRule.list().size() == 2
		assert ScoringSystem.list().size() == 1

		assert flash.info == "Scoring system ${ controller.params.ss_name } successfully created!"
	}

	void testCreateSystemAndRules_NewSystemSomeExistingRules() {
		def rule1 = new ScoringRule(statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, multiplier: 4).save(flush: true)
		def rule2 = new ScoringRule(statKey: FantasyConstants.STAT_PASSING_YARDS, multiplier: 0.4).save(flush: true)

		controller.params.ss_name = "Test System"
        controller.params.fantasy_team_id = fantasyTeam.id.toString()
		controller.params.stat_multiplier_1 = "1"
		controller.params.stat_multiplier_4 = "0.4"   // passing yards

		controller.createSystemAndRules()

		assert response.text == "success"
		assert ScoringRule.list().size() == 3
		assert ScoringSystem.list().size() == 1

		assert flash.info == "Scoring system ${ controller.params.ss_name } successfully created!"
	}

	void testCreateSystemAndRules_ErrorCreatingScoringSystem() {
		controller.createSystemAndRules()

		assert response.text == "error: Unable to save scoring system."
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

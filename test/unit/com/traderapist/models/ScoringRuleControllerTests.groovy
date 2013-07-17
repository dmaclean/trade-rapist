package com.traderapist.models

import com.traderapist.constants.FantasyConstants
import org.junit.*
import grails.test.mixin.*

@TestFor(ScoringRuleController)
@Mock(ScoringRule)
class ScoringRuleControllerTests {

	def populateValidParams(params) {
		assert params != null
		// TODO: Populate valid properties like...
		params["statKey"] = FantasyConstants.STAT_DEFENSIVE_TOUCHDOWN
		params["multiplier"] = 1
	}

	void testIndex() {
		controller.index()
		assert "/scoringRule/list" == response.redirectedUrl
	}

	void testList() {

		def model = controller.list()

		assert model.scoringRuleInstanceList.size() == 0
		assert model.scoringRuleInstanceTotal == 0
	}

	void testCreate() {
		def model = controller.create()

		assert model.scoringRuleInstance != null
	}

	void testSave() {
		controller.save()

		assert model.scoringRuleInstance != null
		assert view == '/scoringRule/create'

		response.reset()

		populateValidParams(params)
		controller.save()

		assert response.redirectedUrl == '/scoringRule/show/1'
		assert controller.flash.message != null
		assert ScoringRule.count() == 1
	}

	void testShow() {
		controller.show()

		assert flash.message != null
		assert response.redirectedUrl == '/scoringRule/list'

		populateValidParams(params)
		def scoringRule = new ScoringRule(params)

		assert scoringRule.save() != null

		params.id = scoringRule.id

		def model = controller.show()

		assert model.scoringRuleInstance == scoringRule
	}

	void testEdit() {
		controller.edit()

		assert flash.message != null
		assert response.redirectedUrl == '/scoringRule/list'

		populateValidParams(params)
		def scoringRule = new ScoringRule(params)

		assert scoringRule.save() != null

		params.id = scoringRule.id

		def model = controller.edit()

		assert model.scoringRuleInstance == scoringRule
	}

	void testUpdate() {
		controller.update()

		assert flash.message != null
		assert response.redirectedUrl == '/scoringRule/list'

		response.reset()

		populateValidParams(params)
		def scoringRule = new ScoringRule(params)

		assert scoringRule.save() != null

		// test invalid parameters in update
		params.id = scoringRule.id
		params["statKey"] = null

		controller.update()

		assert view == "/scoringRule/edit"
		assert model.scoringRuleInstance != null

		scoringRule.clearErrors()

		populateValidParams(params)
		controller.update()

		assert response.redirectedUrl == "/scoringRule/show/$scoringRule.id"
		assert flash.message != null

		//test outdated version number
		response.reset()
		scoringRule.clearErrors()

		populateValidParams(params)
		params.id = scoringRule.id
		params.version = -1
		controller.update()

		assert view == "/scoringRule/edit"
		assert model.scoringRuleInstance != null
		assert model.scoringRuleInstance.errors.getFieldError('version')
		assert flash.message != null
	}

	void testDelete() {
		controller.delete()
		assert flash.message != null
		assert response.redirectedUrl == '/scoringRule/list'

		response.reset()

		populateValidParams(params)
		def scoringRule = new ScoringRule(params)

		assert scoringRule.save() != null
		assert ScoringRule.count() == 1

		params.id = scoringRule.id

		controller.delete()

		assert ScoringRule.count() == 0
		assert ScoringRule.get(scoringRule.id) == null
		assert response.redirectedUrl == '/scoringRule/list'
	}
}

package com.traderapist.models

import com.traderapist.constants.FantasyConstants
import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ScoringRule)
class ScoringRuleTests {

	@Before
	void setUp() {
		mockForConstraintsTests(ScoringRule)
	}

	void testStatKeyNotNullable() {
		def rule = new ScoringRule(statKey: null, multiplier: 1)

		assertFalse rule.validate()

		assertTrue "nullable" == rule.errors["statKey"]
	}

	void testStatKeyNotNegative() {
		def rule = new ScoringRule(statKey: -1, multiplier: 1)

		assertFalse rule.validate()

		assertTrue "min" == rule.errors["statKey"]
	}

	void testMultiplierNotNullable() {
		def rule = new ScoringRule(statKey: FantasyConstants.STAT_COMPLETIONS, multiplier: null)

		assertFalse rule.validate()

		assertTrue "nullable" == rule.errors["multiplier"]
	}
}

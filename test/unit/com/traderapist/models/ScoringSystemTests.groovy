package com.traderapist.models



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ScoringSystem)
class ScoringSystemTests {

	@Before
	void setUp() {
		mockForConstraintsTests(ScoringSystem)
	}

	void testNameNotNull() {
		def system = new ScoringSystem()

		assertFalse system.validate()

		assertTrue "nullable" == system.errors["name"]
	}
}

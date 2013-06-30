package com.traderapist.models



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(FantasyLeagueType)
class FantasyLeagueTypeTests {

	void testCodeNotNullable() {
		FantasyLeagueType flt = new FantasyLeagueType(description: "Something")

		mockForConstraintsTests(FantasyLeagueType, [flt])

		assertFalse "Validation should have been false", flt.validate()

		assert "nullable" == flt.errors["code"]
	}

	void testDescriptionNotNullable() {
		FantasyLeagueType flt = new FantasyLeagueType(code: "Something")

		mockForConstraintsTests(FantasyLeagueType, [flt])

		assertFalse "Validation should have been false", flt.validate()

		assert "nullable" == flt.errors["description"]
	}
}

package com.traderapist.models



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Team)
class TeamTests {

	void setUp() {
		mockForConstraintsTests(Team)
	}

	void tearDown() {

	}

    void testNotNullable() {
        def team = new Team(city: null, name: null, abbreviation: null)

	    assertFalse("The team should not have passed validation", team.validate())
	    assert "nullable" == team.errors["city"]
	    assert "nullable" == team.errors["name"]
	    assert "nullable" == team.errors["abbreviation"]
    }

	void testNotBlank() {
		def team = new Team(city: "", name: "", abbreviation: "")

		assertFalse("The team should not have passed validation", team.validate())
		assert "blank" == team.errors["city"]
		assert "blank" == team.errors["name"]
		assert "blank" == team.errors["abbreviation"]
	}

	void testSuccess() {
		def team = new Team(city: "New England", name: "Patriots", abbreviation: "NE")

		assertTrue("The team should have passed validation", team.validate())
	}
}

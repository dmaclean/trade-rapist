package com.traderapist.models



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(AverageDraftPosition)
@Mock(Player)
class AverageDraftPositionTests {

	void setUp() {
		mockForConstraintsTests(AverageDraftPosition)
	}

    void testMin() {
	    def adp = new AverageDraftPosition(adp: 0.9, season: 2000)

	    assertFalse("ADP validation should have failed.", adp.validate())
	    assert "min" == adp.errors["adp"]
	    assert "min" == adp.errors["season"]
    }

	void testNullable() {
		def adp = new AverageDraftPosition(adp: null, season: null)

		assertFalse("ADP validation should have failed.", adp.validate())
		assert "nullable" == adp.errors["adp"]
		assert "nullable" == adp.errors["season"]
	}

	void testSuccess() {
		def player = new Player(name: "Dan MacLean", position: "QB").save()
		def adp = new AverageDraftPosition(player: player, adp: 1.0, season: 2001)
		assertTrue("ADP validation should not have failed.", adp.validate())
	}
}

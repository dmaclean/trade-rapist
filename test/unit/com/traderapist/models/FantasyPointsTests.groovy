package com.traderapist.models

import com.traderapist.scoringsystem.ESPNStandardScoringSystem
import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(FantasyPoints)
@Mock(Player)
class FantasyPointsTests {

    Player player

    @Before
    void setUp() {
        player = new Player(name: "Dan", position: "QB")
        player.save(flush: true)
    }

    void testSeasonNotNull() {
        FantasyPoints fp = new FantasyPoints(week: 1, points: 1)

        mockForConstraintsTests(FantasyPoints, [fp])

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "nullable" == fp.errors["season"]
    }

    void testWeekNotNull() {
        FantasyPoints fp = new FantasyPoints(season: 2001, points: 1)

        mockForConstraintsTests(FantasyPoints, [fp])

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "nullable" == fp.errors["week"]
    }

    void testWeekInRange() {
        FantasyPoints fp = new FantasyPoints(season: 2001, week: -2, points: 1)

        mockForConstraintsTests(FantasyPoints, [fp])

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "range" == fp.errors["week"]

        fp.week = 18

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "range" == fp.errors["week"]
    }

    void testPointsNotNull() {
        FantasyPoints fp = new FantasyPoints(week: 1, season: 2001)

        mockForConstraintsTests(FantasyPoints, [fp])

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "nullable" == fp.errors["points"]
    }

    void testSystemNotNull() {
        FantasyPoints fp = new FantasyPoints(system: null)

        mockForConstraintsTests(FantasyPoints, [fp])

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "nullable" == fp.errors["system"]
    }

	void testProjectionDefaultFalse() {
		def player = new Player(id: 1, name: "Dan", position: "QB")
		FantasyPoints fp = new FantasyPoints(player: player, season: 2013, week: -1, system: ESPNStandardScoringSystem.class.getName(), points: 100)

		mockForConstraintsTests(FantasyPoints, [fp])

		assertTrue fp.validate()
	}
}

package com.traderapist.models

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(FantasyPoints)
@Mock([Player, ScoringSystem])
class FantasyPointsTests {

    Player player
	ScoringSystem scoringSystem

    @Before
    void setUp() {
        player = new Player(name: "Dan", position: "QB")
        player.save(flush: true)

	    scoringSystem = new ScoringSystem(name: "My Scoring System", scoringRules: new HashSet<ScoringRule>()).save(flush: true)

	    mockForConstraintsTests(FantasyPoints)
    }

    void testSeasonNotNull() {
        FantasyPoints fp = new FantasyPoints(week: 1, points: 1, scoringSystem: scoringSystem)

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "nullable" == fp.errors["season"]
    }

    void testWeekNotNull() {
        FantasyPoints fp = new FantasyPoints(season: 2001, points: 1, scoringSystem: scoringSystem)

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "nullable" == fp.errors["week"]
    }

    void testWeekInRange() {
        FantasyPoints fp = new FantasyPoints(season: 2001, week: -2, points: 1, scoringSystem: scoringSystem)

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "range" == fp.errors["week"]

        fp.week = 18

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "range" == fp.errors["week"]
    }

    void testPointsNotNull() {
        FantasyPoints fp = new FantasyPoints(week: 1, season: 2001, scoringSystem: scoringSystem)

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "nullable" == fp.errors["points"]
    }

    void testScoringSystemNotNull() {
        FantasyPoints fp = new FantasyPoints(week: 1, season: 2001, points: 10)

        assertFalse "The FantasyPoints validation should have failed", fp.validate()

        assert "nullable" == fp.errors["scoringSystem"]
    }

	void testProjectionDefaultFalse() {
		def player = new Player(id: 1, name: "Dan", position: "QB")
		FantasyPoints fp = new FantasyPoints(player: player, season: 2013, week: -1, scoringSystem: scoringSystem, points: 100)

		assertTrue fp.validate()
	}

	void testNumStartableNullable() {
		def player = new Player(id: 1, name: "Dan", position: "QB")
		FantasyPoints fp = new FantasyPoints(
				player: player,
				season: 2013,
				week: -1,
				points: 100,
				scoringSystem: scoringSystem,
				projection: true,
				numOwners: 10
		)

		assertTrue fp.validate()
	}

	void testNumOwnersNullable() {
		def player = new Player(id: 1, name: "Dan", position: "QB")
		FantasyPoints fp = new FantasyPoints(
				player: player,
				season: 2013,
				week: -1,
				points: 100,
				scoringSystem: scoringSystem,
				projection: true,
				numStartable: 1
		)

		assertTrue fp.validate()
	}
}

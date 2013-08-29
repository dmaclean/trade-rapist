package com.traderapist.models

import com.traderapist.constants.FantasyConstants
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Stat)
@Mock([Player,Stat])
class StatTests {

    void testSeasonNotNull() {
        def stat = new Stat()

        mockForConstraintsTests(Stat, [stat])

        assertFalse "Stat with null season passed validation", stat.validate()

        assertEquals "nullable", stat.errors["season"]
    }

    void testWeekNotNull() {
        def stat = new Stat(week: null)

        mockForConstraintsTests(Stat, [stat])

        assertFalse "Stat with null week passed validation", stat.validate()

        assertEquals "nullable", stat.errors["week"]
    }

    void testWeekBetween1and17() {
        def stat = new Stat(week: -2)

        mockForConstraintsTests(Stat, [stat])

        assertFalse "Stat with week outside 1-17 passed validation", stat.validate()

        assertEquals "range", stat.errors["week"]

        stat.week = 18

        assertFalse "Stat with week outside 1-17 passed validation", stat.validate()

        assertEquals "range", stat.errors["week"]
    }

    void testStatKeyNotNull() {
        def stat = new Stat(statKey: null)

        mockForConstraintsTests(Stat, [stat])

        assertFalse "Stat with null statKey passed validation", stat.validate()

        assertEquals "nullable", stat.errors["statKey"]
    }

    void testStatValueNotNull() {
        def stat = new Stat(statValue: null)

        mockForConstraintsTests(Stat, [stat])

        assertFalse "Stat with null statValue passed validation", stat.validate()

        assertEquals "nullable", stat.errors["statValue"]
    }

    void testStatKeyTranslation_PassCompletions() {
        def statCompletions = new Stat(statKey: FantasyConstants.STAT_COMPLETIONS, statValue: 30)
        def statIncompletions = new Stat(statKey: FantasyConstants.STAT_INCOMPLETE_PASSES, statValue: 30)
        def statPassingYards = new Stat(statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 30)

        assertEquals "Completions", statCompletions.translateStatKey()
        assertEquals "Incomplete Passes", statIncompletions.translateStatKey()
        assertEquals "Passing Yards", statPassingYards.translateStatKey()
    }

	void testDumpToCSV() {
		def player = new Player(name: "Test QB", position: Player.POSITION_QB, averageDraftPositions: [], stats: []).save(flush: true)
		def stat = new Stat(player: player, season: 2012, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 10).save(flush: true)

		def csv = Stat.dumpToCSV(player)

		assert csv == "1,Test QB,QB,2012,-1,0,0,0,0,0,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\n"
	}
}

package com.traderapist.models



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Stat)
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
        def stat = new Stat(week: 0)

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
}

package com.traderapist.nba



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Injury)
@Mock(Player)
class InjuryTests {

    void testInjuryDateNotNullable() {
        def player = new Player()
        def injury = new Injury(player: player, returnDate: new Date(), details: "test")

        assertTrue(!injury.validate())

        injury.injuryDate = new Date()
        assertTrue(injury.validate())
    }

    void testReturnDateNotNullable() {
        def player = new Player()
        def injury = new Injury(player: player, injuryDate: new Date(), details: "test")

        assertTrue(!injury.validate())

        injury.returnDate = new Date()
        assertTrue(injury.validate())
    }
}

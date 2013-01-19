package com.traderapist.models



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Player)
class PlayerTests {

   void testNameNotBlank() {
        def player = new Player(name:"")

        mockForConstraintsTests(Player, [player])

        assertFalse "The player validation should have failed", player.validate()

        assert "blank" == player.errors["name"]
    }

    void testPositionNotBlank() {
        def player = new Player(position:"")

        mockForConstraintsTests(Player, [player])

        assertFalse "The player validation should have failed", player.validate()

        assert "blank" == player.errors["position"]
    }
}

package com.traderapist.nba



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Player)
class PlayerTests {

    void testNameNotNullable() {
        def player = new Player(position: "PG", rgPosition: "PG", height: 69, weight: 170, url: "something")
        assertTrue(!player.validate())

        player.name = "Dan"
        assertTrue(player.validate())
    }

    void testPositionNotNullable() {
        def player = new Player(name: "Dan", rgPosition: "PG", height: 69, weight: 170, url: "something")
        assertTrue(!player.validate())

        player.position = "PG"
        assertTrue(player.validate())
    }

    void testPositionInList() {
        def player = new Player(name: "Dan", position: "not in list", rgPosition: "PG", height: 69, weight: 170, url: "something")
        assertTrue(!player.validate())

        player.position = "PG"
        assertTrue(player.validate())
    }

    void testRgPositionNotNullable() {
        def player = new Player(name: "Dan", position: "PG", height: 69, weight: 170, url: "something")
        assertTrue(!player.validate())

        player.rgPosition = "PG"
        assertTrue(player.validate())
    }

    void testRgPositionInList() {
        def player = new Player(name: "Dan", rgPosition: "not in list", position: "PG", height: 69, weight: 170, url: "something")
        assertTrue(!player.validate())

        player.rgPosition = "PG"
        assertTrue(player.validate())
    }

    void testHeightNotNullAndPositive() {
        def player = new Player(name: "Dan", position: "PG", rgPosition: "PG", weight: 170, url: "something")
        assertTrue(!player.validate())

        player.height = -1
        assertTrue(!player.validate())

        player.height = 69
        assertTrue(player.validate())
    }

    void testWeightNotNullAndPositive() {
        def player = new Player(name: "Dan", position: "PG", rgPosition: "PG", height: 69, url: "something")
        assertTrue(!player.validate())

        player.weight = -1
        assertTrue(!player.validate())

        player.weight = 170
        assertTrue(player.validate())
    }

    void testUrlNotNullable() {
        def player = new Player(name: "Dan", position: "PG", rgPosition: "PG", height: 69, weight: 170)
        assertTrue(!player.validate())

        player.url = "something"
        assertTrue(player.validate())
    }
}

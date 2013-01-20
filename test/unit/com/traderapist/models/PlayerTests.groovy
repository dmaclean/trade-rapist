package com.traderapist.models



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Player)
@Mock([Stat])
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

//    void testAfterLoad_Season() {
//        def player = new Player(name: "Dan MacLean", position: "QB")
//        assertEquals 0, player.seasonStats.size()
//        assertEquals 0, player.seasonStatPoints.size()
//        assertEquals 0, player.weeklyStats.size()
//        assertEquals 0, player.weeklyStatPoints.size()
//        player.save(flush:true)
//
//        def stat = new Stat(player: player, season: 2001, week: -1, statKey: 4, statValue: 100)
//        stat.save(flush: true)
//
//        def player2 = Player.get(player.id)
//        assertEquals 1, player2.stats.size()
//
//        /*
//        Should have 1 season stat for 2001
//         */
//        assertTrue player2.seasonStats.size() == 1
//        assertTrue player2.seasonStats.containsKey("2001")
//        def stats = player.seasonStats["2001"]
//        assertEquals stats[0].id, stat.id
//
//        /*
//        Should contain no week stats
//         */
//        assertEquals 0, player.weeklyStats.size()
//        assertEquals 0, player.weeklyStatPoints.size()
//    }
//
//    void testAfterLoad_Weekly() {
//        def player = new Player(name: "Dan MacLean", position: "QB")
//        assertEquals 0, player.seasonStats.size()
//        assertEquals 0, player.seasonStatPoints.size()
//        assertEquals 0, player.weeklyStats.size()
//        assertEquals 0, player.weeklyStatPoints.size()
//        player.save(flush:true)
//
//        def stat = new Stat(player: player, season: 2001, week: 1, statKey: 4, statValue: 100)
//        stat.save(flush: true)
//
//        def player2 = Player.get(player.id)
//        assertEquals 1, player2.stats.size()
//
//        /*
//        Should have no season stats
//         */
//        assertEquals 0, player.seasonStats.size()
//        assertEquals 0, player.seasonStatPoints.size()
//
//        /*
//        Weekly stats should contain a map for 2001, which contains an arraylist for week 1
//         */
//        assertTrue player.weeklyStats.containsKey("2001")
//        def week1 = player.weeklyStats["2001"]["1"]
//        assertEquals week1[0].id, stat.id
//    }
}

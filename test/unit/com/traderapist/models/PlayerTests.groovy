package com.traderapist.models



import grails.test.mixin.*
import org.junit.*

import com.traderapist.constants.FantasyConstants
import com.traderapist.scoringsystem.IFantasyScoringSystem
import com.traderapist.scoringsystem.ESPNStandardScoringSystem

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Player)
@Mock([Stat,FantasyPoints])
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

    void testCalculatePoints_SeasonStats_ESPNStandardScoring() {
        Player player = new Player(name: "Dan", position: "QB")
        Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 200)
        Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2)
        Set stats = new HashSet()
        stats.add(s1)
        stats.add(s2)
        player.setStats(stats)


        IFantasyScoringSystem system = new ESPNStandardScoringSystem()
        player.computeFantasyPoints(system)

        def fantasyPoints = FantasyPoints.list()
        assertTrue "Expecting 1 entry for FantasyPoints", fantasyPoints.size() == 1

        assert fantasyPoints[0].season == 2001
        assert fantasyPoints[0].week == -1
        assert fantasyPoints[0].points == 16
    }

    void testCalculatePoints_WeekStats_ESPNStandardScoring() {
        Player player = new Player(name: "Dan", position: "QB")
        Stat s1 = new Stat(player: player, season: 2001, week: 1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 200)
        Stat s2 = new Stat(player: player, season: 2001, week: 1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2)
        Set stats = new HashSet()
        stats.add(s1)
        stats.add(s2)
        player.setStats(stats)

        IFantasyScoringSystem system = new ESPNStandardScoringSystem()
        player.computeFantasyPoints(system)

        def fantasyPoints = FantasyPoints.list()
        assertTrue "Expecting 1 entry for FantasyPoints", fantasyPoints.size() == 1

        assert fantasyPoints[0].season == 2001
        assert fantasyPoints[0].week == 1
        assert fantasyPoints[0].points == 16
    }
}

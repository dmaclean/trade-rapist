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

	void testCalculatePoints_SeasonStats_ESPNStandardScoring_AlreadyExist() {
		Player player = new Player(name: "Dan", position: "QB")
		Stat s1 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 200).save(flush: true)
		Stat s2 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2).save(flush: true)
		Set stats = new HashSet()
		stats.add(s1)
		stats.add(s2)
		player.setStats(stats)

		IFantasyScoringSystem system = new ESPNStandardScoringSystem()

		// Create existing FantasyPoints entry for 2001 season
		FantasyPoints fp2001 = new FantasyPoints(player: player, points: 16, season: 2001, week: -1, system: system).save(flush: true)

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

	void testCalculatePoints_WeekStats_ESPNStandardScoring_AlreadyExists() {
		Player player = new Player(name: "Dan", position: "QB")
		Stat s1 = new Stat(player: player, season: 2001, week: 1, statKey: FantasyConstants.STAT_PASSING_YARDS, statValue: 200)
		Stat s2 = new Stat(player: player, season: 2001, week: 1, statKey: FantasyConstants.STAT_PASSING_TOUCHDOWNS, statValue: 2)
		Set stats = new HashSet()
		stats.add(s1)
		stats.add(s2)
		player.setStats(stats)

		IFantasyScoringSystem system = new ESPNStandardScoringSystem()

		// Create existing FantasyPoints entry for 2001 season
		FantasyPoints fp2001 = new FantasyPoints(player: player, points: 16, season: 2001, week: 1, system: system).save(flush: true)


		player.computeFantasyPoints(system)

		def fantasyPoints = FantasyPoints.list()
		assertTrue "Expecting 1 entry for FantasyPoints", fantasyPoints.size() == 1

		assert fantasyPoints[0].season == 2001
		assert fantasyPoints[0].week == 1
		assert fantasyPoints[0].points == 16
	}

	void testGetCorrelation_Position_Stat() {
		assert Player.getCorrelation(Player.POSITION_QB, FantasyConstants.STAT_PASSING_YARDS) == 0.5
		assert Player.getCorrelation(Player.POSITION_QB, FantasyConstants.STAT_PASSING_TOUCHDOWNS) == 0.37
		assert Player.getCorrelation(Player.POSITION_QB, FantasyConstants.STAT_INTERCEPTIONS) == 0.08
		assert Player.getCorrelation(Player.POSITION_QB, FantasyConstants.STAT_RUSHING_YARDS) == 0.78
		assert Player.getCorrelation(Player.POSITION_QB, FantasyConstants.STAT_RUSHING_TOUCHDOWNS) == 0.5

		assert Player.getCorrelation(Player.POSITION_RB, FantasyConstants.STAT_RUSHING_YARDS) == 0.5
		assert Player.getCorrelation(Player.POSITION_RB, FantasyConstants.STAT_RUSHING_TOUCHDOWNS) == 0.5
		assert Player.getCorrelation(Player.POSITION_RB, FantasyConstants.STAT_RECEPTIONS) == 0.54
		assert Player.getCorrelation(Player.POSITION_RB, FantasyConstants.STAT_RECEPTION_YARDS) == 0.51
		assert Player.getCorrelation(Player.POSITION_RB, FantasyConstants.STAT_RECEPTION_TOUCHDOWNS) == 0.29

		assert Player.getCorrelation(Player.POSITION_WR, FantasyConstants.STAT_RECEPTION_YARDS) == 0.58
		assert Player.getCorrelation(Player.POSITION_WR, FantasyConstants.STAT_RECEPTION_TOUCHDOWNS) == 0.38
		assert Player.getCorrelation(Player.POSITION_WR, FantasyConstants.STAT_RECEPTIONS) == 0.64

		assert Player.getCorrelation(Player.POSITION_TE, FantasyConstants.STAT_RECEPTION_YARDS) == 0.74
		assert Player.getCorrelation(Player.POSITION_TE, FantasyConstants.STAT_RECEPTION_TOUCHDOWNS) == 0.44
		assert Player.getCorrelation(Player.POSITION_TE, FantasyConstants.STAT_RECEPTIONS) == 0.65

		assert Player.getCorrelation(Player.POSITION_DEF, null) == 0.1

		assert Player.getCorrelation(Player.POSITION_K, null) == 0.1
	}
}

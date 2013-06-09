package com.traderapist.draft

import com.traderapist.models.AverageDraftPosition
import com.traderapist.models.FantasyPoints
import com.traderapist.models.Player
import com.traderapist.scoringsystem.ESPNStandardScoringSystem
import grails.test.mixin.TestFor
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 3/29/13
 * Time: 10:47 PM
 * To change this template use File | Settings | File Templates.
 */
@TestFor(DraftController)
class DraftControllerIntegrationTests {
	@Before
	void setUp() {

	}

	@After
	void tearDown() {

	}

	@Test
	void testGetPlayers() {
		/*
		 * Define players
		 */
		def q1 = new Player(name: "Quarterback 1", position: Player.POSITION_QB).save(flush: true)
		def q2 = new Player(name: "Quarterback 2", position: Player.POSITION_QB).save(flush: true)

		def r1 = new Player(name: "Running back 1", position: Player.POSITION_RB).save(flush: true)
		def r2 = new Player(name: "Running back 2", position: Player.POSITION_RB).save(flush: true)

		def w1 = new Player(name: "Wide receiver 1", position: Player.POSITION_WR).save(flush: true)
		def w2 = new Player(name: "Wide receiver 2", position: Player.POSITION_WR).save(flush: true)

		def t1 = new Player(name: "Tight end 1", position: Player.POSITION_TE).save(flush: true)
		def t2 = new Player(name: "Tight end 2", position: Player.POSITION_TE).save(flush: true)

		def d1 = new Player(name: "Defense 1", position: Player.POSITION_DEF).save(flush: true)
		def d2 = new Player(name: "Defense 2", position: Player.POSITION_DEF).save(flush: true)

		def k1 = new Player(name: "Kicker 1", position: Player.POSITION_K).save(flush: true)
		def k2 = new Player(name: "Kicker 2", position: Player.POSITION_K).save(flush: true)

		/*
		 * Define fantasy points
		 */
		def q1_points = new FantasyPoints(player: q1, points: 300, season: 2002, week: -1, system: ESPNStandardScoringSystem.class.getName()).save(flush: true)
		def q2_points = new FantasyPoints(player: q2, points: 295, season: 2002, week: -1, system: ESPNStandardScoringSystem.class.getName()).save(flush: true)

		def r1_points = new FantasyPoints(player: r1, points: 250, season: 2002, week: -1, system: ESPNStandardScoringSystem.class.getName()).save(flush: true)
		def r2_points = new FantasyPoints(player: r2, points: 275, season: 2002, week: -1, system: ESPNStandardScoringSystem.class.getName()).save(flush: true)

		def w1_points = new FantasyPoints(player: w1, points: 200, season: 2002, week: -1, system: ESPNStandardScoringSystem.class.getName()).save(flush: true)
		def w2_points = new FantasyPoints(player: w2, points: 180, season: 2002, week: -1, system: ESPNStandardScoringSystem.class.getName()).save(flush: true)

		def t1_points = new FantasyPoints(player: t1, points: 150, season: 2002, week: -1, system: ESPNStandardScoringSystem.class.getName()).save(flush: true)
		def t2_points = new FantasyPoints(player: t2, points: 165, season: 2002, week: -1, system: ESPNStandardScoringSystem.class.getName()).save(flush: true)

		def d1_points = new FantasyPoints(player: d1, points: 100, season: 2002, week: -1, system: ESPNStandardScoringSystem.class.getName()).save(flush: true)
		def d2_points = new FantasyPoints(player: d2, points: 95, season: 2002, week: -1, system: ESPNStandardScoringSystem.class.getName()).save(flush: true)

		def k1_points = new FantasyPoints(player: k1, points: 125, season: 2002, week: -1, system: ESPNStandardScoringSystem.class.getName()).save(flush: true)
		def k2_points = new FantasyPoints(player: k2, points: 100, season: 2002, week: -1, system: ESPNStandardScoringSystem.class.getName()).save(flush: true)

		/*
		 * Define ADP
		 */
		def q1_adp = new AverageDraftPosition(player: q1, season: 2002, adp: 1).save(flush: true)
		def q2_adp = new AverageDraftPosition(player: q2, season: 2002, adp: 15).save(flush: true)

		def r1_adp = new AverageDraftPosition(player: r1, season: 2002, adp: 8).save(flush: true)
		def r2_adp = new AverageDraftPosition(player: r2, season: 2002, adp: 2).save(flush: true)

		def w1_adp = new AverageDraftPosition(player: w1, season: 2002, adp: 5).save(flush: true)
		def w2_adp = new AverageDraftPosition(player: w2, season: 2002, adp: 14).save(flush: true)

		def t1_adp = new AverageDraftPosition(player: t1, season: 2002, adp: 20).save(flush: true)
		def t2_adp = new AverageDraftPosition(player: t2, season: 2002, adp: 4).save(flush: true)

		def d1_adp = new AverageDraftPosition(player: d1, season: 2002, adp: 30).save(flush: true)
		def d2_adp = new AverageDraftPosition(player: d2, season: 2002, adp: 31).save(flush: true)

		def k1_adp = new AverageDraftPosition(player: k1, season: 2002, adp: 32).save(flush: true)
		def k2_adp = new AverageDraftPosition(player: k2, season: 2002, adp: 33).save(flush: true)

		request.addParameter("year", "2002")

		controller.players()

		assert response.text == "[" +
				"{\"id\":${q1.id},\"name\":\"Quarterback 1\",\"position\":\"QUARTERBACK\",\"points\":300.0,\"adp\":1.0,\"vorp\":5.0}," +
				"{\"id\":${q2.id},\"name\":\"Quarterback 2\",\"position\":\"QUARTERBACK\",\"points\":295.0,\"adp\":15.0,\"vorp\":0}," +
				"{\"id\":${r2.id},\"name\":\"Running back 2\",\"position\":\"RUNNING_BACK\",\"points\":275.0,\"adp\":2.0,\"vorp\":25.0}," +
				"{\"id\":${r1.id},\"name\":\"Running back 1\",\"position\":\"RUNNING_BACK\",\"points\":250.0,\"adp\":8.0,\"vorp\":0}," +
				"{\"id\":${w1.id},\"name\":\"Wide receiver 1\",\"position\":\"WIDE_RECEIVER\",\"points\":200.0,\"adp\":5.0,\"vorp\":20.0}," +
				"{\"id\":${w2.id},\"name\":\"Wide receiver 2\",\"position\":\"WIDE_RECEIVER\",\"points\":180.0,\"adp\":14.0,\"vorp\":0}," +
				"{\"id\":${t2.id},\"name\":\"Tight end 2\",\"position\":\"TIGHT_END\",\"points\":165.0,\"adp\":4.0,\"vorp\":15.0}," +
				"{\"id\":${t1.id},\"name\":\"Tight end 1\",\"position\":\"TIGHT_END\",\"points\":150.0,\"adp\":20.0,\"vorp\":0}," +
				"{\"id\":${d1.id},\"name\":\"Defense 1\",\"position\":\"DEFENSE\",\"points\":100.0,\"adp\":30.0,\"vorp\":5.0}," +
				"{\"id\":${d2.id},\"name\":\"Defense 2\",\"position\":\"DEFENSE\",\"points\":95.0,\"adp\":31.0,\"vorp\":0}," +
				"{\"id\":${k1.id},\"name\":\"Kicker 1\",\"position\":\"KICKER\",\"points\":125.0,\"adp\":32.0,\"vorp\":25.0}," +
				"{\"id\":${k2.id},\"name\":\"Kicker 2\",\"position\":\"KICKER\",\"points\":100.0,\"adp\":33.0,\"vorp\":0}" +
				"]"
	}
}

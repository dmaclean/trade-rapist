package com.traderapist.models

import grails.converters.JSON
import org.junit.After
import org.junit.Before
import org.junit.Test

import com.traderapist.security.User

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 7/3/13
 * Time: 8:28 AM
 * To change this template use File | Settings | File Templates.
 */
class FantasyTeamPlayerControllerIntegrationTests {
	FantasyTeamPlayerController controller
	def params

	@Before
	void setUp() {
		controller = new FantasyTeamPlayerController()
		params = controller.request.getParameterMap()
	}

	@After
	void tearDown() {
		controller = null
		params = null
	}

	@Test
	void testSaveAllFromDraft() {
		def user = new User(username: "test", password: "password").save(flush: true)
		def fantasyLeagueType = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
		def player = new Player(name: "Dan MacLean", position: Player.POSITION_QB).save(flush: true)
		def player2 = new Player(name: "Mike MacLean", position: Player.POSITION_RB).save(flush: true)
		def fantasyTeam = new FantasyTeam(user: user, fantasyLeagueType: fantasyLeagueType, season: 2013, leagueId: "1111", name: "abc", numOwners: 10).save(flush: true)

		def data = "{" +
				"\"team\" : ${ fantasyTeam.id }," +
				"\"players\" : ["
		for(p in [player,player2]) {
			data += p as JSON
			if(p != player2) {
				data += ","
			}
		}
		data += "]}"

		controller.request.content = data.bytes
		controller.saveAllFromDraft()

		def result = FantasyTeamPlayer.list()

		assert result.size() == 2
		assert result[0].player == player
		assert result[0].fantasyTeam == fantasyTeam
		assert result[1].player == player2
		assert result[1].fantasyTeam == fantasyTeam
	}

	@Test
	void testSaveAllFromDraft_DeletePrevious() {
		def user = new User(username: "test", password: "password").save(flush: true)
		def fantasyLeagueType = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
		def player = new Player(name: "Dan MacLean", position: Player.POSITION_QB).save(flush: true)
		def player2 = new Player(name: "Mike MacLean", position: Player.POSITION_RB).save(flush: true)
		def fantasyTeam = new FantasyTeam(user: user, fantasyLeagueType: fantasyLeagueType, season: 2013, leagueId: "1111", name: "abc", numOwners: 10).save(flush: true)

		/*
		 * Create a FantasyTeamPlayer that should be deleted when we rerun the save
		 */
		def playerToDelete = new Player(name: "Delete me", position: Player.POSITION_K).save(flush: true)
		def ftpToDelete = new FantasyTeamPlayer(player: playerToDelete, fantasyTeam: fantasyTeam).save(flush: true)

		assert FantasyTeamPlayer.list().size() == 1

		/*
		 * Create the data for the request to send to the controller.  Notice that this
		 * does not include the playerToDelete player.
		 */
		def data = "{" +
				"\"team\" : ${ fantasyTeam.id }," +
				"\"players\" : ["
		for(p in [player,player2]) {
			data += p as JSON
			if(p != player2) {
				data += ","
			}
		}
		data += "]}"

		controller.request.content = data.bytes
		controller.saveAllFromDraft()

		def result = FantasyTeamPlayer.list()

		assert result.size() == 2
		assert result[0].player == player
		assert result[0].fantasyTeam == fantasyTeam
		assert result[1].player == player2
		assert result[1].fantasyTeam == fantasyTeam
	}
}

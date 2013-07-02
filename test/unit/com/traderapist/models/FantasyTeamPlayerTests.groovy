package com.traderapist.models



import grails.test.mixin.*
import org.junit.*

import com.traderapist.security.User

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(FantasyTeamPlayer)
@Mock([Player, FantasyTeam, User, FantasyLeagueType])
class FantasyTeamPlayerTests {

	User user
	Player player
	FantasyTeam fantasyTeam
	FantasyLeagueType fantasyLeagueType

	@Before
	void setUp() {
		mockForConstraintsTests(FantasyTeamPlayer)

		User.metaClass.encodePassword = { -> "password"}

		user = new User(username: "dmaclean", password: "password").save(flush: true)
		player = new Player(name: "Dan MacLean", position: Player.POSITION_QB).save(flush: true)
		fantasyLeagueType = new FantasyLeagueType(code: "ESPN", description: "ESPN").save(flush: true)
		fantasyTeam = new FantasyTeam(user: user, name: "ESPN", fantasyLeagueType: fantasyLeagueType, season: 2013).save(flush: true)
	}

	@After
	void tearDown() {
		user = null
		player = null
		fantasyLeagueType = null
		fantasyTeam = null
	}

	void testPlayerNotNull() {
		def fantasyTeamPlayer = new FantasyTeamPlayer(fantasyTeam: fantasyTeam)

		assertFalse "Validation should have failed", fantasyTeamPlayer.validate()

		assertTrue "nullable" == fantasyTeamPlayer.errors["player"]
	}

	void testFantasyTeamNotNull() {
		def fantasyTeamPlayer = new FantasyTeamPlayer(player: player)

		assertFalse "Validation should have failed", fantasyTeamPlayer.validate()

		assertTrue "nullable" == fantasyTeamPlayer.errors["fantasyTeam"]
	}

	void testGoodFantasyTeamPlayer() {
		def fantasyTeamPlayer = new FantasyTeamPlayer(player: player, fantasyTeam: fantasyTeam)

		assertTrue "Validation should not have failed", fantasyTeamPlayer.validate()

		assertTrue 0 == fantasyTeamPlayer.errors.allErrors.size()
	}
}

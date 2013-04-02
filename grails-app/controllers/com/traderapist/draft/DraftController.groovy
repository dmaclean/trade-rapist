package com.traderapist.draft

import com.traderapist.models.Player

class DraftController {

    def index() {
//	    def qb = Player.getPlayersInPointsOrder("QB", 2002)
//	    def rb = Player.getPlayersInPointsOrder("RB", 2002)
//	    def wr = Player.getPlayersInPointsOrder("WR", 2002)
//	    def te = Player.getPlayersInPointsOrder("TE", 2002)
//	    def dst = Player.getPlayersInPointsOrder("DEF", 2002)
//	    def k = Player.getPlayersInPointsOrder("K", 2002)

//	    def tree = new MinimaxTree(players: [qb,rb,wr,te,dst,k])

//	    model = ["executionTime": (end-start)/1000.0, "memoryUsage": (endMem-startMem)/1000.0]


	    render(view: "/draft/index")
    }

	/**
	 * Get all players for the desired season.  The caller will get back a
	 * JSON structure containing all eligible players from all positions, with
	 * player being ordered first by position, then, within position, projected
	 * points.
	 *
	 * Each player will have the following information:
	 * - name
	 * - position
	 * - projected points
	 * - average draft position
	 *
	 * @return        A JSON string representing all players.
	 */
	def players() {
		def qb = Player.getPlayersInPointsOrder("QB", 2002)
		def rb = Player.getPlayersInPointsOrder("RB", 2002)
		def wr = Player.getPlayersInPointsOrder("WR", 2002)
		def te = Player.getPlayersInPointsOrder("TE", 2002)
		def dst = Player.getPlayersInPointsOrder("DEF", 2002)
		def k = Player.getPlayersInPointsOrder("K", 2002)

		// Construct a JSON string representing all the necessary data for drafting.
		def json = "{\"players\":["
		for(playerList in [qb, rb, wr, te, dst, k]) {
			for(player in playerList) {
				json += "{\"player\":{\"name\":\"${player.name}\",\"position\":\"${player.position }\",\"points\":${player.fantasyPoints.toArray()[0].points},\"adp\":${player.averageDraftPositions.toArray()[0].adp}}},"
			}
		}
		json += "]}"

		// Strip out the last comma.  This comma corrupts the JSON.
		json = json.substring(0, json.lastIndexOf(",")) + json.substring(json.lastIndexOf(",")+1)

		render json
	}
}

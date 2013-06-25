package com.traderapist.draft

import com.traderapist.models.Player
import com.traderapist.scoringsystem.ESPNStandardScoringSystem
import org.springframework.web.servlet.ModelAndView

class DraftController {

	/**
	 * This mapping is used so we have a more verbose way to express the position
	 * that is sent over to the browser.  This is done because we're using an
	 * AngularJS filter and if we're filtering on position we can't have K as the
	 * filtered text because it's not unique enough.
	 */
	def positionFilter = [(Player.POSITION_QB):"QUARTERBACK", (Player.POSITION_RB):"RUNNING_BACK",(Player.POSITION_WR):"WIDE_RECEIVER",
							(Player.POSITION_TE):"TIGHT_END", (Player.POSITION_DEF):"DEFENSE", (Player.POSITION_K):"KICKER"]

    def index() {
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
		def year = Integer.parseInt(params["year"])
		def system = (params["system"]) ? params["system"] : ESPNStandardScoringSystem.class.getSimpleName()

		def qb = Player.getPlayersInPointsOrder(Player.POSITION_QB, year, system)
		def rb = Player.getPlayersInPointsOrder(Player.POSITION_RB, year, system)
		def wr = Player.getPlayersInPointsOrder(Player.POSITION_WR, year, system)
		def te = Player.getPlayersInPointsOrder(Player.POSITION_TE, year, system)
		def dst = Player.getPlayersInPointsOrder(Player.POSITION_DEF, year, system)
		def k = Player.getPlayersInPointsOrder(Player.POSITION_K, year, system)

		// Construct a JSON string representing all the necessary data for drafting.
		def json = "["
		for(players in [qb, rb, wr, te, dst, k]) {
			for(int i=0; i<players.size(); i++) {
				// Last player in the list.  Their VORP will be 0.
				if(i == players.size()-1) {
					json += "{\"id\":${players[i].id},\"name\":\"${players[i].name}\",\"position\":\"${positionFilter[players[i].position] }\",\"points\":${players[i].fantasyPoints.toArray()[0].points},\"adp\":${players[i].averageDraftPositions.toArray()[0].adp},\"vorp\":0},"
				}
				// Not the last player.  Their VORP will be <points> - <next player points>
				else {
					def points = players[i].fantasyPoints.toArray()[0].points
					def nextPoints = players[i+1].fantasyPoints.toArray()[0].points
					json += "{\"id\":${players[i].id},\"name\":\"${players[i].name}\",\"position\":\"${positionFilter[players[i].position] }\",\"points\":${points},\"adp\":${players[i].averageDraftPositions.toArray()[0].adp},\"vorp\":${points - nextPoints}},"
				}
			}
		}
		json += "]"

		// Strip out the last comma.  This comma corrupts the JSON.
		if(json != "[]") {
			json = json.substring(0, json.lastIndexOf(",")) + json.substring(json.lastIndexOf(",")+1)
		}

		render json
	}
}

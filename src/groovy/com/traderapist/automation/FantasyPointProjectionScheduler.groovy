package com.traderapist.automation

import com.traderapist.models.FantasyPointsJob
import com.traderapist.models.Player
import grails.converters.JSON
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 7/31/13
 * Time: 5:54 PM
 * To change this template use File | Settings | File Templates.
 */
class FantasyPointProjectionScheduler implements Runnable {
	void run() {
		def httpGenerate = new HTTPBuilder("http://localhost:8080/FantasyAnalysisGrails")
		def httpProject = new HTTPBuilder("http://localhost:8080/FantasyAnalysisGrails")

		def positions = [
				Player.POSITION_QB,
				Player.POSITION_RB,
				Player.POSITION_WR,
				Player.POSITION_TE,
				Player.POSITION_DEF,
				Player.POSITION_K
		]

		while(true) {
			def jobs = FantasyPointsJob.findAllByComplete(false)

			jobs.each {     job ->
				/*
				 * Get all the necessary attributes
				 *
				 * Generate Points
				 * - Fantasy Team Id
				 * - Position
				 *
				 * Project Points
				 *- Fantasy Team Id
				 */

				positions.each {    position ->
					print "Generating fantasy points for ${ job.fantasyTeam.name } for ${ position }"

					// Do HTTP GETs to /fantasyPointsController/generatePoints
					httpGenerate.request(Method.GET, JSON) {
						url.path = "/fantasyPointsController/generatePoints?fantasy_team_id=${ job.fantasyTeam.id }&position=${ position }"
						response.success = {    resp, json ->
							print "Successful - ${ json }"
						}
					}
				}

				print "Projecting fantasy points for ${ job.fantasyTeam.name }"

				// Do HTTP GET to /fantasyPointsController/projectPoints
				httpProject.request(Method.GET, JSON) {
					url.path = "/fantasyPointsController/projectPoints?fantasy_team_id=${ job.fantasyTeam.id }"
					response.success = {    resp, json ->
						print "Successful - ${ json }"
					}
				}

				job.complete = true
				job.save(flush: true)
			}

			Thread.sleep(1000 * 60 * 5)
		}
	}
}

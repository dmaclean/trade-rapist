package com.traderapist.automation

import com.traderapist.models.FantasyPointsJob

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 7/31/13
 * Time: 5:54 PM
 * To change this template use File | Settings | File Templates.
 */
class FantasyPointProjectionScheduler implements Runnable {
	void run() {
		while(true) {
			def jobs = FantasyPointsJob.list()

			jobs.each {     job ->
				// Get all necessary attributes

				// Do HTTP GET to /fantasyPointsController/projectPoints
			}

			Thread.sleep(1000 * 60 * 5)
		}
	}
}

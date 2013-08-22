package com.traderapist.automation

import com.traderapist.models.FantasyPointsJob
import com.traderapist.models.FantasyPointsJobController
import grails.plugins.rest.client.RestBuilder
import org.codehaus.groovy.grails.web.mapping.LinkGenerator

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 7/31/13
 * Time: 5:54 PM
 * To change this template use File | Settings | File Templates.
 */
class FantasyPointProjectionScheduler implements Runnable {
	// Inject link generator
	LinkGenerator grailsLinkGenerator

	void run() {
        def rest = new RestBuilder()

		while(true) {
			if(!FantasyPointsJobController.processing) {
				try {
					/*
					 * Fetch all Projection jobs
					 */
					def jobs = FantasyPointsJob.findAllByCompletedAndProjectionInList(false,
							[FantasyPointsJob.TRADERAPIST_PROJECTION,
							 FantasyPointsJob.YAHOO_PPR_PROJECTION,
							 FantasyPointsJob.YAHOO_STANDARD_PROJECTION])
					jobs.each {     job ->
						long start = System.currentTimeMillis()
						def link = grailsLinkGenerator.link(absolute: true, controller: "fantasyPointsJob", action: "process", params: [fantasy_points_job_id : job.id])
						def resp = rest.get(link)
						long end = System.currentTimeMillis()
						println "FPJ job ${ job.id }/${ job.season }/${ job.week }/${ (job.projection) ? "proj" : "not_proj" } completed in ${ (end-start)/1000.0 }"
					}

					/*
					 * Fetch all point generation jobs
					 *
					 * Holding off on this for now.
					 */
//					jobs = FantasyPointsJob.findAllByCompletedAndProjection(false, FantasyPointsJob.NO_PROJECTION)
//					jobs.each {     job ->
//	                    long start = System.currentTimeMillis()
//	                    def resp = rest.get(grailsLinkGenerator.link(absolute: true, controller: "fantasyPointsJob", action: "process", params: [fantasy_points_job_id : job.id]))
//	                    long end = System.currentTimeMillis()
//	                    println "FPJ job ${ job.id }/${ job.season }/${ job.week }/${ (job.projection) ? "proj" : "not_proj" } completed in ${ (end-start)/1000.0 }"
//	                }
				}
				catch(Exception e) {
					println "${ e.getMessage() }"
					e.printStackTrace()
				}
            }

            Thread.sleep(1000*60*5)
		}
	}
}

package com.traderapist.automation

import com.traderapist.models.FantasyPoints
import com.traderapist.models.FantasyPointsJob
import com.traderapist.models.FantasyPointsJobController
import grails.plugins.rest.client.RestBuilder

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 7/31/13
 * Time: 5:54 PM
 * To change this template use File | Settings | File Templates.
 */
class FantasyPointProjectionScheduler implements Runnable {
	void run() {
        def rest = new RestBuilder()

		while(true) {
			if(!FantasyPointsJobController.processing) {
                def jobs = FantasyPointsJob.findAllByCompletedAndProjection(false, false)

                jobs.each {     job ->
                    long start = System.currentTimeMillis()
                    def resp = rest.get("http://localhost:8080/FantasyAnalysisGrails/fantasyPointsJob/process?fantasy_points_job_id=${ job.id }")
                    long end = System.currentTimeMillis()
                    println "FPJ job ${ job.id }/${ job.season }/${ job.week }/${ (job.projection) ? "proj" : "not_proj" } completed in ${ (end-start)/1000.0 }"
                }
            }

            Thread.sleep(1000*60*5)
		}
	}
}

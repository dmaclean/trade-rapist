package com.traderapist.models

class FantasyPointsJobController {

    static boolean processing = false

    static def positions = [
            Player.POSITION_QB,
            Player.POSITION_RB,
            Player.POSITION_WR,
            Player.POSITION_TE,
            Player.POSITION_DEF,
            Player.POSITION_K
    ]

    def index() {
        render "Running index"
    }

    def process() {
        processing = true

        def job = FantasyPointsJob.get(params.fantasy_points_job_id)

        if(job.projection) {
            println "Projecting fantasy points for ${ job.fantasyTeam.name } for ${ job.fantasyTeam.name } - ${ job.season }"
            FantasyPoints.projectPoints(job.fantasyTeam)
        }
        else {
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
            println "\tGenerating fantasy points for ${ job.fantasyTeam.name } - ${ job.season }/${ job.week }"
            long start = System.currentTimeMillis()
            positions.each {    position ->
                FantasyPoints.generatePoints(job.fantasyTeam, position, job.season, job.week)
            }
            long end = System.currentTimeMillis()
            println "\tGenerated fantasy points for ${ job.fantasyTeam.name } - ${ job.season }/${ job.week } in ${ (end-start)/1000.0 }"
        }

        job.completed = true
        job.save(flush: true)

        processing = false
    }
}

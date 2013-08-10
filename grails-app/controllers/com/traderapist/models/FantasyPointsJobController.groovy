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
            log.info "Projecting fantasy points for ${ job.fantasyTeam.name }"
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
            positions.each {    position ->
                log.info "Generating fantasy points for ${ job.fantasyTeam.name } for ${ position }"
                FantasyPoints.generatePoints(job.fantasyTeam, position, job.season, job.week)
            }
        }

        job.completed = true
        job.save(flush: true)

        processing = false
    }
}

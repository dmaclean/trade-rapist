package com.traderapist.nba

class InjuryController {

    static String ACTION_RETURN_DATE_TODAY = "return_date_today"

    static String INVALID_INJURY_ID_ERROR = "Invalid injury id"

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        def players = Player.findAllByRgPositionIsNotNull()
        def injuries

        if(params.current) {
            def today = new Date()
            injuries = Injury.findAllByInjuryDateLessThanEqualsAndReturnDateGreaterThan(today, today)
        }
        else {
            injuries = Injury.list()
        }

        render (view: "list", model: [injuries: injuries, players: players])
    }

    def save() {
        if(params?.action == ACTION_RETURN_DATE_TODAY) {
            def injury = Injury.get(params.id)

            if(injury) {
                injury.returnDate = new Date()
                injury.save()
            }
            else {
                flash.message = INVALID_INJURY_ID_ERROR
            }
        }
        else {

        }

        redirect action: "list"
    }
}

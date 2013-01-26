package com.traderapist.models

class PlayerDropoffController {

    def index() {
        def results = Player.getDropoffData("QB", 2001)

        String s = ""
        for(r in results) {
            s += "${r[0].name} - ${r[1].points}<br/>"
        }

        render s
    }
}

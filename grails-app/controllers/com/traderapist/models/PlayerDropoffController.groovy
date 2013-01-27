package com.traderapist.models

class PlayerDropoffController {

    def index() {
        def tiers = Player.getDropoffData("QB", 2001)

        String s = ""
        def tierNum = 1
        for(t in tiers) {
            s += "Tier ${tierNum}<br/>"
            for(player in t) {
                s += "${player[0].name} - ${player[1].points}<br/>"
            }

            s += "<br/>"

            tierNum++;
        }

        render s
    }
}

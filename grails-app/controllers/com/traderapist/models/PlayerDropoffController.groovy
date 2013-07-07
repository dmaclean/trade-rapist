package com.traderapist.models

import grails.plugins.springsecurity.Secured
import org.springframework.security.access.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_USER'])
class PlayerDropoffController {

    def index() {
        def seasons = Stat.getStatYears()
        def chosenSeason = (!params["seasons"]) ? 2001 : Integer.parseInt(params["seasons"])
        def quarterbackTiers = Player.getDropoffData("QB", chosenSeason)
        def runningbackTiers = Player.getDropoffData("RB", chosenSeason)
        def receiverTiers = Player.getDropoffData("WR", chosenSeason)
        def tightEndTiers = Player.getDropoffData("TE", chosenSeason)
        def kickerTiers = Player.getDropoffData("K", chosenSeason)
        def teamTiers = Player.getDropoffData("DEF", chosenSeason)

        /*
        Set up chart data.  Loop through each position
         */
        def chartData = []
        def quarterbacks = []
        for(q in quarterbackTiers) {
            quarterbacks.addAll(q)
        }
        def runningbacks = []
        for(r in runningbackTiers) {
            runningbacks.addAll(r)
        }
        def receivers = []
        for(w in receiverTiers) {
            receivers.addAll(w)
        }
        def tightEnds = []
        for(w in tightEndTiers) {
            tightEnds.addAll(w)
        }
        def kickers = []
        for(w in kickerTiers) {
            kickers.addAll(w)
        }
        def teams = []
        for(w in teamTiers) {
            teams.addAll(w)
        }

        def len = Math.max(quarterbacks.size(), runningbacks.size())
        len = Math.max(len, receivers.size())
        len = Math.max(len, tightEnds.size())
        len = Math.max(len, kickers.size())
        len = Math.max(len, teams.size())

        // len might be zero if the user somehow asks for a year that we don't have.  If so, skip this
        // and move on to populating the model.
        if(len > 0) {
            for(i in 0..len-1) {
                def qbPoints = (i < quarterbacks.size()) ? quarterbacks[i][1].points : 0
                def rbPoints = (i < runningbacks.size()) ? runningbacks[i][1].points : 0
                def wrPoints = (i < receivers.size()) ? receivers[i][1].points : 0
                def tePoints = (i < tightEnds.size()) ? tightEnds[i][1].points : 0
                def kPoints = (i < kickers.size()) ? kickers[i][1].points : 0
                def teamPoints = (i < teams.size()) ? teams[i][1].points : 0
                chartData.add([i+1, qbPoints, rbPoints, wrPoints, tePoints, kPoints, teamPoints])
            }
        }

        def model = [   "chartData": chartData,
                        "quarterbackTiers": quarterbackTiers,
                        "runningbackTiers": runningbackTiers,
                        "receiverTiers" : receiverTiers,
                        "tightEndTiers" : tightEndTiers,
                        "kickerTiers" : kickerTiers,
                        "teamTiers" : teamTiers,
                        "chosenSeason" : chosenSeason,
                        "seasons": seasons]
        render(view: "/playerDropoff/index", model: model)
    }
}

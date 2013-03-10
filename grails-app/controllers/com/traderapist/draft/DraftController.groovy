package com.traderapist.draft

import com.traderapist.models.Player

class DraftController {

    def index() {
	    def qb = Player.getPlayersInPointsOrder("QB", 2002)
	    def rb = Player.getPlayersInPointsOrder("RB", 2002)
	    def wr = Player.getPlayersInPointsOrder("WR", 2002)
	    def te = Player.getPlayersInPointsOrder("TE", 2002)
	    def dst = Player.getPlayersInPointsOrder("DEF", 2002)
	    def k = Player.getPlayersInPointsOrder("K", 2002)

	    def tree = new MinimaxTree(players: [qb,rb,wr,te,dst,k])

	    Runtime rt = Runtime.getRuntime()
	    long startMem = rt.freeMemory()
	    long start = System.currentTimeMillis()
	    tree.constructTree()
	    long end = System.currentTimeMillis()
	    long endMem = rt.freeMemory()

	    model = ["executionTime": (end-start)/1000.0, "memoryUsage": (endMem-startMem)/1000.0]

	    render(view: "/draft/index", model: model)
    }
}

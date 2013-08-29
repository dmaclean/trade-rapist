package com.traderapist.models

import com.traderapist.constants.FantasyConstants

class Stat {
    static belongsTo = [player: Player]

    Integer season
    Integer week
    Integer statKey
    Integer statValue
    double points

    static transients = ['points']

    static constraints = {
        season nullable:false
        week nullable:false
        week range:-1..17
        statKey nullable:false
        statValue nullable:false
    }

    static mapping = {
        cache false
        table "stats"
        version false
    }

    def translateStatKey() {
        return FantasyConstants.statTranslation[statKey]
    }

    /**
     * Determines which years are available for player statistics
     *
     * @return      An array of integers representing the seasons available.
     */
    static def getStatYears() {
        return Stat.executeQuery("select distinct s.season from Stat s")
    }

	static def dumpToCSV(player) {
		def csv = ""
		def statMap = [:]
		def stats = Stat.findAllByPlayer(player)
		for(stat in stats) {
			def key = "${ stat.season }_${ stat.week }"
			if(!statMap[key]) {
				def statList = []
				statMap[key] = statList

				for(i in 0..77)     statList << 0
			}
			statMap[key][stat.statKey] = stat.statValue
		}

		for(e in statMap) {
			def pieces = e.key.split("_")
			csv += "${ player.id },${ player.name },${ player.position },${pieces[0]},${pieces[1]},${e.value.join(",")}\n"
		}

		csv
	}
}

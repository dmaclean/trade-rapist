package com.traderapist.utils

import groovy.sql.Sql

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 9/19/13
 * Time: 1:58 PM
 * To change this template use File | Settings | File Templates.
 */
class DumpCSV {
    static def dump() {
        def foo = 'cheese'
        def sql = Sql.newInstance("jdbc:mysql://localhost:3306/fantasy_yahoo", "fantasy", "fantasy", "com.mysql.jdbc.Driver")

        File f = new File("/Users/dmaclean/Desktop/dump.csv")
        def count = 0
        def csv = ""
        sql.eachRow("select * from players where position like '%QB%' or position like '%RB%' or position like '%WR%' or position like '%TE%' or position like '%K%' or position like '%DEF%' order by name") {
            def statMap = [:]

            sql.eachRow("select * from stats s inner join players p on p.id = s.player_id where p.id = ${it.id}") {
                def key = "${ it.season }_${ it.week }"
                if(!statMap[key]) {
                    def statList = []
                    statMap[key] = statList

                    for(i in 0..77)     statList << 0
                }
                statMap[key][it.stat_key] = it.stat_value
            }

            for(e in statMap) {
                def pieces = e.key.split("_")
                f.append("${ it.id }\t${ it.name }\t${ it.position }\t${pieces[0]}\t${pieces[1]}\t${e.value.join("\t")}\n")
            }

            println "Processed ${it.name}"
        }

        println "Processed ${count} players"
    }

    public static void main(String[] args) {
        DumpCSV.dump()
    }
}

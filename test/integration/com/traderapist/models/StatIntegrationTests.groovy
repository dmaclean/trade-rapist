package com.traderapist.models

import com.traderapist.constants.FantasyConstants

import static org.junit.Assert.*
import org.junit.*

class StatIntegrationTests {

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testGetStatYears() {
        def player = new Player(name: "Dan MacLean", position: "RB")
        player.save(flush: true)
        def stat2001 = new Stat(player: player, season: 2001, week: -1, statKey: FantasyConstants.STAT_COMPLETIONS, statValue: 100)
        def stat2002 = new Stat(player: player, season: 2002, week: -1, statKey: FantasyConstants.STAT_COMPLETIONS, statValue: 100)
        def stat2003 = new Stat(player: player, season: 2003, week: -1, statKey: FantasyConstants.STAT_COMPLETIONS, statValue: 100)
        stat2001.save(flush: true)
        stat2002.save(flush: true)
        stat2003.save(flush: true)

        def years = Stat.getStatYears()

        assert years[0] == 2001
        assert years[1] == 2002
        assert years[2] == 2003
    }
}

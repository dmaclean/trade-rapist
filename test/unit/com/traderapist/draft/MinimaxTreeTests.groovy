package com.traderapist.draft

import com.traderapist.models.FantasyPoints
import com.traderapist.models.Player
import com.traderapist.scoringsystem.ESPNStandardScoringSystem
import grails.test.mixin.Mock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 2/23/13
 * Time: 1:05 PM
 * To change this template use File | Settings | File Templates.
 */
@TestMixin(GrailsUnitTestMixin)
@Mock(Player)
class MinimaxTreeTests {

	MinimaxTree tree

	void setUp() {
		tree = new MinimaxTree()
	}

	void tearDown() {
		tree = null
	}

	void testConstructTree_ConfirmStructure_QBOnly() {
		def system = new ESPNStandardScoringSystem()

		// Create players
		def q1 = new Player(name: "Quarterback 1", position: "QB")
		def q2 = new Player(name: "Quarterback 2", position: "QB")
		def q3 = new Player(name: "Quarterback 3", position: "QB")

		// Create projected stats
		def q1_stats = new FantasyPoints(player: q1, season: 2013, week: -1, system: system, points: 300)
		def q2_stats = new FantasyPoints(player: q2, season: 2013, week: -1, system: system, points: 290)
		def q3_stats = new FantasyPoints(player: q3, season: 2013, week: -1, system: system, points: 270)

		// Associate stats with players
		q1.fantasyPoints = new HashSet<FantasyPoints>([q1_stats])
		q2.fantasyPoints = new HashSet<FantasyPoints>([q2_stats])
		q3.fantasyPoints = new HashSet<FantasyPoints>([q3_stats])

		// Add players to tree list
		tree.players[MinimaxTree.QUARTERBACK_INDEX] << q1
		tree.players[MinimaxTree.QUARTERBACK_INDEX] << q2
		tree.players[MinimaxTree.QUARTERBACK_INDEX] << q3

		tree.constructTree()

		assert tree.root.children.size() == 3

		/*************
			DEPTH 1
		 ************/
		// QB1 chosen
		def child1 = tree.root.children[0]
		assert child1.parent == tree.root
		assert child1.draftedPlayer == q1
//		assert child1.players[MinimaxTree.QUARTERBACK_INDEX].size() == 2
//		assert child1.players[MinimaxTree.QUARTERBACK_INDEX][0] == q2         // Players still available
//		assert child1.players[MinimaxTree.QUARTERBACK_INDEX][1] == q3
//		assert child1.players[MinimaxTree.RUNNING_BACK_INDEX].size() == 0
//		assert child1.players[MinimaxTree.WIDE_RECEIVER_INDEX].size() == 0
//		assert child1.players[MinimaxTree.TIGHT_END_INDEX].size() == 0
//		assert child1.players[MinimaxTree.DST_INDEX].size() == 0
//		assert child1.players[MinimaxTree.KICKER_INDEX].size() == 0

		// QB2 chosen
		def child2 = tree.root.children[1]
		assert child2.parent == tree.root
		assert child2.draftedPlayer == q2
//		assert child2.players[MinimaxTree.QUARTERBACK_INDEX].size() == 2
//		assert child2.players[MinimaxTree.QUARTERBACK_INDEX][0] == q1         // Players still available
//		assert child2.players[MinimaxTree.QUARTERBACK_INDEX][1] == q3
//		assert child2.players[MinimaxTree.RUNNING_BACK_INDEX].size() == 0
//		assert child2.players[MinimaxTree.WIDE_RECEIVER_INDEX].size() == 0
//		assert child2.players[MinimaxTree.TIGHT_END_INDEX].size() == 0
//		assert child2.players[MinimaxTree.DST_INDEX].size() == 0
//		assert child2.players[MinimaxTree.KICKER_INDEX].size() == 0

		// QB3 chosen
		def child3 = tree.root.children[2]
		assert child3.parent == tree.root
		assert child3.draftedPlayer == q3
//		assert child3.players[MinimaxTree.QUARTERBACK_INDEX].size() == 2
//		assert child3.players[MinimaxTree.QUARTERBACK_INDEX][0] == q1         // Players still available
//		assert child3.players[MinimaxTree.QUARTERBACK_INDEX][1] == q2
//		assert child3.players[MinimaxTree.RUNNING_BACK_INDEX].size() == 0
//		assert child3.players[MinimaxTree.WIDE_RECEIVER_INDEX].size() == 0
//		assert child3.players[MinimaxTree.TIGHT_END_INDEX].size() == 0
//		assert child3.players[MinimaxTree.DST_INDEX].size() == 0
//		assert child3.players[MinimaxTree.KICKER_INDEX].size() == 0

		/**************
			DEPTH 2
		 *************/
		// QB1, QB2 chosen
		def child1_1 = child1.children[0]                     // child1_1 means <1st pick branch #>_<2nd pick branch #>
		assert child1_1.parent == child1
		assert child1_1.draftedPlayer == q2
//		assert child1_1.players[MinimaxTree.QUARTERBACK_INDEX].size() == 1
//		assert child1_1.players[MinimaxTree.QUARTERBACK_INDEX][0] == q3         // Players still available
//		assert child1_1.players[MinimaxTree.RUNNING_BACK_INDEX].size() == 0
//		assert child1_1.players[MinimaxTree.WIDE_RECEIVER_INDEX].size() == 0
//		assert child1_1.players[MinimaxTree.TIGHT_END_INDEX].size() == 0
//		assert child1_1.players[MinimaxTree.DST_INDEX].size() == 0
//		assert child1_1.players[MinimaxTree.KICKER_INDEX].size() == 0

		// QB1, QB3 chosen
		def child1_2 = child1.children[1]
		assert child1_2.parent == child1
		assert child1_2.draftedPlayer == q3
//		assert child1_2.players[MinimaxTree.QUARTERBACK_INDEX].size() == 1
//		assert child1_2.players[MinimaxTree.QUARTERBACK_INDEX][0] == q2         // Players still available
//		assert child1_2.players[MinimaxTree.RUNNING_BACK_INDEX].size() == 0
//		assert child1_2.players[MinimaxTree.WIDE_RECEIVER_INDEX].size() == 0
//		assert child1_2.players[MinimaxTree.TIGHT_END_INDEX].size() == 0
//		assert child1_2.players[MinimaxTree.DST_INDEX].size() == 0
//		assert child1_2.players[MinimaxTree.KICKER_INDEX].size() == 0

		// QB2, QB1 chosen
		def child2_1 = child2.children[0]
		assert child2_1.parent == child2
		assert child2_1.draftedPlayer == q1
//		assert child2_1.players[MinimaxTree.QUARTERBACK_INDEX].size() == 1
//		assert child2_1.players[MinimaxTree.QUARTERBACK_INDEX][0] == q3         // Players still available
//		assert child2_1.players[MinimaxTree.RUNNING_BACK_INDEX].size() == 0
//		assert child2_1.players[MinimaxTree.WIDE_RECEIVER_INDEX].size() == 0
//		assert child2_1.players[MinimaxTree.TIGHT_END_INDEX].size() == 0
//		assert child2_1.players[MinimaxTree.DST_INDEX].size() == 0
//		assert child2_1.players[MinimaxTree.KICKER_INDEX].size() == 0

		// QB2, QB3 chosen
		def child2_2 = child2.children[1]
		assert child2_2.parent == child2
		assert child2_2.draftedPlayer == q3
//		assert child2_2.players[MinimaxTree.QUARTERBACK_INDEX].size() == 1
//		assert child2_2.players[MinimaxTree.QUARTERBACK_INDEX][0] == q1         // Players still available
//		assert child2_2.players[MinimaxTree.RUNNING_BACK_INDEX].size() == 0
//		assert child2_2.players[MinimaxTree.WIDE_RECEIVER_INDEX].size() == 0
//		assert child2_2.players[MinimaxTree.TIGHT_END_INDEX].size() == 0
//		assert child2_2.players[MinimaxTree.DST_INDEX].size() == 0
//		assert child2_2.players[MinimaxTree.KICKER_INDEX].size() == 0

		// QB3, QB1 chosen
		def child3_1 = child3.children[0]
		assert child3_1.parent == child3
		assert child3_1.draftedPlayer == q1
//		assert child3_1.players[MinimaxTree.QUARTERBACK_INDEX].size() == 1
//		assert child3_1.players[MinimaxTree.QUARTERBACK_INDEX][0] == q2         // Players still available
//		assert child3_1.players[MinimaxTree.RUNNING_BACK_INDEX].size() == 0
//		assert child3_1.players[MinimaxTree.WIDE_RECEIVER_INDEX].size() == 0
//		assert child3_1.players[MinimaxTree.TIGHT_END_INDEX].size() == 0
//		assert child3_1.players[MinimaxTree.DST_INDEX].size() == 0
//		assert child3_1.players[MinimaxTree.KICKER_INDEX].size() == 0

		// QB3, QB2 chosen
		def child3_2 = child3.children[1]
		assert child3_2.parent == child3
		assert child3_2.draftedPlayer == q2
//		assert child3_2.players[MinimaxTree.QUARTERBACK_INDEX].size() == 1
//		assert child3_2.players[MinimaxTree.QUARTERBACK_INDEX][0] == q1         // Players still available
//		assert child3_2.players[MinimaxTree.RUNNING_BACK_INDEX].size() == 0
//		assert child3_2.players[MinimaxTree.WIDE_RECEIVER_INDEX].size() == 0
//		assert child3_2.players[MinimaxTree.TIGHT_END_INDEX].size() == 0
//		assert child3_2.players[MinimaxTree.DST_INDEX].size() == 0
//		assert child3_2.players[MinimaxTree.KICKER_INDEX].size() == 0

		/*************
			DEPTH 3
		 ************/
		// QB1, QB2, QB3 chosen
		def child1_1_1 = child1_1.children[0]
		assert child1_1_1.parent == child1_1
		assert child1_1_1.draftedPlayer == q3
//		assert child1_2_3.players[MinimaxTree.QUARTERBACK_INDEX].size() == 0      // No players still available
//		assert child1_2_3.players[MinimaxTree.RUNNING_BACK_INDEX].size() == 0
//		assert child1_2_3.players[MinimaxTree.WIDE_RECEIVER_INDEX].size() == 0
//		assert child1_2_3.players[MinimaxTree.TIGHT_END_INDEX].size() == 0
//		assert child1_2_3.players[MinimaxTree.DST_INDEX].size() == 0
//		assert child1_2_3.players[MinimaxTree.KICKER_INDEX].size() == 0

		// QB1, QB3, QB2 chosen
		def child1_2_1 = child1_2.children[0]
		assert child1_2_1.parent == child1_2
		assert child1_2_1.draftedPlayer == q2
//		assert child1_3_2.players[MinimaxTree.QUARTERBACK_INDEX].size() == 0      // No players still available
//		assert child1_3_2.players[MinimaxTree.RUNNING_BACK_INDEX].size() == 0
//		assert child1_3_2.players[MinimaxTree.WIDE_RECEIVER_INDEX].size() == 0
//		assert child1_3_2.players[MinimaxTree.TIGHT_END_INDEX].size() == 0
//		assert child1_3_2.players[MinimaxTree.DST_INDEX].size() == 0
//		assert child1_3_2.players[MinimaxTree.KICKER_INDEX].size() == 0

		// QB2, QB1, QB3 chosen
		def child2_1_1 = child2_1.children[0]
		assert child2_1_1.parent == child2_1
		assert child2_1_1.draftedPlayer == q3
//		assert child2_1_3.players[MinimaxTree.QUARTERBACK_INDEX].size() == 0      // No players still available
//		assert child2_1_3.players[MinimaxTree.RUNNING_BACK_INDEX].size() == 0
//		assert child2_1_3.players[MinimaxTree.WIDE_RECEIVER_INDEX].size() == 0
//		assert child2_1_3.players[MinimaxTree.TIGHT_END_INDEX].size() == 0
//		assert child2_1_3.players[MinimaxTree.DST_INDEX].size() == 0
//		assert child2_1_3.players[MinimaxTree.KICKER_INDEX].size() == 0

		// QB2, QB3, QB1 chosen
		def child2_2_1 = child2_2.children[0]
		assert child2_2_1.parent == child2_2
		assert child2_2_1.draftedPlayer == q1
//		assert child2_3_1.players[MinimaxTree.QUARTERBACK_INDEX].size() == 0      // No players still available
//		assert child2_3_1.players[MinimaxTree.RUNNING_BACK_INDEX].size() == 0
//		assert child2_3_1.players[MinimaxTree.WIDE_RECEIVER_INDEX].size() == 0
//		assert child2_3_1.players[MinimaxTree.TIGHT_END_INDEX].size() == 0
//		assert child2_3_1.players[MinimaxTree.DST_INDEX].size() == 0
//		assert child2_3_1.players[MinimaxTree.KICKER_INDEX].size() == 0

		// QB3, QB1, QB2 chosen
		def child3_1_1 = child3_1.children[0]
		assert child3_1_1.parent == child3_1
		assert child3_1_1.draftedPlayer == q2
//		assert child3_1_2.players[MinimaxTree.QUARTERBACK_INDEX].size() == 0      // No players still available
//		assert child3_1_2.players[MinimaxTree.RUNNING_BACK_INDEX].size() == 0
//		assert child3_1_2.players[MinimaxTree.WIDE_RECEIVER_INDEX].size() == 0
//		assert child3_1_2.players[MinimaxTree.TIGHT_END_INDEX].size() == 0
//		assert child3_1_2.players[MinimaxTree.DST_INDEX].size() == 0
//		assert child3_1_2.players[MinimaxTree.KICKER_INDEX].size() == 0

		// QB3, QB2, QB1 chosen
		def child3_2_1 = child3_2.children[0]
		assert child3_2_1.parent == child3_2
		assert child3_2_1.draftedPlayer == q1
//		assert child3_2_1.players[MinimaxTree.QUARTERBACK_INDEX].size() == 0      // No players still available
//		assert child3_2_1.players[MinimaxTree.RUNNING_BACK_INDEX].size() == 0
//		assert child3_2_1.players[MinimaxTree.WIDE_RECEIVER_INDEX].size() == 0
//		assert child3_2_1.players[MinimaxTree.TIGHT_END_INDEX].size() == 0
//		assert child3_2_1.players[MinimaxTree.DST_INDEX].size() == 0
//		assert child3_2_1.players[MinimaxTree.KICKER_INDEX].size() == 0
	}

	void testConstructTree() {
		def system = new ESPNStandardScoringSystem()

		// Create players
		def q1 = new Player(name: "Quarterback 1", position: "QB")
		def q2 = new Player(name: "Quarterback 2", position: "QB")
		def q3 = new Player(name: "Quarterback 3", position: "QB")

		def r1 = new Player(name: "Running Back 1", position: "RB")
		def r2 = new Player(name: "Running Back 2", position: "RB")
		def r3 = new Player(name: "Running Back 3", position: "RB")

		def w1 = new Player(name: "Wide Receiver 1", position: "WR")
		def w2 = new Player(name: "Wide Receiver 2", position: "WR")
		def w3 = new Player(name: "Wide Receiver 3", position: "WR")

		// Create projected stats
		def q1_stats = new FantasyPoints(player: q1, season: 2013, week: -1, system: system, points: 300)
		def q2_stats = new FantasyPoints(player: q2, season: 2013, week: -1, system: system, points: 290)
		def q3_stats = new FantasyPoints(player: q3, season: 2013, week: -1, system: system, points: 270)

		def r1_stats = new FantasyPoints(player: r1, season: 2013, week: -1, system: system, points: 250)
		def r2_stats = new FantasyPoints(player: r2, season: 2013, week: -1, system: system, points: 230)
		def r3_stats = new FantasyPoints(player: r3, season: 2013, week: -1, system: system, points: 225)

		def w1_stats = new FantasyPoints(player: w1, season: 2013, week: -1, system: system, points: 260)
		def w2_stats = new FantasyPoints(player: w1, season: 2013, week: -1, system: system, points: 220)
		def w3_stats = new FantasyPoints(player: w1, season: 2013, week: -1, system: system, points: 210)

		// Associate stats with players
		q1.fantasyPoints = new HashSet<FantasyPoints>([q1_stats])
		q2.fantasyPoints = new HashSet<FantasyPoints>([q2_stats])
		q3.fantasyPoints = new HashSet<FantasyPoints>([q3_stats])

		r1.fantasyPoints = new HashSet<FantasyPoints>([r1_stats])
		r2.fantasyPoints = new HashSet<FantasyPoints>([r2_stats])
		r3.fantasyPoints = new HashSet<FantasyPoints>([r3_stats])

		w1.fantasyPoints = new HashSet<FantasyPoints>([w1_stats])
		w2.fantasyPoints = new HashSet<FantasyPoints>([w2_stats])
		w3.fantasyPoints = new HashSet<FantasyPoints>([w3_stats])

		// Add players to tree list
		tree.players << q1
		tree.players << q2
		tree.players << q3

		tree.players << r1
		tree.players << r2
		tree.players << r3

		tree.players << w1
		tree.players << w2
		tree.players << w3

		tree.constructTree()
	}
}

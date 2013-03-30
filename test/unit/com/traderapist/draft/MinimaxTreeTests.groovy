package com.traderapist.draft

import com.traderapist.models.AverageDraftPosition
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

	void testIsMyPick_10Owners() {
		tree.draftType = MinimaxTree.DRAFT_TYPE_SNAKE
		tree.numOwners = 10

		/*
		 * I have first pick
		 */
		tree.myPick = 0         // Zero-based
		assert tree.isMyPick(0)
		assert !tree.isMyPick(9)
		assert tree.isMyPick(19)
		assert tree.isMyPick(20)

		/*
		 * I have 2nd pick
		 */
		tree.myPick = 1
		assert !tree.isMyPick(0)
		assert tree.isMyPick(1)
		assert !tree.isMyPick(11)       // 2nd pick, 2nd round -> I shouldn't get this
		assert tree.isMyPick(18)        // 8th pick, 2nd round -> I should get this
		assert !tree.isMyPick(20)
		assert tree.isMyPick(21)

		/*
		 * I have 10th pick
		 */
		tree.myPick = 9
		assert !tree.isMyPick(0)
		assert tree.isMyPick(9)
		assert tree.isMyPick(10)
		assert !tree.isMyPick(20)
		assert tree.isMyPick(29)
	}

	void testIsMyPick_12Owners() {
		tree.draftType = MinimaxTree.DRAFT_TYPE_SNAKE
		tree.numOwners = 12

		/*
		 * I have first pick
		 */
		tree.myPick = 0         // Zero-based
		assert tree.isMyPick(0)
		assert !tree.isMyPick(11)
		assert tree.isMyPick(23)
		assert tree.isMyPick(24)

		/*
		 * I have 2nd pick
		 */
		tree.myPick = 1
		assert !tree.isMyPick(0)
		assert tree.isMyPick(1)
		assert !tree.isMyPick(13)       // 2nd pick, 2nd round -> I shouldn't get this
		assert tree.isMyPick(22)        // 11th pick, 2nd round -> I should get this
		assert tree.isMyPick(25)

		/*
		 * I have 12th pick
		 */
		tree.myPick = 11
		assert !tree.isMyPick(0)
		assert tree.isMyPick(11)
		assert tree.isMyPick(12)
		assert tree.isMyPick(35)
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

		// QB2 chosen
		def child2 = tree.root.children[1]
		assert child2.parent == tree.root
		assert child2.draftedPlayer == q2

		// QB3 chosen
		def child3 = tree.root.children[2]
		assert child3.parent == tree.root
		assert child3.draftedPlayer == q3

		/**************
			DEPTH 2
		 *************/
		// QB1, QB2 chosen
		def child1_1 = child1.children[0]                     // child1_1 means <1st pick branch #>_<2nd pick branch #>
		assert child1_1.parent == child1
		assert child1_1.draftedPlayer == q2

		// QB1, QB3 chosen
		def child1_2 = child1.children[1]
		assert child1_2.parent == child1
		assert child1_2.draftedPlayer == q3

		// QB2, QB1 chosen
		def child2_1 = child2.children[0]
		assert child2_1.parent == child2
		assert child2_1.draftedPlayer == q1

		// QB2, QB3 chosen
		def child2_2 = child2.children[1]
		assert child2_2.parent == child2
		assert child2_2.draftedPlayer == q3

		// QB3, QB1 chosen
		def child3_1 = child3.children[0]
		assert child3_1.parent == child3
		assert child3_1.draftedPlayer == q1

		// QB3, QB2 chosen
		def child3_2 = child3.children[1]
		assert child3_2.parent == child3
		assert child3_2.draftedPlayer == q2

		/*************
			DEPTH 3
		 ************/
		// QB1, QB2, QB3 chosen
		def child1_1_1 = child1_1.children[0]
		assert child1_1_1.parent == child1_1
		assert child1_1_1.draftedPlayer == q3

		// QB1, QB3, QB2 chosen
		def child1_2_1 = child1_2.children[0]
		assert child1_2_1.parent == child1_2
		assert child1_2_1.draftedPlayer == q2

		// QB2, QB1, QB3 chosen
		def child2_1_1 = child2_1.children[0]
		assert child2_1_1.parent == child2_1
		assert child2_1_1.draftedPlayer == q3

		// QB2, QB3, QB1 chosen
		def child2_2_1 = child2_2.children[0]
		assert child2_2_1.parent == child2_2
		assert child2_2_1.draftedPlayer == q1

		// QB3, QB1, QB2 chosen
		def child3_1_1 = child3_1.children[0]
		assert child3_1_1.parent == child3_1
		assert child3_1_1.draftedPlayer == q2

		// QB3, QB2, QB1 chosen
		def child3_2_1 = child3_2.children[0]
		assert child3_2_1.parent == child3_2
		assert child3_2_1.draftedPlayer == q1
	}

	void testConstructTree_ConfirmStructure_QB_RB_WR() {
		def system = new ESPNStandardScoringSystem()

		// Create players
		def p1 = new Player(name: "Quarterback", position: "QB")
		def p2 = new Player(name: "Running back", position: "RB")
		def p3 = new Player(name: "Wide Receiver", position: "WR")

		// Create projected stats
		def p1_stats = new FantasyPoints(player: p1, season: 2013, week: -1, system: system, points: 300)
		def p2_stats = new FantasyPoints(player: p2, season: 2013, week: -1, system: system, points: 290)
		def p3_stats = new FantasyPoints(player: p3, season: 2013, week: -1, system: system, points: 270)

		// Associate stats with players
		p1.fantasyPoints = new HashSet<FantasyPoints>([p1_stats])
		p2.fantasyPoints = new HashSet<FantasyPoints>([p2_stats])
		p3.fantasyPoints = new HashSet<FantasyPoints>([p3_stats])

		// Add players to tree list
		tree.players[MinimaxTree.QUARTERBACK_INDEX] << p1
		tree.players[MinimaxTree.RUNNING_BACK_INDEX] << p2
		tree.players[MinimaxTree.WIDE_RECEIVER_INDEX] << p3

		tree.constructTree()

		assert tree.root.children.size() == 3

		/*************
		 DEPTH 1
		 ************/
		// QB1 chosen
		def child1 = tree.root.children[0]
		assert child1.parent == tree.root
		assert child1.draftedPlayer == p1

		// QB2 chosen
		def child2 = tree.root.children[1]
		assert child2.parent == tree.root
		assert child2.draftedPlayer == p2

		// QB3 chosen
		def child3 = tree.root.children[2]
		assert child3.parent == tree.root
		assert child3.draftedPlayer == p3

		/**************
		 DEPTH 2
		 *************/
		// QB1, QB2 chosen
		def child1_1 = child1.children[0]                     // child1_1 means <1st pick branch #>_<2nd pick branch #>
		assert child1_1.parent == child1
		assert child1_1.draftedPlayer == p2

		// QB1, QB3 chosen
		def child1_2 = child1.children[1]
		assert child1_2.parent == child1
		assert child1_2.draftedPlayer == p3

		// QB2, QB1 chosen
		def child2_1 = child2.children[0]
		assert child2_1.parent == child2
		assert child2_1.draftedPlayer == p1

		// QB2, QB3 chosen
		def child2_2 = child2.children[1]
		assert child2_2.parent == child2
		assert child2_2.draftedPlayer == p3

		// QB3, QB1 chosen
		def child3_1 = child3.children[0]
		assert child3_1.parent == child3
		assert child3_1.draftedPlayer == p1

		// QB3, QB2 chosen
		def child3_2 = child3.children[1]
		assert child3_2.parent == child3
		assert child3_2.draftedPlayer == p2

		/*************
		 DEPTH 3
		 ************/
		// QB1, QB2, QB3 chosen
		def child1_1_1 = child1_1.children[0]
		assert child1_1_1.parent == child1_1
		assert child1_1_1.draftedPlayer == p3

		// QB1, QB3, QB2 chosen
		def child1_2_1 = child1_2.children[0]
		assert child1_2_1.parent == child1_2
		assert child1_2_1.draftedPlayer == p2

		// QB2, QB1, QB3 chosen
		def child2_1_1 = child2_1.children[0]
		assert child2_1_1.parent == child2_1
		assert child2_1_1.draftedPlayer == p3

		// QB2, QB3, QB1 chosen
		def child2_2_1 = child2_2.children[0]
		assert child2_2_1.parent == child2_2
		assert child2_2_1.draftedPlayer == p1

		// QB3, QB1, QB2 chosen
		def child3_1_1 = child3_1.children[0]
		assert child3_1_1.parent == child3_1
		assert child3_1_1.draftedPlayer == p2

		// QB3, QB2, QB1 chosen
		def child3_2_1 = child3_2.children[0]
		assert child3_2_1.parent == child3_2
		assert child3_2_1.draftedPlayer == p1
	}

	void testDetermineValue_FootballGuys_2Owners_IPickLast() {
		def system = new ESPNStandardScoringSystem()

		// Create players
		def q1 = new Player(name: "Quarterback 1", position: "QB")
		def q2 = new Player(name: "Quarterback 2", position: "QB")

		def r1 = new Player(name: "Running Back 1", position: "RB")
		def r2 = new Player(name: "Running Back 2", position: "RB")

		def w1 = new Player(name: "Wide Receiver 1", position: "WR")
		def w2 = new Player(name: "Wide Receiver 2", position: "WR")

		def k1 = new Player(name: "Kicker 1", position: "WR")
		def k2 = new Player(name: "Kicker 2", position: "WR")

		// Create projected stats
		def q1_stats = new FantasyPoints(player: q1, season: 2013, week: -1, system: system, points: 35)
		def q2_stats = new FantasyPoints(player: q2, season: 2013, week: -1, system: system, points: 33)

		def r1_stats = new FantasyPoints(player: r1, season: 2013, week: -1, system: system, points: 12)
		def r2_stats = new FantasyPoints(player: r2, season: 2013, week: -1, system: system, points: 7)

		def w1_stats = new FantasyPoints(player: w1, season: 2013, week: -1, system: system, points: 20)
		def w2_stats = new FantasyPoints(player: w1, season: 2013, week: -1, system: system, points: 5)

		def k1_stats = new FantasyPoints(player: k1, season: 2013, week: -1, system: system, points: 22)
		def k2_stats = new FantasyPoints(player: k2, season: 2013, week: -1, system: system, points: 21)

		// Create ADP
		def q1_adp = new AverageDraftPosition(player: q1, adp: 1, season: 2013)
		def q2_adp = new AverageDraftPosition(player: q2, adp: 2, season: 2013)

		def r1_adp = new AverageDraftPosition(player: r1, adp: 6, season: 2013)
		def r2_adp = new AverageDraftPosition(player: r2, adp: 7, season: 2013)

		def w1_adp = new AverageDraftPosition(player: w1, adp: 5, season: 2013)
		def w2_adp = new AverageDraftPosition(player: w2, adp: 8, season: 2013)

		def k1_adp = new AverageDraftPosition(player: k1, adp: 3, season: 2013)
		def k2_adp = new AverageDraftPosition(player: k2, adp: 4, season: 2013)

		// Associate stats with players
		q1.fantasyPoints = new HashSet<FantasyPoints>([q1_stats])
		q2.fantasyPoints = new HashSet<FantasyPoints>([q2_stats])

		r1.fantasyPoints = new HashSet<FantasyPoints>([r1_stats])
		r2.fantasyPoints = new HashSet<FantasyPoints>([r2_stats])

		w1.fantasyPoints = new HashSet<FantasyPoints>([w1_stats])
		w2.fantasyPoints = new HashSet<FantasyPoints>([w2_stats])

		k1.fantasyPoints = new HashSet<FantasyPoints>([k1_stats])
		k2.fantasyPoints = new HashSet<FantasyPoints>([k2_stats])

		// Associate ADP with players
		q1.averageDraftPositions = new HashSet<AverageDraftPosition>([q1_adp])
		q2.averageDraftPositions = new HashSet<AverageDraftPosition>([q2_adp])

		r1.averageDraftPositions = new HashSet<AverageDraftPosition>([r1_adp])
		r2.averageDraftPositions = new HashSet<AverageDraftPosition>([r2_adp])

		w1.averageDraftPositions = new HashSet<AverageDraftPosition>([w1_adp])
		w2.averageDraftPositions = new HashSet<AverageDraftPosition>([w2_adp])

		k1.averageDraftPositions = new HashSet<AverageDraftPosition>([k1_adp])
		k2.averageDraftPositions = new HashSet<AverageDraftPosition>([k2_adp])

		// Add players to tree list
		tree.players[MinimaxTree.QUARTERBACK_INDEX] << q1
		tree.players[MinimaxTree.QUARTERBACK_INDEX] << q2

		tree.players[MinimaxTree.RUNNING_BACK_INDEX] << r1
		tree.players[MinimaxTree.RUNNING_BACK_INDEX] << r2

		tree.players[MinimaxTree.WIDE_RECEIVER_INDEX] << w1
		tree.players[MinimaxTree.WIDE_RECEIVER_INDEX] << w2

		tree.players[MinimaxTree.KICKER_INDEX] << k1
		tree.players[MinimaxTree.KICKER_INDEX] << k2

		// Set up VORP
		tree.vorp[q1] = q1.fantasyPoints.toArray()[0].points - q2.fantasyPoints.toArray()[0].points
		tree.vorp[q2] = 0

		tree.vorp[r1] = r1.fantasyPoints.toArray()[0].points - r2.fantasyPoints.toArray()[0].points
		tree.vorp[r2] = 0

		tree.vorp[w1] = w1.fantasyPoints.toArray()[0].points - w2.fantasyPoints.toArray()[0].points
		tree.vorp[w2] = 0

		tree.vorp[k1] = k1.fantasyPoints.toArray()[0].points - k2.fantasyPoints.toArray()[0].points
		tree.vorp[k2] = 0

		tree.constructTree()

		assert tree.root.children.size() == 8

		/*
		 * Since right now the value function is just VORP, I should get the following
		 * possible combinations:
		 *
		 * Q1/R1/W1/K1 = 2 + 5 + 15 + 1 = 23
		 * Q1/R1/W1/K2 = 2 + 5 + 15 - 1 = 21
		 * Q1/R1/W2/K1 = 2 + 5 - 15 + 1 = -7
		 * Q1/R1/W2/K2 = 2 + 5 - 15 - 1 = -9
		 * Q1/R2/W1/K1 = 2 - 5 + 15 + 1 = 13
		 * Q1/R2/W1/K2 = 2 - 5 + 15 - 1 = 11
		 * Q1/R2/W2/K1 = 2 - 5 - 15 + 1 = -17
		 * Q1/R2/W2/K2 = 2 - 5 - 15 - 1 = -19
		 * Q2/R1/W1/K1 = -2 + 5 + 15 + 1 = 19
		 * Q2/R1/W1/K2 = -2 + 5 + 15 - 1 = 17
		 * Q2/R1/W2/K1 = -2 + 5 - 15 + 1 = -11
		 * Q2/R1/W2/K2 = -2 + 5 - 15 - 1 = -13
		 * Q2/R2/W1/K1 = -2 - 5 + 15 + 1 = 9
		 * Q2/R2/W1/K2 = -2 - 5 + 15 - 1 = 7
		 * Q2/R2/W2/K1 = -2 - 5 - 15 + 1 = -21
		 * Q2/R2/W2/K2 = -2 - 5 - 15 - 1 = -23
		 */

		/*
		 * They take QB1 first
		 * I take WR1          + 15
		 * They take K1
		 * I take RB1          +5
		 * They take RB2
		 * I take QB2          -2 0
		 * They take WR2
		 * I take K2           -1 0
		 */
		MinimaxTreeNode d1c1 = tree.root.children[0]
		assert d1c1.value == 20

		/*
		 * They take QB2 first
		 * I take WR1           +15
		 * They take K1
		 * I take RB1           +5
		 * They take  RB2
		 * I take QB1           +2
		 * They take WR2
		 * I take K2            -1   0
		 */
		MinimaxTreeNode d1c2 = tree.root.children[1]
		assert d1c2.value == 22

		/*
		 * They take RB1 first
		 * I take WR1           +15
		 * They take QB1
		 * I take K1            +1
		 * They take K2
		 * I take QB2           -2 0
		 * They take WR2
		 * I take RB2           -5 0
		 */
		MinimaxTreeNode d1c3 = tree.root.children[2]
		assert d1c3.value == 16

		/*
		 * They take RB2 first
		 * I take WR1           +15
		 * They take QB1
		 * I take RB1           +5
		 * They take K1
		 * I take K2            -1 0
		 * They take WR2
		 * I take QB2           -2 0
		 */
		MinimaxTreeNode d1c4 = tree.root.children[3]
		assert d1c4.value ==20

		/*
		 * They take WR1 first
		 * I take RB1           +5
		 * They take QB1
		 * I take K1            +1
		 * They take K2
		 * I take QB2           -2 0
		 * They take RB2
		 * I take WR2           -15 0
		 */
		MinimaxTreeNode d1c5 = tree.root.children[4]
		assert d1c5.value == 6

		/*
		 * They take WR2 first
		 * I take WR1           +15
		 * They take QB1
		 * I take RB1           +5
		 * They take K1
		 * I take K2            -1   0
		 * They take RB2
		 * I take QB2           -2   0
		 */
		MinimaxTreeNode d1c6 = tree.root.children[5]
		assert d1c6.value == 20

		/*
		 * They take K1 first
		 * I take WR1           +15
		 * They take QB1
		 * It take RB1          +5
		 * They take RB2
		 * I take K2            -1  0
		 * They take WR2
		 * I take QB2           -2  0
		 */
		MinimaxTreeNode d1c7 = tree.root.children[6]
		assert d1c7.value == 20

		/*
		 * They take K2 first
		 * I take WR1           +15
		 * They take QB1
		 * I take RB1           +5
		 * They take RB2
		 * I take K1            +1
		 * They take WR2
		 * I take QB2           -2   0
		 */
		MinimaxTreeNode d1c8 = tree.root.children[7]
		assert d1c8.value == 21
	}

	/*void testConstructTree() {
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
	}    */
}

package com.traderapist.draft

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 2/22/13
 * Time: 7:58 PM
 * To change this template use File | Settings | File Templates.
 */
class MinimaxTree {
	/**
	 * The index of the inner list in the players list for quarterbacks.
	 */
	public static int QUARTERBACK_INDEX = 0;

	/**
	 * The index of the inner list in the players list for running backs.
	 */
	public static int RUNNING_BACK_INDEX = 1;

	/**
	 * The index of the inner list in the players list for wide receivers.
	 */
	public static int WIDE_RECEIVER_INDEX = 2;

	/**
	 * The index of the inner list in the players list for tight ends.
	 */
	public static int TIGHT_END_INDEX = 3;

	/**
	 * The index of the inner list in the players list for defenses.
	 */
	public static int DST_INDEX = 4;

	/**
	 * The index of the inner list in the players list for kickers.
	 */
	public static int KICKER_INDEX = 5;

	/**
	 * The collection of all players available in the draft.  For ease of finding
	 * players, the list is broken into six inner lists, one for each position.
	 *
	 * The position indexes are as follows:
	 * 0- Quarterbacks
	 * 1- Running backs
	 * 2- Wide receivers
	 * 3- Tight ends
	 * 4- Defense/Special Teams
	 * 5- Kickers
	 */
	def players = [[],[],[],[],[],[]]

	/**
	 * The root node of our tree.
	 */
	MinimaxTreeNode root

	/**
	 * Creates the entire tree from the list of players.
	 */
	def constructTree() {
		root = new MinimaxTreeNode()

		constructTree(root)
	}

	/**
	 * Builds out the tree assuming node as the root.
	 *
	 * @param node      The root node of this subtree.
	 */
	def constructTree(MinimaxTreeNode node) {
		for(int i=0; i<players.size(); i++) {
			for(int j=0; j<players[i].size(); j++) {
				// The player at index j is going to be drafted for this iteration of the
				// loop, so build the child without him.
				def draftedPlayer = players[i].remove(j)

				// Create a new child node that represents the current player being drafted.
				def child = new MinimaxTreeNode(players: players, draftedPlayer: draftedPlayer, parent: node)

				// Add our newly-created child to the current root's list of children.
				node.children << child

				// Construct the subtree for the child we just created.
				constructTree(child)

				// Put the drafted player back in the list since the next iteration will
				// mean a different player has been drafted.
				players[i].add(j, draftedPlayer)
			}
		}
	}
}

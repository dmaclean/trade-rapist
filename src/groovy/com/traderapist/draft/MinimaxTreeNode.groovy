package com.traderapist.draft

import com.traderapist.models.Player

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 2/22/13
 * Time: 7:40 AM
 * To change this template use File | Settings | File Templates.
 */
class MinimaxTreeNode {
	/**
	 * The perceived value of this state to the owner we're creating the tree for.
	 */
	Short value

	/**
	 * The parent node in the tree.
	 */
	MinimaxTreeNode parent

	/**
	 * Reference to the player that was drafted to get us to this scenario.
	 */
	Player draftedPlayer

	/**
	 * The players that are still available in this round.
	 */
	def players = []

	/**
	 * All potential states after the owner makes their pick.
	 */
	def children = []

	/**
	 *
	 *
	 * @return
	 */
	def getOptimalChoice(useMax = true) {
		def bestNode

		for(child in children) {
			if (bestNode == null || (useMax && child.value > bestNode.value) || (!useMax && child.value < bestNode.value)) {
				bestNode = child
			}
		}

		return bestNode
	}
}

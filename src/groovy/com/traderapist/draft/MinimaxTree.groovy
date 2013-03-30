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
	 * Constant to represent a snake draft.  We'll assume this type unless
	 * someone changes it.
	 */
	public static int DRAFT_TYPE_SNAKE = 1

	/**
	 * The draft pick number that I have.  This will be zero-based, so the first
	 * pick will be 0, second will be 1, etc...
	 */
	Integer myPick

	/**
	 * The type of draft we're doing.  Defaults to Snake.
	 */
	Integer draftType = DRAFT_TYPE_SNAKE

	/**
	 * The number of owners participating in the draft.
	 */
	Integer numOwners

	/**
	 * The collection of all players available in the draft.  For ease of finding
	 * players, the list is broken into six inner lists, one for each position.
	 * This class assumes the lists are sorted by highest points.
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

	def vorp = [:]

	/**
	 * The root node of our tree.
	 */
	MinimaxTreeNode root

	def queue = []

	/**
	 * Creates the entire tree from the list of players.
	 */
	def constructTree() {
		// Runtime instance so we can interrogate memory usage.
		Runtime rt = Runtime.getRuntime()
		double memoryThreshold = 0.95
		long nodesProcessed = 0
		int maxDepth = 0

		// Create the root and set its player list to players so it has all players as possible picks.
		root = new MinimaxTreeNode(players: players, depth: 0, value: 0)

		queue << root

		while(!queue.isEmpty()) {
			/*
				Grab the node at the front of the list
			 */
			def currNode = queue.remove(0)

			/*
				Go through this node's available players and create a
				node to simulate each one being drafted.
			 */
			for(int i=0; i<currNode.players.size(); i++) {
				for(int j=0; j<currNode.players[i].size(); j++) {
					// The player at index j is going to be drafted for this iteration of the
					// loop, so build the child without him.
					def draftedPlayer = currNode.players[i].remove(j)
					def dValue = (vorp[draftedPlayer] == null) ? 0 : vorp[draftedPlayer]

					def child = new MinimaxTreeNode(players: copyPlayerList(currNode.players),
													draftedPlayer: draftedPlayer,
													parent: currNode,
													depth: currNode.depth+1,
													value: currNode.value + dValue)

					// Add our newly-created child to the current root's list of children.
					currNode.children << child

					// Add this child to the queue for later processing.
					queue << child

					// Put the drafted player back in the list since the next iteration will
					// mean a different player has been drafted.
					currNode.players[i].add(j, draftedPlayer)

					nodesProcessed++

					if(child.depth > maxDepth) {
						maxDepth = child.depth
					}
				}
			}

			if (queue.size() % 100 == 0) {
				print "Queue size is ${queue.size()}; Processed ${nodesProcessed } nodes; Max depth reached is ${maxDepth }"
				print("\t\tMemory stats (max/total/free): ${rt.maxMemory()/1000000}/${rt.totalMemory()/1000000}/${rt.freeMemory()/1000000}")
			}

			/*
				Make sure we're not about to run out of memory.  If so, stop processing for now.
			 */
			if(rt.freeMemory()/rt.maxMemory() > memoryThreshold) {
				break
			}
		}
	}

	def minimax(MinimaxTreeNode node, int depth) {
		// Terminal node
		if (node.children.size() == 0) {
			// apply value function
		}
		else {
			// For my picks, take the lowest valued child

			// For other owners' picks, take the highest valued child

		}
	}

	/**
	 * Determine whether it is my pick based on the depth of the tree that we're analyzing.
	 *
	 * @param depth     The tree depth we're currently at.
	 */
	def isMyPick(int depth) {
		// If it's a snake draft, are we going forwards or backwards through the owners?
		if (draftType == DRAFT_TYPE_SNAKE) {
			/*
			 * Forward
			 *
			 * Assume 10 owners
			 * Picks p={0-9} (first round) will yield p/#owners = 0
			 * Picks p={20-29} (third round) will yield p/#owners = 2
			 */
			if ( ((Integer) (depth/numOwners)) % 2I == 0) {      // This integer casting is bullshit
				return depth % numOwners == myPick
			}
			/*
			 * Backwards
			 *
			 * Picks p={10-19} (second round) will yield p/#owners = 1
			 * Picks p={30-39} (fourth round) will yield p/#owners = 3
			 */
			else {
				def reversePick = numOwners - myPick - 1
				return depth % numOwners == reversePick
			}
		}
	}

	/**
	 * Builds out the tree assuming node as the root.
	 *
	 * @param node      The root node of this subtree.
	 */
	def constructTree(MinimaxTreeNode node, depth) {
//		if (depth%10 == 0) {
			Runtime rt = Runtime.getRuntime()
			print("Memory stats (used/free) at depth ${depth}: ${rt.totalMemory()/1000000}/${rt.freeMemory()/1000000}")
//		}

		for(int i=0; i<players.size(); i++) {
			for(int j=0; j<players[i].size(); j++) {
				// The player at index j is going to be drafted for this iteration of the
				// loop, so build the child without him.
				def draftedPlayer = players[i].remove(j)

				def child = new MinimaxTreeNode(players: players, draftedPlayer: draftedPlayer, parent: node)

				// Add our newly-created child to the current root's list of children.
				node.children << child

				// Construct the subtree for the child we just created.
				constructTree(child, depth+1)

				// Put the drafted player back in the list since the next iteration will
				// mean a different player has been drafted.
				players[i].add(j, draftedPlayer)
			}
		}
	}

	/**
	 * Create a shallow (sort-of) copy of a player list.  Whereas clone() would just
	 * create a shallow copy of the list, this will create a new list and copy the
	 * references of all the contents into it.
	 *
	 * @param list      The list whose contents we'd like to copy.
	 */
	def copyPlayerList(list) {
		def copy = [[],[],[],[],[],[]]

		int i=0
		for(positionList in list) {
			for(player in positionList) {
				copy[i] << player
			}

			i++
		}

		return copy
	}
}

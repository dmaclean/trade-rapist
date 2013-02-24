package com.traderapist.draft

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 2/22/13
 * Time: 7:09 PM
 * To change this template use File | Settings | File Templates.
 */
@TestMixin(GrailsUnitTestMixin)
class MinimaxTreeNodeTests {

	MinimaxTreeNode node

	void setUp() {
		node = new MinimaxTreeNode()
	}

	void tearDown() {
		node = null
	}

	void testGetOptimalChoice_Max() {
		def children = []
		for(i in 1..5) {
			children.add(new MinimaxTreeNode(value: i, children: null))
		}
		node.children = children

		assert node.getOptimalChoice().value == 5
	}

	void testGetOptimalChoice_Min() {
		def children = []
		for(i in 1..5) {
			children.add(new MinimaxTreeNode(value: i, children: null))
		}
		node.children = children

		assert node.getOptimalChoice(false).value == 1
	}
}

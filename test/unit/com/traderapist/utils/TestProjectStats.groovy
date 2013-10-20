package com.traderapist.utils

import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: dmaclean
 * Date: 10/19/13
 * Time: 10:42 PM
 * To change this template use File | Settings | File Templates.
 */
class TestProjectStats extends TestCase {
	ProjectStats ps

	@Before
	void setUp() {
		ps = new ProjectStats()
	}

	@After
	void tearDown() {
		ps = null
	}

	/**
	 * Patriots 2012 week 7
	 *
	 * Opponent - Jets
	 * Jets previous opponents (passing yards)
	 * 1- Buffalo              (195 - .809)
	 * 2- Pittsburgh           (265 - 1.099)
	 * 3- Miami                (196 - 0.81)
	 * 4- San Francisco        (134 - 0.55)
	 * 5- Houston              (209 - 0.86)
	 * 6- Indy                 (257 - 1.06)
	 * avg 0.868
	 *
	 * Avg passing yards/game through week 6 - 240.93
	 */
	@Test
	void testGetOpponentPassingYardsFactor_Season2012Week7() {
		def season = 2012
		def week = 7
		def opponent = "NYJ"

		def factor = ps.getOpponentPassingYardsFactor(opponent, season, week)

		assert "Expected 0.868, got ${factor}", factor > 0.86 && factor < 0.87
	}
}

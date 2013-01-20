package com.traderapist.scoringsystem

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

import com.traderapist.models.Stat
import com.traderapist.constants.FantasyConstants

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class ESPNStandardScoringSystemTests {

    IFantasyScoringSystem system

    void setUp() {
        system = new ESPNStandardScoringSystem()
    }

    void tearDown() {
        system = null
    }

    void testCalculateScore_PassingYards() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_PASSING_YARDS);
        stat.setStatValue(10);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 0.4, got " + value, value == 0.4);

        stat.setStatValue(12);
        value = system.calculateScore(stats);
        assertTrue("Expected 0.48, got " + value, value == 0.48);
    }

    void testCalculateScore_PassingTouchdowns() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_PASSING_TOUCHDOWNS);
        stat.setStatValue(3);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 12, got " + value, value == 12);

        stat.setStatValue(1);
        value = system.calculateScore(stats);
        assertTrue("Expected 4, got " + value, value == 4);
    }

    @Test
    public void testCalculateScore_FortyYardPassingTouchdowns() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_FORTY_PLUS_YARD_PASSING_TOUCHDOWNS);
        stat.setStatValue(3);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 6, got " + value, value == 6);

        stat.setStatValue(1);
        value = system.calculateScore(stats);
        assertTrue("Expected 2, got " + value, value == 2);
    }

    @Test
    public void testCalculateScore_Interceptions() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_INTERCEPTIONS);
        stat.setStatValue(1);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected -2, got " + value, value == -2);

        stat.setStatValue(2);
        value = system.calculateScore(stats);
        assertTrue("Expected -4, got " + value, value == -4);
    }

    @Test
    public void testCalculateScore_2PointConversion() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_TWO_POINT_CONVERSIONS);
        stat.setStatValue(1);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 2, got " + value, value == 2);

        stat.setStatValue(2);
        value = system.calculateScore(stats);
        assertTrue("Expected 4, got " + value, value == 4);
    }

    @Test
    public void testCalculateScore_RushingYards() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_RUSHING_YARDS);
        stat.setStatValue(75);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 7.5, got " + value, value == 7.5);

        stat.setStatValue(100);
        value = system.calculateScore(stats);
        assertTrue("Expected 10, got " + value, value == 10);
    }

    @Test
    public void testCalculateScore_RushingTouchdowns() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_RUSHING_TOUCHDOWNS);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 12, got " + value, value == 12);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 18, got " + value, value == 18);
    }

    @Test
    public void testCalculateScore_FortyYardRushingTouchdowns() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_FORTY_PLUS_YARD_RUSHING_TOUCHDOWNS);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 4, got " + value, value == 4);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 6, got " + value, value == 6);
    }

    @Test
    public void testCalculateScore_TwoPointRushingConversion() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_TWO_POINT_CONVERSIONS);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 4, got " + value, value == 4);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 6, got " + value, value == 6);
    }

    @Test
    public void testCalculateScore_ReceivingYards() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_RECEPTION_YARDS);
        stat.setStatValue(81);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 8.1, got " + value, value == 8.1);

        stat.setStatValue(-10);
        value = system.calculateScore(stats);
        assertTrue("Expected -1, got " + value, value == -1);
    }

    @Test
    public void testCalculateScore_ReceivingTouchdownss() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_RECEPTION_TOUCHDOWNS);
        stat.setStatValue(0);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 0, got " + value, value == 0);

        stat.setStatValue(2);
        value = system.calculateScore(stats);
        assertTrue("Expected 12, got " + value, value == 12);
    }

    @Test
    public void testCalculateScore_FortyYardReceivingTouchdownss() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_FORTY_PLUS_YARD_RECEPTION_TOUCHDOWNS);
        stat.setStatValue(1);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 2, got " + value, value == 2);

        stat.setStatValue(2);
        value = system.calculateScore(stats);
        assertTrue("Expected 4, got " + value, value == 4);
    }

    @Test
    public void testCalculateScore_TwoPointReceivingConversion() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_TWO_POINT_CONVERSIONS);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 4, got " + value, value == 4);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 6, got " + value, value == 6);
    }

    @Test
    public void testCalculateScore_KickoffReturnTouchdowns() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_KICKOFF_AND_PUNT_RETURN_TOUCHDOWNS);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 12, got " + value, value == 12);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 18, got " + value, value == 18);
    }

//	@Test
//	public void testCalculateScore_FumbleRecoveryForTouchdowns() {
//		Stat stat = new Stat();
//		stat.setStatKey(FantasyConstants.STAT_);
//		stat.setStatValue(2);
//
//		List stats = []
//		stats.add(stat);
//
//		double value = system.calculateScore(stats);
//		assertTrue("Expected 12, got " + value, value == 12);
//
//		stat.setStatValue(3);
//		value = system.calculateScore(stats);
//		assertTrue("Expected 18, got " + value, value == 18);
//	}

    @Test
    public void testCalculateScore_FumblesLost() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_FUMBLES_LOST);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected -4, got " + value, value == -4);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected -6, got " + value, value == -6);
    }

    @Test
    public void testCalculateScore_PATMade() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_POINT_AFTER_ATTEMPT_MADE);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 2, got " + value, value == 2);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 3, got " + value, value == 3);
    }

    @Test
    public void testCalculateScore_FieldGoal0_19Made() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_FIELD_GOALS_0_19_YARDS);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 6, got " + value, value == 6);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 9, got " + value, value == 9);
    }

    @Test
    public void testCalculateScore_FieldGoal20_29Made() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_FIELD_GOALS_20_29_YARDS);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 6, got " + value, value == 6);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 9, got " + value, value == 9);
    }

    @Test
    public void testCalculateScore_FieldGoal30_39Made() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_FIELD_GOALS_30_39_YARDS);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 6, got " + value, value == 6);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 9, got " + value, value == 9);
    }

    @Test
    public void testCalculateScore_FieldGoal40_49Made() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_FIELD_GOALS_40_49_YARDS);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 8, got " + value, value == 8);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 12, got " + value, value == 12);
    }

    @Test
    public void testCalculateScore_FieldGoal50plusMade() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_FIELD_GOALS_50_PLUS_YARDS);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 10, got " + value, value == 10);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 15, got " + value, value == 15);
    }

    @Test
    public void testCalculateScore_FieldGoal0_19Missed() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_FIELD_GOALS_MISSED_0_19_YARDS);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected -4, got " + value, value == -4);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected -6, got " + value, value == -6);
    }

    @Test
    public void testCalculateScore_FieldGoal20_29Missed() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_FIELD_GOALS_MISSED_20_29_YARDS);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected -4, got " + value, value == -4);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected -6, got " + value, value == -6);
    }

    @Test
    public void testCalculateScore_FieldGoal30_39Missed() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_FIELD_GOALS_MISSED_30_39_YARDS);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected -4, got " + value, value == -4);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected -6, got " + value, value == -6);
    }

    @Test
    public void testCalculateScore_FieldGoal40_49Missed() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_FIELD_GOALS_MISSED_40_49_YARDS);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected -2, got " + value, value == -2);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected -3, got " + value, value == -3);
    }

    @Test
    public void testCalculateScore_FieldGoal50plussMissed() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_FIELD_GOALS_MISSED_50_PLUS_YARDS);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected -2, got " + value, value == -2);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected -3, got " + value, value == -3);
    }

    @Test
    public void testCalculateScore_TeamSacks() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_SACK);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 2, got " + value, value == 2);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 3, got " + value, value == 3);
    }

    @Test
    public void testCalculateScore_InterceptionReturnTouchdown() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_DEFENSIVE_TOUCHDOWN);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 6, got " + value, value == 6);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 9, got " + value, value == 9);
    }

    /*@Test
    public void testCalculateScore_KickoffAndPuntReturnTouchdown() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_KICKOFF_AND_PUNT_RETURN_TOUCHDOWNS);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 6, got ${value}", value == 6);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 9, got " + value, value == 9);
    }*/

    @Test
    public void testCalculateScore_BlockedKick() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_BLOCK_KICK);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 4, got " + value, value == 4);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 6, got " + value, value == 6);
    }

    @Test
    public void testCalculateScore_Interception() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_INTERCEPTION);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 4, got " + value, value == 4);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 6, got " + value, value == 6);
    }

    @Test
    public void testCalculateScore_FumbleRecovered() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_FUMBLE_RECOVERY);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 4, got " + value, value == 4);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 6, got " + value, value == 6);
    }

    @Test
    public void testCalculateScore_Safety() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_SAFETY);
        stat.setStatValue(2);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 4, got " + value, value == 4);

        stat.setStatValue(3);
        value = system.calculateScore(stats);
        assertTrue("Expected 6, got " + value, value == 6);
    }

    @Test
    public void testCalculateScore_0PointsAllowed() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_POINTS_ALLOWED);
        stat.setStatValue(0);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 10, got " + value, value == 10);
    }

    @Test
    public void testCalculateScore_1_6PointsAllowed() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_POINTS_ALLOWED);
        stat.setStatValue(6);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 7, got " + value, value == 7);
    }

    @Test
    public void testCalculateScore_7_13PointsAllowed() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_POINTS_ALLOWED);
        stat.setStatValue(13);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 4, got " + value, value == 4);
    }

    @Test
    public void testCalculateScore_14_21PointsAllowed() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_POINTS_ALLOWED);
        stat.setStatValue(17);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected 1, got " + value, value == 1);
    }

    @Test
    public void testCalculateScore_22_27PointsAllowed() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_POINTS_ALLOWED);
        stat.setStatValue(27);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected -1, got " + value, value == -1);
    }

    @Test
    public void testCalculateScore_28_34PointsAllowed() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_POINTS_ALLOWED);
        stat.setStatValue(34);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected -4, got " + value, value == -4);
    }

    @Test
    public void testCalculateScore_35_45PointsAllowed() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_POINTS_ALLOWED);
        stat.setStatValue(45);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected -7, got " + value, value == -7);
    }

    @Test
    public void testCalculateScore_46plusPointsAllowed() {
        Stat stat = new Stat();
        stat.setStatKey(FantasyConstants.STAT_POINTS_ALLOWED);
        stat.setStatValue(46);

        List stats = []
        stats.add(stat);

        double value = system.calculateScore(stats);
        assertTrue("Expected -10, got " + value, value == -10);
    }
}

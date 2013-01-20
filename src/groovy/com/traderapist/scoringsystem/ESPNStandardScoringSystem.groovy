package com.traderapist.scoringsystem

import com.traderapist.constants.FantasyConstants

class ESPNStandardScoringSystem implements IFantasyScoringSystem {
    /**
     * Calculates the fantasy score of a given collection of Stat objects.
     */
    public double calculateScore(List stats) {
        double totalPoints = 0.0

        for(stat in stats) {
            double points = 0.0

            /*
             * Passing
             */
            if(stat.statKey == FantasyConstants.STAT_PASSING_YARDS)
                points += (stat.statValue/5) * 0.2;
            else if(stat.getStatKey() == FantasyConstants.STAT_PASSING_TOUCHDOWNS)
                points += stat.getStatValue() * 4;
            else if(stat.getStatKey() == FantasyConstants.STAT_FORTY_PLUS_YARD_PASSING_TOUCHDOWNS)
                points += stat.getStatValue() * 2;
            else if(stat.getStatKey() == FantasyConstants.STAT_INTERCEPTIONS)
                points -= stat.getStatValue() * 2;
            else if(stat.getStatKey() == FantasyConstants.STAT_TWO_POINT_CONVERSIONS)
                points += stat.getStatValue() * 2;

            /*
             * Rushing
             */
            else if(stat.getStatKey() == FantasyConstants.STAT_RUSHING_YARDS)
                points += stat.getStatValue() * 0.1;
            else if(stat.getStatKey() == FantasyConstants.STAT_RUSHING_TOUCHDOWNS)
                points += stat.getStatValue() * 6;
            else if(stat.getStatKey() == FantasyConstants.STAT_FORTY_PLUS_YARD_RUSHING_TOUCHDOWNS)
                points += stat.getStatValue() * 2;

            /*
             * Receiving
             */
            else if(stat.getStatKey() == FantasyConstants.STAT_RECEPTION_YARDS)
                points += stat.getStatValue() * 0.1;
            else if(stat.getStatKey() == FantasyConstants.STAT_RECEPTION_TOUCHDOWNS)
                points += stat.getStatValue() * 6;
            else if(stat.getStatKey() == FantasyConstants.STAT_FORTY_PLUS_YARD_RECEPTION_TOUCHDOWNS)
                points += stat.getStatValue() * 2;

            /*
             * Miscellaneous - missing fumbles recovered for touchdown
             * (check Brandon Lloyd vs Texans week 13 in 2012)
             */
            else if(stat.getStatKey() == FantasyConstants.STAT_KICKOFF_AND_PUNT_RETURN_TOUCHDOWNS)
                points += stat.getStatValue() * 6;
            else if(stat.getStatKey() == FantasyConstants.STAT_FUMBLES_LOST)
                points -= stat.getStatValue() * 2;

            /*
             * Kicking
             */
            else if(stat.getStatKey() == FantasyConstants.STAT_POINT_AFTER_ATTEMPT_MADE)
                points += stat.getStatValue() * 1;
            else if(stat.getStatKey() == FantasyConstants.STAT_FIELD_GOALS_0_19_YARDS)
                points += stat.getStatValue() * 3;
            else if(stat.getStatKey() == FantasyConstants.STAT_FIELD_GOALS_20_29_YARDS)
                points += stat.getStatValue() * 3;
            else if(stat.getStatKey() == FantasyConstants.STAT_FIELD_GOALS_30_39_YARDS)
                points += stat.getStatValue() * 3;
            else if(stat.getStatKey() == FantasyConstants.STAT_FIELD_GOALS_40_49_YARDS)
                points += stat.getStatValue() * 4;
            else if(stat.getStatKey() == FantasyConstants.STAT_FIELD_GOALS_50_PLUS_YARDS)
                points += stat.getStatValue() * 5;
            else if(stat.getStatKey() == FantasyConstants.STAT_FIELD_GOALS_MISSED_0_19_YARDS)
                points -= stat.getStatValue() * 2;
            else if(stat.getStatKey() == FantasyConstants.STAT_FIELD_GOALS_MISSED_20_29_YARDS)
                points -= stat.getStatValue() * 2;
            else if(stat.getStatKey() == FantasyConstants.STAT_FIELD_GOALS_MISSED_30_39_YARDS)
                points -= stat.getStatValue() * 2;
            else if(stat.getStatKey() == FantasyConstants.STAT_FIELD_GOALS_MISSED_40_49_YARDS)
                points -= stat.getStatValue() * 1;
            else if(stat.getStatKey() == FantasyConstants.STAT_FIELD_GOALS_MISSED_50_PLUS_YARDS)
                points -= stat.getStatValue() * 1;

            /*
             * Team Defense/Special Teams
             */
            else if(stat.getStatKey() == FantasyConstants.STAT_SACK)
                points += stat.getStatValue() * 1;
            // Fumble and Interception returns.
            else if(stat.getStatKey() == FantasyConstants.STAT_DEFENSIVE_TOUCHDOWN)
                points += stat.getStatValue() * 3;
            else if(stat.getStatKey() == FantasyConstants.STAT_KICKOFF_AND_PUNT_RETURN_TOUCHDOWNS)
                points += stat.getStatValue() * 3;
            else if(stat.getStatKey() == FantasyConstants.STAT_BLOCK_KICK)
                points += stat.getStatValue() * 2;
            else if(stat.getStatKey() == FantasyConstants.STAT_INTERCEPTION)
                points += stat.getStatValue() * 2;
            else if(stat.getStatKey() == FantasyConstants.STAT_FUMBLE_RECOVERY)
                points += stat.getStatValue() * 2;
            else if(stat.getStatKey() == FantasyConstants.STAT_SAFETY)
                points += stat.getStatValue() * 2;
            else if(stat.getStatKey() == FantasyConstants.STAT_POINTS_ALLOWED && stat.getStatValue() == 0)
                points += 10;
            else if(stat.getStatKey() == FantasyConstants.STAT_POINTS_ALLOWED &&
                    stat.getStatValue() >= 1 && stat.getStatValue() <= 6)
                points += 7;
            else if(stat.getStatKey() == FantasyConstants.STAT_POINTS_ALLOWED &&
                    stat.getStatValue() >= 7 && stat.getStatValue() <= 13)
                points += 4;
            else if(stat.getStatKey() == FantasyConstants.STAT_POINTS_ALLOWED &&
                    stat.getStatValue() >= 14 && stat.getStatValue() <= 21)
                points += 1;
            else if(stat.getStatKey() == FantasyConstants.STAT_POINTS_ALLOWED &&
                    stat.getStatValue() >= 22 && stat.getStatValue() <= 27)
                points += -1;
            else if(stat.getStatKey() == FantasyConstants.STAT_POINTS_ALLOWED &&
                    stat.getStatValue() >= 28 && stat.getStatValue() <= 34)
                points += -4;
            else if(stat.getStatKey() == FantasyConstants.STAT_POINTS_ALLOWED &&
                    stat.getStatValue() >= 35 && stat.getStatValue() <= 45)
                points += -7;
            else if(stat.getStatKey() == FantasyConstants.STAT_POINTS_ALLOWED && stat.getStatValue() >= 46)
                points += -10;

            stat.points = points

            totalPoints += points;
        }

        return totalPoints;
    }
}
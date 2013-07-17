package com.traderapist.constants;

import java.util.HashMap;

public class FantasyConstants {
	/**
	 * Translation table between the Fantasy year and the Yahoo code representing that year.
	 */
	public static HashMap<Integer, String> yearToCode;
	static {
		yearToCode = new HashMap<Integer, String>(20);
		yearToCode.put(2001, "57");
		yearToCode.put(2002, "49");
		yearToCode.put(2003, "79");
		yearToCode.put(2004, "101");
		yearToCode.put(2005, "124");
		yearToCode.put(2006, "153");
		yearToCode.put(2007, "175");
		yearToCode.put(2008, "199");
		yearToCode.put(2009, "223");
		yearToCode.put(2010, "242");
		yearToCode.put(2011, "257");
		yearToCode.put(2012, "273");
	}
	
	/**
	 * Association table to keep track of the public league we're gathering player data from for
	 * each fantasy year.
	 */
	public static HashMap<Integer, String> yearToLeagueKey;
	static {
		yearToLeagueKey = new HashMap<Integer, String>(20);
		yearToLeagueKey.put(2001, yearToCode.get(2001) + ".l.431");
		yearToLeagueKey.put(2002, yearToCode.get(2002) + ".l.431");
		yearToLeagueKey.put(2003, yearToCode.get(2003) + ".l.433");
		yearToLeagueKey.put(2004, yearToCode.get(2004) + ".l.436");
		yearToLeagueKey.put(2005, yearToCode.get(2005) + ".l.431");
		yearToLeagueKey.put(2006, yearToCode.get(2006) + ".l.436");
		yearToLeagueKey.put(2007, yearToCode.get(2007) + ".l.436");
		yearToLeagueKey.put(2008, yearToCode.get(2008) + ".l.437");
		yearToLeagueKey.put(2009, yearToCode.get(2009) + ".l.431");
		yearToLeagueKey.put(2010, yearToCode.get(2010) + ".l.439");
		yearToLeagueKey.put(2011, yearToCode.get(2011) + ".l.450");
		yearToLeagueKey.put(2012, yearToCode.get(2012) + ".l.56212");
	}
	
	/**
	 * Consumer key for OAuth
	 */
	public static final String consumer_key = "dj0yJmk9Y2RLeUNiTkdPeUxTJmQ9WVdrOVozZzRjSGhETTJNbWNHbzlOemt6TnpNMk1nLS0mcz1jb25zdW1lcnNlY3JldCZ4PWYy";

	/**
	 * Consumer secret for OAuth
	 */
	public static final String consumer_secret = "86712de54c4479c386538e80520269970a7db725";
	
	public static final int HTTP_RESPONSE_OK = 200;
	
	public static final int ERROR_999_TIMEOUT_MILLISECONDS = 1000*60*10;	// 10 minutes
	
	public static final String URL_SEASON_STATS = "http://fantasysports.yahooapis.com/fantasy/v2/player/{year}.p.{player_id}/stats?format=json";
	public static final String URL_SEASON_ALL_PLAYERS = "http://fantasysports.yahooapis.com/fantasy/v2/league/{league_key}/players;start={start}/stats?format=json";
	public static final String URL_WEEK_STATS = "http://fantasysports.yahooapis.com/fantasy/v2/player/{year}.p.{player_id}/stats;type=week;week={week}?format=json";
	
	public static final int STAT_GAMES_PLAYED = 0;
	public static final int STAT_PASSING_ATTEMPTS = 1;
	public static final int STAT_COMPLETIONS = 2;
	public static final int STAT_INCOMPLETE_PASSES = 3;
	public static final int STAT_PASSING_YARDS = 4;
	public static final int STAT_PASSING_TOUCHDOWNS = 5;
	public static final int STAT_INTERCEPTIONS = 6;
	public static final int STAT_SACKS = 7;
	public static final int STAT_RUSHING_ATTEMPTS = 8;
	public static final int STAT_RUSHING_YARDS = 9;
	public static final int STAT_RUSHING_TOUCHDOWNS = 10;
	public static final int STAT_RECEPTIONS = 11;
	public static final int STAT_RECEPTION_YARDS = 12;
	public static final int STAT_RECEPTION_TOUCHDOWNS = 13;
	public static final int STAT_RETURN_YARDS = 14;
	public static final int STAT_RETURN_TOUCHDOWNS = 15;
	public static final int STAT_TWO_POINT_CONVERSIONS = 16;
	public static final int STAT_FUMBLES = 17;
	public static final int STAT_FUMBLES_LOST = 18;
	public static final int STAT_FIELD_GOALS_0_19_YARDS = 19;
	public static final int STAT_FIELD_GOALS_20_29_YARDS = 20;
	public static final int STAT_FIELD_GOALS_30_39_YARDS = 21;
	public static final int STAT_FIELD_GOALS_40_49_YARDS = 22;
	public static final int STAT_FIELD_GOALS_50_PLUS_YARDS = 23;
	public static final int STAT_FIELD_GOALS_MISSED_0_19_YARDS = 24;
	public static final int STAT_FIELD_GOALS_MISSED_20_29_YARDS = 25;
	public static final int STAT_FIELD_GOALS_MISSED_30_39_YARDS = 26;
	public static final int STAT_FIELD_GOALS_MISSED_40_49_YARDS = 27;
	public static final int STAT_FIELD_GOALS_MISSED_50_PLUS_YARDS = 28;
	public static final int STAT_POINT_AFTER_ATTEMPT_MADE = 29;
	public static final int STAT_POINT_AFTER_ATTEMPT_MISSED = 30;
	public static final int STAT_POINTS_ALLOWED = 31;
	public static final int STAT_SACK = 32;
	public static final int STAT_INTERCEPTION = 33;
	public static final int STAT_FUMBLE_RECOVERY = 34;
	public static final int STAT_TOUCHDOWN = 35;
	public static final int STAT_SAFETY = 36;
	public static final int STAT_BLOCK_KICK = 37;
	public static final int STAT_TACKLE_SOLO = 38;
	public static final int STAT_TACKLE_ASSIST = 39;
	public static final int STAT_SACK_PLAYER = 40;
	public static final int STAT_INTERCEPTION_PLAYER = 41;
	public static final int STAT_FUMBLE_FORCE = 42;
	public static final int STAT_FUMBLE_RECOVERY_PLAYER = 43;
	public static final int STAT_DEFENSIVE_TOUCHDOWN = 44;
	public static final int STAT_SAFETY_PLAYER = 45;
	public static final int STAT_PASS_DEFENDED = 46;
	public static final int STAT_BLOCK_KICK_PLAYER = 47;
	public static final int STAT_RETURN_YARDS_TEAM = 48;
	public static final int STAT_KICKOFF_AND_PUNT_RETURN_TOUCHDOWNS = 49;
	public static final int STAT_POINTS_ALLOWED_0 = 50;
	public static final int STAT_POINTS_ALLOWED_1_6 = 51;
	public static final int STAT_POINTS_ALLOWED_7_13 = 52;
	public static final int STAT_POINTS_ALLOWED_14_20 = 53;
	public static final int STAT_POINTS_ALLOWED_21_27 = 54;
	public static final int STAT_POINTS_ALLOWED_28_34 = 55;
	public static final int STAT_POINTS_ALLOWED_35_PLUS = 56;
	public static final int STAT_OFFENSIVE_FUMBLE_RETURN_TD = 57;
	public static final int STAT_PICK_SIXES_THROWN = 58;
	public static final int STAT_FORTY_PLUS_YARD_COMPLETIONS = 59;
	public static final int STAT_FORTY_PLUS_YARD_PASSING_TOUCHDOWNS = 60;
	public static final int STAT_FORTY_PLUS_YARD_RUSHING_ATTEMPTS = 61;
	public static final int STAT_FORTY_PLUS_YARD_RUSHING_TOUCHDOWNS = 62;
	public static final int STAT_FORTY_PLUS_YARD_RECEPTIONS = 63;
	public static final int STAT_FORTY_PLUS_YARD_RECEPTION_TOUCHDOWNS = 64;
	public static final int STAT_TACKLES_FOR_LOSS = 65;
	public static final int STAT_TURNOVER_RETURN_YARDS = 66;
	public static final int STAT_FOURTH_DOWN_STOPS = 67;
	public static final int STAT_TACKLES_FOR_LOSS_TEAM = 68;
	public static final int STAT_DEFENSIVE_YARDS_ALLOWED = 69;
	public static final int STAT_DEFENSIVE_YARDS_ALLOWED_NEGATIVE = 70;
	public static final int STAT_DEFENSIVE_YARDS_ALLOWED_0_99 = 71;
	public static final int STAT_DEFENSIVE_YARDS_ALLOWED_100_199 = 72;
	public static final int STAT_DEFENSIVE_YARDS_ALLOWED_200_299 = 73;
	public static final int STAT_DEFENSIVE_YARDS_ALLOWED_300_399 = 74;
	public static final int STAT_DEFENSIVE_YARDS_ALLOWED_400_499 = 75;
	public static final int STAT_DEFENSIVE_YARDS_ALLOWED_500_PLUS = 76;
	public static final int STAT_THREE_AND_OUTS_FORCED = 77;

	public static final HashMap<Integer, String> statTranslation = new HashMap<Integer, String>(100);
	static {
		statTranslation.put(STAT_GAMES_PLAYED, "Games Played");
		statTranslation.put(STAT_PASSING_ATTEMPTS, "Passing Attempts");
		statTranslation.put(STAT_COMPLETIONS, "Completions");
		statTranslation.put(STAT_INCOMPLETE_PASSES, "Incomplete Passes");
		statTranslation.put(STAT_PASSING_YARDS, "Passing Yards");
		statTranslation.put(STAT_PASSING_TOUCHDOWNS, "Passing Touchdowns");
		statTranslation.put(STAT_INTERCEPTIONS, "Interceptions");
		statTranslation.put(STAT_SACKS, "Sacks");
		statTranslation.put(STAT_RUSHING_ATTEMPTS, "Rushing Attempts");
		statTranslation.put(STAT_RUSHING_YARDS, "Rushing Yards");
		statTranslation.put(STAT_RUSHING_TOUCHDOWNS, "Rushing Touchdowns");
		statTranslation.put(STAT_RECEPTIONS, "Receptions");
		statTranslation.put(STAT_RECEPTION_YARDS, "Reception Yards");
		statTranslation.put(STAT_RECEPTION_TOUCHDOWNS, "Reception Touchdowns");
		statTranslation.put(STAT_RETURN_YARDS, "Return Yards");
		statTranslation.put(STAT_RETURN_TOUCHDOWNS, "Return Touchdowns");
		statTranslation.put(STAT_TWO_POINT_CONVERSIONS, "Two Point Conversions");
		statTranslation.put(STAT_FUMBLES, "Fumbles");
		statTranslation.put(STAT_FUMBLES_LOST, "Fumbles Lost");
		statTranslation.put(STAT_FIELD_GOALS_0_19_YARDS, "Field Goals 0-19 Yards");
		statTranslation.put(STAT_FIELD_GOALS_20_29_YARDS, "Field Goals 20-29 Yards");
		statTranslation.put(STAT_FIELD_GOALS_30_39_YARDS, "Field Goal 30-39 Yards");
		statTranslation.put(STAT_FIELD_GOALS_40_49_YARDS, "Field Goals 40-49 Yards");
		statTranslation.put(STAT_FIELD_GOALS_50_PLUS_YARDS, "Field Goals 50+ Yards");
		statTranslation.put(STAT_FIELD_GOALS_MISSED_0_19_YARDS, "Field Goals Missed 0-19 Yards");
		statTranslation.put(STAT_FIELD_GOALS_MISSED_20_29_YARDS, "Field Goals Missed 20-29 Yards");
		statTranslation.put(STAT_FIELD_GOALS_MISSED_30_39_YARDS, "Field Goals Missed 30-39 Yards");
		statTranslation.put(STAT_FIELD_GOALS_MISSED_40_49_YARDS, "Field Goals Missed 40-49 Yards");
		statTranslation.put(STAT_FIELD_GOALS_MISSED_50_PLUS_YARDS, "Field Goals Missed 50+ Yards");
		statTranslation.put(STAT_POINT_AFTER_ATTEMPT_MADE, "PAT Made");
		statTranslation.put(STAT_POINT_AFTER_ATTEMPT_MISSED, "PAT Missed");
		statTranslation.put(STAT_POINTS_ALLOWED, "Points Allowed");
		statTranslation.put(STAT_SACK, "Sacks (Def. Team)");
		statTranslation.put(STAT_INTERCEPTION, "Interceptions (Def. Team)");
		statTranslation.put(STAT_FUMBLE_RECOVERY, "Fumble Recoveries");
		statTranslation.put(STAT_TOUCHDOWN, "Touchdowns (Def. Team)");
		statTranslation.put(STAT_SAFETY, "Safeties (Def. Team)");
		statTranslation.put(STAT_BLOCK_KICK, "Block Kick (Def. Team)");
		statTranslation.put(STAT_TACKLE_SOLO, "Solo Tackles");
		statTranslation.put(STAT_TACKLE_ASSIST, "Tackle Assists");
		statTranslation.put(STAT_SACK_PLAYER, "Sack (Def. Player)");
		statTranslation.put(STAT_INTERCEPTION_PLAYER, "Interception (Def. Player)");
		statTranslation.put(STAT_FUMBLE_FORCE, "Fumble Force");
		statTranslation.put(STAT_FUMBLE_RECOVERY_PLAYER, "Fumble Recoveries (Def. Player)");
		statTranslation.put(STAT_DEFENSIVE_TOUCHDOWN, "Touchdown (Def. Player)");
		statTranslation.put(STAT_SAFETY_PLAYER, "Safeties (Def. Player)");
		statTranslation.put(STAT_PASS_DEFENDED, "Passes Defended (Def. Player)");
		statTranslation.put(STAT_BLOCK_KICK_PLAYER, "Blocked Kicks (Def. Player)");
		statTranslation.put(STAT_RETURN_YARDS_TEAM, "Return Yards (Team)");
		statTranslation.put(STAT_KICKOFF_AND_PUNT_RETURN_TOUCHDOWNS, "Kickoff and Punt Return Touchdowns (Team)");
		statTranslation.put(STAT_POINTS_ALLOWED_0, "Points Allowed 0");
		statTranslation.put(STAT_POINTS_ALLOWED_1_6, "Points Allowed 1-6");
		statTranslation.put(STAT_POINTS_ALLOWED_7_13, "Points Allowed 7-13");
		statTranslation.put(STAT_POINTS_ALLOWED_14_20, "Points Allowed 14-20");
		statTranslation.put(STAT_POINTS_ALLOWED_21_27, "Points Allowed 21-27");
		statTranslation.put(STAT_POINTS_ALLOWED_28_34, "Points Allowed 28-34");
		statTranslation.put(STAT_POINTS_ALLOWED_35_PLUS, "Points Allowed 35+");
		statTranslation.put(STAT_OFFENSIVE_FUMBLE_RETURN_TD, "Offensive Fumble Return Touchdowns");
		statTranslation.put(STAT_PICK_SIXES_THROWN, "Pick-Sixes Thrown");
		statTranslation.put(STAT_FORTY_PLUS_YARD_COMPLETIONS, "40+ Yard Completions");
		statTranslation.put(STAT_FORTY_PLUS_YARD_PASSING_TOUCHDOWNS, "40+ Yard Passing Touchdowns");
		statTranslation.put(STAT_FORTY_PLUS_YARD_RUSHING_ATTEMPTS, "40+ Yard Rushing Attempts");
		statTranslation.put(STAT_FORTY_PLUS_YARD_RUSHING_TOUCHDOWNS, "40+ Yard Rushing Touchdowns");
		statTranslation.put(STAT_FORTY_PLUS_YARD_RECEPTIONS, "40+ Yard Receptions");
		statTranslation.put(STAT_FORTY_PLUS_YARD_RECEPTION_TOUCHDOWNS, "40+ Yard Reception Touchdowns");
		statTranslation.put(STAT_TACKLES_FOR_LOSS, "Tackles for Loss (Def. Player)");
		statTranslation.put(STAT_TURNOVER_RETURN_YARDS, "Turnover Return Yards (Def. Player)");
		statTranslation.put(STAT_FOURTH_DOWN_STOPS, "Fourth-Down Stops");
		statTranslation.put(STAT_TACKLES_FOR_LOSS_TEAM, "Tackles for Loss (Def. Team)");
		statTranslation.put(STAT_DEFENSIVE_YARDS_ALLOWED, "Defensive Yards Allowed");
		statTranslation.put(STAT_DEFENSIVE_YARDS_ALLOWED_NEGATIVE, "Defensive Yards Allowed Negative");
		statTranslation.put(STAT_DEFENSIVE_YARDS_ALLOWED_0_99, "Defensive Yards Allowed 0-99");
		statTranslation.put(STAT_DEFENSIVE_YARDS_ALLOWED_100_199, "Defensive Yards Allowed 100-199");
		statTranslation.put(STAT_DEFENSIVE_YARDS_ALLOWED_200_299, "Defensive Yards Allowed 200-299");
		statTranslation.put(STAT_DEFENSIVE_YARDS_ALLOWED_300_399, "Defensive Yards Allowed 300-399");
		statTranslation.put(STAT_DEFENSIVE_YARDS_ALLOWED_400_499, "Defensive Yards Allowed 400-499");
		statTranslation.put(STAT_DEFENSIVE_YARDS_ALLOWED_500_PLUS, "Defensive Yards Allowed 500+");
		statTranslation.put(STAT_THREE_AND_OUTS_FORCED, "Three-and-Outs Forced");
	}
}

package main.java.engineering.utils;

public class CommonStrings {
	
	private CommonStrings() {
		// this class is used only to retrieve common strings
		// to display, just for the purpose to be a little bit more user friendly
	}
	
	private static final String DATABASE_ERROR_MSG = "An error occurred in interaction with database";
	private static final String WRONG_USER_ERR_MSG = "Only users of type PLAYER can join tables and tournaments";
	private static final String NOT_BUSINESSMAN_ERR_MSG = "Only users of type BUSINESSMAN can perform this action";
	private static final String TABLE_RESERVED_NOTIF = "The user '%s' has reserved a seat at your table '%s'";
	private static final String TOURNAMENT_RESERVED_NOTIF = "The user '%s' has reserved a seat at your tournament '%s'";
	private static final String TOURNAMENT_SPONSORIZATION_NOTIF = "Your tournament '%s' has been sponsorized by the user '%s' with the activity '%s'";
	
	public static String getDatabaseErrorMsg() {
		return DATABASE_ERROR_MSG;
	}

	public static String getWrongUserErrMsg() {
		return WRONG_USER_ERR_MSG;
	}

	public static String getNotBusinessmanErrMsg() {
		return NOT_BUSINESSMAN_ERR_MSG;
	}

	public static String getTableReservedNotif() {
		return TABLE_RESERVED_NOTIF;
	}

	public static String getTournamentReservedNotif() {
		return TOURNAMENT_RESERVED_NOTIF;
	}

	public static String getTournamentSponsorizationNotif() {
		return TOURNAMENT_SPONSORIZATION_NOTIF;
	}

}

package main.java.engineering.utils;

public class CommonStrings {
	
	private CommonStrings() {
		// this class is used only to retrieve common strings
		// to display, just for the purpose to be a little bit more user friendly
	}
	
	private static final String DATABASE_ERROR_MSG = "An error occurred in interaction with database";
	private static final String WRONG_USER_ERR_MSG = "Only users of type PLAYER can join tables and tournaments";
	
	public static String getDatabaseErrorMsg() {
		return DATABASE_ERROR_MSG;
	}

	public static String getWrongUserErrMsg() {
		return WRONG_USER_ERR_MSG;
	}

}

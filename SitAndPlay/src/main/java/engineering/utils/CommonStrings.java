package main.java.engineering.utils;

public class CommonStrings {
	
	private CommonStrings() {
		// this class is used only to retrieve common strings
		// to display, just for the purpose to be a little bit more user friendly
	}
	
	private static final String DATABASE_ERROR_MSG= "An error occurred in interaction with database";
	
	public static String getDatabaseErrorMsg() {
		return DATABASE_ERROR_MSG;
	}

}

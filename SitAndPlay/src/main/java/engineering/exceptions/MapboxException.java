package main.java.engineering.exceptions;

public class MapboxException extends Exception {

	private static final long serialVersionUID = 1L;
	public MapboxException(String source) {
		super("An error occurred in interaction with Mapbox due to " + source);
	}

}

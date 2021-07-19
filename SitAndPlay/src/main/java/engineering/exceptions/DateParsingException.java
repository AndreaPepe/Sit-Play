package main.java.engineering.exceptions;

public class DateParsingException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public DateParsingException(String msg) {
		super(msg);
	}
}

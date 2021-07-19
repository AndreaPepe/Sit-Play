package main.java.engineering.exceptions;

public class AlreadyExistingWinnerException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public AlreadyExistingWinnerException(String msg) {
		super(msg);
	}
}

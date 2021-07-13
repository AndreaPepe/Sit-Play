package main.java.engineering.exceptions;

public class WrongUserTypeException extends Exception {

	private static final long serialVersionUID = 1L;

	public WrongUserTypeException(String msg) {
		super(msg);
	}
}

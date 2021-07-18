package main.java.engineering.exceptions;

public class DeleteSeatException extends Exception{
	private static final long serialVersionUID = -5358064670399541452L;
	
	public DeleteSeatException(String msg) {
		super(msg);
	}
}

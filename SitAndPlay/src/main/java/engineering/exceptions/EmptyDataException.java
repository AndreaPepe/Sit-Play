package main.java.engineering.exceptions;

/**
 * This Exception Class is used to
 * signal login errors with the following 
 * codes:
 * 
 * <p>0 -> empty username
 * <p>1 -> empty name
 * <p>2 -> empty surname
 * <p>3 -> empty password
 * 
 * @author Andrea Pepe
 * 
 **/

public class EmptyDataException extends Exception{
	
	private static final long serialVersionUID = 1L;
	private final int errorCode;
	
	public EmptyDataException(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return this.errorCode;
	}

}

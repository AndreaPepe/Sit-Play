package main.java.engineering.exceptions;

/**
 * This Exception is thrown when username or password
 * inserted for login are wrong.
 * It is also used when username proposed for registration
 * is already in use
 * 
 * @author Andrea Pepe
 *
 */
public class WrongCredentialsExceptions extends Exception{

	private static final long serialVersionUID = 1L;

	public WrongCredentialsExceptions(String msg) {
		super(msg);
	}
	
}

package test;

import static org.junit.Assert.assertEquals;

import java.security.SecureRandom;
import java.util.Random;

import org.junit.Test;

import main.java.controller.applicationcontroller.RegistrationController;
import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.exceptions.BeanCheckException;
import main.java.engineering.exceptions.DAOException;
import main.java.model.UserType;

/**
 * 
 * @author Andrea Pepe
 *
 */
public class TestRegistrationController {

	private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
	
	/**
	 * Test if registration with an already existing username
	 * fails and throws the right exceptions with the correct 
	 * error message.
	 * 
	 * @throws BeanCheckException
	 */
	@Test
	public void testSignUpDuplicatedUsername() throws BeanCheckException {
		var beanUser = new BeanUser();
		
		beanUser.setUsername("abc");
		beanUser.setPassword("random");
		beanUser.setUserType(UserType.BUSINESSMAN);
			
		var ctrl = new RegistrationController();
		
		var errorMsg = "";
		try {
			ctrl.signUp(beanUser);
		} catch (DAOException e) {
			errorMsg = e.getMessage();
		}
		
		assertEquals("Username already in use!", errorMsg);
	}
	
	
	/**
	 * Generate a random username, also used as password, and randomly select
	 * one of the three available user type to perform registration.
	 * If the generated username is a duplicate of an already existing username,
	 * the test is also considered as passed (the chances of this happening are very very low).
	 * @throws BeanCheckException 
	 */
	@Test
	public void testSignUpRandomRegistration() throws BeanCheckException {
		var lowerBound = 4;
		var upperBound = 32;
		var length = RANDOM.nextInt(upperBound- lowerBound) + lowerBound;
		var accountTypes = UserType.values();
		
		var generatedUsername = generateRandomString(length);
		var type = accountTypes[RANDOM.nextInt(3)];
		
		var bean = new BeanUser();
		bean.setUsername(generatedUsername);
		bean.setPassword(generatedUsername);
		bean.setUserType(type);
		
		var ctrl = new RegistrationController();
		var result = true;
		try {
			ctrl.signUp(bean);
		} catch (DAOException e) {
			if (e.getMessage().equals("Username already in use!")) {
				result = true;
			}else {
				result = false;
			}
		}
		
		assertEquals(true, result);
	}
	
	
	private String generateRandomString(int length) {
		var buffer = new StringBuilder(length);
        for (var i = 0; i < length; i++) {
            buffer.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(buffer);
	}
}

package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.exceptions.BeanCheckException;
import main.java.model.UserType;

/**
 * 
 * @author Andrea Pepe
 *
 */
public class TestBeanUser {
	
	/**
	 * Test if setting a blank username throws an exception
	 */
	@Test
	public void testSetUsername() {
		var beanUser = new BeanUser();
		var result = false;
		try {
			beanUser.setUsername(" ");
		} catch (BeanCheckException e) {
			result = true;
		}
		
		assertEquals(true, result);
	}
	
	
	
	/**
	 * Test if a correct settings of attributes doesn't throw any exception
	 */
	@Test
	public void testSetAttributes() {
		var beanUser = new BeanUser();
		var result = true;
		try {
			beanUser.setUsername("Random username");
			beanUser.setPassword("Random password");
			beanUser.setUserType(UserType.ORGANIZER);
		} catch (Exception e) {
			result = false;
		}
		
		assertEquals(true, result);
	}
	
}

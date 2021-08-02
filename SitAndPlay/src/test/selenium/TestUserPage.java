package test.selenium;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * 
 * @author Andrea Pepe
 *
 */

public class TestUserPage {

	/**
	 * Test if logging in with an account of type Player
	 * redirects to the correct page with the correct label
	 * that describes the account type.
	 */
	@Test
	public void testUserType() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		
		WebDriver driver = new ChromeDriver();
		driver.get("http://localhost:8080/SitAndPlay/");
		
		driver.findElement(By.xpath("//*[@id=\"tfUsername\"]")).sendKeys("abc");
		driver.findElement(By.xpath("//*[@id=\"pfPassword\"]")).sendKeys("abc");
		driver.findElement(By.xpath("//*[@id=\"login\"]")).click();
		
		WebElement accountLabel = driver.findElement(By.xpath("//*[@id=\"innerPage\"]/div[1]/h3/strong"));
		
		String actualText = accountLabel.getText();
		assertEquals("Player", actualText);
		driver.close();
	}
}

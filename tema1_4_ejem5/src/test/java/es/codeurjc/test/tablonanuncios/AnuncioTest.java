package es.codeurjc.test.tablonanuncios;

import static org.junit.Assert.*;
import static org.openqa.selenium.remote.DesiredCapabilities.chrome;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.ChromeDriverManager;

public class AnuncioTest {

	private static String sutURL;
	private static String eusURL;
	
	private static Logger logger = LogManager.getLogger(AnuncioTest.class);

	@Rule
	public TestName testName = new TestName();
	
	WebDriver driver;
	
	@BeforeClass
	public static void setupClass() {

		String sutHost = System.getenv("ET_SUT_HOST");
		if (sutHost == null) {
			sutURL = "http://localhost:8080/";
		} else {
			sutURL = "http://" + sutHost + ":8080/";
		}
		System.out.println("App url: " + sutURL);

		eusURL = System.getenv("ET_EUS_API");
		if (eusURL == null) {
			ChromeDriverManager.getInstance().setup();
		}
	}

	@Before
	public void setupTest() throws MalformedURLException {
		
		logger.info("##### Start test: " + testName.getMethodName());
		
		String eusURL = System.getenv("ET_EUS_API");
		if (eusURL == null) {
			// Local Google Chrome
			driver = new ChromeDriver();
		} else {
			// Selenium Grid in ElasTest
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability("browserId", testName.getMethodName());
			driver = new RemoteWebDriver(new URL(eusURL), capabilities);
		}
	}
	
	@After
	public void teardown() {
		if(driver != null) {
			driver.quit();
		}
		
		logger.info("##### Finish test: " + testName.getMethodName());

	}
	
	@Test
	public void createTest() throws InterruptedException {
		
		logger.info("Open page");
		
		driver.get(sutURL);
		
		Thread.sleep(2000);
		
		logger.info("Search link");
		driver.findElement(By.linkText("Nuevo anuncio")).click();
		
		Thread.sleep(2000);
		
		logger.info("Fill in form");
		driver.findElement(By.name("nombre")).sendKeys("Anuncio nuevo con Selenium");
		driver.findElement(By.name("asunto")).sendKeys("Vendo moto");
		driver.findElement(By.name("comentario")).sendKeys("Un comentario muy largo...");
		
		Thread.sleep(2000);
		
		logger.info("Submit");
		driver.findElement(By.xpath("//input[@type='submit']")).click();
		
		logger.info("Back to index");
		driver.findElement(By.linkText("Volver al tablón")).click();
		
		assertNotNull(driver.findElement(By.partialLinkText("Selenium")));
	}
	
	@Test
	public void deleteTest() throws InterruptedException {
		
		logger.info("Open page");
		driver.get(sutURL);
		
		Thread.sleep(2000);
		
		logger.info("Search link");
		driver.findElement(By.linkText("Pepe")).click();
		
		Thread.sleep(2000);
		
		logger.info("Delete");
		driver.findElement(By.linkText("Borrar")).click();
		
		Thread.sleep(2000);
		
		assertNull(driver.findElement(By.partialLinkText("Juan")));
	}

}

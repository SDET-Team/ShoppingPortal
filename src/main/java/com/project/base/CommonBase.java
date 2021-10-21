package com.project.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.interactions.Actions;

import com.project.pages.commonnavbar.NavbarAfterLogin;
import com.project.pages.commonnavbar.NavbarBeforeLogin;
import com.project.testcases.LoginPageTest;
import com.project.utils.TestUtils;

public class CommonBase {

	
	public WebDriver driver;
	

	public static Properties config, logconfig;
	
	public static FileInputStream input;
	public static NavbarBeforeLogin navbeforeLogin;
	public static NavbarAfterLogin navafterLogin;
	public static String filePath;
	public static JavascriptExecutor javascriptExecutor = null;
	public static Alert alert = null;
	public static Actions actions = null;	
	
	public static Logger log;
	public static String dir1;
	
	

	
	// Create a constructor and initialize the variables
	public CommonBase() {
		
		config = new Properties();
		filePath = System.getProperty("user.dir");
		try {
			input = new FileInputStream(filePath + "\\src\\main\\java\\com\\project\\config\\config.properties");
			config.load(input);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void logConfig()
	{	
	
		logconfig= new Properties();
		try {
			input=new FileInputStream(filePath + "\\src\\main\\java\\com\\project\\config\\log4j.properties");
			logconfig.load(input);
			log=Logger.getLogger(CommonBase.class);
			String timeStamp = new SimpleDateFormat(" yyyy.MM.dd.HH.mm.ss").format(new Date());
			String currDate=new SimpleDateFormat("dd.MM.yyyy").format(new Date());
			dir1=currDate;
			String logFilename="TestLog "+timeStamp+".log";
			System.setProperty("logfile.name",filePath+"\\automation test output\\logs\\"+dir1+"\\"+logFilename);
			PropertyConfigurator.configure(logconfig);
			
				
		} catch (FileNotFoundException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
}
		
	}
	
	public static ThreadLocal<WebDriver> threadDriver = new ThreadLocal<WebDriver>();
	
	
	
	public static synchronized WebDriver getDriver() {
		return threadDriver.get();
	}
	public void initialization()
	{
		String browserName = config.getProperty("browser");

		String filePath = System.getProperty("user.dir");
		if (browserName.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", filePath + config.getProperty("browserDriverpath"));
	
			ChromeOptions options=new ChromeOptions();

			DesiredCapabilities capability=new DesiredCapabilities();
			capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
			options.merge(capability);
			
			log.info("Starting Browser");
			
			driver = new ChromeDriver(options);
		} else if (browserName.equals("FireFox")) {
			System.setProperty("webdriver.gecko.driver", filePath + config.getProperty("browserDriverpath"));
			driver = new FirefoxDriver();
		}
		log.info("Maximizing the browser");
		
		driver.manage().window().maximize();

		log.info("Deleting all cookies");
		
		driver.manage().deleteAllCookies();
		
		log.info("Adding implicit wait");
		driver.manage().timeouts().pageLoadTimeout(TestUtils.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(TestUtils.IMPLICIT_WAIT, TimeUnit.SECONDS);

		try {
			
			log.info("Loading URL");
			
			driver.get(config.getProperty("url"));
		} catch (WebDriverException e) {
			log.error("WebDriverException");
		} catch (Exception e) {
			log.error("Exception");
		}

		navbeforeLogin = new NavbarBeforeLogin(driver);
		navafterLogin = new NavbarAfterLogin(driver);
		threadDriver.set(driver);
		
	}

	
	public void initialization(String mode) 
	{
		String browserName = config.getProperty("browser");
		String filePath = System.getProperty("user.dir");
		if (browserName.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", filePath + config.getProperty("browserDriverpath"));
			ChromeOptions options=new ChromeOptions();
			DesiredCapabilities capability=new DesiredCapabilities();
			capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
			options.merge(capability);
			
			log.info("Starting Browser");
			
			driver = new ChromeDriver(options);
		} else if (browserName.equals("FireFox")) {
			System.setProperty("webdriver.gecko.driver", filePath+config.getProperty("browserDriverpath"));
			driver = new FirefoxDriver();
		}
		log.info("Maximizing the browser");
		
		driver.manage().window().maximize();
		
		log.info("Deleting all cookies");
		
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(TestUtils.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
		
		log.info("Adding implicit wait");
		
		driver.manage().timeouts().implicitlyWait(TestUtils.IMPLICIT_WAIT, TimeUnit.SECONDS);

		try {
			if (mode.equals("admin")) {
				
				log.info("Loading URL");
				
				driver.get(config.getProperty("adminurl"));
			}
		} catch (WebDriverException e) {
			log.error("WebDriverException");
		} catch (Exception e) {
			log.error("Exception");
		}

		navbeforeLogin = new NavbarBeforeLogin(driver);
		navafterLogin = new NavbarAfterLogin(driver);
		threadDriver.set(driver);
	

	}
	
}

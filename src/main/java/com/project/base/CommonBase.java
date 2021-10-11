package com.project.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.project.pages.common.NavbarAfterLogin;
import com.project.pages.common.NavbarBeforeLogin;
import com.project.utils.TestUtils;

public class CommonBase {
	
	public static WebDriver driver;
	public static Properties config;
	public static FileInputStream input;
	public static NavbarBeforeLogin navbeforeLogin;
	public static NavbarAfterLogin navafterLogin;
	
	//Create a constructor and initialize the variables
		public CommonBase(){
			try {
				config=new Properties();
				String filePath = System.getProperty("user.dir");
                input = new FileInputStream(filePath+"\\src\\main\\java\\com\\project\\config\\config.properties");
                config.load(input);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
			
			
			
			public static void initialization() {
				String browserName = config.getProperty("browser");
				String filePath = System.getProperty("user.dir");
				if(browserName.equals("chrome")){
					System.setProperty("webdriver.chrome.driver",  filePath +config.getProperty("browserDriverpath"));	
					driver = new ChromeDriver(); 
				}
				else if(browserName.equals("FireFox")){
					System.setProperty("webdriver.gecko.driver", config.getProperty("browserDriverpath"));	
					driver = new FirefoxDriver(); 
			}
				driver.manage().window().maximize();
				driver.manage().deleteAllCookies();
				driver.manage().timeouts().implicitlyWait(TestUtils.IMPLICIT_WAIT, TimeUnit.SECONDS);
				
				driver.get(config.getProperty("url"));
				
				navbeforeLogin=new NavbarBeforeLogin(driver);
				navafterLogin=new NavbarAfterLogin(driver);
				
			}	

		}
		
	
	


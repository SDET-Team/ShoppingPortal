package com.project.testcases;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;  
import org.testng.annotations.AfterClass; 

import com.project.base.CommonBase;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.pages.TrackOrderPage;
import com.project.utils.TestUtils;

public class TrackOrderPageTest extends CommonBase {

	HomePage homepage;
	String filepath;
	TrackOrderPage trackorderpage;

	public TrackOrderPageTest() {
		super();
		
	}

	@BeforeClass
	public void setup() {
		
		/*log=Logger.getLogger(TrackOrderPageTest.class);
		String timeStamp = new SimpleDateFormat(" yyyy.MM.dd.HH.mm.ss").format(new Date());
		String currDate=new SimpleDateFormat("dd.MM.yyyy").format(new Date());
		dir1=currDate;
		dir2="TestLog "+timeStamp;
		String logFilename=TrackOrderPageTest.class.getSimpleName()+timeStamp+".log";
		System.setProperty("logfile.name",filePath+"\\src\\resources\\log\\"+dir1+"\\"+dir2+"\\"+logFilename);
		PropertyConfigurator.configure(logconfig);
		*/
		
		initialization();
		
		homepage = new HomePage(driver);
		trackorderpage=new TrackOrderPage(driver);
		
		Assert.assertEquals(homepage.title(), "Shopping Portal Home Page", "Home Page Title Not Matched");
		navbeforeLogin.clickontrackorder();
	}

	@DataProvider(name = "testdata")
	public Object[][] getPositiveData(Method m) throws IOException {
		String type="Negative";
			filepath = System.getProperty("user.dir") + "\\src\\resources\\testdata\\TrackorderTestdata.xlsx";
		if (m.getName().equals("validatePositiveTrackorderOperation"))
			type="Positive";
		Object data[][] = TestUtils.getTestData(filepath,type);
		return data;
	}
	
	

	@Test(priority=1)
	public void validateTitle()
	{	log.info("Validating Loginpage Title");
		String title=trackorderpage.title();
		Assert.assertEquals(title, "Track Orders","Title Not Matched");
		log.info("Testcase Passed!");
		
	}
	
	@Test(priority=2,dataProvider="testdata")
	public void validatePositiveTrackorderOperation(String orderid,String emailid)
	{
		log.info("Validating Tracking order operation by providing correct emailid and orderid");
		log.info("Order details: Orderid: "+orderid+" , Emailid: "+emailid);
		int col=trackorderpage.trackingOption(orderid, emailid);
		Assert.assertEquals(col,9,"Error message displayed for correct orderid and emailid!!!");
		log.info("Testcase Passed!");
		
	}
	

	@Test(priority=3,dataProvider="testdata")
	public void validateNegativeTrackorderOperation(String orderid,String emailid)
	{
		log.info("Validating Tracking order operation by providing incorrect emailid or orderid");
		log.info("Order details: Orderid: {0} , Emailid: {1}");
		int col=trackorderpage.trackingOption(orderid, emailid);
		Assert.assertEquals(col,1,"No error message displayed for incorrect orderid or emailid!!");
		log.info("Testcase Passed!");
	}

	@AfterClass
	public void tearDown() {
		log.info("Closing Browser");
		driver.close();
	}

}

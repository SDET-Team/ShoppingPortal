package com.project.testcases;

import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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

	@BeforeTest
	public void setup() {
		initialization();
		homepage = new HomePage();
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
		trackorderpage=new TrackOrderPage();
		String title=trackorderpage.title();
		Assert.assertEquals(title, "Track Orders","Title Not Matched");
		log.info("Testcase Passed!");
		
	}
	
	@Test(priority=2,dataProvider="testdata")
	public void validatePositiveTrackorderOperation(String orderid,String emailid)
	{
		log.info("Validating Tracking order operation by providing correct emailid and orderid");
		log.info("Order details: Orderid: {0} , Emailid: {1}");
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

	@AfterTest
	public void tearDown() {
		log.info("Closing Browser");
		driver.close();
	}

}

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
	
	public TrackOrderPageTest()
	{
		super();
	}
	
	@BeforeTest
	public void setup()
	{	
		initialization();
		homepage=new HomePage();
		Assert.assertEquals(homepage.title(), "Shopping Portal Home Page","Home Page Title Not Matched");
		navbeforeLogin.clickontrackorder();
	}
	
	@DataProvider(name="testdata")
	public Object[][] getloginData(Method m) throws IOException{
		
		filepath=System.getProperty("user.dir")+"\\src\\resources\\testdata\\trackTestdata.xlsx";
		Object data[][] = TestUtils.getTestData(filepath); 
		return data; 
	}
	

	@Test(priority=1)
	public void validateTitle()
	{
		trackorderpage=new TrackOrderPage();
		String title=trackorderpage.title();
		Assert.assertEquals(title, "Track Orders","Title Not Matched");
		
	}
	
	@Test(priority=2,dataProvider="testdata")
	public void validatTrackorderOperation(String orderid,String emailid,String remarks)
	{
		boolean status=trackorderpage.trackingOption(orderid, emailid, remarks);
		if(remarks.equals("Invalid"))
			Assert.assertTrue(status);
		else
			Assert.assertFalse(status);
		driver.navigate().back();
	
	}
	
	@AfterTest
	public void tearDown()
	{
		driver.close();
	}
	

}

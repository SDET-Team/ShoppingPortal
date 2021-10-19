package com.project.testcases;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.AfterLoginPage;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.pages.MyAccountPage;
import com.project.pages.OrderHistoryPage;
import com.project.utils.TestUtils;

public class OrderHistoryPageTest extends CommonBase {
	
	AfterLoginPage afterLoginPage;
	
	public OrderHistoryPageTest() {
		super();
	}
	
	
	@BeforeTest
	public void setup() {
		initialization();
		HomePage homepage=new HomePage();
		Assert.assertEquals(homepage.title(), "Shopping Portal Home Page", "Home Page Title Not Matched.");
		navbeforeLogin.navigatetologin();
		
		LoginPage loginpage=new LoginPage();
		Assert.assertEquals(loginpage.loginpageTitle(), "Shopping Portal | Signi-in | Signup", "Login Page Title Not Matched.");
		
		String file = System.getProperty("user.dir") + "\\src\\resources\\testdata\\myAccountTestData.xlsx";
		Object[][] data;
		try {
			data = TestUtils.getTestData(file, 0);
			String email = data[0][0].toString();
			String password = data[0][1].toString();
			
			loginpage.loginOperation(email, password);
			
			afterLoginPage = new AfterLoginPage();
			Assert.assertEquals(afterLoginPage.getTitle(), "My Cart");
		} catch (IOException e) {  }
	}
	
	@Test
	public void checkIfNewOrdersAtTopInOrderHistory() {
		afterLoginPage.goToMyAccountPage();
		
		MyAccountPage myAccountPage = new MyAccountPage();
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		myAccountPage.gotoOrderHistoryPageButton();
		
		OrderHistoryPage orderHistoryPage = new OrderHistoryPage();
		Assert.assertEquals(orderHistoryPage.getTitle(), "Order History");
		
		boolean output = orderHistoryPage.checkLatestOrderIsOnTop();
		Assert.assertTrue(output, "Order history does not show newer orders at top.");
	}
	
	@Test
	public void checkIfAllOrdersContainProductDetails() {
		afterLoginPage.goToMyAccountPage();
		
		MyAccountPage myAccountPage = new MyAccountPage();
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		myAccountPage.gotoOrderHistoryPageButton();
		
		OrderHistoryPage orderHistoryPage = new OrderHistoryPage();
		Assert.assertEquals(orderHistoryPage.getTitle(), "Order History");
		
		boolean output = orderHistoryPage.checkAllOrdersContainProductDetails();
		Assert.assertTrue(output, "One or more orders are missing product details.");
	}
	
	
	@AfterTest
	public void tearDown() {
		//try { Thread.sleep(5*1000); } catch(Exception e) {}
		driver.quit();
	}

}

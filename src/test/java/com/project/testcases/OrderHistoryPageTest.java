package com.project.testcases;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.AfterLoginPage;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.pages.MyAccountPage;
import com.project.pages.OrderHistoryPage;

public class OrderHistoryPageTest extends CommonBase {
	
	LoginPage loginpage;
	OrderHistoryPage orderHistoryPage;
	
	public OrderHistoryPageTest() {
		super();
	}
	
	
	@BeforeClass
	public void setup() {
		initialization();
		HomePage homepage=new HomePage();
		Assert.assertEquals(homepage.title(), "Shopping Portal Home Page", "Home Page Title Not Matched.");
		navbeforeLogin.navigatetologin();
		
		loginpage=new LoginPage();
		Assert.assertEquals(loginpage.loginpageTitle(), "Shopping Portal | Signi-in | Signup", "Login Page Title Not Matched.");
	}
	
	
	@Test(priority=1, dataProvider="login_data")
	public void loginAndNavigateToMyAccount(String email, String password, String expectedResult) {
		boolean actualResult = loginpage.loginOperation(email, password, expectedResult);
		Assert.assertFalse(actualResult);
		
		AfterLoginPage afterLoginPage = new AfterLoginPage();
		Assert.assertEquals(afterLoginPage.getTitle(), "My Cart");
		afterLoginPage.goToMyAccountPage();
		
		MyAccountPage myAccountPage = new MyAccountPage();
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		myAccountPage.gotoOrderHistoryPageButton();
		
		orderHistoryPage = new OrderHistoryPage();
		Assert.assertEquals(orderHistoryPage.getTitle(), "Order History");
	}
	
	@Test(priority=2)
	public void checkIfNewOrdersAtTopInOrderHistory() {
		boolean output = orderHistoryPage.checkLatestOrderIsOnTop();
		Assert.assertTrue(output, "Order history does not show newer orders at top.");
	}
	
	@Test(priority=2)
	public void checkIfAllOrdersContainProductDetails() {
		boolean output = orderHistoryPage.checkAllOrdersContainProductDetails();
		Assert.assertTrue(output, "One or more orders are missing product details.");
	}
	
	
	@DataProvider(name="login_data")
	private Object[][] getLoginDetails() {
		return new Object[][] { {"anuj.lpu1@gmail.com", "Test@123", "pass"} };
	}
	
	
	@AfterClass
	public void tearDown() {
		//try { Thread.sleep(5*1000); } catch(Exception e) {}
		driver.quit();
	}

}

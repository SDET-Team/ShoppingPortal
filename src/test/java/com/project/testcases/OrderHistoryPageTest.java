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
			log.info("Login to website successful.");
		} catch (IOException e) { 
			log.error("Login to website failed.");
		}
	}
	
	@Test
	public void checkIfNewOrdersAtTopInOrderHistory() {
		String testcase = "Verify_New_Orders_Present_On_Top_In_Order_History :: ";
		
		afterLoginPage.goToMyAccountPage();
		MyAccountPage myAccountPage = new MyAccountPage();
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		log.info(testcase + "Navigated to My Account page.");
		
		myAccountPage.gotoOrderHistoryPageButton();
		OrderHistoryPage orderHistoryPage = new OrderHistoryPage();
		Assert.assertEquals(orderHistoryPage.getTitle(), "Order History");
		log.info(testcase + "Naviagetd to Order History page.");
		
		boolean output = orderHistoryPage.checkLatestOrderIsOnTop();
		if(output == true) {
			log.info(testcase + "New orders in Order History page are present at top in orders list.");
			log.info(testcase + "Testcase passed.");
		} else {
			log.error(testcase + "New orders in Order History page are not present at top in orders list.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertTrue(output, "Order history does not show newer orders at top.");
	}
	
	@Test
	public void checkIfAllOrdersContainProductDetails() {
		String testcase = "Verify_All_Orders_In_Order_History_Contain_Product_Details :: ";
		
		afterLoginPage.goToMyAccountPage();
		MyAccountPage myAccountPage = new MyAccountPage();
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		log.info(testcase + "Navigated to My Account page.");
		
		myAccountPage.gotoOrderHistoryPageButton();
		OrderHistoryPage orderHistoryPage = new OrderHistoryPage();
		Assert.assertEquals(orderHistoryPage.getTitle(), "Order History");
		log.info(testcase + "Navigated to Order History page.");
		
		boolean output = orderHistoryPage.checkAllOrdersContainProductDetails();
		if(output == true) {
			log.info(testcase + "Products in Order History page contain product details.");
			log.info(testcase + "Testcase passed.");
		} else {
			log.error(testcase + "Products in Order History page does not contain product details.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertTrue(output, "One or more orders are missing product details.");
	}
	
	
	@AfterTest
	public void tearDown() {
		//try { Thread.sleep(5*1000); } catch(Exception e) {}
		//driver.quit();
		driver.close();
	}

}

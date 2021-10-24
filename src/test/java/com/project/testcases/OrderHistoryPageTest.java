package com.project.testcases;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.AfterLoginPage;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.pages.MyAccountPage;
import com.project.pages.OrderHistoryPage;
import com.project.utils.TestUtils;

/**
 * @brief OrderHistoryPageTest class tests Order History page features. 
 * 
 * @details Verifies features of Order History page like new orders are at top in list, orders in list contain product details, etc.
 * 
 */
public class OrderHistoryPageTest extends CommonBase {
	
	AfterLoginPage afterLoginPage;
	
	
	/**
	 * @brief Constructs object of OrderHistoryPageTest class and calls base class @ref CommonBase constructor internally. 
	 * 
	 * @see CommonBase 
	 * 
	 */
	public OrderHistoryPageTest() {
		super();
	}
	
	
	/**
	 * @brief Initialize log4j log configuration to print testcase logs to specific file. 
	 * 
	 */
	@BeforeSuite(groups="Log")
	public void logInit()
	{
		logConfig();
	}
	
	
	/**
	 * @brief Initializes web driver, opens browser, navigates to shopping website and login to website using valid credentials. 
	 * 
	 */
	@BeforeTest
	public void setup() {
		initialization();
		HomePage homepage=new HomePage(driver);
		Assert.assertEquals(homepage.title(), "Shopping Portal Home Page", "Home Page Title Not Matched.");
		navbeforeLogin.navigatetologin();
		
		LoginPage loginpage=new LoginPage(driver);
		Assert.assertEquals(loginpage.loginpageTitle(), "Shopping Portal | Signi-in | Signup", "Login Page Title Not Matched.");
		
		String file = System.getProperty("user.dir") + "\\src\\resources\\testdata\\myAccountTestData.xlsx";
		Object[][] data;
		try {
			data = TestUtils.getTestData(file, 0);
			String email = data[0][0].toString();
			String password = data[0][1].toString();
			
			loginpage.loginOperation(email, password);
			
			afterLoginPage = new AfterLoginPage(driver);
			Assert.assertEquals(afterLoginPage.getTitle(), "My Cart");
			log.info("Login to website successful.");
		} catch (IOException e) { 
			log.error("Login to website failed.");
		}
	}
	
	
	/**
	 * @brief Testcase 1: Verifies orders in Order History page show recent orders at top. 
	 * 
	 * @details Navigates to My Account page, goes to Order History page and checks if recent orders are at top of orders list. 
	 * 
	 * @warning Function will exit with error if valid user login is not done before test. 
	 * 
	 */
	@Test
	public void checkIfNewOrdersAtTopInOrderHistory() {
		String testcase = "Verify_New_Orders_Present_On_Top_In_Order_History :: ";
		
		afterLoginPage.goToMyAccountPage();
		MyAccountPage myAccountPage = new MyAccountPage(driver);
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		log.info(testcase + "Navigated to My Account page.");
		
		myAccountPage.gotoOrderHistoryPageButton();
		OrderHistoryPage orderHistoryPage = new OrderHistoryPage(driver);
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
	
	/**
	 * @brief Testcase 2: Verifies that orders in Order History page contain product details. 
	 * 
	 * @details Navigates to My Account page, goes to Order History page and then checks if orders contain product details. 
	 * 
	 * @warning Function will exit with error if valid user login is not done before test. 
	 * 
	 */
	@Test
	public void checkIfAllOrdersContainProductDetails() {
		String testcase = "Verify_All_Orders_In_Order_History_Contain_Product_Details :: ";
		
		afterLoginPage.goToMyAccountPage();
		MyAccountPage myAccountPage = new MyAccountPage(driver);
		Assert.assertEquals(myAccountPage.getTitle(), "My Account");
		log.info(testcase + "Navigated to My Account page.");
		
		myAccountPage.gotoOrderHistoryPageButton();
		OrderHistoryPage orderHistoryPage = new OrderHistoryPage(driver);
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
	
	
	/**
	 * @brief Closes browser session. 
	 * 
	 */
	@AfterTest
	public void tearDown() {
		//try { Thread.sleep(5*1000); } catch(Exception e) {}
		//driver.quit();
		driver.close();
		log.info("Browser closed successfully.");
	}

}

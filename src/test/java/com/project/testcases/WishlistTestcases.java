package com.project.testcases;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.AfterLoginPage;
import com.project.pages.BooksCategoryPage;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.pages.MyWishlistPage;
import com.project.utils.TestUtils;

public class WishlistTestcases extends CommonBase {
	
	AfterLoginPage afterLoginPage;
	
	
	public WishlistTestcases() {
		super();
	}
	
	@BeforeSuite(groups="Log")
	public void loginit()
	{
		logConfig();
	}
	
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
	
	@Test(priority=1, dataProvider="product_type_name_data")
	public void addProductToWishList(String category, String productTitle) {
		String testcase = "Verify_User_Can_Add_Product_To_Wishlist :: ";
		
		if(category.equals("") || productTitle.equals("")) {
			log.error(testcase + "Testcase values are not valid to continue testing.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertFalse(category.equals(""), "Product category is found empty.");
		Assert.assertFalse(productTitle.equals(""), "Product title is found empty.");
		
		if(category.equals("Books")) {
			afterLoginPage.goToBooksCategory();
			BooksCategoryPage booksCategoryPage = new BooksCategoryPage(driver);
			log.info(testcase + "Navigated to Books category page.");
			
			booksCategoryPage.addBookToWishList(productTitle);
			log.info(testcase + "Product '" + productTitle + "' successfully added to wishlist.");
			
			MyWishlistPage myWishlistPage = new MyWishlistPage(driver);
			boolean output = myWishlistPage.findOccurencesOfProduct(productTitle) > 0;
			if(output == true) {
				log.info(testcase + "Wishlisted product found on Wishlist page.");
				log.info(testcase + "Testcase passed.");
			} else {
				log.error(testcase + "Wishlisted product not found on Wishlist page.");
				log.info(testcase + "Testcase failed.");
			}
			Assert.assertTrue(output, "Product did not added to wishlist.");
		}
	}
	
	@Test(priority=2, dataProvider="product_name_data")
	public void deleteProductFromWishList(String productTitle) {
		String testcase = "Verify_User_Can_Remove_Product_From_Wishlist :: ";
		
		if(productTitle.equals("")) {
			log.error(testcase + "Testcase values are not valid to continue testing.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertFalse(productTitle.equals(""), "Product title is found empty.");
		
		afterLoginPage.goToWishlistPage();
		MyWishlistPage myWishlistPage = new MyWishlistPage(driver);
		myWishlistPage.visit();
		log.info(testcase + "Navigated to Wishlist page.");
		
		boolean output = myWishlistPage.findOccurencesOfProduct(productTitle) > 0;
		if(output == true) {
			log.info(testcase + "Precondition : Product to remove from wishlist present in Wishlist page, satisfied.");
		} else {
			log.error(testcase + "Precondition : Product to remove from wishlist present in Wishlist page, failed.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertTrue(output, "Product to remove from wishlist is actually not present in wishlist.");
		
		output = myWishlistPage.removeProductFromWishlist(productTitle);
		if(output == true) {
			log.info(testcase + "Product '" + productTitle + "' successfully removed from wishlist.");
			log.info(testcase + "Testcase passed.");
		} else {
			log.error(testcase + "Product '" + productTitle + "' failed to remove from wishlist.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertTrue(output, "Removing product from wishlist failed.");
	}
	
	@Test(priority=3, dataProvider="product_type_name_data")
	public void checkProductAddToWishlistOnlyOnce(String category, String productTitle) {
		String testcase = "Verify_Already_Wishlisted_Product_Not_Wishlist_Again :: ";
		
		if(category.equals("") || productTitle.equals("")) {
			log.error(testcase + "Testcase values are not valid to continue testing.");
			log.info(testcase + "Testcase failed.");
		}
		Assert.assertFalse(category.equals(""), "Product category is found empty.");
		Assert.assertFalse(productTitle.equals(""), "Product title is found empty.");
		
		if(category.equals("Books")) {
			afterLoginPage.goToWishlistPage();
			MyWishlistPage myWishlistPage = new MyWishlistPage(driver);
			log.info(testcase + "Navigated to Wishlist page.");
			
			boolean output = (myWishlistPage.findOccurencesOfProduct(productTitle) == 0);
			if(output == true) {
				log.info(testcase + "Precondition : Product not in wishlist, satisfied.");
			} else {
				log.error(testcase + "Precondition : Product not in wishlist, failed.");
				log.info(testcase + "Testcase failed.");
			}
			Assert.assertTrue(output, "Product already present in wishlist page, precondition failed.");
			
			myWishlistPage.goToBooksCategoryPage();
			BooksCategoryPage booksCategoryPage = new BooksCategoryPage(driver);
			log.info(testcase + "Navigated to Books category page.");
			booksCategoryPage.addBookToWishList(productTitle);
			log.info(testcase + "Product '" + productTitle + "' successfully added to wishlist.");
			
			myWishlistPage = new MyWishlistPage(driver);
			//Assert.assertEquals(myWishlistPage.findOccurencesOfProduct(productTitle), 1, "Either product did not got added to wishlist OR multiple entries of same product in wishlist.");
			myWishlistPage.goToBooksCategoryPage();
			booksCategoryPage = new BooksCategoryPage(driver);
			log.info(testcase + "Navigated to Books category page.");
			
			booksCategoryPage.addBookToWishList(productTitle);
			log.info(testcase + "Product '" + productTitle + "' successfully added to wishlist.");
			myWishlistPage = new MyWishlistPage(driver);
			output = myWishlistPage.findOccurencesOfProduct(productTitle) == 1;
			if(output == true) {
				log.info(testcase + "Only one entry of wishlisted product found on Wishlist page.");
				log.info(testcase + "Testcase passed.");
			} else {
				log.error(testcase + "Multiple entries of wishlisted product found on Wishlist page.");
				log.info(testcase + "Testcase failed.");
			}
			Assert.assertTrue(output, "A product added to wishlist multiple times, which is wrong.");
		}
	}
	
	

	@DataProvider(name="product_type_name_data")
	private Object[][] getProductTypeTitle() {
		String file = System.getProperty("user.dir") + "\\src\\resources\\testdata\\myAccountTestData.xlsx";
		Object[][] data;
		try {
			data = TestUtils.getTestData(file, 4);
			String type = data[0][0].toString();
			String title = data[0][1].toString();
			return new Object[][] { { type, title } };
		} catch (IOException e) {  }
		
		return new Object[][] { {"", ""} };
	}
	
	@DataProvider(name="product_name_data")
	private Object[][] getProductTitle() {
		String file = System.getProperty("user.dir") + "\\src\\resources\\testdata\\myAccountTestData.xlsx";
		Object[][] data;
		try {
			data = TestUtils.getTestData(file, 4);
			String title = data[0][1].toString();
			return new Object[][] { { title } };
		} catch (IOException e) {  }
		
		return new Object[][] { {""} };
	}
	
	
	@AfterTest
	public void tearDown() {
		//try { Thread.sleep(3*1000); } catch(Exception e) {}
		//driver.quit();
		driver.close();
	}

}

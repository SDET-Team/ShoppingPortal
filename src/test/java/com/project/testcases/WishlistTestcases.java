package com.project.testcases;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
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
	
	@Test(priority=1, dataProvider="product_type_name_data")
	public void addProductToWishList(String category, String productTitle) {
		if(category.equals("Books")) {
			afterLoginPage.goToBooksCategory();
			BooksCategoryPage booksCategoryPage = new BooksCategoryPage();
			booksCategoryPage.addBookToWishList(productTitle);
			MyWishlistPage myWishlistPage = new MyWishlistPage();
			Assert.assertTrue(myWishlistPage.findOccurencesOfProduct(productTitle) > 0, "Product did not added to wishlist.");
		}
	}
	
	@Test(priority=2, dataProvider="product_name_data")
	public void deleteProductFromWishList(String productTitle) {
		afterLoginPage.goToWishlistButton();
		MyWishlistPage myWishlistPage = new MyWishlistPage();
		myWishlistPage.visit();
		//Assert.assertEquals(myWishlistPage.findOccurencesOfProduct(productTitle), 1, "Product to remove from wishlist is actually not present in wishlist.");
		Assert.assertTrue(myWishlistPage.removeProductFromWishlist(productTitle), "Removing product from wishlist failed.");
	}
	
	@Test(priority=3, dataProvider="product_type_name_data")
	public void checkProductAddToWishlistOnlyOnce(String category, String productTitle) {
		if(category.equals("Books")) {
			afterLoginPage.goToBooksCategory();
			
			BooksCategoryPage booksCategoryPage = new BooksCategoryPage();
			booksCategoryPage.addBookToWishList(productTitle);
			
			MyWishlistPage myWishlistPage = new MyWishlistPage();
			//Assert.assertEquals(1, myWishlistPage.findOccurencesOfProduct(productTitle), "Either product did not got added to wishlist OR multiple entries of same product in wishlist.");
			myWishlistPage.goToBooksCategoryPage();
			
			booksCategoryPage = new BooksCategoryPage();
			booksCategoryPage.addBookToWishList(productTitle);
			
			myWishlistPage = new MyWishlistPage();
			Assert.assertEquals(myWishlistPage.findOccurencesOfProduct(productTitle), 1, "A product added to wishlist multiple times, which is wrong.");
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
		
		return new Object[][] { {"Books", "The Wimpy Kid Do -It- Yourself Book"} };
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
		
		return new Object[][] { {"The Wimpy Kid Do -It- Yourself Book"} };
	}
	
	
	@AfterTest
	public void tearDown() {
		//try { Thread.sleep(3*1000); } catch(Exception e) {}
		driver.quit();
	}

}

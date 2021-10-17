package com.project.testcases;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.project.base.CommonBase;
import com.project.pages.AfterLoginPage;
import com.project.pages.BooksCategoryPage;
import com.project.pages.HomePage;
import com.project.pages.LoginPage;
import com.project.pages.MyAccountPage;
import com.project.pages.MyWishlistPage;
import com.project.pages.OrderHistoryPage;

public class WishlistTestcases extends CommonBase {
	
	LoginPage loginpage;
	AfterLoginPage afterLoginPage;
	MyWishlistPage myWishlistPage;
	
	
	public WishlistTestcases() {
		super();
	}
	
	
	@BeforeClass
	public void setup() {initialization();
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
		
		afterLoginPage = new AfterLoginPage();
		Assert.assertEquals(afterLoginPage.getTitle(), "My Cart");
	}
	
	@Test(priority=2, dataProvider="product_type_name_data")
	public void addProductToWishList(String category, String productTitle) {
		if(category.equals("Books")) {
			afterLoginPage.goToBooksCategory();
			BooksCategoryPage booksCategoryPage = new BooksCategoryPage();
			booksCategoryPage.addBookToWishList(productTitle);
			myWishlistPage = new MyWishlistPage();
			Assert.assertTrue(myWishlistPage.findOccurencesOfProduct(productTitle) > 0, "Product did not added to wishlist.");
		}
	}
	
	@Test(priority=3, dataProvider="product_name_data")
	public void deleteProductFromWishList(String productTitle) {
		//afterLoginPage.goToWishlistButton();
		//myWishlistPage = new MyWishlistPage();
		myWishlistPage.visit();
		//Assert.assertEquals(myWishlistPage.findOccurencesOfProduct(productTitle), 1, "Product to remove from wishlist is actually not present in wishlist.");
		Assert.assertTrue(myWishlistPage.removeProductFromWishlist(productTitle), "Removing product from wishlist failed.");
	}
	
	@Test(priority=4, dataProvider="product_type_name_data")
	public void checkProductAddToWishlistOnlyOnce(String category, String productTitle) {
		if(category.equals("Books")) {
			//afterLoginPage.goToBooksCategory();
			myWishlistPage.goToBooksCategoryPage();
			BooksCategoryPage booksCategoryPage = new BooksCategoryPage();
			booksCategoryPage.addBookToWishList(productTitle);
			myWishlistPage = new MyWishlistPage();
			//Assert.assertEquals(1, myWishlistPage.findOccurencesOfProduct(productTitle), "Either product did not got added to wishlist OR multiple entries of same product in wishlist.");
			myWishlistPage.goToBooksCategoryPage();
			booksCategoryPage = new BooksCategoryPage();
			booksCategoryPage.addBookToWishList(productTitle);
			myWishlistPage = new MyWishlistPage();
			Assert.assertEquals(myWishlistPage.findOccurencesOfProduct(productTitle), 1, "A product added to wishlist multiple times, which is wrong.");
		}
	}
	
	
	@DataProvider(name="login_data")
	private Object[][] getLoginDetails() {
		return new Object[][] { {"anuj.lpu1@gmail.com", "Test@123", "pass"} };
	}
	
	@DataProvider(name="product_type_name_data")
	private Object[][] getProductTypeTitle() {
		return new Object[][] { {"Books", "The Wimpy Kid Do -It- Yourself Book"} };
	}
	
	@DataProvider(name="product_name_data")
	private Object[][] getProductTitle() {
		return new Object[][] { {"The Wimpy Kid Do -It- Yourself Book"} };
	}
	
	
	@AfterClass
	public void tearDown() {
		//try { Thread.sleep(3*1000); } catch(Exception e) {}
		driver.quit();
	}

}

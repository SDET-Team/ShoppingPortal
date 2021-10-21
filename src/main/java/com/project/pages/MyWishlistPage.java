package com.project.pages;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.project.base.CommonBase;

public class MyWishlistPage extends CommonBase {
	
	public MyWishlistPage(WebDriver driver) {
		Assert.assertEquals(driver.getTitle(), "My Wishlist", "Page title is not as expected.");
		Assert.assertTrue(driver.getCurrentUrl().startsWith("http://localhost/shopping/my-wishlist.php"), "Page URL is not as expected.");
		PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	
	
	@FindBy(css = ".cnt-account > ul:nth-child(1) > li:nth-child(3) > a:nth-child(1)")
	WebElement wishlistButton;
	
	@FindBy(css = "li.dropdown:nth-child(2) > a:nth-child(1)")
	WebElement booksCategoryPageButton;
	
	
	public int findOccurencesOfProduct(String productTitle) {
		String xpathToProductLink = "//*[contains(text(),\"" + productTitle + "\")]";
		List<WebElement> finds = driver.findElements(By.xpath(xpathToProductLink));
		return finds.size();
	}
	
	public boolean removeProductFromWishlist(String productTitle) {
		String wishlistTableXpath = "/html/body/div[2]/div/div[1]/div/div/div/table";
		int wishlistCount = driver.findElements(By.xpath(wishlistTableXpath + "/tbody/tr")).size();
		for(int i=1; i<=wishlistCount; i++) {
			String productTitleXpath = wishlistTableXpath + "/tbody/tr[" + i + "]/td[2]/div[1]/a";
			String wishlistProductTitle = "";
			try { wishlistProductTitle = driver.findElement(By.xpath(productTitleXpath)).getText(); } catch(Exception e) { return false; }
			if(wishlistProductTitle.equals(productTitle)) {
				String removeFromWishlistButtonXpath = wishlistTableXpath + "/tbody/tr[" + i + "]/td[4]/a";
				WebElement removeFromWishlistButton = driver.findElement(By.xpath(removeFromWishlistButtonXpath));
				removeFromWishlistButton.click();
				Alert confirm = driver.switchTo().alert();
				confirm.accept();
				return true;
			}
		}
		return false;
	}
	
	
	public void goToBooksCategoryPage() {
		booksCategoryPageButton.click();
	}
	
	public void visit() {
		wishlistButton.click();
	}

}

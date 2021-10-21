package com.project.pages;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.project.base.CommonBase;

public class BooksCategoryPage extends CommonBase {
	
	public BooksCategoryPage(WebDriver driver) {
		Assert.assertEquals(driver.getTitle(), "Product Category", "Page title is not as expected.");
		Assert.assertEquals(driver.getCurrentUrl(), "http://localhost/shopping/category.php?cid=3", "Page URL is not as expected.");
		PageFactory.initElements(driver, this);
		this.driver=driver;
	}
	
	
	public void addBookToWishList(String bookName) {
		String xpathToFindBookUrlLink = "//*[contains(text(),\"" + bookName + "\")]";
		WebElement bookHyperLink = driver.findElement(By.xpath(xpathToFindBookUrlLink));
		String urlInLink = bookHyperLink.getAttribute("href");
		Map<String, String> params = getQueryMap(urlInLink);
		String pid = params.get("pid");
		String productAddToWishlistButtonHrefText = "category.php?pid=" + pid + "&&action=wishlist";
		String productWishlistButtonXpath = "//a[@href=\"" + productAddToWishlistButtonHrefText + "\"]";
		WebElement productAddToWishlistButton = driver.findElement(By.xpath(productWishlistButtonXpath));
		productAddToWishlistButton.click();
	}
	
	
	private Map<String, String> getQueryMap(String query) {
		String parameterString = query.split("\\?")[1];
	    String[] params = parameterString.split("&");
	    Map<String, String> map = new HashMap<String, String>();
	    
	    for (String param : params) {
	        String name = param.split("=")[0];
	        String value = param.split("=")[1];
	        map.put(name, value);
	    }
	    return map;
	}

}

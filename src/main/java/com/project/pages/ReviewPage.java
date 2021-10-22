package com.project.pages;

import com.project.base.CommonBase;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.project.utils.TestUtils;
public class ReviewPage extends CommonBase
{
	@FindBy(id="exampleInputEmail1")
	WebElement loginEmail;
	
	@FindBy(id="exampleInputPassword1")
	WebElement pwd;
	
	@FindBy(linkText="ADD TO CART")
	WebElement addToCart;
	
	@FindBy(name="ordersubmit")
	WebElement orderSubmit;
	
	@FindBy(name="login")
	WebElement loginBtn;
	
	@FindBy(name="submit")
	WebElement finalOrderPlace;
	
	public void addReview2()
	{
		driver.get(config.getProperty("url"));
		String parent=driver.getWindowHandle();
		
	    driver.findElement(By.cssSelector(".menu-item > .dropdown-toggle:nth-child(1)")).click();
	    driver.findElement(By.cssSelector(".col-sm-6:nth-child(1) img")).click();
	    driver.findElement(By.linkText("ADD TO CART")).click();
	    Alert a=driver.switchTo().alert();
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        a.accept();
        
        driver.findElement(By.name("ordersubmit")).click();
	 
	    loginEmail.click();
	    loginEmail.sendKeys("anuj.lpu1@gmail.com");
	    pwd.click();
	    pwd.sendKeys("Test@123");
	    loginBtn.click();
	    orderSubmit.click();
	    driver.findElement(By.cssSelector("input:nth-child(2)")).click();
	    finalOrderPlace.click();
	    
	    driver.get(config.getProperty("adminurl"));
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        
        //Window Handle
        String child=driver.getWindowHandle();
        
        
	    driver.findElement(By.cssSelector("tr:nth-child(9) > td:nth-child(10) > a")).click();
	
	    driver.findElement(By.cssSelector("tbody")).click();
	    String status=driver.findElement(By.cssSelector("tr:nth-child(7) b")).getText();
	    
	   
	    driver.findElement(By.cssSelector("tr:nth-child(9) > .cart-product-name-info a")).click();
	    driver.findElement(By.linkText("REVIEW")).click();
	    driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > .radio")).click();
	    driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(4) > .radio")).click();
	    driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(5) > .radio")).click();
	    driver.findElement(By.id("exampleInputName")).click();
	    driver.findElement(By.id("exampleInputName")).sendKeys("Anuj");
	    driver.findElement(By.id("exampleInputSummary")).click();
	    driver.findElement(By.id("exampleInputSummary")).sendKeys("Nice book");
	    driver.findElement(By.id("exampleInputReview")).click();
	    driver.findElement(By.id("exampleInputReview")).sendKeys("Everyone should read it.");
	    driver.findElement(By.name("submit")).click();
	    driver.findElement(By.linkText("REVIEW")).click();
	    driver.findElement(By.cssSelector(".author")).click();
	    driver.findElement(By.cssSelector(".author")).click();
	    driver.findElement(By.cssSelector(".author")).click();
	    String userName=driver.findElement(By.cssSelector(".name:nth-child(2)")).getText();
	    driver.findElement(By.cssSelector(".author")).click();
	    driver.findElement(By.linkText("Logout")).click();
	}
	
    public void addReview(String emailId,String pwd,String orderStatus,String name,String summary,String review )
    {
    	
    	driver.get(config.getProperty("url"));
		String parent=driver.getWindowHandle();
		
	    driver.findElement(By.cssSelector(".menu-item > .dropdown-toggle:nth-child(1)")).click();
	    driver.findElement(By.cssSelector(".col-sm-6:nth-child(1) img")).click();
	    driver.findElement(By.linkText("ADD TO CART")).click();
	    Alert a=driver.switchTo().alert();
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        a.accept();
        
        driver.findElement(By.name("ordersubmit")).click();
        driver.findElement(By.id("exampleInputEmail1")).click();

        driver.findElement(By.id("exampleInputEmail1")).sendKeys(emailId);
        driver.findElement(By.id("exampleInputPassword1")).click();
        driver.findElement(By.id("exampleInputPassword1")).sendKeys(pwd);
        driver.findElement(By.name("login")).click();
        driver.findElement(By.name("ordersubmit")).click();
        driver.findElement(By.cssSelector("input:nth-child(2)")).click();
        driver.findElement(By.name("submit")).click();
        
        driver.get(config.getProperty("adminurl"));
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        
        driver.findElement(By.id("inputEmail")).click();
        driver.findElement(By.id("inputEmail")).sendKeys(config.getProperty("adminUserName"));
        driver.findElement(By.name("password")).click();
        driver.findElement(By.name("password")).sendKeys(config.getProperty("adminPassword"));
        driver.findElement(By.name("submit")).click();
        //Window Handle
        String child=driver.getWindowHandle();
        driver.findElement(By.xpath("//body[1]/div[2]/div[1]/div[1]/div[1]/div[1]/ul[1]/li[1]/a[1]/i[2]")).click();
        driver.findElement(By.cssSelector("li:nth-child(1) > a > .label")).click();
        driver.findElement(By.cssSelector("tbody")).click();
        driver.findElement(By.xpath("//body[1]/div[2]/div[1]/div[1]/div[1]/div[1]/ul[1]/li[1]/a[1]/i[2]")).click();

        JavascriptExecutor js=(JavascriptExecutor)driver;
        js.executeScript("window.scrollBy(0,800)");
       
      //After each test execution tr value needs to be changed
        driver.findElement(By.xpath("//tbody/tr[2]/td[9]/a[1]/i[1]")).click();    
      
      
        Set<String> allWindows = driver.getWindowHandles();
        for(String curWindow : allWindows){
            driver.switchTo().window(curWindow);
        }
        driver.findElement(By.xpath("//*[@id=\"updateticket\"]/table/tbody/tr[3]/td[2]/span/select")).click();
        {
          WebElement dropdown = driver.findElement(By.xpath("//*[@id=\"updateticket\"]/table/tbody/tr[3]/td[2]/span/select"));
          dropdown.findElement(By.xpath("//option[. = 'Delivered']")).click();
        }
        driver.findElement(By.name("remark")).click();
        driver.findElement(By.name("remark")).sendKeys(orderStatus);
        driver.findElement(By.name("submit2")).click();
        Alert a2=driver.switchTo().alert();
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        a2.accept();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.switchTo().window(child);
        js.executeScript("window.scrollBy(0,-800)");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      
        driver.get(config.getProperty("url"));
        driver.findElement(By.xpath("//*[@id=\"top-banner-and-menu\"]/div/div/div[1]/div[1]/div/nav/ul/li/a[1]")).click();
        driver.findElement(By.xpath("//*[@id=\"grid-container\"]/div/div/div[1]/div/div/div[1]/div/a/img")).click();
        js.executeScript("window.scrollBy(0,800)");
        driver.findElement(By.linkText("REVIEW")).click();
        driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > .radio")).click();
        driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(4) > .radio")).click();
        driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(5) > .radio")).click();
        driver.findElement(By.id("exampleInputName")).click();
        driver.findElement(By.id("exampleInputName")).sendKeys(name);
        driver.findElement(By.id("exampleInputSummary")).click();
        driver.findElement(By.id("exampleInputSummary")).sendKeys(summary);
        driver.findElement(By.id("exampleInputReview")).click();
        driver.findElement(By.id("exampleInputReview")).sendKeys(review);
        driver.findElement(By.name("submit")).click();
        driver.findElement(By.linkText("REVIEW")).click();
        driver.findElement(By.cssSelector(".author")).click();
        driver.findElement(By.cssSelector(".author")).click();
        driver.findElement(By.cssSelector(".author")).click();
        driver.findElement(By.cssSelector(".author")).click();
        driver.findElement(By.linkText("Logout")).click();
    }
    
    public String verifyUserName(String userName,String summary,String review,String searchKey,String expUserName)
    {
    	driver.get(config.getProperty("url"));
         driver.findElement(By.name("product")).click();
        driver.findElement(By.name("product")).sendKeys(searchKey);
        driver.findElement(By.cssSelector(".search-button")).click();
        driver.findElement(By.cssSelector(".image > a > img")).click();
        driver.findElement(By.linkText("REVIEW")).click();
        driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > .radio")).click();
        driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(5) > .radio")).click();
        driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(4) > .radio")).click();
        driver.findElement(By.id("exampleInputName")).click();
        driver.findElement(By.id("exampleInputName")).sendKeys(userName);
        driver.findElement(By.id("exampleInputSummary")).click();
        driver.findElement(By.id("exampleInputSummary")).sendKeys(summary);
        driver.findElement(By.id("exampleInputReview")).click();
        driver.findElement(By.id("exampleInputReview")).sendKeys(review);
        driver.findElement(By.name("submit")).click();
        driver.findElement(By.linkText("REVIEW")).click();
        driver.findElement(By.cssSelector(".name:nth-child(2)")).click();
        String actualUserName=driver.findElement(By.cssSelector(".name:nth-child(2)")).getText();
        return actualUserName;
    }
    
	public boolean verifyAutoFillUserName(String userName,String summary,String review,String searchKey,String expUserName)
    {
    	driver.get(config.getProperty("url"));
        
        driver.findElement(By.name("product")).click();
        driver.findElement(By.name("product")).sendKeys(searchKey);
        driver.findElement(By.cssSelector(".search-button")).click();
        driver.findElement(By.cssSelector(".image > a > img")).click();
        driver.findElement(By.linkText("REVIEW")).click();
        driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > .radio")).click();
        driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(5) > .radio")).click();
        driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(4) > .radio")).click();

        driver.findElement(By.id("exampleInputSummary")).click();
        driver.findElement(By.id("exampleInputSummary")).sendKeys(summary);
        driver.findElement(By.id("exampleInputReview")).click();
        driver.findElement(By.id("exampleInputReview")).sendKeys(review);
        driver.findElement(By.name("submit")).click();
        driver.findElement(By.cssSelector(".form-group:nth-child(2)")).click();
        driver.findElement(By.id("exampleInputName")).click();
        driver.findElement(By.name("submit")).click();
        boolean status=driver.findElement(By.id("exampleInputName")).getAttribute("value")!="";
        boolean status1=driver.findElement(By.xpath("//body/div[2]/div[1]/div[1]/div[2]/div[2]/div[1]/div[2]/div[1]/div[2]/div[1]/form[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/label[1]/span[1]")).isDisplayed();
        return status;
    }

}

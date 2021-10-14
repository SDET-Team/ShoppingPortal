package com.project.pages;

import java.io.IOException;
import java.util.List;
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

import com.project.base.CommonBase;
import com.project.utils.TestUtils;

public class AdminPage extends CommonBase
{		
	@FindBy(id="inputEmail")
	WebElement adminLoginMail;
	
	@FindBy(name="password")
	WebElement adminLoginPass;
	
	@FindBy(name="submit")
	WebElement adminLoginBtn;
	
	@FindBy(xpath="//body/div[2]/div[1]/div[1]/div[1]/div[1]/ul[1]/li[1]/a[1]/i[2]")
	WebElement orderManagementList;

	@FindBy(xpath="//body/div[2]/div[1]/div[1]/div[1]/div[1]/ul[1]/li[1]/ul[1]/li[3]/a[1]/b[1]")
	WebElement colorCode;
	
	@FindBy(xpath="//body/div[2]/div[1]/div[1]/div[1]/div[1]/ul[1]/li[1]/ul[1]/li[3]/a[1]")
	WebElement deliveredOrders;
	
	@FindBy(linkText="Create Category")
	WebElement createCategoryBtn;
	
	@FindBy(name="category")
	WebElement inputCategoryName;
	
	@FindBy(name="submit")
	WebElement createBtn;
	
	@FindBy(name="description")
	WebElement descriptionBox;
	
	@FindBy(linkText="Insert Product")
	WebElement insertProductBtn;
	
	@FindBy(id="productimage1")
	WebElement productImageBtn1;
	
	@FindBy(name="submit")
	WebElement productSubmitBtn;
	
	@FindBy(name="productName")
	WebElement productNameField;
	
	@FindBy(name="category")
	WebElement selectCategory;
	
	@FindBy(name="productCompany")
	WebElement productCompanyField;
	
	@FindBy(name="productpricebd")
	WebElement productPriceBDField;
	
	@FindBy(name="productprice")
	WebElement productPriceField;

	@FindBy(id="subcategory")
	WebElement selectsubCategory;
	
	@FindBy(xpath="//body/div[2]/div[1]/div[1]/div[2]/div[1]/div[1]/div[2]/form[1]/div[7]/div[1]/div[2]/div[1]")
	WebElement productDescriptionField;
	
	@FindBy(name="productShippingcharge")
	WebElement productShippingChargeField;
	
	@FindBy(name="productimage2")
	WebElement productImageBtn2;
	
	@FindBy(id="productAvailability")
	WebElement productAvailabilityField;
	
	@FindBy(name="productimage3")
	WebElement productImageBtn3;
	
	@FindBy(name="submit")
	WebElement insertProductSubmitBtn;
	
	boolean status;
	public AdminPage()
	{
		PageFactory.initElements(driver,this);
	}
	
	//Login 
	public String adminloginpageTitle()
	{
		return driver.getTitle();
	}
	
	public void adminloginOperation(String email,String password) 
	{
		adminLoginMail.sendKeys(email);
		adminLoginPass.sendKeys(password);
		adminLoginBtn.click();
		
	}
	
	public boolean orderManagement()
	{
		orderManagementList.click();
		boolean status=deliveredOrders.isDisplayed();
		return status;
	}
	public String getColorCode()
	{
		return colorCode.getCssValue("color");
	}
	public String createCategory()
	{
		
		createCategoryBtn.click();
		inputCategoryName.click();
		boolean isEditable = inputCategoryName.isEnabled() && inputCategoryName.getAttribute("readonly") == null;
	    Assert.assertTrue(isEditable); 
		inputCategoryName.sendKeys("Electronics");
		String categoryName="Electronics";
		descriptionBox.click();
		descriptionBox.sendKeys("Regular electronic products");
		{
		List<WebElement> elements=driver.findElements(By.name("submit"));
		assert(elements.size()>0);
		}
		driver.findElement(By.name("submit")).click();
	    {
	      List<WebElement> elements = driver.findElements(By.cssSelector("strong"));
	      assert(elements.size() > 0);
	    }
	    driver.findElement(By.cssSelector(".even:nth-child(2) > td:nth-child(2)")).click();
	    //String getCategoryText=driver.findElement(By.cssSelector(".even:nth-child(2) > td:nth-child(2)")).getText();
	   
	    
	    inputCategoryName.click();
	    inputCategoryName.sendKeys("Electronics");
	    if(driver.getPageSource().contains(categoryName))
	    {
	    	status=false;
	    	System.out.println("Category already exists");
	    }
	    descriptionBox.click();
	    descriptionBox.sendKeys("Regular electronics item");
	    createBtn.click();
		
		return categoryName;
	}
	
	public String uploadImage()
	{
		JavascriptExecutor js=(JavascriptExecutor)driver;
		boolean status=insertProductBtn.isDisplayed();
		Assert.assertTrue(status);
		insertProductBtn.click();
		productNameField.click();
		productNameField.sendKeys("TV");
		
		js.executeScript("window.scrollBy(0,800)");
		status=productImageBtn1.isDisplayed();
		Assert.assertTrue(status);
		
		Actions builder=new Actions(driver);
        builder.moveToElement(productImageBtn1).click().build().perform();
		
		//AutoIt
		try {
			Runtime.getRuntime().exec("F:/Selenium/MiniProject.exe");
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
		String expectedTestData="SamsungTVFrontView.jpg";
		 if(driver.getPageSource().contains(expectedTestData))
		    {

		    	System.out.println("Product Image Added successfull");
		    }
		      
		return expectedTestData;
		
	}
	public String todaysOrder()
	{
	    
	   // orderManagementList.click();
	    String status=driver.findElement(By.cssSelector("li:nth-child(1) > a > .label")).getText();
	    driver.findElement(By.cssSelector("li:nth-child(1) > a > .label")).click();
	    return status;
	}
	public String insertProduct()
	{
		WebDriverWait wait = new WebDriverWait(driver, 30);
		JavascriptExecutor js=(JavascriptExecutor)driver;
		boolean status=insertProductBtn.isDisplayed();
		Assert.assertTrue(status);
		insertProductBtn.click();
		productNameField.click();
		
		selectCategory.click();
		WebElement dropdown=selectCategory;
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//option[. = 'Electronics']")));
		dropdown.findElement(By.xpath("//option[. = 'Electronics']")).click();
		
		selectsubCategory.click();
		WebElement dropdown2=selectsubCategory;
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//option[. = 'Television']")));
        dropdown2.findElement(By.xpath("//option[. = 'Television']")).click();
		
		productNameField.sendKeys("Samsung");
		productCompanyField.click();
		productCompanyField.sendKeys("Samsung");
		productPriceBDField.click();
		productPriceBDField.sendKeys("52500");
		productPriceField.click();
		productPriceField.sendKeys("49500");
		productDescriptionField.click();
		WebElement element=productDescriptionField;
		js.executeScript("if(arguments[0].contentEditable === 'true') {arguments[0].innerText = 'Basic Tv'}", element);
		
		js.executeScript("window.scrollBy(0,800)");
		
		productShippingChargeField.click();
		productShippingChargeField.sendKeys("0");
	    productAvailabilityField.click();
	    WebElement dropdown3=productAvailabilityField;
	    dropdown3.findElement(By.xpath("//option[. = 'In Stock']")).click();
		
		
		status=productImageBtn1.isDisplayed();
		Assert.assertTrue(status);
		
		int i=0;
		Actions builder1=new Actions(driver);
        builder1.moveToElement(productImageBtn1).click().build().perform();
		AutoIt(i++);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
		Actions builder2=new Actions(driver);
        builder2.moveToElement(productImageBtn2).click().build().perform();
		AutoIt(i++);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
		Actions builder3=new Actions(driver);
        builder3.moveToElement(productImageBtn3).click().build().perform();
		AutoIt(i++);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
		wait.until(ExpectedConditions.elementToBeClickable(insertProductBtn));
		insertProductBtn.click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
		/*wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//body[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[1]/div[2]/div[1]"), "×\\\\\\\\nWell done! Product Inserted Successfully !!"));
		String output=driver.findElement(By.xpath("//body[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[1]/div[2]/div[1]")).getText();
        */
		driver.get("http://localhost/OnlineShoppingPortal/shopping/");
		driver.findElement(By.cssSelector(".dropdown:nth-child(3) > a")).click();
	    driver.findElement(By.cssSelector(".dropdown:nth-child(6) > a")).click();
	    driver.findElement(By.cssSelector(".dropdown:nth-child(7) > a")).click();
	    driver.findElement(By.cssSelector(".dropdown:nth-child(3) > a")).click();
	    driver.findElement(By.cssSelector(".dropdown-toggle:nth-child(2)")).click();
	    String output=driver.findElement(By.linkText("Samsung")).getText();
	    driver.findElement(By.cssSelector(".col-sm-6:nth-child(2) img")).click();
	    {
	      List<WebElement> elements = driver.findElements(By.cssSelector(".owl-item:nth-child(1) > #slide1 .img-responsive"));
	      assert(elements.size() > 0);
	    }
	    driver.findElement(By.name("product")).click();
	    driver.findElement(By.name("product")).sendKeys("samsung");
	    driver.findElement(By.cssSelector(".search-button")).click();
	    output=driver.findElement(By.linkText("Samsung")).getText();
	    System.out.println(output);
		return output;
		
	}
	//AutoIt
	public void AutoIt(int i)
	{
		try {
		if(i==1)
		{
		    Runtime.getRuntime().exec("F:/Selenium/AutoItScript1.exe");
		        
	    }
		if(i==2)
		{
			Runtime.getRuntime().exec("F:/Selenium/AutoItScript2.exe");
		    
		}
		if(i==3)
		{
			Runtime.getRuntime().exec("F:/Selenium/AutoItScript3.exe");
		    
		}
		
		} catch (IOException e){
	         e.printStackTrace();
	        }
	}
}
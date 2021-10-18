package com.project.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.project.base.CommonBase;


class OrderDetail {
	private String productName;
	private String productQuantity;
	private String pricePerUnit;
	private String shippingCharge;
	private String grandTotal;
	private String paymentMode;
	private String orderDate;
	
	public OrderDetail(WebDriver driver, By tableRowXPath) {
		String tableRowXPathString = tableRowXPath.toString().split(" ")[1];
		// set product name
		productName = driver.findElement(By.xpath(tableRowXPathString + "/td[" + 3 + "]")).getText();
		// set product quantity
		productQuantity = driver.findElement(By.xpath(tableRowXPathString + "/td[" + 4 + "]")).getText();
		// set price per unit
		pricePerUnit = driver.findElement(By.xpath(tableRowXPathString + "/td[" + 5 + "]")).getText();
		// set shipping charges
		shippingCharge = driver.findElement(By.xpath(tableRowXPathString + "/td[" + 6 + "]")).getText();
		// set grand total
		grandTotal = driver.findElement(By.xpath(tableRowXPathString + "/td[" + 7 + "]")).getText();
		// set payment mode
		paymentMode = driver.findElement(By.xpath(tableRowXPathString + "/td[" + 8 + "]")).getText();
		// set order date
		orderDate = driver.findElement(By.xpath(tableRowXPathString + "/td[" + 9 + "]")).getText();
	}

	public String getProductName() {
		return productName;
	}
	
	public String getProductQuantity() {
		return productQuantity;
	}

	public String getPricePerUnit() {
		return pricePerUnit;
	}

	public String getShippingCharge() {
		return shippingCharge;
	}

	public String getGrandTotal() {
		return grandTotal;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public String getOrderDate() {
		return orderDate;
	}

	@Override
	public String toString() {
		return "productName=" + productName + ", productQuantity=" + productQuantity + ", pricePerUnit="
				+ pricePerUnit + ", shippingCharge=" + shippingCharge + ", grandTotal=" + grandTotal + ", paymentMode="
				+ paymentMode + ", orderDate=" + orderDate;
	}

}


class OrderTableExtractFromWebpage {
	
	private WebDriver driver;
	private By tableXPath;
	
	public OrderTableExtractFromWebpage(WebDriver driver, By tableXPath) {
		this.driver = driver;
		this.tableXPath = tableXPath;
	}
	
	public List<OrderDetail> getOrderDetails() {
		List<OrderDetail> orders = new ArrayList<OrderDetail>();
		int rows = getRowCountFromTable();
		for(int i=1; i<=rows; i++) {
			orders.add(getOrderDetailByIndex(i));
		}
		return orders;
	}
	
	private int getColumnCountFromTable() {
		String tableHeaderXPathString = tableXPath.toString().split(" ")[1] + "/thead/tr/th";
		return driver.findElements(By.xpath(tableHeaderXPathString)).size();
	}
	
	private int getRowCountFromTable() {
		String tableBodyXPathString = tableXPath.toString().split(" ")[1] + "/tbody/tr";
		return driver.findElements(By.xpath(tableBodyXPathString)).size();
	}
	
	private OrderDetail getOrderDetailByIndex(int index) {
		String tableRowXPathString = tableXPath.toString().split(" ")[1] + "/tbody/tr[" + index + "]";
		return (new OrderDetail(driver, By.xpath(tableRowXPathString)));
	}
}


public class OrderHistoryPage extends CommonBase {
	
	private By tableXPath;
	
	public OrderHistoryPage() {
		Assert.assertEquals(driver.getTitle(), "Order History", "Page title is not as expected.");
		Assert.assertEquals(driver.getCurrentUrl(), "http://localhost/shopping/order-history.php", "Page URL is not as expected.");
		PageFactory.initElements(driver, this);
		
		tableXPath = By.xpath(".//*[@class='shopping-cart']/div/div/form/table");
	}
	
	
	@FindBy(css = ".cnt-account > ul:nth-child(1) > li:nth-child(2) > a:nth-child(1)")
	WebElement myAccountButton;
	
	
	public String getTitle() {
		return driver.getTitle();
	}
	
	public void returnToMyAccount() {
		myAccountButton.click();
	}
	
	public boolean checkLatestOrderIsOnTop() {
		List<OrderDetail> orders = extractOrderHistoryFromPage();
		
		if(orders.size() <= 1) {
			return true;
		}
		else if(orders.size() > 1) {
			String orderDate1 = extractNumbersOnly( orders.get(0).getOrderDate() );
			String orderDate2 = extractNumbersOnly( orders.get(1).getOrderDate() );
			for(int i=0; i<orderDate1.length(); i++) {
				if(orderDate1.charAt(i) < orderDate2.charAt(i)) {
					return false;
				}
			}
			return true;
		}
		
		return false;
	}
	
	public boolean checkAllOrdersContainProductDetails() {
		List<OrderDetail> orders = extractOrderHistoryFromPage();
		for(OrderDetail order : orders) {
			if(order.getProductName().equals("")) {
				return false;
			}
			else if(order.getProductQuantity().equals("")) {
				return false;
			}
			else if(order.getPricePerUnit().equals("")) {
				return false;
			}
			else if(order.getShippingCharge().equals("")) {
				return false;
			}
			else if(order.getGrandTotal().equals("")) {
				return false;
			}
			else if(order.getPaymentMode().equals("")) {
				return false;
			}
			else if(order.getOrderDate().equals("")) {
				return false;
			}
		}
		return true;
	}
	
	
	private List<OrderDetail> extractOrderHistoryFromPage() {
		return ( new OrderTableExtractFromWebpage(driver, tableXPath) ).getOrderDetails();
	}
	
	private String extractNumbersOnly(String str) {
		String res = "";
		for(int i=0; i<str.length(); i++) {
			if( Character.isDigit( str.charAt(i) ) ) {
				res += str.charAt(i);
			}
		}
		return res;
	}

}

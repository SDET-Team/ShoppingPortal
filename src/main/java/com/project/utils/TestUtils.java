package com.project.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.openqa.selenium.TimeoutException;
import java.util.concurrent.TimeUnit;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.project.base.CommonBase;
import com.project.pages.CartActivity;
import com.project.pages.MyCartPage;

public class TestUtils extends CommonBase {

	public static long IMPLICIT_WAIT = 5;
	static HttpURLConnection huc = null;
	static int respCode = 200;
	static CartActivity cartActivity;
	static MyCartPage myCartPage;

	public static Object[][] getTestData(String filepath, String type) throws IOException {

		File src = new File(filepath);
		FileInputStream fileinput = new FileInputStream(src);
		Workbook wb = new XSSFWorkbook(fileinput);
		XSSFSheet sheet1;
		if (type.equals("Positive"))
			sheet1 = (XSSFSheet) wb.getSheetAt(0);
		else
			sheet1 = (XSSFSheet) wb.getSheetAt(1);

		DataFormatter formatter = new DataFormatter();

		int row_count = sheet1.getLastRowNum();
		int column_count = sheet1.getRow(0).getLastCellNum();
		Object data[][] = new Object[row_count][column_count];
		for (int i = 0; i < row_count; i++) {
			for (int j = 0; j < column_count; j++) {

				data[i][j] = sheet1.getRow(i + 1).getCell(j).toString();
			}

		}
		return data;

	}

	public static Alert switchToAlert() {
		Alert alert = driver.switchTo().alert();
		return alert;
	}

	public static boolean isLinkValid(String urlString) {
		String baseUrl = config.getProperty("url");
		boolean isValid = false;

		if (!isTextFormated(urlString)) {
			log.error(urlString + " => " + "Either url is not configured or it is empty");
			return isValid;
		}

		if (!urlString.startsWith(baseUrl)) {
			log.error(urlString + " => " + "belongs to another domain, skipping it");
			return isValid;
		}

		try {
			huc = (HttpURLConnection) (new URL(urlString).openConnection());
			huc.setRequestMethod("HEAD");
			huc.connect();
			respCode = huc.getResponseCode();
			if (respCode >= 400) {
				log.error(urlString + " => " + "is a broken link");
				return isValid;
			} else {
				log.info(urlString + " => " + "is a valid link");
				isValid = true;
				return isValid;
			}
		} catch (MalformedURLException e) {
			log.error("MalformedURLException");
		} catch (IOException e) {
			log.error("IOException");
		} catch (Exception e) {
			log.error("Error");
		}
		return isValid;
	}

	public static boolean isTextFormated(String string) {
		if (string == null || string.isEmpty() || string == "#") {
			return false;
		}
		return true;
	}

	public static void setTestData(String fileName, String sheetName, Map<Integer, ArrayList<String>> dataMap,
			String[] columnNames) throws FileNotFoundException {

		File file = new File(fileName);
		XSSFWorkbook workbook = null;
		if (file.exists() == false) {
			workbook = new XSSFWorkbook();
		} else {
			try {
				InputStream inputStream = new FileInputStream(file);
				workbook = new XSSFWorkbook(inputStream);
			} catch (IOException e) {
				log.error("IOException");
			}
		}

		XSSFSheet spreadsheet = workbook.getSheet(sheetName);
		if (workbook.getNumberOfSheets() != 0) {
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				if (workbook.getSheetName(i).equals(sheetName)) {
					spreadsheet = workbook.getSheet(sheetName);
				} else {
					spreadsheet = workbook.createSheet(sheetName);
				}
			}
		} else {
			spreadsheet = workbook.createSheet(sheetName);
		}

		XSSFRow row;
		row = spreadsheet.createRow(0);
		for (int i = 0; i < columnNames.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(columnNames[i]);
		}

		Set<Integer> keyid = dataMap.keySet();
		int rowid = 1;
		for (Integer key : keyid) {
			row = spreadsheet.createRow(rowid++);
			ArrayList<String> dataArrayList = dataMap.get(key);
			int cellid = 0;

			for (String sData : dataArrayList) {
				Cell cell = row.createCell(cellid++);
				cell.setCellValue(sData);
			}
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException");
		} catch (IOException e) {
			log.error("IOException");
		}

	}

	public static boolean isAlertPresent() {

		try {
			WebDriverWait wait = new WebDriverWait(driver, 50);
			wait.until(ExpectedConditions.alertIsPresent());
			return true;
		} catch (TimeoutException e) {
			return false;
		}

	}

	public static boolean isVisible(WebElement element) {
		try {
			if (element.isDisplayed()) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public static void addToCartProduct(int numberOfproducts, int numberOfTimes) {
		cartActivity = new CartActivity();
		List<WebElement> featureProductList = cartActivity.getfeatureProductListElement();
		
		if(numberOfproducts >= featureProductList.size()) {
			numberOfproducts = featureProductList.size();
		} else if(numberOfproducts <= 0) {
			numberOfproducts = 0;
		}
		
		if (numberOfTimes >= featureProductList.size()) {
			numberOfTimes = featureProductList.size();
		} else if (numberOfTimes <= featureProductList.size()) {
			numberOfTimes = 1;
		} 
		
		for (int i = 1; i <= numberOfproducts + 1; i++) {
			for (int j = 1; j <= numberOfTimes; j++) {
				driver.manage().timeouts().implicitlyWait(TestUtils.IMPLICIT_WAIT, TimeUnit.SECONDS);
				WebElement firstElement = featureProductList.get(i);
				cartActivity.handleClickActionOnWebElement(firstElement, i);

				try {
					navbeforeLogin.clickOnLogoImage();
				} catch (ElementNotInteractableException e) {
					log.error("ElementNotInteractableException");
				}

			}

		}
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		navbeforeLogin.clickOnMyCartImage();
		log.info(numberOfproducts + "Products added to cart");
	}

	public static void removeProductFromCart(int count) throws AssertionError {
		myCartPage = new MyCartPage();
		Map<Integer, ArrayList<WebElement>> bodyElements = myCartPage.getTableBodyData();

		boolean isSelected = myCartPage.selectElementToBDeleted(bodyElements, count);
		if (isSelected) {
			driver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
			myCartPage.updateShoppingCartClick();
		}

	}

}

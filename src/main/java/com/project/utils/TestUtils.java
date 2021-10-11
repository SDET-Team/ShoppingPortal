package com.project.utils;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;

import com.project.base.CommonBase;

public class TestUtils extends CommonBase{

	
	public static long IMPLICIT_WAIT = 5;
	
	
	
	 public static Object[][] getTestData(String filepath) throws IOException{
			
		 	File src=new File(filepath);
			
			FileInputStream fileinput=new FileInputStream(src);
			
			Workbook wb=new XSSFWorkbook(fileinput);
			
			XSSFSheet sheet1=(XSSFSheet) wb.getSheetAt(0);
			
			DataFormatter formatter = new DataFormatter();
			
			int row_count=sheet1.getLastRowNum();
			int column_count=sheet1.getRow(0).getLastCellNum();
			
			Object data[][] = new Object[row_count][column_count];
			for (int i = 0; i <row_count; i++) 
			{ 
				for (int j = 0; j < column_count; j++) {
					data[i][j] = sheet1.getRow(i + 1).getCell(j).toString();
				}
			
			

	 }
			return data;
			
}
	 
	 public static Alert switchToAlert(){
		 Alert alert=driver.switchTo().alert();
		 return alert;
		}
}

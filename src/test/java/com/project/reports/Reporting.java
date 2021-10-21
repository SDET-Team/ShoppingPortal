package com.project.reports;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Reporting extends TestListenerAdapter
{
	public ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	
	public static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();
	
	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}
	
	public synchronized void onStart(ITestContext testContext)
	{
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());//time stamp
		String repName="Test-Report-"+timeStamp+".html";
		
		htmlReporter=new ExtentHtmlReporter(System.getProperty("user.dir")+ "\\src\\resources\\report\\"+repName);//specify location of the report
		htmlReporter.loadXMLConfig(System.getProperty("user.dir")+ "\\src\\main\\java\\com\\project\\config\\extentconfig.xml");
		
		extent=new ExtentReports();
		
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Host name","localhost");
		extent.setSystemInfo("Environemnt","QA");
		extent.setSystemInfo("user","SDET");
		
		htmlReporter.config().setDocumentTitle("Shopping portal Automation"); // Tile of report
		htmlReporter.config().setReportName("Functional Test Automation Report"); // name of the report
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP); //location of the chart
		htmlReporter.config().setTheme(Theme.DARK);
	}




	public synchronized void onTestStart(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		String qualifiedName = result.getMethod().getQualifiedName();
		int last = qualifiedName.lastIndexOf(".");
		int mid = qualifiedName.substring(0, last).lastIndexOf(".");
		String className = qualifiedName.substring(mid + 1, last);

		
		ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName(),result.getMethod().getDescription());

		extentTest.assignCategory(result.getTestContext().getSuite().getName());
		
		extentTest.assignCategory(className);
		test.set(extentTest);
		test.get().getModel().setStartTime(getTime(result.getStartMillis()));
	}
	
	
	public synchronized void onTestSuccess(ITestResult result)
	{
		
		//logger.log(Status.PASS,MarkupHelper.createLabel(result.getName(),ExtentColor.GREEN)); // send the passed information to the report with GREEN color highlighted
	
		test.get().pass("Test passed");
		test.get().getModel().setEndTime(getTime(result.getEndMillis()));
	}
	

	public synchronized void onTestFailure(ITestResult result)
	{
		/*logger=extent.createTest(result.getName()); // create new entry in th report
		logger.log(Status.FAIL,MarkupHelper.createLabel(result.getName(),ExtentColor.RED)); // send the passed information to the report with GREEN color highlighted
		
		String screenshotPath=System.getProperty("user.dir")+"\\src\\resources\\screenshots\\"+result.getName()+".png";
		
		File f = new File(screenshotPath); 
		
		if(f.exists())
		{
		try {
			logger.fail("Screenshot is below:" + logger.addScreenCaptureFromPath(screenshotPath));
			} 
		catch (IOException e) 
				{
				e.printStackTrace();
				}
		}*/

try {
		String screenshotPath=System.getProperty("user.dir")+"\\src\\resources\\screenshots\\"+result.getName()+".png";
		String filename=result.getName()+".png";
		test.get().fail(result.getThrowable(),MediaEntityBuilder.createScreenCaptureFromPath("ss.png").build());
		} catch (IOException e) {
			System.err.println("Exception thrown while updating test fail status " + Arrays.toString(e.getStackTrace()));
		}
		test.get().getModel().setEndTime(getTime(result.getEndMillis()));
		
	}
	

	public synchronized void onTestSkipped(ITestResult result)
	{
		//logger=extent.createTest(result.getName()); // create new entry in th report
		//logger.log(Status.SKIP,MarkupHelper.createLabel(result.getName(),ExtentColor.ORANGE));
	
		try {
			String screenshotPath=System.getProperty("user.dir")+"\\src\\resources\\screenshots\\";
			String filename=result.getName()+".png";
			test.get().skip(result.getThrowable(),MediaEntityBuilder.createScreenCaptureFromPath("ss.png").build());
		} catch (IOException e) {
			System.err.println("Exception thrown while updating test skip status " + Arrays.toString(e.getStackTrace()));
		}
			
		test.get().getModel().setEndTime(getTime(result.getEndMillis()));
	}
	


	public synchronized void onFinish(ITestContext testContext)
	{
		extent.flush();
		test.remove();
	}
}

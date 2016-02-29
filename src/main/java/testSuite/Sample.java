package testSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import junit.framework.Assert;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


import com.galenframework.api.Galen;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import com.galenframework.reports.TestReport;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.tests.GalenBasicTest;

public class Sample {

	WebDriver driver;
	static final Logger logger = Logger.getLogger(Sample.class);
	LayoutReport layoutReport =null;
	static List<GalenTestInfo> tests = new LinkedList<GalenTestInfo>();
	final String url ="http://mail.synechron.com";
	GalenTestInfo test = null;
	String browserName ="";
	GalenBasicTest basictest = new GalenBasicTest();
	
	@Parameters({ "browser","width", "height" })
	@BeforeTest
	public void setUp(String browser,int width, int height) throws InterruptedException
	{
		//BasicConfigurator.configure();

		logger.info("Launching "+browser+" Browser..........");
		browserName = browser;

		try {
			if (browser.equalsIgnoreCase("Firefox")) {
				driver = new FirefoxDriver();
			} else if (browser.equalsIgnoreCase("chrome")) {
				System.setProperty("webdriver.chrome.driver",
						"C:/Srikanth/Synechron/Driver/chromedriver.exe");
				driver = new ChromeDriver();
			} else if (browser.equalsIgnoreCase("IE")) {
				System.setProperty("webdriver.ie.driver",
						"C:/Srikanth/Synechron/Driver/IEDriverServer.exe");
				driver = new InternetExplorerDriver();
			}

		} catch (WebDriverException e) {
			System.out.println(e.getMessage());
		}
		driver.manage().window().setSize(new Dimension(width,height));

		driver.get(url);

		Thread.sleep(5000);
		logger.info("Launched "+browser+" Browser Successfullly");
	}

	@Test
	public void testCase_01()
	{
		Reporter.log("Started Executing testCase_01");
		try {
			logger.info("Started Executing the homepage Galen Specs");
			Reporter.log("Started Executing the homepage Galen Specs");
			
			generateResourceFile("src\\main\\resources\\homepage.xls","src\\main\\resources\\resources.properties");

			Properties prop= new Properties();
			InputStream input = null;
			String filename = "homepage.properties";
    		input = Sample.class.getClassLoader().getResourceAsStream(filename);
    		System.out.println("File : "+input);
    		if(input==null){
    	            System.out.println("Sorry, unable to find " + filename);
    		    return;
    		}
    		prop.load(input);
			layoutReport = Galen.checkLayout(driver, "specs/homepage.gspec", Arrays.asList("desktop"), null, prop, null);

			logger.info("Exected the homepage Galen Specs Successfully......");
			driver.findElement(By.linkText("here")).click();

			logger.info("Navigated to Quarantine Login Page");
			// Creating a list of tests
			
			// Creating an object that will contain the information about the test
			/*GalenTestInfo testInfo = new GalenTestInfo("Check Synechron Mail Site on"+browserName, basictest);
			testInfo.setTest(test);*/
			test = GalenTestInfo.fromString("Check Synechron Mail Site on "+browserName);
			
			TestReport testReport = new TestReport();
			testReport.layout(layoutReport, "Check Layout on mail.synechron.com");
			test.setReport(testReport);
			System.out.println("Report File"+testReport.getFileStorage().getFiles());
			// Adding layout report to the test report
			//test.getReport().layout(layoutReport, "Check Layout on mail.synechron.com");

			logger.info("Started Executing the Quarantine Galen Specs");
			layoutReport = Galen.checkLayout(driver, "specs/Quarantine.gspec", Arrays.asList("desktop"), null, null, null);
			logger.info("Exected the Quarantine Galen Specs Successfully......");

			test.getReport().layout(layoutReport, "Check Layout on Quarantine Site");

			// Exporting all test reports to html
			tests.add(test);


		} catch (IOException e) {
			logger.error("Sorry, Execption in the testCase_01!", e);
		}
		
	}
	
	public void generateResourceFile(String sourceFilePath,String destFileName)
	{
		ReadWriteExcelProperties readWriteExcelProperties = new ReadWriteExcelProperties();
		File f= new File(sourceFilePath);
		readWriteExcelProperties.readExcelFile(f.getAbsolutePath());

		readWriteExcelProperties.writeToPropertiesFile(destFileName);

	}
	
	@AfterClass
	public void shutdown()
	{

		driver.quit();
		logger.info(browserName+" shutdown successfully...");
	}

	@AfterSuite
	public void generateReport()
	{
		try {
			new HtmlReportBuilder().build(tests, "target/galen-html-reports");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

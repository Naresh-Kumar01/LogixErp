package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
<<<<<<< HEAD

=======
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
>>>>>>> 39f078d2457410d273f71c51854c8b423013057f
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

public class ExtentReporterManager implements ITestListener {
<<<<<<< HEAD
	private ExtentSparkReporter sparkReporter;
	private ExtentReports extent;
	private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private String repName;

	@Override
	public void onStart(ITestContext testContext) {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		repName = "Test-Report-" + timeStamp + ".html";
		sparkReporter = new ExtentSparkReporter(".\\reports\\" + repName);

		sparkReporter.config().setDocumentTitle("LogixERP Automation Report");
		sparkReporter.config().setReportName("LogixERP Functional Testing");
		sparkReporter.config().setTheme(Theme.DARK);

		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Application", "logixerp");
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		extent.setSystemInfo("Environment", "QA");

		String os = testContext.getCurrentXmlTest().getParameter("os");
		if (os != null) {
			extent.setSystemInfo("Operating System", os);
		}

		String browser = testContext.getCurrentXmlTest().getParameter("browser");
		if (browser != null) {
			extent.setSystemInfo("Browser", browser);
		}

		List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
		if (!includedGroups.isEmpty()) {
			extent.setSystemInfo("Groups", includedGroups.toString());
		}
	}

	@Override
	public void onTestStart(ITestResult result) {
		ExtentTest node = extent.createTest(result.getMethod().getMethodName());
		node.assignCategory(result.getMethod().getGroups());
		test.set(node);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		test.get().log(Status.PASS, result.getName() + " executed successfully");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		ExtentTest node = test.get() != null ? test.get() : extent.createTest(result.getMethod().getMethodName());
		node.assignCategory(result.getMethod().getGroups());
		node.log(Status.FAIL, result.getName() + " failed");
		if (result.getThrowable() != null) {
			node.log(Status.INFO, result.getThrowable().getMessage());
		}
		try {
			String imgPath = new BaseClass().captureScreen(result.getName());
			node.addScreenCaptureFromPath(imgPath);
		} catch (IOException e) {
			node.log(Status.WARNING, "Could not capture screenshot: " + e.getMessage());
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		ExtentTest node = test.get() != null ? test.get() : extent.createTest(result.getMethod().getMethodName());
		node.assignCategory(result.getMethod().getGroups());
		node.log(Status.SKIP, result.getName() + " skipped");
		if (result.getThrowable() != null) {
			node.log(Status.INFO, result.getThrowable().getMessage());
		}
	}

	@Override
	public void onFinish(ITestContext testContext) {
		if (extent != null) {
			extent.flush();
		}
		String pathOfExtentReport = System.getProperty("user.dir") + "\\reports\\" + repName;
		File extentReport = new File(pathOfExtentReport);
		try {
			if (Desktop.isDesktopSupported() && extentReport.exists()) {
				Desktop.getDesktop().browse(extentReport.toURI());
			}
		} catch (IOException ignored) {
			// Report generation should not fail the test run.
		}
	}
=======
	  private static ExtentReports extent;
	    private static ExtentSparkReporter sparkReporter;
	    private static ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();
	    private static Map<String, ExtentTest> testSuiteMap = new ConcurrentHashMap<>();
	    private static String reportName;

	    public class ExtentReportManager implements ITestListener {
	    	public ExtentSparkReporter sparkReporter;
	    	public ExtentReports extent;
	    	public ExtentTest test;

	    	String repName;

	    	public void onStart(ITestContext testContext) {
	    		
	    		/*SimpleDateFormat df=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	    		Date dt=new Date();
	    		String currentdatetimestamp=df.format(dt);
	    		*/
	    		
	    		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());// time stamp
	    		repName = "Test-Report-" + timeStamp + ".html";
	    		sparkReporter = new ExtentSparkReporter(".\\reports\\" + repName);// specify location of the report

	    		sparkReporter.config().setDocumentTitle("opencart Automation Report"); // Title of report
	    		sparkReporter.config().setReportName("opencart Functional Testing"); // name of the report
	    		sparkReporter.config().setTheme(Theme.DARK);
	    		
	    		extent = new ExtentReports();
	    		extent.attachReporter(sparkReporter);
	    		extent.setSystemInfo("Application", "logixerp");
	    		extent.setSystemInfo("Module", "Admin");
	    		extent.setSystemInfo("Sub Module", "Customers");
	    		extent.setSystemInfo("User Name", System.getProperty("user.name"));
	    		extent.setSystemInfo("Environemnt", "QA");
	    		
	    		String os = testContext.getCurrentXmlTest().getParameter("os");
	    		extent.setSystemInfo("Operating System", os);
	    		
	    		String browser = testContext.getCurrentXmlTest().getParameter("browser");
	    		extent.setSystemInfo("Browser", browser);
	    		
	    		List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
	    		if(!includedGroups.isEmpty()) {
	    		extent.setSystemInfo("Groups", includedGroups.toString());
	    		}
	    	}

	    	public void onTestSuccess(ITestResult result) {
	    	
	    		test = extent.createTest(result.getTestClass().getName());
	    		test.assignCategory(result.getMethod().getGroups()); // to display groups in report
	    		test.log(Status.PASS,result.getName()+" got successfully executed");
	    		
	    	}

	    	public void onTestFailure(ITestResult result) {
	    		test = extent.createTest(result.getTestClass().getName());
	    		test.assignCategory(result.getMethod().getGroups());
	    		
	    		test.log(Status.FAIL,result.getName()+" got failed");
	    		test.log(Status.INFO, result.getThrowable().getMessage());
	    		
	    		try {
	    			String imgPath = new BaseClass().captureScreen(result.getName());
	    			test.addScreenCaptureFromPath(imgPath);
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    	}

	    	public void onTestSkipped(ITestResult result) {
	    		test = extent.createTest(result.getTestClass().getName());
	    		test.assignCategory(result.getMethod().getGroups());
	    		test.log(Status.SKIP, result.getName()+" got skipped");
	    		test.log(Status.INFO, result.getThrowable().getMessage());
	    	}

	    	public void onFinish(ITestContext testContext) {
	    		
	    		extent.flush();
	    		
	    		String pathOfExtentReport = System.getProperty("user.dir")+"\\reports\\"+repName;
	    		File extentReport = new File(pathOfExtentReport);
	    		
	    		try {
	    			Desktop.getDesktop().browse(extentReport.toURI());
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}

	    		
	    		/*  try {
	    			  URL url = new  URL("file:///"+System.getProperty("user.dir")+"\\reports\\"+repName);
	    		  
	    		  // Create the email message 
	    		  ImageHtmlEmail email = new ImageHtmlEmail();
	    		  email.setDataSourceResolver(new DataSourceUrlResolver(url));
	    		  email.setHostName("smtp.googlemail.com"); 
	    		  email.setSmtpPort(465);
	    		  email.setAuthenticator(new DefaultAuthenticator("pavanoltraining@gmail.com","password")); 
	    		  email.setSSLOnConnect(true);
	    		  email.setFrom("pavanoltraining@gmail.com"); //Sender
	    		  email.setSubject("Test Results");
	    		  email.setMsg("Please find Attached Report....");
	    		  email.addTo("pavankumar.busyqa@gmail.com"); //Receiver 
	    		  email.attach(url, "extent report", "please check report..."); 
	    		  email.send(); // send the email 
	    		  }
	    		  catch(Exception e) 
	    		  { 
	    			  e.printStackTrace(); 
	    			  }
	    		 */ 
	    		 
	    	}


	    }
>>>>>>> 39f078d2457410d273f71c51854c8b423013057f
}

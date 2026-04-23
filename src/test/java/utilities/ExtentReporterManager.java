package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
}

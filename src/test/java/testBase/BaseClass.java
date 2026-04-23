package testBase;
import java.awt.AWTException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utilities.VideoRecorder;
public class BaseClass {
	public WebDriver driver;
	public Logger logger;  //Log4j
	public Properties p;
	private String testClassName;

	/**
	 * Build ChromeOptions with recommended args to avoid DevToolsActivePort and sandbox issues.
	 */
	private ChromeOptions getChromeOptions(String os) {
		ChromeOptions options = new ChromeOptions();
		// Basic recommended flags
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--disable-gpu");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-extensions");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--start-maximized");
		options.addArguments("--disable-infobars");

		// Respect user-specified headless property (default false)
		try {
			String headless = (p != null) ? p.getProperty("headless", "false") : "false";
			if (headless.equalsIgnoreCase("true")) {
				// modern headless flag
				options.addArguments("--headless=new");
				options.addArguments("--window-size=1920,1080");
			}
		} catch (Exception e) {
			// ignore if properties not available yet
		}

		// Allow overriding binary path via config property
		if (p != null) {
			String chromeBinary = p.getProperty("chromeBinary");
			if (chromeBinary != null && !chromeBinary.isBlank()) {
				options.setBinary(chromeBinary);
			}
		}

		return options;
	}
	
	@Parameters({"os","browser"})
		@BeforeClass(groups= {"Sanity","Regression","Master"})
		
		public void setup(@Optional("linux") String os, @Optional("chrome") String browser) throws IOException, AWTException
		{
			// Initialize logger first
			logger = LogManager.getLogger(this.getClass());
			
			// Get test class name for video recording
			testClassName = this.getClass().getSimpleName();
			
			// Start video recording
			try {
				String videoDir = VideoRecorder.getDefaultVideoDir();
				VideoRecorder.startRecording(testClassName, videoDir);
				logger.info("Video recording started for test class: " + testClassName);
			} catch (Exception e) {
				logger.warn("Failed to start video recording: " + e.getMessage());
				logger.warn("Tests will continue without video recording.");
			}
			
			// Suppress Selenium java.util.logging messages
			java.util.logging.Logger seleniumLogger = java.util.logging.Logger.getLogger("org.openqa.selenium");
			seleniumLogger.setLevel(java.util.logging.Level.SEVERE);
			java.util.logging.Logger devtoolsLogger = java.util.logging.Logger.getLogger("org.openqa.selenium.devtools");
			devtoolsLogger.setLevel(java.util.logging.Level.SEVERE);
			java.util.logging.Logger tracingLogger = java.util.logging.Logger.getLogger("org.openqa.selenium.remote.tracing");
			tracingLogger.setLevel(java.util.logging.Level.SEVERE);
			
			//Loading config.properties file from classpath
			logger=LogManager.getLogger(this.getClass());  //lOG4J2
			p=new Properties();
			try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
				if (inputStream != null) {
					p.load(inputStream);
					logger.info("Loaded config.properties from classpath");
				} else {
					// Fallback to relative path if classpath loading fails
					logger.warn("config.properties not found in classpath, trying relative path");
					try (FileReader file = new FileReader("./src//test//resources//config.properties")) {
			p.load(file);
						logger.info("Loaded config.properties from relative path");
					} catch (IOException e) {
						logger.error("Failed to load config.properties from relative path: " + e.getMessage());
						throw new RuntimeException("Could not load config.properties file", e);
					}
				}
			} catch (IOException e) {
				logger.error("Failed to load config.properties from classpath: " + e.getMessage());
				throw new RuntimeException("Could not load config.properties file", e);
			}
					
			if(p.getProperty("execution_env").equalsIgnoreCase("remote"))
			{
				org.openqa.selenium.Capabilities capabilities = null;
				
				// browser - using modern Selenium 4.x approach
				switch (browser.toLowerCase()) {
									case "chrome":
										ChromeOptions chromeOptions = getChromeOptions(os);
										// set platform for remote grid if needed
										if(os.equalsIgnoreCase("linux")) {
											chromeOptions.setPlatformName("linux");
										} else if(os.equalsIgnoreCase("mac")) {
											chromeOptions.setPlatformName("mac");
										} else {
											chromeOptions.setPlatformName("windows");
										}
										capabilities = chromeOptions;
				        break;
				    case "edge":
				        EdgeOptions edgeOptions = new EdgeOptions();
				        if(os.equalsIgnoreCase("linux")) {
				            edgeOptions.setPlatformName("linux");
				        } else if(os.equalsIgnoreCase("mac")) {
				            edgeOptions.setPlatformName("mac");
				        } else {
				            edgeOptions.setPlatformName("windows");
				        }
				        capabilities = edgeOptions;
				        break;
				    case "firefox":
				        FirefoxOptions firefoxOptions = new FirefoxOptions();
				        if(os.equalsIgnoreCase("linux")) {
				            firefoxOptions.setPlatformName("linux");
				        } else if(os.equalsIgnoreCase("mac")) {
				            firefoxOptions.setPlatformName("mac");
				        } else {
				            firefoxOptions.setPlatformName("windows");
				        }
				        capabilities = firefoxOptions;
				        break;
				    default:
				        logger.error("No matching browser: " + browser);
				        return;
				}
				// Initialize RemoteWebDriver with Selenium Grid hub URL
				String gridHubURL = p.getProperty("gridHubURL", "http://localhost:4444/wd/hub");
				boolean fallbackToLocal = Boolean.parseBoolean(p.getProperty("fallbackToLocal", "true"));
				
				try {
					logger.info("Attempting to connect to Selenium Grid at: " + gridHubURL);
					driver = new RemoteWebDriver(new URL(gridHubURL), capabilities);
					logger.info("Successfully connected to Selenium Grid");
				} catch (Exception e) {
					logger.error("Failed to connect to Selenium Grid at " + gridHubURL + ": " + e.getMessage());
						if (fallbackToLocal) {
						logger.warn("Falling back to local execution mode");
						// Fallback to local execution
						switch(browser.toLowerCase())
						{
						case "chrome" : driver=new ChromeDriver(getChromeOptions(os));
						break;
						
						case "edge" : driver=new EdgeDriver();
						break;
						case "firefox": driver=new FirefoxDriver();
						break;
						default :
							logger.error("Invalid browser name: " + browser);
							return;
						}
					} else {
						logger.error("Remote execution failed and fallback is disabled. Please start Selenium Grid or set fallbackToLocal=true");
						throw new RuntimeException("Could not connect to Selenium Grid. Please ensure Grid is running at: " + gridHubURL, e);
					}
				}
			}
			
					
			if(p.getProperty("execution_env").equalsIgnoreCase("local"))
			{
				
				
				switch(browser.toLowerCase())
				{
				case "chrome" : driver=new ChromeDriver(getChromeOptions(os));
				break;
				
				case "edge" : driver=new EdgeDriver();
				break;
				case "firefox": driver=new FirefoxDriver();
				break;
				default : System.out.println("Invalid browser name..");
				return;
				}
			}
			
				
			// Navigate to application URL
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			driver.get(p.getProperty("appURL")); // reading url from properties file.
			driver.manage().window().maximize();
		}
		
		@AfterClass(groups= {"Sanity","Regression","Master"})
		public void tearDown()
		{
			// Stop video recording
			try {
				String videoPath = VideoRecorder.stopRecording();
				if (videoPath != null) {
					logger.info("Video recording saved: " + videoPath);
				}
			} catch (Exception e) {
				logger.warn("Error stopping video recording: " + e.getMessage());
			}
			
			if (driver != null) {
				try {
					driver.quit();
					logger.info("Test class completed. Browser session closed.");
				} catch (Exception e) {
					logger.warn("Error while closing browser session in AfterClass: " + e.getMessage());
				} finally {
					driver = null;
				}
			}
		}
		
		@AfterSuite(groups= {"Sanity","Regression","Master"})
		public void closeDriver()
		{
			// Ensure video recording is stopped if still running
			try {
				if (VideoRecorder.isRecording()) {
					String videoPath = VideoRecorder.stopRecording();
					if (videoPath != null) {
						logger.info("Final video recording saved: " + videoPath);
					}
				}
			} catch (Exception e) {
				logger.warn("Error stopping video recording in AfterSuite: " + e.getMessage());
			}
			
			// Close driver only after all tests in suite are complete
			if (driver != null) {
				try {
					logger.info("All tests completed. Closing browser session.");
					driver.quit();
					driver = null;
					logger.info("Browser session closed successfully.");
				} catch (Exception e) {
					logger.warn("Error closing driver: " + e.getMessage());
				}
			}
		}
		
		public String randomeString()
		{
			String generatedstring=RandomStringUtils.randomAlphabetic(5);
			return generatedstring;
		}
		
		public String randomeNumber()
		{
			String generatednumber=RandomStringUtils.randomNumeric(10);
			return generatednumber;
		}
		
		public String randomeAlphaNumberic()
		{
			String generatedstring=RandomStringUtils.randomAlphabetic(3);
			String generatednumber=RandomStringUtils.randomNumeric(3);
			return (generatedstring+"@"+generatednumber);
		}
		
		public String captureScreen(String tname) throws IOException {
			String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
					
			TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
			File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
			
			String targetFilePath=System.getProperty("user.dir")+"\\screenshots\\" + tname + "_" + timeStamp + ".png";
			File targetFile=new File(targetFilePath);
			
			sourceFile.renameTo(targetFile);
				
			return targetFilePath;
		}
		
}


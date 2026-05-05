package utilities;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * VideoRecorder utility class for recording test execution videos
 * Uses Java Robot class to capture screenshots and save them as images
 * These images can later be converted to video using FFmpeg or other tools
 * 
 * Note: For actual video files, you can use FFmpeg to convert the images:
 * ffmpeg -framerate 2 -pattern_type glob -i '*.png' -c:v libx264 -pix_fmt yuv420p output.mp4
 */
public class VideoRecorder {
	
	private static Logger logger = LogManager.getLogger(VideoRecorder.class);
	private static Robot robot;
	private static boolean isRecording = false;
	private static Thread recordingThread;
	private static String videoDir;
	private static String testName;
	private static List<String> screenshotPaths = new ArrayList<>();
	private static final int CAPTURE_INTERVAL_MS = 500; // Capture every 500ms (2 fps)
	
	/**
	 * Start recording video by capturing screenshots periodically
	 * @param testName Name of the test case
	 * @param videoDir Directory to save video files
	 * @throws IOException
	 * @throws AWTException
	 */
	public static void startRecording(String testName, String videoDir) throws IOException, AWTException {
		if (isRecording) {
			logger.warn("Recording is already in progress. Stopping previous recording before starting new one.");
			stopRecording();
		}
		
		VideoRecorder.testName = testName;
		VideoRecorder.videoDir = videoDir;
		screenshotPaths.clear();
		
		// Create video directory if it doesn't exist
		File videoDirectory = new File(videoDir);
		if (!videoDirectory.exists()) {
			videoDirectory.mkdirs();
			logger.info("Created video directory: " + videoDir);
		}
		
		// Create subdirectory for this test
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String testDir = videoDir + File.separator + testName + "_" + timeStamp;
		File testDirectory = new File(testDir);
		testDirectory.mkdirs();
		VideoRecorder.videoDir = testDir;
		
		try {
			// Initialize Robot for screen capture
			robot = new Robot();
			isRecording = true;
			
			// Start recording thread
			recordingThread = new Thread(() -> {
				int frameCount = 0;
				while (isRecording) {
					try {
						// Capture screen
						Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
						Rectangle screenRect = new Rectangle(screenSize);
						BufferedImage screenCapture = robot.createScreenCapture(screenRect);
						
						// Save screenshot
						String frameFileName = String.format("frame_%05d.png", frameCount++);
						File frameFile = new File(VideoRecorder.videoDir, frameFileName);
						ImageIO.write(screenCapture, "png", frameFile);
						screenshotPaths.add(frameFile.getAbsolutePath());
						
						// Wait before next capture
						Thread.sleep(CAPTURE_INTERVAL_MS);
					} catch (Exception e) {
						if (isRecording) {
							logger.error("Error during screen capture: " + e.getMessage());
						}
					}
				}
			});
			
			recordingThread.setDaemon(true);
			recordingThread.start();
			
			logger.info("Video recording started for test: " + testName);
			logger.info("Screenshots will be saved to: " + VideoRecorder.videoDir);
			logger.info("To create video, use FFmpeg: ffmpeg -framerate 2 -pattern_type glob -i '" + 
			           VideoRecorder.videoDir + File.separator + "*.png' -c:v libx264 -pix_fmt yuv420p " +
			           VideoRecorder.videoDir + File.separator + testName + ".mp4");
		} catch (Exception e) {
			logger.error("Failed to start video recording: " + e.getMessage());
			isRecording = false;
			throw e;
		}
	}
	
	/**
	 * Stop recording video
	 * @return Path to the directory containing screenshots
	 */
	public static String stopRecording() {
		if (!isRecording) {
			logger.warn("No active recording to stop.");
			return null;
		}
		
		try {
			isRecording = false;
			
			// Wait for recording thread to finish
			if (recordingThread != null && recordingThread.isAlive()) {
				try {
					recordingThread.join(2000); // Wait up to 2 seconds
				} catch (InterruptedException e) {
					logger.warn("Interrupted while waiting for recording thread to finish");
				}
			}
			
			logger.info("Video recording stopped. Captured " + screenshotPaths.size() + " frames.");
			logger.info("Screenshots saved in: " + videoDir);
			
			// Create a simple HTML viewer for the screenshots
			createHTMLViewer();
			
			String result = videoDir;
			videoDir = null;
			testName = null;
			robot = null;
			recordingThread = null;
			
			return result;
		} catch (Exception e) {
			logger.error("Error stopping video recording: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Create an HTML file to view the screenshots as a slideshow
	 */
	private static void createHTMLViewer() {
		try {
			File htmlFile = new File(videoDir, "viewer.html");
			StringBuilder html = new StringBuilder();
			html.append("<!DOCTYPE html>\n");
			html.append("<html><head><title>Test Execution Video - ").append(testName).append("</title>\n");
			html.append("<style>body { font-family: Arial; text-align: center; background: #f0f0f0; } ");
			html.append("img { max-width: 100%; height: auto; border: 2px solid #333; margin: 10px; } ");
			html.append("h1 { color: #333; } </style>\n");
			html.append("<script>var images = [");
			
			// Add all screenshot paths
			for (int i = 0; i < screenshotPaths.size(); i++) {
				File imgFile = new File(screenshotPaths.get(i));
				if (i > 0) html.append(",");
				html.append("'").append(imgFile.getName()).append("'");
			}
			
			html.append("]; var current = 0; ");
			html.append("function showImage() { document.getElementById('img').src = images[current]; ");
			html.append("document.getElementById('info').textContent = 'Frame ' + (current + 1) + ' of ' + images.length; } ");
			html.append("function next() { current = (current + 1) % images.length; showImage(); } ");
			html.append("function prev() { current = (current - 1 + images.length) % images.length; showImage(); } ");
			html.append("window.onload = function() { showImage(); setInterval(next, 500); }; ");
			html.append("</script></head><body>\n");
			html.append("<h1>Test Execution: ").append(testName).append("</h1>\n");
			html.append("<div><button onclick='prev()'>Previous</button> ");
			html.append("<span id='info'></span> ");
			html.append("<button onclick='next()'>Next</button></div>\n");
			html.append("<img id='img' src='' alt='Screenshot'><br>\n");
			html.append("<p>Auto-playing slideshow (2 fps). Use buttons to navigate manually.</p>\n");
			html.append("</body></html>");
			
			java.nio.file.Files.write(htmlFile.toPath(), html.toString().getBytes());
			logger.info("Created HTML viewer: " + htmlFile.getAbsolutePath());
		} catch (Exception e) {
			logger.warn("Failed to create HTML viewer: " + e.getMessage());
		}
	}
	
	/**
	 * Check if recording is in progress
	 * @return true if recording, false otherwise
	 */
	public static boolean isRecording() {
		return isRecording;
	}
	
	/**
	 * Get default video directory path
	 * @return Path to videos directory
	 */
	public static String getDefaultVideoDir() {
		return System.getProperty("user.dir") + File.separator + "videos";
	}
	
	/**
	 * Get list of captured screenshot paths
	 * @return List of screenshot file paths
	 */
	public static List<String> getScreenshotPaths() {
		return new ArrayList<>(screenshotPaths);
	}
}

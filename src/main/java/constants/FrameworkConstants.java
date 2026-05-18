package constants;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class FrameworkConstants {

    private FrameworkConstants() {
    }

    public static final String PROJECT_ROOT = System.getProperty("user.dir");
    public static final Path REPORTS_DIR = Paths.get(PROJECT_ROOT, "reports");
    public static final Path SCREENSHOTS_DIR = Paths.get(PROJECT_ROOT, "screenshots");
    public static final Path VIDEOS_DIR = Paths.get(PROJECT_ROOT, "videos");
    public static final Path LOGS_DIR = Paths.get(PROJECT_ROOT, "logs");
    public static final Path TEST_DATA_DIR = Paths.get(PROJECT_ROOT, "src", "test", "resources", "testdata");
    public static final Path SQL_DIR = Paths.get(PROJECT_ROOT, "src", "test", "resources", "sql");

    public static final String CONFIG_FILE = "config.properties";
    public static final String DEFAULT_SUITE = "testng.xml";
    public static final int DEFAULT_RETRY_COUNT = 2;
    public static final int DEFAULT_EXPLICIT_WAIT = 20;
    public static final int DEFAULT_IMPLICIT_WAIT = 10;
    public static final int DEFAULT_PAGE_LOAD_TIMEOUT = 60;
}

package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    
    private Properties properties;
    
    public ConfigReader() {
        loadProperties();
    }
    
    /**
     * Load properties from config.properties file
     */
    private void loadProperties() {
        properties = new Properties();
        try {
            // Try to load from test resources first
            FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/config.properties");
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            System.err.println("Error loading config.properties file: " + e.getMessage());
            // Initialize with default properties if file not found
            setDefaultProperties();
        }
    }
    
    /**
     * Set default properties if config file is not found
     */
    private void setDefaultProperties() {
        properties.setProperty("execution_env", "local");
        properties.setProperty("browser", "chrome");
        properties.setProperty("appURL", "https://tutorialsninja.com/demo/");
        properties.setProperty("email", "test@example.com");
        properties.setProperty("password", "test123");
    }
    
    /**
     * Get the Properties object
     * @return Properties object containing configuration
     */
    public Properties getProperties() {
        return properties;
    }
    
    /**
     * Get property value by key
     * @param key Property key
     * @return Property value
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get property value by key with default value
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}

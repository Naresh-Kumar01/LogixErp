package config;

import constants.FrameworkConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();
    private static volatile boolean loaded;

    private ConfigReader() {
    }

    public static synchronized void load() {
        if (loaded) {
            return;
        }
        String env = System.getProperty("env", System.getenv().getOrDefault("ENV", "qa"));
        try (InputStream base = ConfigReader.class.getClassLoader()
                .getResourceAsStream(FrameworkConstants.CONFIG_FILE)) {
            if (base != null) {
                PROPERTIES.load(base);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load " + FrameworkConstants.CONFIG_FILE, e);
        }
        try (InputStream envFile = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config-" + env + ".properties")) {
            if (envFile != null) {
                PROPERTIES.load(envFile);
            }
        } catch (IOException ignored) {
            // optional env overlay
        }
        System.getProperties().forEach((k, v) -> {
            if (k != null && PROPERTIES.containsKey(k.toString())) {
                PROPERTIES.setProperty(k.toString(), v.toString());
            }
        });
        loaded = true;
    }

    public static String get(String key) {
        load();
        return PROPERTIES.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        load();
        return PROPERTIES.getProperty(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return Integer.parseInt(value.trim());
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.trim());
    }

    public static Properties getAll() {
        load();
        Properties copy = new Properties();
        copy.putAll(PROPERTIES);
        return copy;
    }
}

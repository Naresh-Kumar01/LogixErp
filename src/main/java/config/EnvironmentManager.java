package config;

import enums.EnvironmentType;

public final class EnvironmentManager {

    private EnvironmentManager() {
    }

    public static EnvironmentType currentEnvironment() {
        String env = System.getProperty("env",
                System.getenv().getOrDefault("ENV", ConfigReader.get("environment", "qa")));
        return EnvironmentType.from(env);
    }

    public static String getAppUrl() {
        EnvironmentType env = currentEnvironment();
        String key = "appURL." + env.getValue();
        String url = ConfigReader.get(key);
        if (url == null || url.isBlank()) {
            url = ConfigReader.get("appURL");
        }
        return url;
    }

    public static String getApiBaseUrl() {
        EnvironmentType env = currentEnvironment();
        String key = "api.baseURL." + env.getValue();
        String url = ConfigReader.get(key);
        if (url == null || url.isBlank()) {
            url = ConfigReader.get("api.baseURL", "https://beta.logixerp.com/api");
        }
        return url;
    }

    public static String getDbUrl() {
        EnvironmentType env = currentEnvironment();
        String key = "db.url." + env.getValue();
        String url = ConfigReader.get(key);
        if (url == null || url.isBlank()) {
            url = ConfigReader.get("db.url");
        }
        return url;
    }

    public static boolean isHeadless() {
        return ConfigReader.getBoolean("headless", false);
    }

    public static boolean isRemoteExecution() {
        return "remote".equalsIgnoreCase(ConfigReader.get("execution_env", "local"))
                || "cloud".equalsIgnoreCase(ConfigReader.get("execution_env", "local"));
    }
}

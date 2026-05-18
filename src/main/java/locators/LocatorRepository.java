package locators;

import config.ConfigReader;
import org.openqa.selenium.By;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central locator store with simple auto-heal fallback aliases.
 */
public final class LocatorRepository {

    private static final Map<String, By[]> LOCATORS = new ConcurrentHashMap<>();

    static {
        register("login.username",
                By.name("username"),
                By.xpath("//input[contains(@name,'user')]"));
        register("login.password",
                By.name("password"),
                By.xpath("//input[@type='password']"));
        register("login.submit",
                By.xpath("//button[normalize-space()='Sign in']"),
                By.xpath("//button[contains(.,'Sign')]"));
        register("wms.menu",
                By.xpath("//a[contains(.,'WMS') or contains(@href,'wms')]"));
        register("product.sku",
                By.xpath("//input[@name='sku' or contains(@id,'sku')]"));
        register("product.save",
                By.xpath("//button[contains(.,'Save') or contains(.,'Create')]"));
        register("picking.fifo.rule",
                By.xpath("//select[@name='pickingRule']//option[contains(.,'FIFO')]"));
    }

    private LocatorRepository() {
    }

    public static void register(String key, By... candidates) {
        LOCATORS.put(key, candidates);
    }

    public static By resolve(String key) {
        By[] candidates = LOCATORS.get(key);
        if (candidates == null || candidates.length == 0) {
            throw new IllegalArgumentException("No locator registered for: " + key);
        }
        if (ConfigReader.getBoolean("locator.autoheal", true) && candidates.length > 1) {
            return candidates[0]; // primary; healing handled in page actions via fallbacks
        }
        return candidates[0];
    }

    public static By[] all(String key) {
        return LOCATORS.get(key);
    }
}

package pageobjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for Product Creation form.
 * Covers toggles: Mfg Date, Expiry Date, Batch, Serial, ASN,
 * Skip Auto Putaway, Skip Auto Pickup, and picking strategy (FMFO/FEFO/FIFO).
 */
public class ProductCreationPage extends BasePage {

    private final WebDriverWait wait;

    public ProductCreationPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ─── Navigation ──────────────────────────────────────────────────────────

    /** Navigate to the Products / Items section via sidebar or top nav. */
    public void navigateToProducts() {
        By locator = By.xpath(
            "//a[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'product')" +
            " or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'item')" +
            " or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'inventory')]");
        List<WebElement> links = driver.findElements(locator);
        for (WebElement link : links) {
            try {
                if (link.isDisplayed()) {
                    wait.until(ExpectedConditions.elementToBeClickable(link)).click();
                    return;
                }
            } catch (Exception ignored) { }
        }
    }

    /** Click the Add / New / Create product button. */
    public void clickAddProduct() {
        By btn = By.xpath(
            "//button[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add')" +
            " or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'new')" +
            " or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'create')]" +
            " | //a[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add')" +
            " or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'new')]");
        wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
    }

    // ─── Basic Fields ─────────────────────────────────────────────────────────

    /** Enter the product name. */
    public void enterProductName(String name) {
        fillField("name", name);
    }

    /** Enter the SKU / product code. */
    public void enterSku(String sku) {
        fillField("sku", sku);
    }

    // ─── Toggle Helpers ───────────────────────────────────────────────────────

    /**
     * Enable a checkbox/toggle by matching its nearby label text (case-insensitive).
     * Tries checkbox inputs, then toggle switches.
     */
    public void enableToggle(String labelKeyword) {
        setToggle(labelKeyword, true);
    }

    /** Disable a checkbox/toggle by matching its nearby label text. */
    public void disableToggle(String labelKeyword) {
        setToggle(labelKeyword, false);
    }

    private void setToggle(String keyword, boolean enable) {
        String kw = keyword.toLowerCase();
        // Strategy 1: label → following checkbox/toggle input
        By byLabel = By.xpath(
            "//label[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + kw + "')]" +
            "/following::input[@type='checkbox' or @type='radio'][1]");
        // Strategy 2: input with matching name/id/placeholder
        By byAttr = By.xpath(
            "//input[@type='checkbox' or @type='radio'][" +
            "contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + kw + "')" +
            " or contains(translate(@id,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + kw + "')]");
        // Strategy 3: span/div toggle with matching text
        By bySpan = By.xpath(
            "//*[self::span or self::div][contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + kw + "')]" +
            "/preceding::input[@type='checkbox'][1]" +
            " | //*[self::span or self::div][contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + kw + "')]" +
            "/following::input[@type='checkbox'][1]");

        WebElement el = findFirstVisible(byLabel, byAttr, bySpan);
        if (el != null) {
            boolean checked = el.isSelected();
            if (enable && !checked) clickElement(el);
            else if (!enable && checked) clickElement(el);
        }
    }

    // ─── Picking Strategy ─────────────────────────────────────────────────────

    /**
     * Select a picking strategy (FMFO / FEFO / FIFO) from a dropdown or radio group.
     * @param strategy e.g. "FMFO", "FEFO", "FIFO"
     */
    public void selectPickingStrategy(String strategy) {
        String kw = strategy.toLowerCase();

        // Try dropdown (select element) labelled "picking" or "strategy"
        By selectLocator = By.xpath(
            "//select[contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pick')" +
            " or contains(translate(@id,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pick')" +
            " or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'strateg')" +
            " or contains(translate(@id,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'strateg')]");
        List<WebElement> selects = driver.findElements(selectLocator);
        for (WebElement sel : selects) {
            try {
                if (sel.isDisplayed()) {
                    new Select(sel).selectByVisibleText(strategy.toUpperCase());
                    return;
                }
            } catch (Exception ignored) { }
        }

        // Try radio button or option with matching text
        By radioLocator = By.xpath(
            "//input[@type='radio'][contains(translate(@value,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + kw + "')]" +
            " | //label[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + kw + "')]" +
            "/preceding::input[@type='radio'][1]" +
            " | //label[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + kw + "')]" +
            "/following::input[@type='radio'][1]");
        WebElement radio = findFirstVisible(radioLocator);
        if (radio != null) {
            clickElement(radio);
            return;
        }

        // Try clickable option/li/div with matching text
        By optionLocator = By.xpath(
            "//*[self::option or self::li or self::div or self::span]" +
            "[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + kw + "')]");
        WebElement option = findFirstVisible(optionLocator);
        if (option != null) clickElement(option);
    }

    // ─── Save ─────────────────────────────────────────────────────────────────

    /** Click the Save / Submit / Create button. */
    public void clickSave() {
        By saveBtn = By.xpath(
            "//button[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'save')" +
            " or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'submit')" +
            " or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'create')]" +
            " | //input[@type='submit']");
        wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
    }

    // ─── Verification ─────────────────────────────────────────────────────────

    /**
     * Returns true if a success message is shown OR the product name appears in the page.
     */
    public boolean isProductCreated(String productName) {
        // Check success alert/toast
        By successAlert = By.xpath(
            "//*[contains(@class,'alert') or contains(@class,'toast') or contains(@class,'success') or contains(@class,'notification')]" +
            "[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'success')" +
            " or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'created')" +
            " or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'saved')]");
        try {
            WebElement alert = new WebDriverWait(driver, Duration.ofSeconds(8))
                .until(ExpectedConditions.visibilityOfElementLocated(successAlert));
            if (alert != null) return true;
        } catch (Exception ignored) { }

        // Fallback: product name visible on page
        By nameOnPage = By.xpath(
            "//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" +
            productName.toLowerCase() + "')]");
        return !driver.findElements(nameOnPage).isEmpty();
    }

    // ─── Private Utilities ────────────────────────────────────────────────────

    private void fillField(String keyword, String value) {
        String kw = keyword.toLowerCase();
        By[] strategies = {
            By.xpath("//label[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + kw + "')]/following::input[1]"),
            By.xpath("//input[contains(translate(@placeholder,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + kw + "')" +
                     " or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + kw + "')" +
                     " or contains(translate(@id,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + kw + "')]")
        };
        for (By by : strategies) {
            try {
                WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                el.clear();
                el.sendKeys(value);
                return;
            } catch (Exception ignored) { }
        }
    }

    @SafeVarargs
    private WebElement findFirstVisible(By... locators) {
        for (By by : locators) {
            List<WebElement> elements = driver.findElements(by);
            for (WebElement el : elements) {
                try {
                    if (el.isDisplayed()) return el;
                } catch (Exception ignored) { }
            }
        }
        return null;
    }

    private void clickElement(WebElement el) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(el)).click();
        } catch (Exception e) {
            // JS fallback for hidden-but-present toggles
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }
}

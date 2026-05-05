package pageobjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage extends BasePage {

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    // Navigate to Products / Items / Inventory menu
    public void navigateToProducts() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        // Try several common menu labels
        By menuLocator = By.xpath("//a[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'product') or contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'products') or contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'item') or contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'inventory')]");
        List<WebElement> menus = driver.findElements(menuLocator);
        if (!menus.isEmpty()) {
            // click the first visible one
            for (WebElement m : menus) {
                try {
                    if (m.isDisplayed()) {
                        wait.until(ExpectedConditions.elementToBeClickable(m)).click();
                        return;
                    }
                } catch (Exception e) {
                    // try next
                }
            }
        }
        // If direct menu not found, try sidebar links
        try {
            By alt = By.xpath("//li//a[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'product') or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'item')]");
            WebElement altEl = wait.until(ExpectedConditions.elementToBeClickable(alt));
            altEl.click();
        } catch (Exception e) {
            // swallow - caller will attempt to continue
        }
    }

    // Click Add / New / Create product button
    public void clickAddProduct() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By addBtn = By.xpath("//button[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add') or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'new') or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'create')]");
        try {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(addBtn));
            btn.click();
            return;
        } catch (Exception e) {
            // fallback: try links
            try {
                By addLink = By.xpath("//a[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add') or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'new')]");
                WebElement l = wait.until(ExpectedConditions.elementToBeClickable(addLink));
                l.click();
            } catch (Exception ex) {
                // no-op
            }
        }
    }

    // Create product using flexible locators
    public void createProduct(String name, String sku, String price) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Try to fill product name
        fillFieldByLabelOrPlaceholderOrName("name", name, wait);

        // Try SKU / code field
        fillFieldByLabelOrPlaceholderOrName("sku", sku, wait);

        // Try price field
        fillFieldByLabelOrPlaceholderOrName("price", price, wait);

        // Click Save/Create
        By saveBtn = By.xpath("//button[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'save') or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'create') or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'submit')]");
        try {
            WebElement s = wait.until(ExpectedConditions.elementToBeClickable(saveBtn));
            s.click();
        } catch (Exception e) {
            // try alternative
            try {
                By alt = By.xpath("//input[@type='submit' or @value='Save' or @value='Create']");
                WebElement s2 = wait.until(ExpectedConditions.elementToBeClickable(alt));
                s2.click();
            } catch (Exception ex) {
                // let caller handle
            }
        }
    }

    private void fillFieldByLabelOrPlaceholderOrName(String keyWord, String value, WebDriverWait wait) {
        try {
            // label-based
            By labelBased = By.xpath("//label[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + keyWord + "')]/following::input[1]");
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(labelBased));
            el.clear();
            el.sendKeys(value);
            return;
        } catch (Exception e) {
            // try placeholder/name/id
        }

        try {
            By placeholder = By.xpath("//input[contains(translate(@placeholder,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + keyWord + "') or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + keyWord + "') or contains(translate(@id,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + keyWord + "')]");
            WebElement el2 = wait.until(ExpectedConditions.visibilityOfElementLocated(placeholder));
            el2.clear();
            el2.sendKeys(value);
            return;
        } catch (Exception e) {
            // try textarea
        }

        try {
            By textarea = By.xpath("//textarea[contains(translate(@placeholder,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + keyWord + "') or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + keyWord + "')]");
            WebElement el3 = wait.until(ExpectedConditions.visibilityOfElementLocated(textarea));
            el3.clear();
            el3.sendKeys(value);
            return;
        } catch (Exception e) {
            // final fallback: try any visible input
        }

        try {
            List<WebElement> inputs = driver.findElements(By.xpath("//input[not(@type='hidden') and string-length(normalize-space(@value))=0]"));
            for (WebElement i : inputs) {
                try {
                    if (i.isDisplayed()) {
                        i.clear();
                        i.sendKeys(value);
                        return;
                    }
                } catch (Exception ex) {
                    // continue
                }
            }
        } catch (Exception e) {
            // give up
        }
    }

    // Verify product created by checking success alert or searching for product name in table
    public boolean isProductCreated(String name) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            // Check for common success alerts
            By alert = By.xpath("//div[contains(@class,'alert') and (contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'success') or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'created') or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'saved'))]");
            WebElement a = wait.until(ExpectedConditions.visibilityOfElementLocated(alert));
            if (a != null && a.getText().toLowerCase().contains("success"))
                return true;
        } catch (Exception e) {
            // ignore
        }

        // Search for product name in table/list
        try {
            By cell = By.xpath("//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), '" + name.toLowerCase() + "')]");
            List<WebElement> matches = driver.findElements(cell);
            return !matches.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

}

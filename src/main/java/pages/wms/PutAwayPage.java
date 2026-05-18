package pages.wms;

import helpers.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.common.BasePage;

public class PutAwayPage extends BasePage {

    public PutAwayPage(WebDriver driver) {
        super(driver);
    }

    public PutAwayPage skipAutoSuggestedLocation() {
        try {
            WaitHelper.waitForClickable(By.xpath("//input[@type='checkbox' and contains(@name,'skipAutoPutAway')]")).click();
        } catch (Exception ignored) {
        }
        return this;
    }

    public PutAwayPage confirmPutAway(String sku, String location) {
        setField("sku", sku);
        setField("location", location);
        WaitHelper.waitForClickable(By.xpath("//button[contains(.,'Confirm') or contains(.,'Put Away')]")).click();
        return this;
    }

    private void setField(String name, String value) {
        By locator = By.xpath("//input[contains(@name,'" + name + "')]");
        WaitHelper.waitForVisible(locator).clear();
        WaitHelper.waitForVisible(locator).sendKeys(value);
    }
}

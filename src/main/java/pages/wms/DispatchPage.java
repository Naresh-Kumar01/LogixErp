package pages.wms;

import helpers.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.common.BasePage;

public class DispatchPage extends BasePage {

    public DispatchPage(WebDriver driver) {
        super(driver);
    }

    public DispatchPage dispatchShipment(String shipmentRef) {
        setField("shipmentRef", shipmentRef);
        WaitHelper.waitForClickable(By.xpath("//button[contains(.,'Dispatch') or contains(.,'Confirm Dispatch')]")).click();
        WaitHelper.waitForPageLoad();
        return this;
    }

    public boolean isDispatchConfirmed() {
        try {
            return WaitHelper.waitForVisible(By.xpath("//*[contains(.,'Dispatched') or contains(.,'success')]")).isDisplayed();
        } catch (Exception ex) {
            return false;
        }
    }

    private void setField(String name, String value) {
        By locator = By.xpath("//input[contains(@name,'" + name + "')]");
        WaitHelper.waitForVisible(locator).clear();
        WaitHelper.waitForVisible(locator).sendKeys(value);
    }
}

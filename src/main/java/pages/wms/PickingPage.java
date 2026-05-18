package pages.wms;

import helpers.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.common.BasePage;

public class PickingPage extends BasePage {

    public PickingPage(WebDriver driver) {
        super(driver);
    }

    public PickingPage selectPickingRule(String rule) {
        WaitHelper.waitForClickable(By.xpath("//select[contains(@name,'picking') or contains(@id,'picking')]")).click();
        WaitHelper.waitForClickable(By.xpath("//option[contains(.,'" + rule + "')]")).click();
        return this;
    }

    public PickingPage applyFifo() {
        return selectPickingRule("FIFO");
    }

    public PickingPage applyFefo() {
        return selectPickingRule("FEFO");
    }

    public PickingPage applyAsnBased() {
        return selectPickingRule("ASN");
    }

    public PickingPage applyFmfo() {
        return selectPickingRule("FMFO");
    }

    public PickingPage skipAutoSuggestedPickupLocation() {
        try {
            WaitHelper.waitForClickable(By.xpath("//input[contains(@name,'skipAutoPickup')]")).click();
        } catch (Exception ignored) {
        }
        return this;
    }

    public PickingPage generatePicklist(String orderRef) {
        setField("orderRef", orderRef);
        WaitHelper.waitForClickable(By.xpath("//button[contains(.,'Generate') or contains(.,'Create Picklist')]")).click();
        WaitHelper.waitForPageLoad();
        return this;
    }

    public PickingPage pickSerial(String serialNumber) {
        setField("serial", serialNumber);
        WaitHelper.waitForClickable(By.xpath("//button[contains(.,'Scan') or contains(.,'Add Serial')]")).click();
        return this;
    }

    public PickingPage pickBatch(String batchNumber, int qty) {
        setField("batch", batchNumber);
        setField("qty", String.valueOf(qty));
        WaitHelper.waitForClickable(By.xpath("//button[contains(.,'Pick')]")).click();
        return this;
    }

    public boolean isPicklistGenerated() {
        try {
            return WaitHelper.waitForVisible(By.xpath("//*[contains(.,'Picklist') and contains(.,'created')]")).isDisplayed();
        } catch (Exception ex) {
            return driver().getPageSource().toLowerCase().contains("picklist");
        }
    }

    private void setField(String name, String value) {
        By locator = By.xpath("//input[contains(@name,'" + name + "')]");
        WaitHelper.waitForVisible(locator).clear();
        WaitHelper.waitForVisible(locator).sendKeys(value);
    }
}

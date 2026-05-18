package pages.wms;

import helpers.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.common.BasePage;

public class WmsNavigationPage extends BasePage {

    public WmsNavigationPage(WebDriver driver) {
        super(driver);
    }

    public WmsNavigationPage openWmsModule() {
        try {
            click("wms.menu");
        } catch (Exception ex) {
            WaitHelper.waitForClickable(By.xpath("//*[contains(text(),'WMS') or contains(@title,'WMS')]")).click();
        }
        WaitHelper.waitForPageLoad();
        return this;
    }

    public ProductManagementPage goToProducts() {
        WaitHelper.waitForClickable(By.xpath("//*[contains(.,'Product') and (self::a or self::span or self::button)]")).click();
        return new ProductManagementPage(driver());
    }

    public PutAwayPage goToPutAway() {
        WaitHelper.waitForClickable(By.xpath("//*[contains(.,'Put Away') or contains(.,'Putaway')]")).click();
        return new PutAwayPage(driver());
    }

    public PickingPage goToPicking() {
        WaitHelper.waitForClickable(By.xpath("//*[contains(.,'Picking') or contains(.,'Pick List')]")).click();
        return new PickingPage(driver());
    }

    public InventoryPage goToInventory() {
        WaitHelper.waitForClickable(By.xpath("//*[contains(.,'Inventory')]")).click();
        return new InventoryPage(driver());
    }

    public DispatchPage goToDispatch() {
        WaitHelper.waitForClickable(By.xpath("//*[contains(.,'Dispatch')]")).click();
        return new DispatchPage(driver());
    }
}

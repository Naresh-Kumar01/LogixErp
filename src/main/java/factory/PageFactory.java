package factory;

import drivers.DriverFactory;
import org.openqa.selenium.WebDriver;
import pages.auth.LoginPage;
import pages.wms.DispatchPage;
import pages.wms.InventoryPage;
import pages.wms.PickingPage;
import pages.wms.ProductManagementPage;
import pages.wms.PutAwayPage;
import pages.wms.WmsNavigationPage;

public final class PageFactory {

    private PageFactory() {
    }

    private static WebDriver driver() {
        return DriverFactory.getDriver();
    }

    public static LoginPage loginPage() {
        return new LoginPage(driver());
    }

    public static WmsNavigationPage wmsNavigation() {
        return new WmsNavigationPage(driver());
    }

    public static ProductManagementPage productPage() {
        return new ProductManagementPage(driver());
    }

    public static PutAwayPage putAwayPage() {
        return new PutAwayPage(driver());
    }

    public static PickingPage pickingPage() {
        return new PickingPage(driver());
    }

    public static InventoryPage inventoryPage() {
        return new InventoryPage(driver());
    }

    public static DispatchPage dispatchPage() {
        return new DispatchPage(driver());
    }
}

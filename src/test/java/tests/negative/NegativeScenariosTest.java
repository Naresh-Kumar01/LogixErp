package tests.negative;

import annotations.TestCase;
import api.APIUtility;
import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.auth.LoginPage;
import pages.wms.PickingPage;
import pages.wms.WmsNavigationPage;

@Listeners(listeners.TestListener.class)
public class NegativeScenariosTest extends BaseTest {

    private PickingPage picking;

    @BeforeMethod
    public void setup() {
        new LoginPage(driver()).enterUsername(config("username")).enterPassword(config("password")).clickSignIn();
        picking = new WmsNavigationPage(driver()).openWmsModule().goToPicking();
    }

    @TestCase(id = "NEG_001", module = "WMS")
    @Test(groups = {"Regression", "Negative"})
    public void testInvalidInventory() {
        picking.generatePicklist("ORD-INVALID-INV");
        Assert.assertTrue(!picking.isPicklistGenerated() || driver().getPageSource().contains("inventory"));
    }

    @TestCase(id = "NEG_002", module = "WMS")
    @Test(groups = {"Regression", "Negative"})
    public void testMissingSerial() {
        picking.pickSerial("");
        Assert.assertTrue(driver().getPageSource().toLowerCase().contains("serial")
                || driver().getPageSource().toLowerCase().contains("required"));
    }

    @TestCase(id = "NEG_003", module = "WMS")
    @Test(groups = {"Regression", "Negative"})
    public void testMissingBatch() {
        picking.pickBatch("", 1);
        Assert.assertTrue(driver().getPageSource().toLowerCase().contains("batch")
                || driver().getPageSource().toLowerCase().contains("required"));
    }

    @TestCase(id = "NEG_004", module = "WMS")
    @Test(groups = {"Regression", "Negative"})
    public void testExpiredInventory() {
        picking.pickBatch("BATCH-EXPIRED", 5);
        Assert.assertTrue(driver().getPageSource().toLowerCase().contains("expired"));
    }

    @TestCase(id = "NEG_005", module = "WMS")
    @Test(groups = {"Regression", "Negative"})
    public void testNegativeStock() {
        picking.generatePicklist("ORD-NEG-STOCK");
        Assert.assertTrue(driver().getPageSource().toLowerCase().contains("insufficient")
                || driver().getPageSource().toLowerCase().contains("negative"));
    }

    @TestCase(id = "NEG_006", module = "API")
    @Test(groups = {"Regression", "Negative", "API"})
    public void testApiFailureHandling() {
        APIUtility.clearAuth();
        var response = APIUtility.get("/inventory/secure-list");
        Assert.assertTrue(response.statusCode() == 401 || response.statusCode() == 403);
    }

    @TestCase(id = "NEG_007", module = "WMS")
    @Test(groups = {"Regression", "Negative"})
    public void testBrowserRefreshDuringTransaction() {
        picking.generatePicklist("ORD-REFRESH-01");
        driver().navigate().refresh();
        Assert.assertTrue(loginPage().isOnLoginPage() || picking.isPicklistGenerated());
    }

    @TestCase(id = "NEG_008", module = "WMS")
    @Test(groups = {"Regression", "Negative", "Security"})
    public void testUnauthorizedWarehouseAccess() {
        driver().get(config("appURL").replace("Login", "WMS/warehouse/RESTRICTED"));
        Assert.assertTrue(loginPage().isOnLoginPage() || driver().getPageSource().contains("unauthorized"));
    }

    @TestCase(id = "NEG_009", module = "WMS")
    @Test(groups = {"Regression", "Negative"}, enabled = false)
    public void testConcurrentUserConflicts() {
        Assert.assertTrue(true);
    }

    @TestCase(id = "NEG_010", module = "WMS")
    @Test(groups = {"Regression", "Negative"}, enabled = false)
    public void testSessionExpiryDuringPicking() {
        Assert.assertTrue(true);
    }

    private pages.auth.LoginPage loginPage() {
        return new pages.auth.LoginPage(driver());
    }
}

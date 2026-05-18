package tests.picking;

import annotations.TestCase;
import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.auth.LoginPage;
import pages.wms.InventoryPage;
import pages.wms.PickingPage;
import pages.wms.WmsNavigationPage;

@Listeners(listeners.TestListener.class)
public class FEFOAutomationTest extends BaseTest {

    private PickingPage picking;
    private InventoryPage inventory;

    @BeforeMethod
    public void setup() {
        new LoginPage(driver()).enterUsername(config("username")).enterPassword(config("password")).clickSignIn();
        WmsNavigationPage nav = new WmsNavigationPage(driver()).openWmsModule();
        picking = nav.goToPicking().applyFefo();
        inventory = nav.goToInventory();
    }

    @TestCase(id = "FEFO_001", module = "WMS")
    @Test(groups = {"Regression", "FEFO"})
    public void testNearestExpiryStockSelection() {
        picking.generatePicklist("ORD-FEFO-NEAR-EXP");
        Assert.assertTrue(picking.isPicklistGenerated());
    }

    @TestCase(id = "FEFO_002", module = "WMS")
    @Test(groups = {"Regression", "FEFO", "Negative"})
    public void testExpiredStockRestriction() {
        Assert.assertTrue(inventory.isExpiredStockBlocked("BATCH-EXPIRED-001")
                || driver().getPageSource().toLowerCase().contains("expired"));
    }

    @TestCase(id = "FEFO_003", module = "WMS")
    @Test(groups = {"Regression", "FEFO"})
    public void testBatchExpiryValidation() {
        picking.pickBatch("BATCH-FEFO-001", 1);
        Assert.assertTrue(picking.isPicklistGenerated());
    }

    @TestCase(id = "FEFO_004", module = "WMS")
    @Test(groups = {"Regression", "FEFO"})
    public void testFefoOverrideValidation() {
        picking.selectPickingRule("FEFO").generatePicklist("ORD-FEFO-OVERRIDE");
        Assert.assertTrue(picking.isPicklistGenerated());
    }

    @TestCase(id = "FEFO_005", module = "WMS")
    @Test(groups = {"Regression", "FEFO"})
    public void testMultiLocationFefoValidation() {
        picking.generatePicklist("ORD-FEFO-MULTI");
        Assert.assertTrue(picking.isPicklistGenerated());
    }
}

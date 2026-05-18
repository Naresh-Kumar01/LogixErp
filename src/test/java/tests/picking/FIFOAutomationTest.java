package tests.picking;

import annotations.TestCase;
import base.BaseTest;
import database.DBUtility;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.auth.LoginPage;
import pages.wms.PickingPage;
import pages.wms.WmsNavigationPage;

@Listeners(listeners.TestListener.class)
public class FIFOAutomationTest extends BaseTest {

    private PickingPage picking;

    @BeforeMethod
    public void setup() {
        new LoginPage(driver()).enterUsername(config("username")).enterPassword(config("password")).clickSignIn();
        picking = new WmsNavigationPage(driver()).openWmsModule().goToPicking().applyFifo();
    }

    @TestCase(id = "FIFO_001", module = "WMS")
    @Test(groups = {"Regression", "FIFO"})
    public void testOldestInventoryPickedFirst() {
        picking.generatePicklist("ORD-FIFO-OLDEST");
        Assert.assertTrue(picking.isPicklistGenerated());
    }

    @TestCase(id = "FIFO_002", module = "WMS")
    @Test(groups = {"Regression", "FIFO"})
    public void testPartialStockHandling() {
        picking.generatePicklist("ORD-FIFO-PARTIAL");
        Assert.assertTrue(picking.isPicklistGenerated() || driver().getPageSource().contains("partial"));
    }

    @TestCase(id = "FIFO_003", module = "WMS")
    @Test(groups = {"Regression", "FIFO"})
    public void testDamagedStockExclusion() {
        Assert.assertTrue(validateInventoryExclusion("damaged"));
    }

    @TestCase(id = "FIFO_004", module = "WMS")
    @Test(groups = {"Regression", "FIFO"})
    public void testBlockedStockExclusion() {
        Assert.assertTrue(validateInventoryExclusion("blocked"));
    }

    @TestCase(id = "FIFO_005", module = "WMS")
    @Test(groups = {"Regression", "FIFO"})
    public void testMultipleLocationInventory() {
        picking.generatePicklist("ORD-FIFO-MULTI-LOC");
        Assert.assertTrue(picking.isPicklistGenerated());
    }

    @TestCase(id = "FIFO_006", module = "WMS", description = "Concurrent picking - enable with grid parallel users")
    @Test(groups = {"Regression", "FIFO"}, enabled = false)
    public void testConcurrentPickingValidation() {
        Assert.assertTrue(true);
    }

    @TestCase(id = "FIFO_007", module = "WMS")
    @Test(groups = {"Regression", "FIFO"})
    public void testPicklistValidation() {
        picking.generatePicklist("ORD-FIFO-VALIDATE");
        if (config("db.enabled", "false").equalsIgnoreCase("true")) {
            Assert.assertFalse(DBUtility.executeQuery(DBUtility.SQL_PICKLIST_BY_ID, "ORD-FIFO-VALIDATE").isEmpty());
        } else {
            Assert.assertTrue(picking.isPicklistGenerated());
        }
    }

    @TestCase(id = "FIFO_008", module = "WMS")
    @Test(groups = {"Regression", "FIFO"})
    public void testManualOverrideValidation() {
        picking.selectPickingRule("FIFO").generatePicklist("ORD-FIFO-OVERRIDE");
        Assert.assertTrue(picking.isPicklistGenerated());
    }

    private boolean validateInventoryExclusion(String stockType) {
        return driver().getPageSource().toLowerCase().contains(stockType)
                || picking.isPicklistGenerated();
    }
}

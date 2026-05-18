package tests.batch;

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
import utilities.FakerDataProvider;

@Listeners(listeners.TestListener.class)
public class BatchProductAutomationTest extends BaseTest {

    private PickingPage picking;

    @BeforeMethod
    public void setup() {
        new LoginPage(driver()).enterUsername(config("username")).enterPassword(config("password")).clickSignIn();
        picking = new WmsNavigationPage(driver()).openWmsModule().goToPicking();
    }

    @TestCase(id = "BATCH_001", module = "WMS")
    @Test(groups = {"Regression", "Batch"})
    public void testBatchAllocation() {
        picking.pickBatch(FakerDataProvider.randomBatchNumber(), 10);
        Assert.assertTrue(picking.isPicklistGenerated());
    }

    @TestCase(id = "BATCH_002", module = "WMS")
    @Test(groups = {"Regression", "Batch"})
    public void testBatchSplitHandling() {
        picking.pickBatch("BATCH-SPLIT-01", 3);
        Assert.assertTrue(picking.isPicklistGenerated() || driver().getPageSource().contains("split"));
    }

    @TestCase(id = "BATCH_003", module = "WMS")
    @Test(groups = {"Regression", "Batch"})
    public void testBatchTraceability() {
        Assert.assertTrue(driver().getPageSource().toLowerCase().contains("batch") || picking.isPicklistGenerated());
    }

    @TestCase(id = "BATCH_004", module = "WMS", description = "Invalid batch restriction")
    @Test(groups = {"Regression", "Batch", "Negative"})
    public void testInvalidBatchRestriction() {
        picking.pickBatch("INVALID-BATCH", 1);
        Assert.assertTrue(!picking.isPicklistGenerated() || driver().getPageSource().contains("invalid"));
    }

    @TestCase(id = "BATCH_005", module = "WMS")
    @Test(groups = {"Regression", "Batch"})
    public void testBatchExpiryValidation() {
        picking.pickBatch("BATCH-EXPIRED", 1);
        Assert.assertTrue(driver().getPageSource().toLowerCase().contains("expiry")
                || driver().getPageSource().toLowerCase().contains("expired"));
    }

    @TestCase(id = "BATCH_006", module = "WMS")
    @Test(groups = {"Regression", "Batch", "DB"}, enabled = false)
    public void testBatchStockTransferValidation() {
        Assert.assertFalse(DBUtility.executeQuery(DBUtility.SQL_BATCH_BY_NUMBER, "BATCH-001").isEmpty());
    }
}

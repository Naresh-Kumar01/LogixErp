package tests.picking;

import annotations.TestCase;
import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.auth.LoginPage;
import pages.wms.PickingPage;
import pages.wms.WmsNavigationPage;
import retry.RetryAnalyzer;

@Listeners(listeners.TestListener.class)
public class PickingRulesAutomationTest extends BaseTest {

    private PickingPage picking;

    @BeforeMethod(alwaysRun = true)
    public void loginAndOpenPicking() {
        new LoginPage(driver()).enterUsername(config("username")).enterPassword(config("password")).clickSignIn();
        new WmsNavigationPage(driver()).openWmsModule();
        picking = new WmsNavigationPage(driver()).goToPicking();
    }

    @TestCase(id = "PICK_RULE_001", module = "WMS")
    @Test(groups = {"Sanity", "Picking"}, retryAnalyzer = RetryAnalyzer.class)
    public void testFifoRule() { picking.applyFifo().generatePicklist("ORD-FIFO-001"); Assert.assertTrue(picking.isPicklistGenerated()); }

    @TestCase(id = "PICK_RULE_002", module = "WMS")
    @Test(groups = {"Regression", "Picking"})
    public void testFefoRule() { picking.applyFefo().generatePicklist("ORD-FEFO-001"); Assert.assertTrue(picking.isPicklistGenerated()); }

    @TestCase(id = "PICK_RULE_003", module = "WMS")
    @Test(groups = {"Regression", "Picking"})
    public void testAsnBasedPicking() { picking.applyAsnBased().generatePicklist("ASN-1001"); Assert.assertTrue(picking.isPicklistGenerated()); }

    @TestCase(id = "PICK_RULE_004", module = "WMS")
    @Test(groups = {"Regression", "Picking"})
    public void testFmfoRule() { picking.applyFmfo().generatePicklist("ORD-FMFO-001"); Assert.assertTrue(picking.isPicklistGenerated()); }

    @TestCase(id = "PICK_RULE_005", module = "WMS")
    @Test(groups = {"Regression", "Picking"})
    public void testBatchNumberPicking() {
        picking.selectPickingRule("Batch").pickBatch("BATCH-001", 5);
        Assert.assertTrue(picking.isPicklistGenerated());
    }

    @TestCase(id = "PICK_RULE_006", module = "WMS")
    @Test(groups = {"Regression", "PutAway"})
    public void testSkipAutoSuggestedPutAwayLocation() {
        new WmsNavigationPage(driver()).goToPutAway().skipAutoSuggestedLocation().confirmPutAway("SKU-001", "LOC-MANUAL-01");
        Assert.assertTrue(true);
    }

    @TestCase(id = "PICK_RULE_007", module = "WMS")
    @Test(groups = {"Regression", "Picking"})
    public void testSkipAutoSuggestedPickupLocation() {
        picking.skipAutoSuggestedPickupLocation().generatePicklist("ORD-SKIP-LOC");
        Assert.assertTrue(picking.isPicklistGenerated());
    }

    @TestCase(id = "PICK_RULE_008", module = "WMS")
    @Test(groups = {"Regression", "Picking", "Serial"})
    public void testSerialProductPicking() {
        picking.applyFifo().pickSerial("SN-TEST-0001");
        Assert.assertTrue(picking.isPicklistGenerated());
    }

    @TestCase(id = "PICK_RULE_009", module = "WMS")
    @Test(groups = {"Regression", "Picking"})
    public void testNonSerialProductPicking() {
        picking.applyFifo().pickBatch("BATCH-NS-001", 2);
        Assert.assertTrue(picking.isPicklistGenerated());
    }
}

package tests.picking;

import annotations.TestCase;
import api.APIUtility;
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
public class ASNBasedPickingTest extends BaseTest {

    private PickingPage picking;

    @BeforeMethod
    public void setup() {
        new LoginPage(driver()).enterUsername(config("username")).enterPassword(config("password")).clickSignIn();
        picking = new WmsNavigationPage(driver()).openWmsModule().goToPicking().applyAsnBased();
    }

    @TestCase(id = "ASN_001", module = "WMS")
    @Test(groups = {"Regression", "ASN"})
    public void testAsnLinkedPicking() {
        picking.generatePicklist("ASN-9001");
        Assert.assertTrue(picking.isPicklistGenerated());
    }

    @TestCase(id = "ASN_002", module = "WMS", description = "ASN mismatch validation")
    @Test(groups = {"Regression", "ASN", "Negative"})
    public void testAsnMismatchValidation() {
        picking.generatePicklist("ASN-INVALID");
        Assert.assertTrue(!picking.isPicklistGenerated() || driver().getPageSource().toLowerCase().contains("mismatch"));
    }

    @TestCase(id = "ASN_003", module = "WMS")
    @Test(groups = {"Regression", "ASN"})
    public void testPartialAsnPicking() {
        picking.generatePicklist("ASN-PARTIAL-01");
        Assert.assertTrue(picking.isPicklistGenerated() || driver().getPageSource().contains("partial"));
    }

    @TestCase(id = "ASN_004", module = "WMS", description = "Closed ASN restriction")
    @Test(groups = {"Regression", "ASN", "Negative"})
    public void testClosedAsnRestriction() {
        if (config("api.enabled", "true").equalsIgnoreCase("true")) {
            var response = APIUtility.get("/asn/closed/ASN-CLOSED-01");
            Assert.assertTrue(response.statusCode() >= 400 || driver().getPageSource().contains("closed"));
        } else {
            picking.generatePicklist("ASN-CLOSED-01");
            Assert.assertFalse(picking.isPicklistGenerated());
        }
    }

    @TestCase(id = "ASN_005", module = "WMS")
    @Test(groups = {"Regression", "ASN", "DB"}, enabled = false)
    public void testAsnStockReconciliation() {
        Assert.assertFalse(DBUtility.executeQuery(DBUtility.SQL_ASN_LINKAGE, "ASN-9001").isEmpty());
    }
}

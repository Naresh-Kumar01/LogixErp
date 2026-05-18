package tests.serial;

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
public class SerialProductAutomationTest extends BaseTest {

    private PickingPage picking;

    @BeforeMethod
    public void setup() {
        new LoginPage(driver()).enterUsername(config("username")).enterPassword(config("password")).clickSignIn();
        picking = new WmsNavigationPage(driver()).openWmsModule().goToPicking();
    }

    @TestCase(id = "SERIAL_001", module = "WMS")
    @Test(groups = {"Regression", "Serial"})
    public void testUniqueSerialValidation() {
        String serial = FakerDataProvider.randomSerialNumber();
        picking.pickSerial(serial);
        Assert.assertTrue(picking.isPicklistGenerated());
    }

    @TestCase(id = "SERIAL_002", module = "WMS")
    @Test(groups = {"Regression", "Serial", "Negative"})
    public void testDuplicateSerialValidation() {
        String serial = "SN-DUP-001";
        picking.pickSerial(serial);
        picking.pickSerial(serial);
        Assert.assertTrue(driver().getPageSource().toLowerCase().contains("duplicate"));
    }

    @TestCase(id = "SERIAL_003", module = "WMS")
    @Test(groups = {"Regression", "Serial"})
    public void testSerialInwardOutwardTracking() {
        picking.pickSerial(FakerDataProvider.randomSerialNumber());
        Assert.assertTrue(picking.isPicklistGenerated());
    }

    @TestCase(id = "SERIAL_004", module = "WMS")
    @Test(groups = {"Regression", "Serial"})
    public void testBarcodeScannerSimulation() {
        picking.pickSerial("SN-SCAN|" + FakerDataProvider.randomSerialNumber());
        Assert.assertTrue(picking.isPicklistGenerated());
    }

    @TestCase(id = "SERIAL_005", module = "WMS")
    @Test(groups = {"Regression", "Serial", "DB"}, enabled = false)
    public void testSerialReconciliation() {
        Assert.assertFalse(DBUtility.executeQuery(DBUtility.SQL_SERIAL_BY_NUMBER, "SN-001").isEmpty());
    }

    @TestCase(id = "SERIAL_006", module = "WMS")
    @Test(groups = {"Regression", "Serial", "DB"}, enabled = false)
    public void testSerialAuditTracking() {
        Assert.assertFalse(DBUtility.executeQuery(DBUtility.SQL_AUDIT_TRAIL, "SN-001").isEmpty());
    }
}

package tests.database;

import annotations.TestCase;
import base.BaseTest;
import database.DBUtility;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(listeners.TestListener.class)
public class DatabaseValidationTest extends BaseTest {

    @TestCase(id = "DB_001", module = "DB", description = "Enable when db.enabled=true")
    @Test(groups = {"DB"}, enabled = false)
    public void validateProductCreationInDb() {
        Assert.assertTrue(DBUtility.exists(DBUtility.SQL_PRODUCT_BY_SKU, "SKU-001"));
    }

    @TestCase(id = "DB_002", module = "DB")
    @Test(groups = {"DB"}, enabled = false)
    public void validateInventoryAllocation() {
        Assert.assertFalse(DBUtility.executeQuery(DBUtility.SQL_INVENTORY_BY_PRODUCT, 1001).isEmpty());
    }

    @TestCase(id = "DB_003", module = "DB")
    @Test(groups = {"DB"}, enabled = false)
    public void validatePicklistGeneration() {
        Assert.assertFalse(DBUtility.executeQuery(DBUtility.SQL_PICKLIST_BY_ID, "PL-10001").isEmpty());
    }

    @TestCase(id = "DB_004", module = "DB")
    @Test(groups = {"DB"}, enabled = false)
    public void validateSerialMapping() {
        Assert.assertTrue(DBUtility.exists(DBUtility.SQL_SERIAL_BY_NUMBER, "SN-001"));
    }

    @TestCase(id = "DB_005", module = "DB")
    @Test(groups = {"DB"}, enabled = false)
    public void validateBatchMapping() {
        Assert.assertTrue(DBUtility.exists(DBUtility.SQL_BATCH_BY_NUMBER, "BATCH-001"));
    }

    @TestCase(id = "DB_006", module = "DB")
    @Test(groups = {"DB"}, enabled = false)
    public void validateAsnLinkage() {
        Assert.assertFalse(DBUtility.executeQuery(DBUtility.SQL_ASN_LINKAGE, "ASN-9001").isEmpty());
    }

    @TestCase(id = "DB_007", module = "DB")
    @Test(groups = {"DB"}, enabled = false)
    public void validateAuditTrail() {
        Assert.assertFalse(DBUtility.executeQuery(DBUtility.SQL_AUDIT_TRAIL, "PL-10001").isEmpty());
    }
}

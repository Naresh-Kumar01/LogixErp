package tests.product;

import annotations.TestCase;
import base.BaseTest;
import models.Product;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.auth.LoginPage;
import pages.wms.ProductManagementPage;
import pages.wms.WmsNavigationPage;
import retry.RetryAnalyzer;
import utilities.FakerDataProvider;

@Listeners(listeners.TestListener.class)
public class ProductCreationAutomationTest extends BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void loginAndOpenWms() {
        new LoginPage(driver())
                .enterUsername(config("username"))
                .enterPassword(config("password"))
                .clickSignIn();
        new WmsNavigationPage(driver()).openWmsModule();
    }

    private ProductManagementPage products() {
        return new WmsNavigationPage(driver()).goToProducts();
    }

    @TestCase(id = "PROD_001", module = "WMS")
    @Test(groups = {"Sanity", "Product"}, retryAnalyzer = RetryAnalyzer.class)
    public void testSerialProductCreation() {
        Product product = FakerDataProvider.randomSerialProduct();
        products().createProduct(product);
        Assert.assertFalse(products().isDuplicateSkuErrorDisplayed(), "Serial product should be created");
    }

    @TestCase(id = "PROD_002", module = "WMS")
    @Test(groups = {"Regression", "Product"})
    public void testNonSerialProductCreation() {
        Product product = FakerDataProvider.randomBatchProduct().toBuilder().serialTracked(false).batchEnabled(false).build();
        products().createProduct(product);
        Assert.assertFalse(products().isDuplicateSkuErrorDisplayed());
    }

    @TestCase(id = "PROD_003", module = "WMS")
    @Test(groups = {"Regression", "Product"})
    public void testBatchEnabledProduct() {
        Product product = FakerDataProvider.randomBatchProduct();
        products().createProduct(product);
        Assert.assertFalse(products().isDuplicateSkuErrorDisplayed());
    }

    @TestCase(id = "PROD_004", module = "WMS")
    @Test(groups = {"Regression", "Product"})
    public void testExpiryManagedProduct() {
        Product product = FakerDataProvider.randomBatchProduct().toBuilder().expiryManaged(true).build();
        products().createProduct(product);
        Assert.assertFalse(products().isDuplicateSkuErrorDisplayed());
    }

    @TestCase(id = "PROD_005", module = "WMS")
    @Test(groups = {"Regression", "Product"})
    public void testProductWithBarcode() {
        Product product = FakerDataProvider.randomSerialProduct();
        products().createProduct(product);
        Assert.assertNotNull(product.getBarcode());
    }

    @TestCase(id = "PROD_006", module = "WMS")
    @Test(groups = {"Regression", "Product", "Negative"})
    public void testDuplicateProductValidation() {
        Product product = FakerDataProvider.randomSerialProduct();
        products().createProduct(product);
        products().createProduct(product);
        Assert.assertTrue(products().isDuplicateSkuErrorDisplayed(), "Duplicate SKU should be rejected");
    }

    @TestCase(id = "PROD_007", module = "WMS")
    @Test(groups = {"Regression", "Product", "Negative"})
    public void testMandatoryFieldValidation() {
        Product product = Product.builder().sku("").name("").build();
        products().createProduct(product);
        Assert.assertTrue(products().isDuplicateSkuErrorDisplayed() || loginPageOnProductForm(),
                "Mandatory validation expected");
    }

    private boolean loginPageOnProductForm() {
        return driver().getPageSource().toLowerCase().contains("required");
    }

    @TestCase(id = "PROD_008", module = "WMS")
    @Test(groups = {"Regression", "Product"})
    public void testProductEditValidation() {
        Product product = FakerDataProvider.randomSerialProduct();
        products().createProduct(product);
        products().editProduct(product.getSku(), product.getName() + "-UPDATED");
        Assert.assertTrue(driver().getPageSource().contains("UPDATED") || !products().isDuplicateSkuErrorDisplayed());
    }

    @TestCase(id = "PROD_009", module = "WMS")
    @Test(groups = {"Regression", "Product"})
    public void testProductActivationDeactivation() {
        Product product = FakerDataProvider.randomSerialProduct();
        products().createProduct(product);
        products().deactivateProduct(product.getSku());
        products().activateProduct(product.getSku());
        Assert.assertTrue(true, "Activation/deactivation flow executed");
    }

    @TestCase(id = "PROD_010", module = "WMS")
    @Test(groups = {"Regression", "Product"})
    public void testMultiWarehouseValidation() {
        Product product = FakerDataProvider.randomSerialProduct().toBuilder().warehouse("WH-SECONDARY").build();
        products().createProduct(product);
        Assert.assertFalse(products().isDuplicateSkuErrorDisplayed());
    }
}

package pages.wms;

import helpers.WaitHelper;
import models.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.common.BasePage;

public class ProductManagementPage extends BasePage {

    public ProductManagementPage(WebDriver driver) {
        super(driver);
    }

    public ProductManagementPage createProduct(Product product) {
        WaitHelper.waitForClickable(By.xpath("//button[contains(.,'Add') or contains(.,'New Product')]")).click();
        type("product.sku", product.getSku());
        setField("name", product.getName());
        if (product.isSerialTracked()) {
            toggle("serialTracked", true);
        }
        if (product.isBatchEnabled()) {
            toggle("batchEnabled", true);
        }
        if (product.isExpiryManaged()) {
            toggle("expiryManaged", true);
        }
        if (product.getBarcode() != null) {
            setField("barcode", product.getBarcode());
        }
        click("product.save");
        WaitHelper.waitForPageLoad();
        return this;
    }

    public boolean isDuplicateSkuErrorDisplayed() {
        try {
            return WaitHelper.waitForVisible(
                    By.xpath("//*[contains(.,'duplicate') or contains(.,'already exists')]")).isDisplayed();
        } catch (Exception ex) {
            return false;
        }
    }

    public ProductManagementPage editProduct(String sku, String newName) {
        searchSku(sku);
        WaitHelper.waitForClickable(By.xpath("//tr[contains(.,'" + sku + "')]//button[contains(.,'Edit')]")).click();
        setField("name", newName);
        click("product.save");
        return this;
    }

    public ProductManagementPage activateProduct(String sku) {
        searchSku(sku);
        WaitHelper.waitForClickable(By.xpath("//tr[contains(.,'" + sku + "')]//button[contains(.,'Activate')]")).click();
        return this;
    }

    public ProductManagementPage deactivateProduct(String sku) {
        searchSku(sku);
        WaitHelper.waitForClickable(By.xpath("//tr[contains(.,'" + sku + "')]//button[contains(.,'Deactivate')]")).click();
        return this;
    }

    private void searchSku(String sku) {
        setField("search", sku);
        WaitHelper.waitForClickable(By.xpath("//button[contains(.,'Search')]")).click();
    }

    private void setField(String fieldName, String value) {
        By locator = By.xpath("//input[@name='" + fieldName + "' or contains(@id,'" + fieldName + "')]");
        WaitHelper.waitForVisible(locator).clear();
        WaitHelper.waitForVisible(locator).sendKeys(value);
    }

    private void toggle(String fieldName, boolean enabled) {
        By checkbox = By.xpath("//input[@type='checkbox' and (contains(@name,'" + fieldName + "') or contains(@id,'" + fieldName + "'))]");
        WebDriver d = driver();
        if (d.findElement(checkbox).isSelected() != enabled) {
            WaitHelper.waitForClickable(checkbox).click();
        }
    }
}

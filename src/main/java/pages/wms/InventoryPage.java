package pages.wms;

import helpers.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.common.BasePage;

public class InventoryPage extends BasePage {

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public int getAvailableQty(String sku) {
        searchSku(sku);
        String qtyText = WaitHelper.waitForVisible(
                By.xpath("//tr[contains(.,'" + sku + "')]//td[contains(@class,'available') or position()=4]"))
                .getText().trim();
        return Integer.parseInt(qtyText.replaceAll("[^0-9]", ""));
    }

    public boolean isExpiredStockBlocked(String batch) {
        searchBatch(batch);
        try {
            return WaitHelper.waitForVisible(
                    By.xpath("//*[contains(.,'expired') or contains(.,'not allowed')]")).isDisplayed();
        } catch (Exception ex) {
            return false;
        }
    }

    private void searchSku(String sku) {
        setField("sku", sku);
        WaitHelper.waitForClickable(By.xpath("//button[contains(.,'Search')]")).click();
    }

    private void searchBatch(String batch) {
        setField("batch", batch);
        WaitHelper.waitForClickable(By.xpath("//button[contains(.,'Search')]")).click();
    }

    private void setField(String name, String value) {
        By locator = By.xpath("//input[contains(@name,'" + name + "')]");
        WaitHelper.waitForVisible(locator).clear();
        WaitHelper.waitForVisible(locator).sendKeys(value);
    }
}

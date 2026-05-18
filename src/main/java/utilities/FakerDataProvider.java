package utilities;

import com.github.javafaker.Faker;
import models.Product;

import java.util.Locale;

public final class FakerDataProvider {

    private static final Faker FAKER = new Faker(Locale.ENGLISH);

    private FakerDataProvider() {
    }

    public static Product randomSerialProduct() {
        return Product.builder()
                .sku("SKU-" + FAKER.code().ean8())
                .name(FAKER.commerce().productName())
                .serialTracked(true)
                .batchEnabled(false)
                .expiryManaged(false)
                .barcode(FAKER.code().ean13())
                .warehouse("WH-MAIN")
                .build();
    }

    public static Product randomBatchProduct() {
        return Product.builder()
                .sku("BATCH-" + FAKER.code().ean8())
                .name(FAKER.commerce().productName())
                .serialTracked(false)
                .batchEnabled(true)
                .expiryManaged(true)
                .barcode(FAKER.code().ean13())
                .warehouse("WH-MAIN")
                .build();
    }

    public static String randomSerialNumber() {
        return "SN-" + FAKER.regexify("[A-Z0-9]{10}");
    }

    public static String randomBatchNumber() {
        return "BATCH-" + FAKER.number().digits(8);
    }
}

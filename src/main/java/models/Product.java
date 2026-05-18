package models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Product {
    private String sku;
    private String name;
    private String category;
    private boolean serialTracked;
    private boolean batchEnabled;
    private boolean expiryManaged;
    private String barcode;
    private String warehouse;
    private String status;
}

package models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Picklist {
    private String picklistId;
    private String pickingRule;
    private String warehouse;
    private String status;
    private String asnId;
}

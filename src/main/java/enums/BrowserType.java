package enums;

public enum BrowserType {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge");

    private final String value;

    BrowserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BrowserType from(String browser) {
        for (BrowserType type : values()) {
            if (type.value.equalsIgnoreCase(browser)) {
                return type;
            }
        }
        return CHROME;
    }
}

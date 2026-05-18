package enums;

public enum EnvironmentType {
    DEV("dev"),
    QA("qa"),
    UAT("uat"),
    PROD("prod");

    private final String value;

    EnvironmentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EnvironmentType from(String env) {
        for (EnvironmentType type : values()) {
            if (type.value.equalsIgnoreCase(env)) {
                return type;
            }
        }
        return QA;
    }
}

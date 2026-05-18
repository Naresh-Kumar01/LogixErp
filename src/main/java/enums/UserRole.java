package enums;

public enum UserRole {
    ADMIN("admin"),
    WMS_OPERATOR("wms_operator"),
    PICKER("picker"),
    DISPATCHER("dispatcher"),
    VIEWER("viewer");

    private final String roleKey;

    UserRole(String roleKey) {
        this.roleKey = roleKey;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public String configUsernameKey() {
        return roleKey + ".username";
    }

    public String configPasswordKey() {
        return roleKey + ".password";
    }
}

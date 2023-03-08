package util;

public enum Role {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");
    private final String role;

    Role(String roleAdmin) {
        this.role = roleAdmin;
    }
    public String getRole() {
        return role;
    }
}

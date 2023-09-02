package nvq.nvq.common.constant;

public enum Role {
    ADMIN("ADMIN"),
    USER("USER");
    private final String data;

    Role(String data) {
        this.data = data;
    }

    public String data() {
        return data;
    }
}

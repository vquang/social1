package nvq.nvq.common.constant;

public enum ObjectType {
    POST("post"),
    COMMENT("comment");
    private final String data;

    ObjectType(String data) {
        this.data = data;
    }

    public String data() {
        return this.data;
    }
}

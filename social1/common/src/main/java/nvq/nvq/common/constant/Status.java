package nvq.nvq.common.constant;

public enum Status {
    ACTIVE((byte) 1),
    DELETED((byte) -1);
    private final byte code;

    Status(byte code) {
        this.code = code;
    }

    public byte code() {
        return this.code;
    }
}

package nvq.nvq.common.constant;

public enum Media {
    IMAGE("image", "D:\\nvq\\nvq2\\cdn\\images\\"),
    VIDEO("video", " "),
    WORD("word", " "),
    PDF("pdf", " ");
    private final String data;
    private final String path;

    Media(String data, String path) {
        this.data = data;
        this.path = path;
    }

    public String data() {
        return this.data;
    }

    public String path() {
        return this.path;
    }
}

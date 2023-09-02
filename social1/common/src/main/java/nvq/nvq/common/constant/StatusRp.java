package nvq.nvq.common.constant;

public enum StatusRp {
    OK(200, "ok"),
    UNAUTHORIZED(401, "unauthorized"),
    RESOURCE_NOT_FOUND(400, "resource not found"),
    EXISTS_USERNAME(400, "username has already existed"),
    BAD_REQUEST(400, "bad request"),
    WRONG_PASS(401, "wrong password"),
    SELF_FOLLOW(400, "you can not follow yourself");
    private final int code;
    private final String message;

    StatusRp(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}

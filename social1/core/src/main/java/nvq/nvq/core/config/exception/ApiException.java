package nvq.nvq.core.config.exception;

import nvq.nvq.common.constant.StatusRp;

public class ApiException extends Exception {
    private final int code;
    private final String message;

    public ApiException(StatusRp status) {
        this.code = status.code();
        this.message = status.message();
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

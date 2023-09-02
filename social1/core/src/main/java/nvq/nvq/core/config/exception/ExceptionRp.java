package nvq.nvq.core.config.exception;

import nvq.nvq.common.response.DfResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionRp {
    @ExceptionHandler(ApiException.class)
    public static ResponseEntity<DfResponse<?>> handleApiException(ApiException e) {
        return ResponseEntity.status(e.getCode())
                .body(DfResponse.builder()
                        .code(e.getCode())
                        .message(e.getMessage())
                        .data(null)
                        .build());
    }
}

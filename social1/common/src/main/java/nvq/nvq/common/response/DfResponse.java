package nvq.nvq.common.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import nvq.nvq.common.constant.StatusRp;
import org.springframework.http.ResponseEntity;

import static nvq.nvq.common.constant.StatusRp.OK;

@Data
@Builder
@Accessors(chain = true)
public class DfResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ResponseEntity<DfResponse<T>> ok(T data) {
        return ResponseEntity.ok(DfResponse.<T>builder()
                .code(OK.code())
                .message(OK.message())
                .data(data)
                .build());
    }

    public static DfResponse<?> status(StatusRp statusRp) {
        return DfResponse.builder()
                .code(statusRp.code())
                .message(statusRp.message())
                .data(null)
                .build();
    }
}

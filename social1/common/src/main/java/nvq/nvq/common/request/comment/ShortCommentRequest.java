package nvq.nvq.common.request.comment;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShortCommentRequest {
    private String content;
}

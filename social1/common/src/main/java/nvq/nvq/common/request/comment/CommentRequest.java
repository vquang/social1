package nvq.nvq.common.request.comment;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentRequest {
    private String content;
    private String postId;
    private String parentId;
    private String userId;
}

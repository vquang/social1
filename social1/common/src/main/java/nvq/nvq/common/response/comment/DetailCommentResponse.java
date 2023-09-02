package nvq.nvq.common.response.comment;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import nvq.nvq.common.response.user.ShortUserResponse;

import java.time.LocalDateTime;

@Data
@Builder
@Accessors(chain = true)
public class DetailCommentResponse {
    private String id;
    private String content;
    private LocalDateTime createdAt;
    private String parentId;
    private String postId;
    private String userId;
    private int numVotes;
    private int numComments;
    private ShortUserResponse user;
}

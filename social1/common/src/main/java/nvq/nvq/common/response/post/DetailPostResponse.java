package nvq.nvq.common.response.post;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import nvq.nvq.common.response.user.ShortUserResponse;

import java.time.LocalDateTime;

@Data
@Builder
@Accessors(chain = true)
public class DetailPostResponse {
    private String id;
    private String image;
    private String content;
    private String userId;
    private LocalDateTime createdAt;
    private int numVotes;
    private int numComments;
    private ShortUserResponse user;
}

package nvq.nvq.common.response.user;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class DetailUserResponse {
    private String id;
    private String avatar;
    private String fullName;
    private int numFollowers;
    private int numFollowings;
    private int numPosts;
}

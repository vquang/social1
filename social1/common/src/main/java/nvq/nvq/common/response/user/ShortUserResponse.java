package nvq.nvq.common.response.user;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class ShortUserResponse {
    private String id;
    private String avatar;
    private String fullName;
}

package nvq.nvq.common.response.user;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class LoginUserResponse {
    private String token;
    private String userId;
}

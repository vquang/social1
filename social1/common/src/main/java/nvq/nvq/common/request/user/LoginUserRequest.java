package nvq.nvq.common.request.user;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginUserRequest {
    private String username;
    private String password;
}

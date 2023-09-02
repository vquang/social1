package nvq.nvq.common.request.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegisterUserRequest {
    private String username;
    private String password;
    private String fullName;
}

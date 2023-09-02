package nvq.nvq.common.request.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChangePassUserRequest {
    private String oldPassword;
    private String newPassword;
}

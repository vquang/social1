package nvq.nvq.common.request.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateUserRequest {
    private String fullName;
    private String avatar;
}

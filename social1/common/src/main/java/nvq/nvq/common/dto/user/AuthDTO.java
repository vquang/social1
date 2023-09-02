package nvq.nvq.common.dto.user;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import nvq.nvq.common.constant.Role;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
public class AuthDTO {
    private String userId;
    private List<Role> roles;
}

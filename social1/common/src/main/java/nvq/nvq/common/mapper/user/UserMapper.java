package nvq.nvq.common.mapper.user;

import nvq.nvq.common.dto.user.AuthDTO;
import nvq.nvq.common.request.user.ChangePassUserRequest;
import nvq.nvq.common.request.user.RegisterUserRequest;
import nvq.nvq.common.request.user.UpdateUserRequest;
import nvq.nvq.common.response.user.DetailUserResponse;
import nvq.nvq.common.response.user.ShortUserResponse;
import nvq.nvq.mysql.tables.pojos.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public abstract User toPojo(RegisterUserRequest registerUserRequest);

    public abstract User toPojo(UpdateUserRequest updateUserRequest);

    @Mapping(target = "password", source = "newPassword")
    public abstract User toPojo(ChangePassUserRequest changePassUserRequest);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "userId", source = "id")
    public abstract AuthDTO toDTO(User user);

    public abstract List<ShortUserResponse> toResponses(List<User> user);

    public abstract DetailUserResponse toResponse(User user);
    public abstract ShortUserResponse toShortResponse(User user);
}

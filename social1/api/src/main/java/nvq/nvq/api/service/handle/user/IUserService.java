package nvq.nvq.api.service.handle.user;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.request.user.ChangePassUserRequest;
import nvq.nvq.common.request.user.RegisterUserRequest;
import nvq.nvq.common.request.user.LoginUserRequest;
import nvq.nvq.common.request.user.UpdateUserRequest;
import nvq.nvq.common.response.user.DetailUserResponse;
import nvq.nvq.common.response.user.LoginUserResponse;

public interface IUserService {
    Single<String> register(RegisterUserRequest registerUserRequest);
    Single<LoginUserResponse> login(LoginUserRequest loginUserRequest);
    Single<String> updateUser(String loggedId, UpdateUserRequest updateUserRequest);
    Single<String> changePassword(String loggedId, ChangePassUserRequest changePassUserRequest);
    Single<DetailUserResponse> getUser(String userId);
}

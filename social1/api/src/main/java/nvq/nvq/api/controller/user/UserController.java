package nvq.nvq.api.controller.user;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.api.service.handle.user.IUserService;
import nvq.nvq.common.request.user.ChangePassUserRequest;
import nvq.nvq.common.request.user.UpdateUserRequest;
import nvq.nvq.common.response.DfResponse;
import nvq.nvq.common.response.user.DetailUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static nvq.nvq.core.util.AuthUtil.userId;

@RestController
@RequestMapping("/api/nvq/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @PutMapping("")
    public Single<ResponseEntity<DfResponse<String>>> updateUser(
            @RequestBody UpdateUserRequest updateUserRequest,
            Authentication authentication
    ) {
        return userService.updateUser(userId(authentication), updateUserRequest)
                .map(DfResponse::ok);
    }

    @PutMapping("/password")
    public Single<ResponseEntity<DfResponse<String>>> changePassword(
            @RequestBody ChangePassUserRequest changePassUserRequest,
            Authentication authentication
    ) {
        return userService.changePassword(userId(authentication), changePassUserRequest)
                .map(DfResponse::ok);
    }

    @GetMapping("/{id}")
    public Single<ResponseEntity<DfResponse<DetailUserResponse>>> getUser(
            @PathVariable("id") String userId
    ) {
        return userService.getUser(userId)
                .map(DfResponse::ok);
    }
}

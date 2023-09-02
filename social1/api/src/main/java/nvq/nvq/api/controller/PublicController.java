package nvq.nvq.api.controller;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.api.service.handle.user.IUserService;
import nvq.nvq.common.request.user.LoginUserRequest;
import nvq.nvq.common.request.user.RegisterUserRequest;
import nvq.nvq.common.response.DfResponse;
import nvq.nvq.common.response.user.LoginUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nvq/public")
public class PublicController {
    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public Single<ResponseEntity<DfResponse<String>>> register(
            @RequestBody RegisterUserRequest registerUserRequest
    ) {
        return userService.register(registerUserRequest)
                .map(DfResponse::ok);
    }

    @PostMapping("/login")
    public Single<ResponseEntity<DfResponse<LoginUserResponse>>> login(
            @RequestBody LoginUserRequest loginUserRequest
    ) {
        return userService.login(loginUserRequest)
                .map(DfResponse::ok);
    }
}

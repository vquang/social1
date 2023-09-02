package nvq.nvq.api.controller.follower;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.api.service.handle.follower.IFollowerService;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.common.response.DfResponse;
import nvq.nvq.common.response.PageResponse;
import nvq.nvq.common.response.user.ShortUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static nvq.nvq.core.util.AuthUtil.userId;

@RestController
@RequestMapping("/api/nvq")
public class FollowerController {
    @Autowired
    private IFollowerService followerService;

    @PostMapping("/user/follow/{id}")
    public Single<ResponseEntity<DfResponse<String>>> follow(
            @PathVariable("id") String userId,
            Authentication authentication
    ) {
        return followerService.follow(userId(authentication), userId)
                .map(DfResponse::ok);
    }

    @DeleteMapping("/user/unfollow/{id}")
    public Single<ResponseEntity<DfResponse<String>>> unfollow(
            @PathVariable("id") String userId,
            Authentication authentication
    ) {
        return followerService.unfollow(userId(authentication), userId)
                .map(DfResponse::ok);
    }

    @GetMapping("/user/followers/{id}")
    public Single<ResponseEntity<DfResponse<PageResponse<ShortUserResponse>>>> getFollowers(
            @PathVariable("id") String userId,
            PageRequest pageRequest
    ) {
        return followerService.getFollowers(userId, pageRequest)
                .map(DfResponse::ok);
    }

    @GetMapping("/user/followings/{id}")
    public Single<ResponseEntity<DfResponse<PageResponse<ShortUserResponse>>>> getFollowings(
            @PathVariable("id") String userId,
            PageRequest pageRequest
    ) {
        return followerService.getFollowings(userId, pageRequest)
                .map(DfResponse::ok);
    }
}

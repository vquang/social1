package nvq.nvq.api.service.handle.follower;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.common.response.PageResponse;
import nvq.nvq.common.response.user.ShortUserResponse;

public interface IFollowerService {
    Single<String> follow(String loggerId, String userId);
    Single<String> unfollow(String loggerId, String userId);
    Single<PageResponse<ShortUserResponse>> getFollowers(String userId, PageRequest pageRequest);
    Single<PageResponse<ShortUserResponse>> getFollowings(String followerId, PageRequest pageRequest);
}

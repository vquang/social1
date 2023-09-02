package nvq.nvq.api.service.handle.follower;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.mapper.user.UserMapper;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.common.response.PageResponse;
import nvq.nvq.common.response.user.ShortUserResponse;
import nvq.nvq.core.config.exception.ApiException;
import nvq.nvq.core.util.ReturnUtil;
import nvq.nvq.mysql.tables.pojos.Follower;
import nvq.nvq.repository.mysql.follower.IFollowerMysqlRepository;
import nvq.nvq.repository.mysql.user.IUserMysqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static nvq.nvq.common.constant.Status.ACTIVE;
import static nvq.nvq.common.constant.Status.DELETED;
import static nvq.nvq.common.constant.StatusRp.SELF_FOLLOW;
import static nvq.nvq.core.util.DBUtil.generateID;
import static nvq.nvq.core.util.TimeUtil.currentLocal;

@Service
public class FollowerService implements IFollowerService {
    @Autowired
    private IFollowerMysqlRepository followerMysqlRepository;
    @Autowired
    private IUserMysqlRepository userMysqlRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Single<String> follow(String loggerId, String userId) {
        return followerMysqlRepository.findOne(
                        new Follower()
                                .setUserId(userId)
                                .setFollowerId(loggerId))
                .map(followerOp -> {
                    if (loggerId.equals(userId))
                        throw new ApiException(SELF_FOLLOW);
                    return followerOp;
                })
                .flatMap(followerOp -> followerOp.isEmpty()
                        ? followerMysqlRepository.insert(
                        new Follower()
                                .setId(generateID())
                                .setUserId(userId)
                                .setFollowerId(loggerId)
                                .setStatus(ACTIVE.code())
                                .setCreatedAt(currentLocal())
                                .setCreatedBy(loggerId))
                        : followerMysqlRepository.update(
                        new Follower()
                                .setUserId(userId)
                                .setFollowerId(loggerId)
                                .setStatus(ACTIVE.code())
                                .setUpdatedAt(currentLocal())
                                .setUpdatedBy(loggerId))
                )
                .map(ReturnUtil::statusDB);
    }

    @Override
    public Single<String> unfollow(String loggerId, String userId) {
        return followerMysqlRepository.update(new Follower()
                        .setUserId(userId)
                        .setFollowerId(loggerId)
                        .setStatus(DELETED.code())
                        .setDeletedAt(currentLocal())
                        .setDeletedBy(loggerId))
                .map(ReturnUtil::statusDB);
    }

    @Override
    public Single<PageResponse<ShortUserResponse>> getFollowers(String userId, PageRequest pageRequest) {
        return followerMysqlRepository.getFollowerIds(userId, pageRequest.offset())
                .flatMap(ids -> Single.zip(
                        followerMysqlRepository.countFollowers(userId),
                        userMysqlRepository.getUsers(ids),
                        (total, user) -> PageResponse.config(
                                pageRequest.setTotal(total),
                                userMapper.toResponses(user))
                ));
    }

    @Override
    public Single<PageResponse<ShortUserResponse>> getFollowings(String followerId, PageRequest pageRequest) {
        return followerMysqlRepository.getFollowingIds(followerId, pageRequest.offset())
                .flatMap(ids -> Single.zip(
                        followerMysqlRepository.countFollowings(followerId),
                        userMysqlRepository.getUsers(ids),
                        (total, users) -> PageResponse.config(
                                pageRequest.setTotal(total),
                                userMapper.toResponses(users))
                ));
    }
}

package nvq.nvq.repository.mysql.follower;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.mysql.tables.pojos.Follower;

import java.util.List;
import java.util.Optional;

public interface IFollowerMysqlRepository {
    Single<Optional<Follower>> findOne(Follower follower);
    Single<Integer> insert(Follower follower);
    Single<Integer> update(Follower follower);
    Single<List<String>> getFollowerIds(String userId, PageRequest pageRequest);
    Single<Integer> countFollowers(String userId);
    Single<List<String>> getFollowingIds(String followerId, PageRequest pageRequest);
    Single<Integer> countFollowings(String followerId);
}

package nvq.nvq.repository.mysql.follower;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.mysql.tables.pojos.Follower;
import nvq.nvq.mysql.tables.records.FollowerRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static nvq.nvq.common.constant.Status.ACTIVE;
import static nvq.nvq.core.util.DBUtil.toFieldQueries;
import static nvq.nvq.core.util.RxUtil.rxSchedulerIo;
import static nvq.nvq.mysql.Tables.FOLLOWER;
import static org.jooq.impl.DSL.and;
import static org.jooq.impl.DSL.count;

@Repository
public class FollowerMysqlRepository implements IFollowerMysqlRepository {
    @Autowired
    private DSLContext dsl;
    private final FollowerRecord followerRecord = new FollowerRecord();

    @Override
    public Single<Optional<Follower>> findOne(Follower follower) {
        return rxSchedulerIo(() -> dsl
                .select()
                .from(FOLLOWER)
                .where(
                        and(
                                FOLLOWER.USER_ID.eq(follower.getUserId()),
                                FOLLOWER.FOLLOWER_ID.eq(follower.getFollowerId())
                        )
                )
                .fetchOptionalInto(Follower.class));
    }

    @Override
    public Single<Integer> insert(Follower follower) {
        return rxSchedulerIo(() -> dsl
                .insertInto(FOLLOWER)
                .set(toFieldQueries(followerRecord, follower))
                .execute());
    }

    @Override
    public Single<Integer> update(Follower follower) {
        return rxSchedulerIo(() -> dsl
                .update(FOLLOWER)
                .set(toFieldQueries(followerRecord, follower))
                .where(
                        and(
                                FOLLOWER.USER_ID.eq(follower.getUserId()),
                                FOLLOWER.FOLLOWER_ID.eq(follower.getFollowerId())
                        )
                )
                .execute());
    }

    @Override
    public Single<List<String>> getFollowerIds(String userId, PageRequest pageRequest) {
        return rxSchedulerIo(() -> dsl
                .select(FOLLOWER.FOLLOWER_ID)
                .from(FOLLOWER)
                .where(
                        and(
                                FOLLOWER.USER_ID.eq(userId),
                                FOLLOWER.STATUS.eq(ACTIVE.code())
                        )
                )
                .orderBy(FOLLOWER.CREATED_AT.desc())
                .limit(pageRequest.getLimit() + 1)
                .offset(pageRequest.getOffset())
                .fetchInto(String.class));
    }

    @Override
    public Single<Integer> countFollowers(String userId) {
        return rxSchedulerIo(() -> dsl
                .select(count())
                .from(FOLLOWER)
                .where(
                        and(
                                FOLLOWER.USER_ID.eq(userId),
                                FOLLOWER.STATUS.eq(ACTIVE.code())
                        )
                )
                .fetchOneInto(Integer.class));
    }

    @Override
    public Single<List<String>> getFollowingIds(String followerId, PageRequest pageRequest) {
        return rxSchedulerIo(() -> dsl
                .select(FOLLOWER.USER_ID)
                .from(FOLLOWER)
                .where(
                        and(
                                FOLLOWER.FOLLOWER_ID.eq(followerId),
                                FOLLOWER.STATUS.eq(ACTIVE.code())
                        )
                )
                .orderBy(FOLLOWER.CREATED_AT.desc())
                .limit(pageRequest.getLimit() + 1)
                .offset(pageRequest.getOffset())
                .fetchInto(String.class));
    }

    @Override
    public Single<Integer> countFollowings(String followerId) {
        return rxSchedulerIo(() -> dsl
                .select(count())
                .from(FOLLOWER)
                .where(
                        and(
                                FOLLOWER.FOLLOWER_ID.eq(followerId),
                                FOLLOWER.STATUS.eq(ACTIVE.code())
                        )
                )
                .fetchOneInto(Integer.class));
    }
}

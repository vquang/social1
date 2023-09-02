package nvq.nvq.repository.mysql.vote;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.mysql.tables.pojos.Vote;
import nvq.nvq.mysql.tables.records.VoteRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static nvq.nvq.common.constant.Status.ACTIVE;
import static nvq.nvq.core.util.DBUtil.toFieldQueries;
import static nvq.nvq.core.util.RxUtil.rxSchedulerIo;
import static nvq.nvq.mysql.Tables.VOTE;
import static org.jooq.impl.DSL.and;
import static org.jooq.impl.DSL.count;

@Repository
public class VoteMysqlRepository implements IVoteMysqlRepository {
    @Autowired
    private DSLContext dsl;
    private final VoteRecord voteRecord = new VoteRecord();

    @Override
    public Single<Integer> insert(Vote vote) {
        return rxSchedulerIo(() -> dsl
                .insertInto(VOTE)
                .set(toFieldQueries(voteRecord, vote))
                .execute());
    }

    @Override
    public Single<Integer> update(Vote vote) {
        return rxSchedulerIo(() -> dsl
                .update(VOTE)
                .set(toFieldQueries(voteRecord, vote))
                .where(
                        and(
                                VOTE.OBJECT_ID.eq(vote.getObjectId()),
                                VOTE.OBJECT_TYPE.eq(vote.getObjectType()),
                                VOTE.USER_ID.eq(vote.getUserId())
                        )
                )
                .execute());
    }

    @Override
    public Single<Optional<Vote>> findOneByVote(Vote vote) {
        return rxSchedulerIo(() -> dsl
                .select()
                .from(VOTE)
                .where(
                        and(
                                VOTE.OBJECT_ID.eq(vote.getObjectId()),
                                VOTE.OBJECT_TYPE.eq(vote.getObjectType()),
                                VOTE.USER_ID.eq(vote.getUserId())
                        )
                )
                .fetchOptionalInto(Vote.class));
    }

    @Override
    public Single<Integer> deleteByCommentIds(Vote vote, List<String> commentIds) {
        return rxSchedulerIo(() -> dsl
                .update(VOTE)
                .set(toFieldQueries(voteRecord, vote))
                .where(
                        and(
                                VOTE.OBJECT_ID.in(commentIds),
                                VOTE.OBJECT_TYPE.eq(vote.getObjectType()),
                                VOTE.STATUS.eq(ACTIVE.code())
                        )
                )
                .execute());
    }

    @Override
    public Single<Integer> deleteByPostId(Vote vote, String postId) {
        return rxSchedulerIo(() -> dsl
                .update(VOTE)
                .set(toFieldQueries(voteRecord, vote))
                .where(
                        and(
                                VOTE.OBJECT_ID.eq(postId),
                                VOTE.OBJECT_TYPE.eq(vote.getObjectType()),
                                VOTE.STATUS.eq(ACTIVE.code())
                        )
                )
                .execute());
    }

    @Override
    public Single<List<String>> getUserIds(Vote vote, PageRequest pageRequest) {
        return rxSchedulerIo(() -> dsl
                .select(VOTE.USER_ID)
                .from(VOTE)
                .where(
                        and(
                                VOTE.OBJECT_ID.eq(vote.getObjectId()),
                                VOTE.OBJECT_TYPE.eq(vote.getObjectType()),
                                VOTE.STATUS.eq(ACTIVE.code())
                        )
                )
                .orderBy(VOTE.CREATED_AT.desc())
                .limit(pageRequest.getLimit() + 1)
                .offset(pageRequest.getOffset())
                .fetchInto(String.class));
    }

    @Override
    public Single<Integer> countUsers(Vote vote) {
        return rxSchedulerIo(() -> dsl
                .select(count())
                .from(VOTE)
                .where(
                        and(
                                VOTE.OBJECT_ID.eq(vote.getObjectId()),
                                VOTE.OBJECT_TYPE.eq(vote.getObjectType()),
                                VOTE.STATUS.eq(ACTIVE.code())
                        )
                )
                .fetchOneInto(Integer.class));
    }

    @Override
    public Single<Map<String, Integer>> countVotes(List<String> objectIds, String objectType) {
        return rxSchedulerIo(() -> dsl
                .select(VOTE.OBJECT_ID, count())
                .from(VOTE)
                .where(
                        and(
                                VOTE.OBJECT_ID.in(objectIds),
                                VOTE.OBJECT_TYPE.eq(objectType),
                                VOTE.STATUS.eq(ACTIVE.code())
                        )
                )
                .groupBy(VOTE.OBJECT_ID)
                .fetchMap(VOTE.OBJECT_ID, count()));
    }

    @Override
    public Single<Integer> countVotes(String objectIds, String objectType) {
        return rxSchedulerIo(() -> dsl
                .select(count())
                .from(VOTE)
                .where(
                        and(
                                VOTE.OBJECT_ID.eq(objectIds),
                                VOTE.OBJECT_TYPE.eq(objectType),
                                VOTE.STATUS.eq(ACTIVE.code())
                        )
                )
                .fetchOneInto(Integer.class));
    }
}

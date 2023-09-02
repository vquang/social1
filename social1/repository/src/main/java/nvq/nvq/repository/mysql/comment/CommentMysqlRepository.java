package nvq.nvq.repository.mysql.comment;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.mysql.tables.pojos.Comment;
import nvq.nvq.mysql.tables.records.CommentRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static nvq.nvq.common.constant.Status.ACTIVE;
import static nvq.nvq.core.util.DBUtil.toFieldQueries;
import static nvq.nvq.core.util.RxUtil.rxSchedulerIo;
import static nvq.nvq.mysql.Tables.COMMENT;
import static org.jooq.impl.DSL.*;

@Repository
public class CommentMysqlRepository implements ICommentMysqlRepository {
    @Autowired
    private DSLContext dsl;
    private final CommentRecord commentRecord = new CommentRecord();

    @Override
    public Single<Integer> insert(Comment comment) {
        return rxSchedulerIo(() -> dsl
                .insertInto(COMMENT)
                .set(toFieldQueries(commentRecord, comment))
                .execute()
        );
    }

    @Override
    public Single<Optional<Comment>> findOneById(String id) {
        return rxSchedulerIo(() -> dsl
                .select()
                .from(COMMENT)
                .where(
                        and(
                                COMMENT.ID.eq(id),
                                COMMENT.STATUS.eq(ACTIVE.code())
                        )
                )
                .fetchOptionalInto(Comment.class));
    }

    @Override
    public Single<Integer> updateComment(Comment comment) {
        return rxSchedulerIo(() -> dsl
                .update(COMMENT)
                .set(toFieldQueries(commentRecord, comment))
                .where(
                        and(
                                COMMENT.ID.eq(comment.getId()),
                                COMMENT.USER_ID.eq(comment.getUserId()),
                                COMMENT.STATUS.eq(ACTIVE.code())
                        )
                )
                .execute());
    }

    @Override
    public Single<Integer> deleteById(String id, Comment comment) {
        return rxSchedulerIo(() -> dsl
                .update(COMMENT)
                .set(toFieldQueries(commentRecord, comment))
                .where(
                        and(
                                or(
                                        COMMENT.ID.eq(id),
                                        COMMENT.PARENT_ID.eq(id)
                                ),
                                COMMENT.STATUS.eq(ACTIVE.code())
                        )
                )
                .execute()
        );
    }

    @Override
    public Single<List<String>> findAllSubIds(String id) {
        return rxSchedulerIo(() -> dsl
                .select(COMMENT.ID)
                .from(COMMENT)
                .where(
                        and(
                                COMMENT.PARENT_ID.eq(id),
                                COMMENT.STATUS.eq(ACTIVE.code())
                        )
                )
                .fetchInto(String.class));
    }

    @Override
    public Single<Integer> deleteByPostId(String postId, Comment comment) {
        return rxSchedulerIo(() -> dsl
                .update(COMMENT)
                .set(toFieldQueries(commentRecord, comment))
                .where(
                        and(
                                COMMENT.POST_ID.eq(postId),
                                COMMENT.STATUS.eq(ACTIVE.code())
                        )
                )
                .execute()
        );
    }

    @Override
    public Single<List<String>> findAllByPostId(String postId) {
        return rxSchedulerIo(() -> dsl
                .select(COMMENT.ID)
                .from(COMMENT)
                .where(
                        and(
                                COMMENT.POST_ID.eq(postId),
                                COMMENT.STATUS.eq(ACTIVE.code())
                        )
                )
                .fetchInto(String.class));
    }

    @Override
    public Single<Map<String, Integer>> countCommentsOfPosts(List<String> postIds) {
        return rxSchedulerIo(() -> dsl
                .select(COMMENT.POST_ID, count())
                .from(COMMENT)
                .where(
                        and(
                                COMMENT.POST_ID.in(postIds),
                                COMMENT.STATUS.eq(ACTIVE.code()),
                                or(
                                        COMMENT.PARENT_ID.isNull(),
                                        COMMENT.PARENT_ID.eq("")
                                )
                        )
                )
                .groupBy(COMMENT.POST_ID)
                .fetchMap(COMMENT.POST_ID, count()));
    }

    @Override
    public Single<List<Comment>> getCommentsOfPost(String postId, PageRequest pageRequest) {
        return rxSchedulerIo(() -> dsl
                .select()
                .from(COMMENT)
                .where(
                        and(
                                COMMENT.POST_ID.eq(postId),
                                COMMENT.STATUS.eq(ACTIVE.code()),
                                or(
                                        COMMENT.PARENT_ID.isNull(),
                                        COMMENT.PARENT_ID.eq("")
                                )
                        )
                )
                .orderBy(COMMENT.CREATED_AT.desc())
                .limit(pageRequest.getLimit() + 1)
                .offset(pageRequest.getOffset())
                .fetchInto(Comment.class));
    }

    @Override
    public Single<Integer> countCommentsOfPost(String postId) {
        return rxSchedulerIo(() -> dsl
                .select(count())
                .from(COMMENT)
                .where(
                        and(
                                COMMENT.POST_ID.eq(postId),
                                COMMENT.STATUS.eq(ACTIVE.code()),
                                or(
                                        COMMENT.PARENT_ID.isNull(),
                                        COMMENT.PARENT_ID.eq("")
                                )
                        )
                )
                .fetchOneInto(Integer.class));
    }

    @Override
    public Single<List<Comment>> getCommentsOfComment(String parentId, PageRequest pageRequest) {
        return rxSchedulerIo(() -> dsl
                .select(COMMENT)
                .from(COMMENT)
                .where(
                        and(
                                COMMENT.PARENT_ID.eq(parentId),
                                COMMENT.STATUS.eq(ACTIVE.code())
                        )
                )
                .orderBy(COMMENT.CREATED_AT.desc())
                .limit(pageRequest.getLimit() + 1)
                .offset(pageRequest.getOffset())
                .fetchInto(Comment.class));
    }

    @Override
    public Single<Integer> countCommentsOfComment(String parentId) {
        return rxSchedulerIo(() -> dsl
                .select(count())
                .from(COMMENT)
                .where(
                        and(
                                COMMENT.PARENT_ID.eq(parentId),
                                COMMENT.STATUS.eq(ACTIVE.code())
                        )
                )
                .fetchOneInto(Integer.class));
    }

    @Override
    public Single<Map<String, Integer>> countCommentsOfComments(List<String> parentIds) {
        return rxSchedulerIo(() -> dsl
                .select(COMMENT.PARENT_ID, count())
                .from(COMMENT)
                .where(
                        and(
                                COMMENT.PARENT_ID.in(parentIds),
                                COMMENT.STATUS.eq(ACTIVE.code())
                        )
                )
                .groupBy(COMMENT.PARENT_ID)
                .fetchMap(COMMENT.PARENT_ID, count()));
    }
}

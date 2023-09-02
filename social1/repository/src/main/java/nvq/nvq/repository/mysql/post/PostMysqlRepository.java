package nvq.nvq.repository.mysql.post;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.mysql.tables.pojos.Post;
import nvq.nvq.mysql.tables.records.PostRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static nvq.nvq.common.constant.Status.ACTIVE;
import static nvq.nvq.core.util.DBUtil.toFieldQueries;
import static nvq.nvq.core.util.RxUtil.rxSchedulerIo;
import static nvq.nvq.mysql.Tables.POST;
import static org.jooq.impl.DSL.and;
import static org.jooq.impl.DSL.count;

@Repository
public class PostMysqlRepository implements IPostMysqlRepository {
    @Autowired
    private DSLContext dsl;
    private final PostRecord postRecord = new PostRecord();

    @Override
    public Single<Integer> insert(Post post) {
        return rxSchedulerIo(() -> dsl
                .insertInto(POST)
                .set(toFieldQueries(postRecord, post))
                .execute());
    }

    @Override
    public Single<Integer> update(Post post) {
        return rxSchedulerIo(() -> dsl
                .update(POST)
                .set(toFieldQueries(postRecord, post))
                .where(
                        and(
                                POST.ID.eq(post.getId()),
                                POST.USER_ID.eq(post.getUserId()),
                                POST.STATUS.eq(ACTIVE.code())
                        )
                )
                .execute());
    }

    @Override
    public Single<Optional<Post>> findOneById(String id) {
        return rxSchedulerIo(() -> dsl
                .select()
                .from(POST)
                .where(
                        and(
                                POST.ID.eq(id),
                                POST.STATUS.eq(ACTIVE.code())
                        )
                )
                .fetchOptionalInto(Post.class));
    }

    @Override
    public Single<Integer> countPosts(String userId) {
        return rxSchedulerIo(() -> dsl
                .select(count())
                .from(POST)
                .where(
                        and(
                                POST.USER_ID.eq(userId),
                                POST.STATUS.eq(ACTIVE.code())
                        )
                )
                .fetchOneInto(Integer.class));
    }

    @Override
    public Single<List<Post>> getPosts(PageRequest pageRequest) {
        return rxSchedulerIo(() -> dsl
                .select()
                .from(POST)
                .where(POST.STATUS.eq(ACTIVE.code()))
                .orderBy(POST.CREATED_AT.desc())
                .limit(pageRequest.getLimit() + 1)
                .offset(pageRequest.getOffset())
                .fetchInto(Post.class));
    }

    @Override
    public Single<Integer> countPosts() {
        return rxSchedulerIo(() -> dsl
                .select(count())
                .from(POST)
                .where(POST.STATUS.eq(ACTIVE.code()))
                .fetchOneInto(Integer.class));
    }

    @Override
    public Single<List<Post>> getPostsOfUser(String userId, PageRequest pageRequest) {
        return rxSchedulerIo(() -> dsl
                .select()
                .from(POST)
                .where(
                        and(
                                POST.USER_ID.eq(userId),
                                POST.STATUS.eq(ACTIVE.code())
                        )
                )
                .orderBy(POST.CREATED_AT.desc())
                .limit(pageRequest.getLimit() + 1)
                .offset(pageRequest.getOffset())
                .fetchInto(Post.class));
    }
}

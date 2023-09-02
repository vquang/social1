package nvq.nvq.repository.mysql.user;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.mysql.tables.pojos.User;
import nvq.nvq.mysql.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static nvq.nvq.common.constant.Status.ACTIVE;
import static nvq.nvq.core.util.DBUtil.toFieldQueries;
import static nvq.nvq.core.util.RxUtil.rxSchedulerIo;
import static nvq.nvq.mysql.Tables.USER;
import static org.jooq.impl.DSL.and;

@Repository
public class UserMysqlRepository implements IUserMysqlRepository {
    @Autowired
    private DSLContext dsl;
    private final UserRecord userRecord = new UserRecord();

    @Override
    public Single<Optional<User>> findByUsername(String username) {
        return rxSchedulerIo(() -> dsl
                .select()
                .from(USER)
                .where(
                        and(
                                USER.USERNAME.eq(username),
                                USER.STATUS.eq(ACTIVE.code())
                        )
                )
                .fetchOptionalInto(User.class));
    }

    @Override
    public Single<Integer> insert(User user) {
        return rxSchedulerIo(() -> dsl
                .insertInto(USER)
                .set(toFieldQueries(userRecord, user))
                .execute());
    }

    @Override
    public Single<Integer> update(User user) {
        return rxSchedulerIo(() -> dsl
                .update(USER)
                .set(toFieldQueries(userRecord, user))
                .where(
                        and(
                                USER.ID.eq(user.getId()),
                                USER.STATUS.eq(ACTIVE.code())
                        )
                )
                .execute());
    }

    @Override
    public Single<Optional<User>> findById(String id) {
        return rxSchedulerIo(() -> dsl
                .select()
                .from(USER)
                .where(
                        and(
                                USER.ID.eq(id),
                                USER.STATUS.eq(ACTIVE.code())
                        )
                )
                .fetchOptionalInto(User.class));
    }

    @Override
    public Single<List<User>> getUsers(List<String> ids) {
        return rxSchedulerIo(() -> dsl
                .select()
                .from(USER)
                .where(
                        and(
                                USER.ID.in(ids),
                                USER.STATUS.eq(ACTIVE.code())
                        )
                )
                .orderBy(USER.CREATED_AT.desc())
                .fetchInto(User.class));
    }

    @Override
    public Single<Map<String, User>> getUsersMap(List<String> ids) {
        return rxSchedulerIo(() -> dsl
                .select()
                .from(USER)
                .where(
                        and(
                                USER.ID.in(ids),
                                USER.STATUS.eq(ACTIVE.code())
                        )
                )
                .orderBy(USER.CREATED_AT.desc())
                .fetchMap(USER.ID, User.class));
    }
}

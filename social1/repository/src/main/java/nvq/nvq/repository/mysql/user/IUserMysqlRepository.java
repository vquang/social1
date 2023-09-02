package nvq.nvq.repository.mysql.user;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.mysql.tables.pojos.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IUserMysqlRepository {
    Single<Optional<User>> findByUsername(String username);
    Single<Integer> insert(User user);
    Single<Integer> update(User user);
    Single<Optional<User>> findById(String id);
    Single<List<User>> getUsers(List<String> ids);
    Single<Map<String, User>> getUsersMap(List<String> ids);
}

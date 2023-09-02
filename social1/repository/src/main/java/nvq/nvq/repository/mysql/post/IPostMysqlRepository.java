package nvq.nvq.repository.mysql.post;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.mysql.tables.pojos.Post;

import java.util.List;
import java.util.Optional;

public interface IPostMysqlRepository {
    Single<Integer> insert(Post post);
    Single<Integer> update(Post post);
    Single<Optional<Post>> findOneById(String id);
    Single<Integer> countPosts(String userId);
    Single<List<Post>> getPosts(PageRequest pageRequest);
    Single<Integer> countPosts();
    Single<List<Post>> getPostsOfUser(String userId, PageRequest pageRequest);
}

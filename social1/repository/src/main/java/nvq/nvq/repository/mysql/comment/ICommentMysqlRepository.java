package nvq.nvq.repository.mysql.comment;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.mysql.tables.pojos.Comment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ICommentMysqlRepository {
    Single<Integer> insert(Comment comment);
    Single<Optional<Comment>> findOneById(String id);
    Single<Integer> updateComment(Comment comment);
    Single<Integer> deleteById(String id, Comment comment);
    Single<List<String>> findAllSubIds(String id);
    Single<Integer> deleteByPostId(String postId, Comment comment);
    Single<List<String>> findAllByPostId(String postId);
    Single<Map<String, Integer>> countCommentsOfPosts(List<String> postIds);
    Single<List<Comment>> getCommentsOfPost(String postId, PageRequest pageRequest);
    Single<Integer> countCommentsOfPost(String postId);
    Single<List<Comment>> getCommentsOfComment(String parentId, PageRequest pageRequest);
    Single<Integer> countCommentsOfComment(String parentId);
    Single<Map<String, Integer>> countCommentsOfComments(List<String> parentIds);
}

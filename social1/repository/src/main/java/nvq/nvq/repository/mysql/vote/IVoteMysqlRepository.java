package nvq.nvq.repository.mysql.vote;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.mysql.tables.pojos.Vote;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IVoteMysqlRepository {
    Single<Integer> insert(Vote vote);
    Single<Integer> update(Vote vote);
    Single<Optional<Vote>> findOneByVote(Vote vote);
    Single<Integer> deleteByCommentIds(Vote vote, List<String> commentIds);
    Single<Integer> deleteByPostId(Vote vote, String postId);
    Single<List<String>> getUserIds(Vote vote, PageRequest pageRequest);
    Single<Integer> countUsers(Vote vote);
    Single<Map<String, Integer>> countVotes(List<String> objectIds, String objectType);
    Single<Integer> countVotes(String objectIds, String objectType);
}

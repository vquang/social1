package nvq.nvq.api.service.handle.vote;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.common.request.vote.VoteRequest;
import nvq.nvq.common.response.PageResponse;
import nvq.nvq.common.response.user.ShortUserResponse;

public interface IVoteService {
    Single<String> createVote(String loggedId, VoteRequest voteRequest);
    Single<String> unVote(String loggedId, VoteRequest voteRequest);
    Single<PageResponse<ShortUserResponse>> getVotes(VoteRequest voteRequest, PageRequest pageRequest);
}

package nvq.nvq.api.controller.vote;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.api.service.handle.vote.IVoteService;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.common.request.vote.VoteRequest;
import nvq.nvq.common.response.DfResponse;
import nvq.nvq.common.response.PageResponse;
import nvq.nvq.common.response.user.ShortUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static nvq.nvq.core.util.AuthUtil.userId;

@RestController
@RequestMapping("/api/nvq/vote")
public class VoteController {
    @Autowired
    private IVoteService voteService;

    @PostMapping("")
    public Single<ResponseEntity<DfResponse<String>>> createVote(
            @RequestBody VoteRequest voteRequest,
            Authentication authentication
    ) {
        return voteService.createVote(userId(authentication), voteRequest)
                .map(DfResponse::ok);
    }

    @DeleteMapping("")
    public Single<ResponseEntity<DfResponse<String>>> unVote(
            @RequestBody VoteRequest voteRequest,
            Authentication authentication
    ) {
        return voteService.unVote(userId(authentication), voteRequest)
                .map(DfResponse::ok);
    }

    @PostMapping("/list")
    public Single<ResponseEntity<DfResponse<PageResponse<ShortUserResponse>>>> getVotes(
            @RequestBody VoteRequest voteRequest,
            PageRequest pageRequest
    ) {
        return voteService.getVotes(voteRequest, pageRequest)
                .map(DfResponse::ok);
    }
}

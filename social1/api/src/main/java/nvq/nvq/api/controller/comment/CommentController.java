package nvq.nvq.api.controller.comment;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.api.service.handle.comment.ICommentService;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.common.request.comment.CommentRequest;
import nvq.nvq.common.request.comment.ShortCommentRequest;
import nvq.nvq.common.response.DfResponse;
import nvq.nvq.common.response.PageResponse;
import nvq.nvq.common.response.comment.DetailCommentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static nvq.nvq.core.util.AuthUtil.userId;

@RestController
@RequestMapping("/api/nvq/comment")
public class CommentController {
    @Autowired
    private ICommentService commentService;

    @PostMapping("")
    public Single<ResponseEntity<DfResponse<String>>> createComment(
            @RequestBody CommentRequest commentRequest,
            Authentication authentication
    ) {
        return commentService.createComment(userId(authentication), commentRequest)
                .map(DfResponse::ok);
    }

    @PutMapping("/{id}")
    public Single<ResponseEntity<DfResponse<String>>> updateComment(
            @PathVariable("id") String commentId,
            @RequestBody ShortCommentRequest shortCommentRequest,
            Authentication authentication
    ) {
        return commentService.updateComment(userId(authentication), commentId, shortCommentRequest)
                .map(DfResponse::ok);
    }

    @DeleteMapping("/{cid}/post/{pid}")
    public Single<ResponseEntity<DfResponse<String>>> deleteComment(
            @PathVariable("cid") String commentId,
            @PathVariable("pid") String postId,
            Authentication authentication
    ) {
        return commentService.deleteComment(userId(authentication), commentId, postId)
                .map(DfResponse::ok);
    }

    @GetMapping("/list/post/{id}")
    public Single<ResponseEntity<DfResponse<PageResponse<DetailCommentResponse>>>> getCommentsOfPost(
            @PathVariable("id") String postId,
            PageRequest pageRequest
    ) {
        return commentService.getCommentsOfPost(postId, pageRequest)
                .map(DfResponse::ok);
    }

    @GetMapping("/{id}")
    public Single<ResponseEntity<DfResponse<PageResponse<DetailCommentResponse>>>> getCommentsOfComment(
            @PathVariable("id") String parentId,
            PageRequest pageRequest
    ) {
        return commentService.getCommentsOfComment(parentId, pageRequest)
                .map(DfResponse::ok);
    }
}

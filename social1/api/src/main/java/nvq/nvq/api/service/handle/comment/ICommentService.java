package nvq.nvq.api.service.handle.comment;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.common.request.comment.CommentRequest;
import nvq.nvq.common.request.comment.ShortCommentRequest;
import nvq.nvq.common.response.PageResponse;
import nvq.nvq.common.response.comment.DetailCommentResponse;

public interface ICommentService {
    Single<String> createComment(String loggedId, CommentRequest commentRequest);

    Single<String> updateComment(String loggedId, String commentId, ShortCommentRequest shortCommentRequest);

    Single<String> deleteComment(String loggedId, String commentId, String postId);
    Single<PageResponse<DetailCommentResponse>> getCommentsOfPost(String postId, PageRequest pageRequest);
    Single<PageResponse<DetailCommentResponse>> getCommentsOfComment(String parentId, PageRequest pageRequest);
}

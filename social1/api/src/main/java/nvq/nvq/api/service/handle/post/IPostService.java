package nvq.nvq.api.service.handle.post;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.common.request.post.ShortPostRequest;
import nvq.nvq.common.response.PageResponse;
import nvq.nvq.common.response.post.DetailPostResponse;

public interface IPostService {
    Single<String> createPost(String loggedId, ShortPostRequest shortPostRequest);
    Single<String> updatePost(String loggedId, String postId, ShortPostRequest shortPostRequest);
    Single<String> deletePost(String loggedId, String postId);
    Single<PageResponse<DetailPostResponse>> getPosts(PageRequest pageRequest);
    Single<PageResponse<DetailPostResponse>> getPostsOfUser(String userId, PageRequest pageRequest);
    Single<DetailPostResponse> getPost(String postId);
}

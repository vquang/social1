package nvq.nvq.api.controller.post;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.api.service.handle.post.IPostService;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.common.request.post.ShortPostRequest;
import nvq.nvq.common.response.DfResponse;
import nvq.nvq.common.response.PageResponse;
import nvq.nvq.common.response.post.DetailPostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static nvq.nvq.core.util.AuthUtil.userId;

@RestController
@RequestMapping("/api/nvq/post")
public class PostController {
    @Autowired
    private IPostService postService;

    @PostMapping("")
    public Single<ResponseEntity<DfResponse<String>>> createPost(
            @RequestBody ShortPostRequest shortPostRequest,
            Authentication authentication
    ) {
        return postService.createPost(userId(authentication), shortPostRequest)
                .map(DfResponse::ok);
    }

    @PutMapping("/{id}")
    public Single<ResponseEntity<DfResponse<String>>> updatePost(
            @PathVariable("id") String postId,
            @RequestBody ShortPostRequest shortPostRequest,
            Authentication authentication
    ) {
        return postService.updatePost(userId(authentication), postId, shortPostRequest)
                .map(DfResponse::ok);
    }

    @DeleteMapping("/{id}")
    public Single<ResponseEntity<DfResponse<String>>> deletePost(
            @PathVariable("id") String postId,
            Authentication authentication
    ) {
        return postService.deletePost(userId(authentication), postId)
                .map(DfResponse::ok);
    }

    @GetMapping("/list")
    public Single<ResponseEntity<DfResponse<PageResponse<DetailPostResponse>>>> getPosts(
            PageRequest pageRequest
    ) {
        return postService.getPosts(pageRequest)
                .map(DfResponse::ok);
    }

    @GetMapping("/list/user/{id}")
    public Single<ResponseEntity<DfResponse<PageResponse<DetailPostResponse>>>> getPostsOfUser(
            @PathVariable("id") String userId,
            PageRequest pageRequest
    ) {
        return postService.getPostsOfUser(userId, pageRequest)
                .map(DfResponse::ok);
    }

    @GetMapping("/{id}")
    public Single<ResponseEntity<DfResponse<DetailPostResponse>>> getPost(
            @PathVariable("id") String postId
    ) {
        return postService.getPost(postId)
                .map(DfResponse::ok);
    }
}

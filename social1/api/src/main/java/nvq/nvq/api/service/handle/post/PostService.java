package nvq.nvq.api.service.handle.post;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.mapper.post.PostMapper;
import nvq.nvq.common.mapper.user.UserMapper;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.common.request.post.ShortPostRequest;
import nvq.nvq.common.response.PageResponse;
import nvq.nvq.common.response.post.DetailPostResponse;
import nvq.nvq.core.config.exception.ApiException;
import nvq.nvq.core.util.ReturnUtil;
import nvq.nvq.mysql.tables.pojos.Comment;
import nvq.nvq.mysql.tables.pojos.Post;
import nvq.nvq.mysql.tables.pojos.Vote;
import nvq.nvq.repository.mysql.comment.ICommentMysqlRepository;
import nvq.nvq.repository.mysql.post.IPostMysqlRepository;
import nvq.nvq.repository.mysql.user.IUserMysqlRepository;
import nvq.nvq.repository.mysql.vote.IVoteMysqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static nvq.nvq.common.constant.ObjectType.COMMENT;
import static nvq.nvq.common.constant.ObjectType.POST;
import static nvq.nvq.common.constant.Status.ACTIVE;
import static nvq.nvq.common.constant.Status.DELETED;
import static nvq.nvq.common.constant.StatusRp.RESOURCE_NOT_FOUND;
import static nvq.nvq.core.util.DBUtil.generateID;
import static nvq.nvq.core.util.TimeUtil.currentLocal;

@Service
public class PostService implements IPostService {
    @Autowired
    private IPostMysqlRepository postMysqlRepository;
    @Autowired
    private ICommentMysqlRepository commentMysqlRepository;
    @Autowired
    private IVoteMysqlRepository voteMysqlRepository;
    @Autowired
    private IUserMysqlRepository userMysqlRepository;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserMapper userMapper;
    @Value("${spring.cdn.user.df-post}")
    private String dfPost;

    @Override
    public Single<String> createPost(String loggedId, ShortPostRequest shortPostRequest) {
        return postMysqlRepository.insert(postMapper.toPojo(shortPostRequest.dfImage(dfPost))
                        .setId(generateID())
                        .setUserId(loggedId)
                        .setCreatedAt(currentLocal())
                        .setCreatedBy(loggedId)
                        .setStatus(ACTIVE.code()))
                .map(ReturnUtil::statusDB);
    }

    @Override
    public Single<String> updatePost(String loggedId, String postId, ShortPostRequest shortPostRequest) {
        return postMysqlRepository.update(postMapper.toPojo(shortPostRequest)
                        .setId(postId)
                        .setUserId(loggedId)
                        .setUpdatedAt(currentLocal())
                        .setUpdatedBy(loggedId))
                .map(ReturnUtil::statusDB);
    }

    @Override
    public Single<String> deletePost(String loggedId, String postId) {
        return commentMysqlRepository.findAllByPostId(postId)
                .flatMap(ids -> Single.zip(
                        postMysqlRepository.update(new Post()
                                .setId(postId)
                                .setUserId(loggedId)
                                .setStatus(DELETED.code())
                                .setDeletedAt(currentLocal())
                                .setDeletedBy(loggedId)),
                        commentMysqlRepository.deleteByPostId(postId, new Comment()
                                .setStatus(DELETED.code())
                                .setDeletedAt(currentLocal())
                                .setDeletedBy(loggedId)),
                        voteMysqlRepository.deleteByPostId(new Vote()
                                        .setObjectId(postId)
                                        .setObjectType(POST.data())
                                        .setStatus(DELETED.code())
                                        .setDeletedAt(currentLocal())
                                        .setDeletedBy(loggedId),
                                postId),
                        voteMysqlRepository.deleteByCommentIds(new Vote()
                                        .setObjectType(COMMENT.data())
                                        .setDeletedAt(currentLocal())
                                        .setDeletedBy(loggedId)
                                        .setStatus(DELETED.code())
                                , ids),
                        (rPost, rComments, rVotePost, rVoteComments) -> rPost
                ))
                .map(ReturnUtil::statusDB);
    }

    @Override
    public Single<PageResponse<DetailPostResponse>> getPosts(PageRequest pageRequest) {
        return postMysqlRepository.getPosts(pageRequest.offset())
                .flatMap(posts -> {
                    List<String> postIds = posts.stream().map(Post::getId).toList();
                    List<String> userIds = posts.stream().map(Post::getUserId).toList();
                    return Single.zip(
                            postMysqlRepository.countPosts(),
                            voteMysqlRepository.countVotes(postIds, POST.data()),
                            commentMysqlRepository.countCommentsOfPosts(postIds),
                            userMysqlRepository.getUsersMap(userIds),
                            (totalPosts, totalVotesMap, totalCommentsMap, usersMap) -> {
                                List<DetailPostResponse> postResponses = posts.stream()
                                        .map(post -> postMapper.toResponse(post)
                                                .setContent(post.getContent().substring(0, 5) + "...")
                                                .setNumVotes(totalVotesMap.getOrDefault(post.getId(), 0))
                                                .setNumComments(totalCommentsMap.getOrDefault(post.getId(), 0))
                                                .setUser(userMapper.toShortResponse(usersMap.get(post.getUserId()))))
                                        .collect(Collectors.toList());
                                return PageResponse.config(pageRequest.setTotal(totalPosts), postResponses);
                            }
                    );
                });
    }

    @Override
    public Single<PageResponse<DetailPostResponse>> getPostsOfUser(String userId, PageRequest pageRequest) {
        return postMysqlRepository.getPostsOfUser(userId, pageRequest.offset())
                .flatMap(posts -> {
                    List<String> postIds = posts.stream().map(Post::getId).toList();
                    List<String> userIds = posts.stream().map(Post::getUserId).toList();
                    return Single.zip(
                            postMysqlRepository.countPosts(userId),
                            voteMysqlRepository.countVotes(postIds, POST.data()),
                            commentMysqlRepository.countCommentsOfPosts(postIds),
                            userMysqlRepository.getUsersMap(userIds),
                            (totalPosts, totalVotesMap, totalCommentsMap, usersMap) -> {
                                List<DetailPostResponse> postResponses = posts.stream()
                                        .map(post -> postMapper.toResponse(post)
                                                .setContent(post.getContent().substring(0, 5) + "...")
                                                .setNumVotes(totalVotesMap.getOrDefault(post.getId(), 0))
                                                .setNumComments(totalCommentsMap.getOrDefault(post.getId(), 0))
                                                .setUser(userMapper.toShortResponse(usersMap.get(post.getUserId()))))
                                        .collect(Collectors.toList());
                                return PageResponse.config(pageRequest.setTotal(totalPosts), postResponses);
                            }
                    );
                });
    }

    @Override
    public Single<DetailPostResponse> getPost(String postId) {
        return postMysqlRepository.findOneById(postId)
                .flatMap(postOp -> {
                    if (postOp.isEmpty())
                        return Single.error(new ApiException(RESOURCE_NOT_FOUND));
                    return Single.zip(
                            voteMysqlRepository.countVotes(postId, POST.data()),
                            commentMysqlRepository.countCommentsOfPost(postId),
                            userMysqlRepository.findById(postOp.get().getUserId()),
                            (totalVotes, totalComments, userOp) -> {
                                if (userOp.isEmpty())
                                    throw new ApiException(RESOURCE_NOT_FOUND);
                                return postMapper.toResponse(postOp.get())
                                        .setNumVotes(totalVotes)
                                        .setNumComments(totalComments)
                                        .setUser(userMapper.toShortResponse(userOp.get()));
                            }
                    );
                });
    }
}

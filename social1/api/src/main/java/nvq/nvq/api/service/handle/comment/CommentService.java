package nvq.nvq.api.service.handle.comment;

import io.micrometer.common.util.StringUtils;
import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.mapper.comment.CommentMapper;
import nvq.nvq.common.mapper.user.UserMapper;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.common.request.comment.CommentRequest;
import nvq.nvq.common.request.comment.ShortCommentRequest;
import nvq.nvq.common.response.PageResponse;
import nvq.nvq.common.response.comment.DetailCommentResponse;
import nvq.nvq.core.config.exception.ApiException;
import nvq.nvq.core.util.ReturnUtil;
import nvq.nvq.mysql.tables.pojos.Comment;
import nvq.nvq.mysql.tables.pojos.Vote;
import nvq.nvq.repository.mysql.comment.ICommentMysqlRepository;
import nvq.nvq.repository.mysql.post.IPostMysqlRepository;
import nvq.nvq.repository.mysql.user.IUserMysqlRepository;
import nvq.nvq.repository.mysql.vote.IVoteMysqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static nvq.nvq.common.constant.ObjectType.COMMENT;
import static nvq.nvq.common.constant.Status.ACTIVE;
import static nvq.nvq.common.constant.Status.DELETED;
import static nvq.nvq.common.constant.StatusRp.RESOURCE_NOT_FOUND;
import static nvq.nvq.common.constant.StatusRp.UNAUTHORIZED;
import static nvq.nvq.core.util.DBUtil.generateID;
import static nvq.nvq.core.util.TimeUtil.currentLocal;

@Service
public class CommentService implements ICommentService {
    @Autowired
    private ICommentMysqlRepository commentMysqlRepository;
    @Autowired
    private IPostMysqlRepository postMysqlRepository;
    @Autowired
    private IVoteMysqlRepository voteMysqlRepository;
    @Autowired
    private IUserMysqlRepository userMysqlRepository;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Single<String> createComment(String loggedId, CommentRequest commentRequest) {
        return Single.zip(
                        postMysqlRepository.findOneById(commentRequest.getPostId()),
                        commentMysqlRepository.findOneById(commentRequest.getParentId()),
                        (postOp, commentOp) -> {
                            if (postOp.isEmpty())
                                throw new ApiException(RESOURCE_NOT_FOUND);
                            if (commentOp.isEmpty() && !StringUtils.isBlank(commentRequest.getParentId()))
                                throw new ApiException(RESOURCE_NOT_FOUND);
                            return "SUCCESS";
                        }
                ).flatMap(r -> commentMysqlRepository.insert(commentMapper.toPojo(commentRequest)
                        .setId(generateID())
                        .setUserId(loggedId)
                        .setCreatedAt(currentLocal())
                        .setCreatedBy(loggedId)
                        .setStatus(ACTIVE.code())))
                .map(ReturnUtil::statusDB);
    }

    @Override
    public Single<String> updateComment(String loggedId, String commentId, ShortCommentRequest shortCommentRequest) {
        return commentMysqlRepository.updateComment(commentMapper.toPojo(shortCommentRequest)
                        .setId(commentId)
                        .setUserId(loggedId)
                        .setUpdatedAt(currentLocal())
                        .setUpdatedBy(loggedId))
                .map(ReturnUtil::statusDB);
    }

    @Override
    public Single<String> deleteComment(String loggedId, String commentId, String postId) {
        return Single.zip(
                        commentMysqlRepository.findOneById(commentId),
                        postMysqlRepository.findOneById(postId),
                        commentMysqlRepository.findAllSubIds(commentId),
                        (commentOp, postOp, ids) -> {
                            if (commentOp.isEmpty() || postOp.isEmpty())
                                throw new ApiException(RESOURCE_NOT_FOUND);
                            if (!loggedId.equals(commentOp.get().getUserId()) && !loggedId.equals(postOp.get().getUserId()))
                                throw new ApiException(UNAUTHORIZED);
                            if (!commentOp.get().getPostId().equals(postOp.get().getId()))
                                throw new ApiException(UNAUTHORIZED);
                            ids.add(commentId);
                            return ids;
                        }
                )
                .flatMap(ids -> Single.zip(
                        commentMysqlRepository.deleteById(commentId, new Comment()
                                .setStatus(DELETED.code())
                                .setDeletedAt(currentLocal())
                                .setDeletedBy(loggedId)),
                        voteMysqlRepository.deleteByCommentIds(new Vote()
                                        .setObjectType(COMMENT.data())
                                        .setDeletedAt(currentLocal())
                                        .setDeletedBy(loggedId)
                                        .setStatus(DELETED.code())
                                , ids),
                        (rComment, rVote) -> 1
                ))
                .map(ReturnUtil::statusDB);
    }

    @Override
    public Single<PageResponse<DetailCommentResponse>> getCommentsOfPost(String postId, PageRequest pageRequest) {
        return commentMysqlRepository.getCommentsOfPost(postId, pageRequest.offset())
                .flatMap(comments -> {
                    List<String> commentIds = comments.stream().map(Comment::getId).toList();
                    List<String> userIds = comments.stream().map(Comment::getUserId).toList();
                    return Single.zip(
                            commentMysqlRepository.countCommentsOfPost(postId),
                            voteMysqlRepository.countVotes(commentIds, COMMENT.data()),
                            commentMysqlRepository.countCommentsOfComments(commentIds),
                            userMysqlRepository.getUsersMap(userIds),
                            (totalComments, totalVotesMap, totalSubCommentsMap, usersMap) -> {
                                List<DetailCommentResponse> detailCommentResponses = comments.stream()
                                        .map(comment -> commentMapper.toResponse(comment)
                                                .setNumVotes(totalVotesMap.getOrDefault(comment.getId(), 0))
                                                .setNumComments(totalSubCommentsMap.getOrDefault(comment.getId(), 0))
                                                .setUser(userMapper.toShortResponse(usersMap.get(comment.getUserId()))))
                                        .collect(Collectors.toList());
                                return PageResponse.config(pageRequest.setTotal(totalComments), detailCommentResponses);
                            }
                    );
                });
    }

    @Override
    public Single<PageResponse<DetailCommentResponse>> getCommentsOfComment(String parentId, PageRequest pageRequest) {
        return commentMysqlRepository.getCommentsOfComment(parentId, pageRequest.offset())
                .flatMap(comments -> {
                    List<String> commentIds = comments.stream().map(Comment::getId).toList();
                    List<String> userIds = comments.stream().map(Comment::getUserId).toList();
                    return Single.zip(
                            commentMysqlRepository.countCommentsOfComment(parentId),
                            voteMysqlRepository.countVotes(commentIds, COMMENT.data()),
                            userMysqlRepository.getUsersMap(userIds),
                            (totalComments, totalVotesMap, usersMap) -> {
                                List<DetailCommentResponse> detailCommentResponses = comments.stream()
                                        .map(comment -> commentMapper.toResponse(comment)
                                                .setNumVotes(totalVotesMap.getOrDefault(comment.getId(), 0))
                                                .setUser(userMapper.toShortResponse(usersMap.get(comment.getUserId()))))
                                        .collect(Collectors.toList());
                                return PageResponse.config(pageRequest.setTotal(totalComments), detailCommentResponses);
                            }
                    );
                });
    }
}

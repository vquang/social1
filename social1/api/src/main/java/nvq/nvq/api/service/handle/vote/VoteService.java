package nvq.nvq.api.service.handle.vote;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.mapper.user.UserMapper;
import nvq.nvq.common.mapper.vote.VoteMapper;
import nvq.nvq.common.request.PageRequest;
import nvq.nvq.common.request.vote.VoteRequest;
import nvq.nvq.common.response.PageResponse;
import nvq.nvq.common.response.user.ShortUserResponse;
import nvq.nvq.core.config.exception.ApiException;
import nvq.nvq.core.util.ReturnUtil;
import nvq.nvq.repository.mysql.comment.ICommentMysqlRepository;
import nvq.nvq.repository.mysql.post.IPostMysqlRepository;
import nvq.nvq.repository.mysql.user.IUserMysqlRepository;
import nvq.nvq.repository.mysql.vote.IVoteMysqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static nvq.nvq.common.constant.ObjectType.COMMENT;
import static nvq.nvq.common.constant.ObjectType.POST;
import static nvq.nvq.common.constant.Status.ACTIVE;
import static nvq.nvq.common.constant.Status.DELETED;
import static nvq.nvq.common.constant.StatusRp.RESOURCE_NOT_FOUND;
import static nvq.nvq.core.util.DBUtil.generateID;
import static nvq.nvq.core.util.TimeUtil.currentLocal;

@Service
public class VoteService implements IVoteService {
    @Autowired
    private IVoteMysqlRepository voteMysqlRepository;
    @Autowired
    private ICommentMysqlRepository commentMysqlRepository;
    @Autowired
    private IPostMysqlRepository postMysqlRepository;
    @Autowired
    private IUserMysqlRepository userMysqlRepository;
    @Autowired
    private VoteMapper voteMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Single<String> createVote(String loggedId, VoteRequest voteRequest) {
        return Single.zip(
                        postMysqlRepository.findOneById(voteRequest.getObjectId()),
                        commentMysqlRepository.findOneById(voteRequest.getObjectId()),
                        (postOp, commentOp) -> {
                            if (postOp.isEmpty() && voteRequest.getObjectType().equals(POST.data()))
                                throw new ApiException(RESOURCE_NOT_FOUND);
                            if (commentOp.isEmpty() && voteRequest.getObjectType().equals(COMMENT.data()))
                                throw new ApiException(RESOURCE_NOT_FOUND);
                            return voteMapper
                                    .toPojo(voteRequest)
                                    .setUserId(loggedId);
                        }
                )
                .flatMap(vote -> voteMysqlRepository.findOneByVote(vote))
                .flatMap(voteOp -> voteOp.isEmpty()
                        ? voteMysqlRepository
                        .insert(voteMapper.toPojo(voteRequest)
                                .setId(generateID())
                                .setUserId(loggedId)
                                .setStatus(ACTIVE.code())
                                .setCreatedAt(currentLocal())
                                .setCreatedBy(loggedId))
                        : voteMysqlRepository
                        .update(voteOp.get()
                                .setUpdatedAt(currentLocal())
                                .setUpdatedBy(loggedId)
                                .setStatus(ACTIVE.code())))
                .map(ReturnUtil::statusDB);
    }

    @Override
    public Single<String> unVote(String loggedId, VoteRequest voteRequest) {
        return voteMysqlRepository.update(voteMapper.toPojo(voteRequest)
                        .setUserId(loggedId)
                        .setStatus(DELETED.code())
                        .setDeletedAt(currentLocal())
                        .setDeletedBy(loggedId))
                .map(ReturnUtil::statusDB);
    }

    @Override
    public Single<PageResponse<ShortUserResponse>> getVotes(VoteRequest voteRequest, PageRequest pageRequest) {
        return voteMysqlRepository.getUserIds(voteMapper.toPojo(voteRequest), pageRequest.offset())
                .flatMap(ids -> Single.zip(
                        userMysqlRepository.getUsers(ids),
                        voteMysqlRepository.countUsers(voteMapper.toPojo(voteRequest)),
                        (users, total) -> PageResponse.config(
                                pageRequest.setTotal(total),
                                userMapper.toResponses(users)))
                );
    }
}

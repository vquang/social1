package nvq.nvq.api.service.handle.user;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.mapper.user.UserMapper;
import nvq.nvq.common.request.user.ChangePassUserRequest;
import nvq.nvq.common.request.user.LoginUserRequest;
import nvq.nvq.common.request.user.RegisterUserRequest;
import nvq.nvq.common.request.user.UpdateUserRequest;
import nvq.nvq.common.response.user.DetailUserResponse;
import nvq.nvq.common.response.user.LoginUserResponse;
import nvq.nvq.core.config.exception.ApiException;
import nvq.nvq.core.config.jwt.JwtService;
import nvq.nvq.core.util.ReturnUtil;
import nvq.nvq.mysql.tables.pojos.User;
import nvq.nvq.repository.mysql.follower.IFollowerMysqlRepository;
import nvq.nvq.repository.mysql.post.IPostMysqlRepository;
import nvq.nvq.repository.mysql.user.IUserMysqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static nvq.nvq.common.constant.Role.USER;
import static nvq.nvq.common.constant.Status.ACTIVE;
import static nvq.nvq.common.constant.StatusRp.*;
import static nvq.nvq.core.util.DBUtil.generateID;
import static nvq.nvq.core.util.JsonUtil.jsonToList;
import static nvq.nvq.core.util.JsonUtil.objectToJson;
import static nvq.nvq.core.util.TimeUtil.currentLocal;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IUserMysqlRepository userMysqlRepository;
    @Autowired
    private IFollowerMysqlRepository followerMysqlRepository;
    @Autowired
    private IPostMysqlRepository postMysqlRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Value("${spring.cdn.user.df-avatar}")
    private String dfAvatar;

    @Override
    public Single<String> register(RegisterUserRequest registerUserRequest) {
        return userMysqlRepository.findByUsername(registerUserRequest.getUsername())
                .flatMap(userOp -> {
                    if (userOp.isPresent())
                        return Single.error(new ApiException(EXISTS_USERNAME));
                    String id = generateID();
                    return userMysqlRepository.insert(userMapper
                            .toPojo(registerUserRequest)
                            .setId(id)
                            .setAvatar(dfAvatar)
                            .setPassword(passwordEncoder.encode(registerUserRequest.getPassword()))
                            .setRoles(objectToJson(Collections.singletonList(USER.data())))
                            .setCreatedAt(currentLocal())
                            .setCreatedBy(id)
                            .setStatus(ACTIVE.code()));
                })
                .map(ReturnUtil::statusDB);
    }

    @Override
    public Single<LoginUserResponse> login(LoginUserRequest loginUserRequest) {
        return userMysqlRepository.findByUsername(loginUserRequest.getUsername())
                .map(userOp -> {
                    if (userOp.isEmpty())
                        throw new ApiException(UNAUTHORIZED);
                    User user = userOp.get();
                    if (!passwordEncoder.matches(loginUserRequest.getPassword(), user.getPassword()))
                        throw new ApiException(UNAUTHORIZED);
                    return LoginUserResponse.builder()
                            .token(jwtService.generateToken(userMapper
                                    .toDTO(user)
                                    .setRoles(jsonToList(user.getRoles()))))
                            .userId(user.getId())
                            .build();
                });
    }

    @Override
    public Single<String> updateUser(String loggedId, UpdateUserRequest updateUserRequest) {
        return userMysqlRepository.update(userMapper
                        .toPojo(updateUserRequest)
                        .setId(loggedId)
                        .setUpdatedAt(currentLocal())
                        .setUpdatedBy(loggedId))
                .map(ReturnUtil::statusDB);
    }

    @Override
    public Single<String> changePassword(String loggedId, ChangePassUserRequest changePassUserRequest) {
        return userMysqlRepository.findById(loggedId)
                .flatMap(userOp -> {
                    if (userOp.isEmpty())
                        return Single.error(new ApiException(UNAUTHORIZED));
                    if (!passwordEncoder.matches(changePassUserRequest.getOldPassword(), userOp.get().getPassword()))
                        return Single.error(new ApiException(WRONG_PASS));
                    return userMysqlRepository.update(userMapper
                            .toPojo(changePassUserRequest)
                            .setId(loggedId)
                            .setPassword(passwordEncoder.encode(changePassUserRequest.getNewPassword()))
                            .setUpdatedAt(currentLocal())
                            .setUpdatedBy(loggedId));
                })
                .map(ReturnUtil::statusDB);
    }

    @Override
    public Single<DetailUserResponse> getUser(String userId) {
        return Single.zip(
                userMysqlRepository.findById(userId),
                followerMysqlRepository.countFollowers(userId),
                followerMysqlRepository.countFollowings(userId),
                postMysqlRepository.countPosts(userId),
                (userOp, numFollowers, numFollowings, numPosts) -> {
                    if (userOp.isEmpty())
                        throw new ApiException(RESOURCE_NOT_FOUND);
                    return userMapper.toResponse(userOp.get())
                            .setNumFollowers(numFollowers)
                            .setNumFollowings(numFollowings)
                            .setNumPosts(numPosts);
                }
        );
    }

}

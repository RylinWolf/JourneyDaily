package com.wolfhouse.journeydaily.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wolfhouse.journeydaily.common.annotation.NotBlankArgs;
import com.wolfhouse.journeydaily.common.constant.CommonConstant;
import com.wolfhouse.journeydaily.common.constant.ServiceExceptionConstant;
import com.wolfhouse.journeydaily.common.constant.UserConstant;
import com.wolfhouse.journeydaily.common.exceptions.ServiceException;
import com.wolfhouse.journeydaily.common.properties.JwtProperties;
import com.wolfhouse.journeydaily.common.util.BeanUtil;
import com.wolfhouse.journeydaily.common.util.ServiceUtil;
import com.wolfhouse.journeydaily.context.BaseContext;
import com.wolfhouse.journeydaily.mapper.AdminMapper;
import com.wolfhouse.journeydaily.mapper.UserMapper;
import com.wolfhouse.journeydaily.pojo.dto.UserDto;
import com.wolfhouse.journeydaily.pojo.entity.User;
import com.wolfhouse.journeydaily.pojo.vo.UserLoginVo;
import com.wolfhouse.journeydaily.pojo.vo.UserVo;
import com.wolfhouse.journeydaily.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.validation.constraints.NotNull;


/**
 * @author linexsong
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2025-01-23 16:55:28
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final AdminMapper adminMapper;
    private final JwtProperties properties;

    @Override
    public @NotNull UserVo verifyPassword(String userEmail, String password) {
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        User user = userMapper.getByEmail(userEmail);
        if (BeanUtil.isBlank(user)) {
            throw new ServiceException(UserConstant.ACCOUNT_NOT_EXIST);
        }
        return user.getPwdHash().equals(password) ? BeanUtil.copyProperties(user, UserVo.class) : null;
    }

    @Override
    public @NotNull UserVo verifyPassword(Long userId, String password) {
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        User user = userMapper.getById(userId);
        if (BeanUtil.isBlank(user)) {
            throw new ServiceException(UserConstant.ACCOUNT_NOT_EXIST);
        }
        return user.getPwdHash().equals(password) ? BeanUtil.copyProperties(user, UserVo.class) : null;
    }

    @Override
    public UserVo verifyPasswordNotDeleted(String userEmail, String password) {
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        return BeanUtil.copyProperties(lambdaQuery().eq(User::getEmail, userEmail).eq(User::getPwdHash, password).one(),
                                       UserVo.class);

    }

    @Override
    @NotBlankArgs({"email", "passwd"})
    public UserLoginVo login(String email, String passwd) {
        UserLoginVo userLoginVo = BeanUtil.copyProperties(verifyPasswordNotDeleted(email, passwd), UserLoginVo.class);
        if (BeanUtil.isBlank(userLoginVo)) {
            return null;
        }
        String jwt = ServiceUtil.getToken(properties, userLoginVo.getUserId());
        userLoginVo.setToken(jwt);

        return userLoginVo;
    }

    @Override
    public UserLoginVo register(UserDto userDto) {
        User user = BeanUtil.copyProperties(userDto, User.class);
        if (!verifyUser(user)) {
            return null;
        }
        user.setPwdHash(DigestUtils.md5DigestAsHex(user.getPwdHash().getBytes()));
        if (userMapper.countByEmail(user.getEmail()) != 0 || userMapper.insertWithGeneratedKeys(user) <= 0) {
            return null;
        }
        user = userMapper.getById(user.getUserId());
        return BeanUtil.copyProperties(user, UserLoginVo.class);
    }

    @Override
    public Boolean recoveryUser(String userEmail) {
        // 用户已删除，且成功恢复
        return !lambdaQuery().eq(User::getEmail, userEmail).exists() &&
               userMapper.recovery(userEmail, CommonConstant.NOT_DELETED) > 0;
    }

    @Override
    public UserVo updateById(UserDto userDto) {
        User user = BeanUtil.copyProperties(userDto, User.class);
        user.setUserId(BaseContext.getUserId());
        user.setEmail(userMapper.selectById(user.getUserId()).getEmail());
        if (userMapper.updateById(user) > 0) {
            return BeanUtil.copyProperties(getById(user.getUserId()), UserVo.class);
        }
        return null;
    }

    @Override
    public Boolean deregister(Long userId, String password) {
        User user;

        if (BeanUtil.isBlank(userId) || (user = userMapper.selectById(userId)) == null) {
            // 未登录
            throw ServiceException.loginRequired();
        }

        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPwdHash())) {
            // 密码错误
            throw ServiceException.authorizedFailed();
        }
        // 若是管理员，则从管理员中移除
        if (userMapper.deleteById(userId) > 0) {
            adminMapper.deleteByUserId(userId);
            return true;
        }
        return false;
    }

    private boolean verifyUser(User user) {
        return !BeanUtil.isAnyBlank(user, user.getEmail(), user.getPwdHash());
    }

    @Override
    public Boolean updateIsAdmin(Long userId, Integer isAdmin) {
        return lambdaUpdate().eq(User::getUserId, userId).set(User::getIsAdmin, isAdmin).update();
    }

    @Override
    public UserVo getCurrentUserInfo() {
        return BeanUtil.copyProperties(
                getById(ServiceUtil.userIdOrException(new ServiceException(ServiceExceptionConstant.LOGIN_REQUIRED))),
                UserVo.class);
    }
}

package com.wolfhouse.journeydaily.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wolfhouse.journeydaily.common.annotation.NotBlankArgs;
import com.wolfhouse.journeydaily.common.constant.AdminConstant;
import com.wolfhouse.journeydaily.common.constant.CommonConstant;
import com.wolfhouse.journeydaily.common.constant.UserConstant;
import com.wolfhouse.journeydaily.common.exceptions.ServiceException;
import com.wolfhouse.journeydaily.common.properties.AdminAddProperties;
import com.wolfhouse.journeydaily.common.util.BeanUtil;
import com.wolfhouse.journeydaily.context.BaseContext;
import com.wolfhouse.journeydaily.mapper.AdminMapper;
import com.wolfhouse.journeydaily.mapper.UserMapper;
import com.wolfhouse.journeydaily.pojo.dto.AdminAddDto;
import com.wolfhouse.journeydaily.pojo.dto.AdminDto;
import com.wolfhouse.journeydaily.pojo.entity.Admin;
import com.wolfhouse.journeydaily.pojo.entity.User;
import com.wolfhouse.journeydaily.pojo.vo.AdminVo;
import com.wolfhouse.journeydaily.pojo.vo.UserVo;
import com.wolfhouse.journeydaily.service.AdminService;
import com.wolfhouse.journeydaily.service.AuthorizeService;
import com.wolfhouse.journeydaily.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * @author linexsong
 * @description 针对表【admin(管理员表)】的数据库操作Service实现
 * @createDate 2025-02-19 12:42:14
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    private final AdminMapper mapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final AdminAddProperties properties;
    private final AuthorizeService authService;

    /**
     * 验证指定账号是否具有操作权限。
     * <p>
     * 当前登录账号为指定的 userId 或为管理员（需要 allowAdmin 为 true）时验证通过
     *
     * @param userId     用户 ID, 为 null 时抛出异常
     * @param allowAdmin 是否验证管理员
     * @return 若 allowAdmin 为 true (允许管理员权限)，则为管理员时返回 userId，非管理员时若与登录用户ID一致则返回 null，否则抛出异常；
     * 若无需验证，则在未登录或登录账号不一致时返回 null
     */
    public Long isUserReachable(Long userId, Boolean allowAdmin) {
        if (userId == null) {
            throw ServiceException.fieldRequired(UserConstant.USER_ID);
        }

        // 获取当前登录账号
        var currentUser = BaseContext.getUserId();

        // 登录账号是否为指定的 userId
        var isCurrent = BaseContext.isCurrent(userId);

        // 无需管理员验证，方法结束
        if (!allowAdmin) {
            // 返回是否与登录账号相同
            return isCurrent ? userId : null;
        }

        // 需要验证，且当前用户为管理员，方法结束
        if (isAdminExist(currentUser)) {
            return userId;
        }

        // 已登录且要查询的ID是当前登录 ID
        if (isCurrent) {
            return null;
        }

        // 未登录或越权查询
        throw ServiceException.requestNotAllowed();
    }

    @Override
    public Boolean isAdminExist(Long userId) {
        return authService.isAdminExist(userId);
    }

    @Override
    public Boolean isUserAdmin(Long userId) {
        return authService.isUserAdmin(userId);
    }

    @Override
    public Boolean isJourneyAdmin(Long userId) {
        return authService.isJourneyAdmin(userId);
    }

    @Override
    public Boolean isSupperAdmin(Long userId) {
        return authService.isJourneyAdmin(userId);
    }

    @Override
    public AdminVo getVoByUserId(Long userId) {
        return getVoByUserId(userId, true);
    }

    @Override
    public AdminVo getVoByUserId(Long userId, Boolean verify) {
        return BeanUtil.copyProperties(getByUserId(userId, verify), AdminVo.class);
    }

    @Override
    public Admin getByUserId(Long userId) {
        return getByUserId(userId, true);
    }

    /**
     * @param userId     用户 ID
     * @param allowAdmin 是否进行权限验证, true - 登录用户为管理员，则返回管理员，登录用户为验证用户，则返回 null
     *                   false - 登录用户为验证用户，查询管理员；登录用户不为验证用户，返回 null
     * @return Admin
     */
    @Override
    public Admin getByUserId(Long userId, Boolean allowAdmin) {
        userId = isUserReachable(userId, allowAdmin);
        return userId == null ? null : lambdaQuery().eq(Admin::getUserId, userId).one();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @NotBlankArgs({"userId", "password"})
    public Boolean delete(Long userId, String password) {
        // 当前登录非超级管理员
        if (!isSupperAdmin(BaseContext.getUserId())) {
            throw ServiceException.requestNotAllowed();
        }
        Admin admin = getByUserId(userId, true);
        if (BeanUtil.isBlank(admin)) {
            // 管理员不存在
            throw new ServiceException(AdminConstant.ADMIN_NOT_EXIST);
        }
        if (!admin.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            // 管理员密码不正确
            throw ServiceException.authorizedFailed();
        }

        return mapper.delete(new QueryWrapper<Admin>().lambda().eq(Admin::getUserId, userId)) > 0 && userService.update(
                new UpdateWrapper<User>().lambda()
                                         .eq(User::getUserId, userId)
                                         .set(User::getIsAdmin, AdminConstant.NOT_ADMIN));
    }

    /**
     * 添加管理员
     *
     * @param dto 管理员添加 DTO
     * @return bool
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean insert(AdminAddDto dto) {
        // 添加管理员失败
        if (mapper.insert(BeanUtil.copyProperties(dto, Admin.class)) != 1) {
            throw new ServiceException(AdminConstant.ADMIN_ADD_FAILED_UNKNOWN);
        }
        // 更新用户身份失败
        if (!userService.updateIsAdmin(dto.getUserId(), AdminConstant.IS_ADMIN)) {
            throw new ServiceException(UserConstant.ACCOUNT_NOT_EXIST);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public AdminVo addWithoutLogin(AdminAddDto dto) {
        // 判断 UserID, password, purview, verifyPassword 是否为空
        List<String> blank = BeanUtil.blankFieldsFrom(dto);
        if (!blank.isEmpty()) {
            throw ServiceException.fieldRequired(String.join(",", blank));
        }
        // 管理员已存在
        if (lambdaQuery().eq(Admin::getUserId, dto.getUserId()).exists()) {
            throw new ServiceException(AdminConstant.ADMIN_HAS_EXIST);
        }

        // 加密验证密码
        dto.setVerifyPassword(DigestUtils.md5DigestAsHex(dto.getVerifyPassword().getBytes()));

        // 密码不匹配
        if (!DigestUtils.md5DigestAsHex(properties.getPassword().getBytes()).equals(dto.getVerifyPassword())) {
            return null;
        }

        // 加密密码
        dto.setPassword(DigestUtils.md5DigestAsHex(dto.getPassword().getBytes()));

        return insert(dto) ?
                // 获取管理员
                BeanUtil.copyProperties(lambdaQuery().eq(Admin::getUserId, dto.getUserId()).one(),
                                        AdminVo.class) : null;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public AdminVo add(AdminAddDto dto) {
        AdminVo adminVo = addWithoutLogin(dto);
        // 特权密码正确
        if (!BeanUtil.isBlank(adminVo)) {
            return adminVo;
        }
        // 特权密码错误
        Long userId = BaseContext.getUserId();
        // 当前未登录 或 非超级管理员身份
        if (!isSupperAdmin(userId)) {
            throw ServiceException.authorizedFailed();
        }

        Admin admin = getByUserId(userId);
        // 管理员密码错误
        if (admin.getPassword().equals(dto.getVerifyPassword())) {
            throw ServiceException.authorizedFailed();
        }
        // 非验证获取管理员信息
        return insert(dto) ? getVoByUserId(dto.getUserId(), false) : null;
    }

    @Override
    public AdminVo update(AdminDto dto) {
        // 鉴权
        if (isSupperAdmin(BaseContext.getUserId())) {
            // 非超级管理员, 不可进行修改
            throw ServiceException.requestNotAllowed();
        }
        // 获取管理员对象
        Long userId = dto.getUserId();
        if (!isAdminExist(userId)) {
            // 管理员不存在
            return null;
        }
        // 加密密码
        String pwd = dto.getPassword();
        dto.setPassword(pwd == null ? null : DigestUtils.md5DigestAsHex(pwd.getBytes()));
        // 根据 dto 更新信息
        // 返回 VO
        return mapper.update(dto) > 0 ? getVoByUserId(userId, false) : null;
    }

    @Override
    @NotBlankArgs({"userEmail", "password"})
    public UserVo recoveryUser(String userEmail, String password) {
        Long loginId = BaseContext.getUserId();
        // 管理员验证管理员密码，否则验证恢复用户密码
        UserVo userVo = isUserAdmin(loginId) ? userService.verifyPassword(loginId,
                                                                          password) : userService.verifyPassword(
                userEmail, password);
        // 密码错误
        if (BeanUtil.isBlank(userVo)) {
            throw ServiceException.authorizedFailed();
        }
        if (!userService.recoveryUser(userEmail)) {
            // 恢复失败 可能是用户未被删除
            throw new ServiceException(AdminConstant.USER_RECOVERY_FAILED);
        }
        return userVo;
    }

    @Override
    @NotBlankArgs({"userId", "password"})
    @Transactional(rollbackFor = RuntimeException.class)
    public UserVo deregisterUser(Long userId, String password) {
        Admin currentAdmin;
        // 非用户管理员
        if ((currentAdmin = getByUserId(BaseContext.getUserId())) == null || !isUserAdmin(currentAdmin.getUserId())) {
            throw ServiceException.requestNotAllowed();
        }
        // 密码错误
        if (!currentAdmin.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            throw ServiceException.authorizedFailed();
        }
        // 注意，先从管理员列表删除，再标记用户注销，否则查询不到
        // 从管理员列表中删除
        if (isAdminExist(userId)) {
            if (!delete(userId, password)) {
                throw new ServiceException(AdminConstant.ADMIN_DEREGISTER_FAILED);
            }
        }
        // 注销用户
        if (userMapper.recoveryById(userId, CommonConstant.DELETED) != 1) {
            // 无该用户
            throw new ServiceException(UserConstant.ACCOUNT_NOT_EXIST);
        }
        return BeanUtil.copyProperties(userMapper.getById(userId), UserVo.class);
    }
}


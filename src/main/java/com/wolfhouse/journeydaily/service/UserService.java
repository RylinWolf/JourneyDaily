package com.wolfhouse.journeydaily.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wolfhouse.journeydaily.pojo.dto.UserDto;
import com.wolfhouse.journeydaily.pojo.entity.User;
import com.wolfhouse.journeydaily.pojo.vo.UserLoginVo;
import com.wolfhouse.journeydaily.pojo.vo.UserVo;

/**
 * @author linexsong
 * @description 针对表【user】的数据库操作Service
 * @createDate 2025-01-23 16:55:28
 */
public interface UserService extends IService<User> {
    /**
     * 用户登录
     *
     * @param email  email
     * @param passwd 密码
     * @return 用户登录视图对象
     */
    UserLoginVo login(String email, String passwd);

    /**
     * 用户注册
     *
     * @param userDto 用户数据传输对象
     * @return 用户登录视图对象
     */
    UserLoginVo register(UserDto userDto);

    /**
     * 通过 Email 修改用户信息
     *
     * @param userDto 用户数据传输对象
     * @return 用户视图对象
     */
    UserVo updateById(UserDto userDto);

    /**
     * 注销用户
     *
     * @param userId
     * @param password 账号密码
     * @return bool
     */
    Boolean deregister(Long userId, String password);

    /**
     * 更新管理员身份
     *
     * @param userId  用户ID
     * @param isAdmin 是否管理员
     * @return bool
     */
    Boolean updateIsAdmin(Long userId, Integer isAdmin);

    /**
     * 恢复用户
     *
     * @param userEmail 用户邮箱
     * @return 用户Vo
     */
    Boolean recoveryUser(String userEmail);

    /**
     * 验证密码
     *
     * @param userEmail 用户邮箱
     * @param password  密码
     * @return 用户 Vo
     */
    UserVo verifyPassword(String userEmail, String password);

    /**
     * 验证密码
     *
     * @param userId   用户 ID
     * @param password 密码
     * @return 用户 Vo
     */
    UserVo verifyPassword(Long userId, String password);

    /**
     * 验证密码（仅未删除用户）
     *
     * @param userEmail 用户邮箱
     * @param password  密码
     * @return 用户 Vo
     */
    UserVo verifyPasswordNotDeleted(String userEmail, String password);

    /**
     * 获取用户信息
     *
     * @return 用户 Vo
     */
    UserVo getCurrentUserInfo();

}

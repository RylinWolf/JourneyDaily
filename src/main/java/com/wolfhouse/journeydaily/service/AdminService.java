package com.wolfhouse.journeydaily.service;

import com.wolfhouse.journeydaily.pojo.dto.AdminAddDto;
import com.wolfhouse.journeydaily.pojo.dto.AdminDto;
import com.wolfhouse.journeydaily.pojo.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wolfhouse.journeydaily.pojo.vo.AdminVo;
import com.wolfhouse.journeydaily.pojo.vo.UserVo;

/**
 * @author linexsong
 * @description 针对表【admin(管理员表)】的数据库操作Service
 * @createDate 2025-02-19 12:42:14
 */
public interface AdminService extends IService<Admin> {
    /**
     * 管理员是否存在
     *
     * @param userId 用户 ID
     * @return bool
     */
    Boolean isAdminExist(Long userId);

    /**
     * 是否为用户管理员
     *
     * @param userId 用户 ID
     * @return bool
     */
    Boolean isUserAdmin(Long userId);

    /**
     * 是否为文章管理员
     *
     * @param userId 用户 ID
     * @return bool
     */
    Boolean isJourneyAdmin(Long userId);

    /**
     * 是否为超级管理员
     *
     * @param userId 用户 ID
     * @return bool
     */
    Boolean isSupperAdmin(Long userId);

    /**
     * 根据用户 ID 获取 VO 对象
     *
     * @param userId 用户 ID
     * @return AdminVo
     */
    AdminVo getVoByUserId(Long userId);

    /**
     * 外部根据用户 ID 获取 VO 对象
     *
     * @param verify 是否进行权限验证
     * @param userId 用户 ID
     * @return AdminVo
     */
    AdminVo getVoByUserId(Long userId, Boolean verify);

    /**
     * 外部根据用户 ID 获取
     *
     * @param verify 是否进行权限验证
     * @param userId 用户 ID
     * @return Admin
     */
    Admin getByUserId(Long userId, Boolean verify);

    /**
     * 外部根据用户 ID 获取
     *
     * @param userId 用户 ID
     * @return Admin
     */
    Admin getByUserId(Long userId);

    /**
     * 添加管理员
     *
     * @param dto 管理员 DTO
     * @return AdminVo
     */
    AdminVo add(AdminAddDto dto);

    /**
     * 非登录状态添加管理员
     *
     * @param dto 管理员 DTO
     * @return AdminVo
     */
    AdminVo addWithoutLogin(AdminAddDto dto);

    /**
     * 删除管理员
     *
     * @param userId   用户 ID
     * @param password 管理员密码
     * @return bool
     */
    Boolean delete(Long userId, String password);

    /**
     * 修改管理员
     *
     * @param dto 管理员Dto
     * @return AdminVo
     */
    AdminVo update(AdminDto dto);

    /**
     * 恢复用户
     *
     * @param userEmail 要恢复的用户邮箱
     * @param password  以管理员身份登录时，为管理员密码，否则为用户密码
     * @return 用户 Vo
     */
    UserVo recoveryUser(String userEmail, String password);

    /**
     * 注销用户
     *
     * @param userId   用户 Id
     * @param password 管理员密码
     * @return 用户 Vo
     */
    UserVo deregisterUser(Long userId, String password);
}

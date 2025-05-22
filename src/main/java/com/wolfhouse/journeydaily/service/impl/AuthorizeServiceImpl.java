package com.wolfhouse.journeydaily.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wolfhouse.journeydaily.common.annotation.NotBlankArgs;
import com.wolfhouse.journeydaily.common.constant.AdminConstant;
import com.wolfhouse.journeydaily.common.enums.AdminPurviewEnum;
import com.wolfhouse.journeydaily.mapper.AdminMapper;
import com.wolfhouse.journeydaily.pojo.entity.Admin;
import com.wolfhouse.journeydaily.service.AuthorizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author linexsong
 */
@Service
@RequiredArgsConstructor
public class AuthorizeServiceImpl implements AuthorizeService {
    private final AdminMapper adminMapper;

    @Override
    @NotBlankArgs
    public Boolean isAdminExist(Long userId) {
        return adminMapper.exists(new QueryWrapper<Admin>().eq(AdminConstant.ADMIN_ID, userId));
    }

    @Override
    @NotBlankArgs
    public Boolean isUserAdmin(Long userId) {
        return adminMapper.exists(new QueryWrapper<Admin>().lambda().eq(Admin::getUserId, userId).and(wrapper -> {
            // 超级管理员
            wrapper.or().eq(Admin::getPurview, AdminPurviewEnum.ALL);
            // 用户管理员
            wrapper.or().eq(Admin::getPurview, AdminPurviewEnum.USER_ONLY);
        }));
    }

    @Override
    @NotBlankArgs
    public Boolean isJourneyAdmin(Long userId) {
        return adminMapper.exists(new QueryWrapper<Admin>().lambda().eq(Admin::getUserId, userId).and(wrapper -> {
            // 超级管理员
            wrapper.or().eq(Admin::getPurview, AdminPurviewEnum.ALL);
            // 日记管理员
            wrapper.or().eq(Admin::getPurview, AdminPurviewEnum.JOURNEY_ONLY);
        }));
    }

    @Override
    @NotBlankArgs
    public Boolean isSuperAdmin(Long userId) {
        return adminMapper.exists(new QueryWrapper<Admin>().lambda().eq(Admin::getUserId, userId).and(wrapper -> {
            wrapper.or().eq(Admin::getPurview, AdminPurviewEnum.ALL);
        }));
    }
}

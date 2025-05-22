package com.wolfhouse.journeydaily.service;

/**
 * @author linexsong
 */
public interface AuthorizeService {
    /**
     * 指定用户是否为管理员
     *
     * @param userId 用户 ID
     * @return 是否为管理员
     */
    Boolean isAdminExist(Long userId);

    /**
     * 指定用户是否为用户管理员
     *
     * @param userId 用户 ID
     * @return 是否为用户管理员
     */
    Boolean isUserAdmin(Long userId);

    /**
     * 指定用户是否为日记管理员
     *
     * @param userId 用户 ID
     * @return 是否为日记管理员
     */
    Boolean isJourneyAdmin(Long userId);

    /**
     * 指定用户是否为超级管理员
     *
     * @param userId 用户 ID
     * @return 是否为超级管理员
     */
    Boolean isSuperAdmin(Long userId);


}

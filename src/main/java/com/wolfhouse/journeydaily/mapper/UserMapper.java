package com.wolfhouse.journeydaily.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wolfhouse.journeydaily.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author linexsong
 * @description 针对表【user】的数据库操作Mapper
 * @createDate 2025-01-23 16:55:28
 * @Entity com.wolfhouse.journeydaily.pojo.entity.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 插入用户，返回生成的主键
     *
     * @param user user
     * @return 主键
     */
    int insertWithGeneratedKeys(User user);

    /**
     * 通过 Email 修改用户信息
     *
     * @param user 用户
     * @return 0:失败;1:成功
     */
    @Override
    int updateById(User user);

    /**
     * 通过 Email 选择用户
     *
     * @param email Email
     * @return 用户
     */
    @Select("select * from user where email = #{email} limit 1")
    User selectByEmail(String email);

    /**
     * 获取指定 Email 用户数量
     *
     * @param email Email
     * @return 0:不存在;1:存在
     */
    @Select("select count(*) from user where email = #{email}")
    int countByEmail(String email);

    /**
     * 获取用户名
     *
     * @param userId 用户 ID
     * @return String 用户名
     */
    @Select("select user_name from user where user_id = #{userId}")
    String getUserName(Long userId);

    /**
     * 恢复用户
     *
     * @param userEmail     用户邮箱
     * @param undeletedCode 删除对应的字段
     * @return int
     */
    @Update("update user set is_delete = #{undeletedCode} where email = #{userEmail}")
    int recovery(String userEmail, Integer undeletedCode);

    /**
     * 恢复用户
     *
     * @param userId        用户Id
     * @param undeletedCode 删除对应的字段
     * @return int
     */
    @Update("update user set is_delete = #{undeletedCode} where user_id = #{userId}")
    int recoveryById(Long userId, Integer undeletedCode);

    /**
     * 通过邮箱获取用户
     *
     * @param userEmail 用户邮箱
     * @return User
     */
    @Select("select * from user where email = #{userEmail}")
    User getByEmail(String userEmail);

    /**
     * 通过 ID 获取用户
     *
     * @param userId 用户Id
     * @return User
     */
    @Select("select * from user where user_id = #{userid}")
    User getById(Long userId);
}

package com.wolfhouse.journeydaily.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wolfhouse.journeydaily.pojo.dto.AdminDto;
import com.wolfhouse.journeydaily.pojo.entity.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author linexsong
 * @description 针对表【admin(管理员表)】的数据库操作Mapper
 * @createDate 2025-02-19 12:42:14
 * @Entity com.wolfhouse.journeydaily.pojo.entity.Admin
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 根据 DTO 更新信息
     *
     * @param dto 管理员Dto
     * @return 更新条数，正常时应为 1
     */
    Integer update(AdminDto dto);

    /**
     * 根据 用户 ID 删除
     *
     * @param userId 用户 ID
     * @return int
     */
    @Delete("delete from admin where user_id = #{userId}")
    Integer deleteByUserId(Long userId);
}





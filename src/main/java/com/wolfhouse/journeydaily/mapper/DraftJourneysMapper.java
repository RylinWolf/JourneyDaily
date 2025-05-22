package com.wolfhouse.journeydaily.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wolfhouse.journeydaily.pojo.entity.DraftJourneys;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author linexsong
 * @description 针对表【draft_journeys(暂存文章)】的数据库操作Mapper
 * @createDate 2025-04-12 16:38:43
 * @Entity com.wolfhouse.journeydaily.pojo.entity.DraftJourneys
 */
@Mapper
public interface DraftJourneysMapper extends BaseMapper<DraftJourneys> {

    /**
     * 获取所有暂存文章的ID列表。
     *
     * @return 包含所有暂存文章ID的列表
     */
    @Select("SELECT journey_id FROM draft_journeys")
    List<Long> getAll();


    /**
     * 根据用户 ID 获取其相关的暂存文章 ID 。
     *
     * @param userId 用户 ID
     * @return 包含该用户相关暂存文章 ID
     */
    @Select("SELECT journey_id FROM draft_journeys WHERE user_id = #{userId}")
    Long getById(Long userId);
}





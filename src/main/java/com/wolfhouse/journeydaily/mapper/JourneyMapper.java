package com.wolfhouse.journeydaily.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.wolfhouse.journeydaily.pojo.entity.Journey;
import com.wolfhouse.journeydaily.pojo.vo.JourneyBriefVo;
import org.apache.ibatis.annotations.*;


/**
 * @author linexsong
 * @description 针对表【journey】的数据库操作Mapper
 * @createDate 2025-01-27 11:37:38
 * @Entity com.wolfhouse.journeydaily.pojo.entity.Journey
 */
@Mapper
public interface JourneyMapper extends BaseMapper<Journey> {
    /**
     * 插入文章并获取生成主键
     *
     * @param journey 文章对象
     */
    @Insert("insert into journey (title, content, summary, author_id, partition_id, `length`) values (#{title}, #{content}, #{summary}, #{authorId}, #{partitionId}, #{length})")
    @Options(
            useGeneratedKeys = true,
            keyProperty = "journeyId")
    void insertWithGeneratedKeys(Journey journey);

    /**
     * 根据 ID 批量获取日记列表
     *
     * @param userId 用户 ID
     * @return 日记列表
     */
    Page<JourneyBriefVo> getBatchUserId(Long userId);


    /**
     * 根据日记 ID 获取作者 ID
     *
     * @param journeyId 日记ID
     * @return long 作者 ID
     */
    @Select("select author_id from journey where journey_id = #{journeyId}")
    Long getAuthorId(Long journeyId);

    /**
     * 根据日记 Id 恢复
     *
     * @param jid  日记 Id
     * @param code 未删除状态对应的编码
     * @return int
     */
    @Update("update journey set is_delete = #{code} where journey_id = #{jid}")
    Integer recovery(Long jid, Integer code);

    /**
     * 根据日记 Id 获取
     *
     * @param jid 日记 Id
     * @return 日记对象
     */
    @Select("select * from journey where journey_id = #{jid}")
    Journey getById(Long jid);
}





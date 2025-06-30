package com.wolfhouse.journeydaily.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wolfhouse.journeydaily.pojo.dto.JourneyPartitionDto;
import com.wolfhouse.journeydaily.pojo.entity.JourneyPartition;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author linexsong
 * @description 针对表【journey_partition(日记分区表)】的数据库操作Mapper
 * @createDate 2025-02-21 17:28:06
 * @Entity com.wolfhouse.journeydaily.pojo.entity.JourneyPartition
 */
@Mapper
public interface JourneyPartitionMapper extends BaseMapper<JourneyPartition> {

    /**
     * 查询分区是否存在
     *
     * @param userId     用户 Id
     * @param partitions 分区 Id
     * @return 分区列表
     */
    List<JourneyPartition> hasPartitions(Long userId, Long[] partitions);

    /**
     * 添加分区，并更新 ID
     *
     * @param jp 日记分区对象
     * @return 日记分区对象
     */
    Integer insertWithGk(JourneyPartition jp);

    /**
     * 查询分区标题是否存在
     *
     * @param userId 用户 Id
     * @param titles 标题
     * @return 存在的标题列表
     */
    List<Long> hasTitles(Long userId, String[] titles);

    /**
     * 根据 dto 更新
     *
     * @param dto 分区 Dto
     * @return int
     */
    Integer updateByDto(JourneyPartitionDto dto);

    /**
     * 根据 Id 删除
     *
     * @param userId 用户 Id
     * @param id     分区 Id
     * @return int
     */
    Integer delete(Long userId, Long id);

    /**
     * 移除父级
     *
     * @param userId 用户 Id
     * @param id     父级 Id
     */
    void changeParent(Long userId, Long id);
}





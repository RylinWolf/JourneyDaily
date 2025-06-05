package com.wolfhouse.journeydaily.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wolfhouse.journeydaily.pojo.dto.JourneyPartitionDto;
import com.wolfhouse.journeydaily.pojo.entity.JourneyPartition;
import com.wolfhouse.journeydaily.pojo.vo.JourneyPartitionVo;

import java.util.List;

/**
 * @author linexsong
 * @description 针对表【journey_partition(日记分区表)】的数据库操作Service
 * @createDate 2025-02-21 17:28:06
 */
public interface JourneyPartitionService extends IService<JourneyPartition> {
    /**
     * 获取所有分区
     *
     * @return 分区 Vo List
     */
    List<JourneyPartitionVo> getPartitions();

    /**
     * 添加分区
     *
     * @param dto 分区 Dto
     * @return 分区 Vo
     */
    JourneyPartitionVo addPartition(JourneyPartitionDto dto);

    /**
     * 查询当前登录用户的指定分区是否存在
     *
     * @param partitions 分区 Id
     * @return 分区列表
     */
    List<JourneyPartition> hasPartitions(Long... partitions);

    /**
     * 更新分区
     *
     * @param dto 分区 Dto
     * @return 分区 Vo
     */
    JourneyPartitionVo updatePartition(JourneyPartitionDto dto);

    /**
     * 删除分区
     *
     * @param partitionId 分区 Id
     * @return bool
     */
    Boolean deletePartition(Long partitionId);

    /**
     * 根据 ID 获取分区
     *
     * @param partitionId 分区 ID
     * @return 分区 VO
     */
    JourneyPartitionVo getPartition(Long partitionId);
}

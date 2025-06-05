package com.wolfhouse.journeydaily.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 日记分区表
 *
 * @author linexsong
 * @TableName journey_partition
 */
@TableName(value = "journey_partition")
@Component
@Data
public class JourneyPartition implements Serializable {
    /**
     * 分区 ID
     */
    @TableId(type = IdType.AUTO)
    private Long partitionId;

    /**
     * 分区所属用户 ID
     */
    private Long userId;

    /**
     * 分区标题
     */
    private String title;

    /**
     * 分区简介
     */
    private String info;

    /**
     * 分区所属上级
     */
    private Long parent;

}
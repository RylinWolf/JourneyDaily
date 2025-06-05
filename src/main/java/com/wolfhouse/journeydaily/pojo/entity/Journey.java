package com.wolfhouse.journeydaily.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author linexsong
 * @TableName journey
 */
@TableName(value = "journey")
@Data
public class Journey implements Serializable {
    /**
     * 日记 ID
     */
    @TableId
    private Long journeyId;

    /**
     * 日记标题
     */
    private String title;

    /**
     * 日记内容
     */
    private String content;

    /**
     * 日记总结
     */
    private String summary;

    /**
     * 作者 ID
     */
    private Long authorId;

    /**
     * 发布日期
     */
    private LocalDateTime postTime;

    /**
     * 编辑日期
     */
    private LocalDateTime editTime;

    /**
     * 可见性
     */
    private Integer visibility;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 日记分区
     */
    private Long partitionId;

    /**
     * 日记字数统计
     */
    private Integer length;
}

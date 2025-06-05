package com.wolfhouse.journeydaily.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 暂存文章
 *
 * @author linexsong
 * @TableName draft_journeys
 */
@TableName(value = "draft_journeys")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DraftJourneys implements Serializable {
    /**
     * 用户 ID
     */
    @TableId
    private Long userId;

    /**
     * 日记 ID
     */
    private Long journeyId;
}

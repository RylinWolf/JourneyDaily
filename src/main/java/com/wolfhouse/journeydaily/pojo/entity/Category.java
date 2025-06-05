package com.wolfhouse.journeydaily.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author linexsong
 * @TableName category
 */
@TableName(value = "category")
@Data
public class Category implements Serializable {
    /**
     * 类型 ID
     */
    private Long categoryId;

    /**
     * 所属用户 ID
     */
    private Long userId;

    /**
     * 分类标题
     */
    private String title;

    /**
     * 分类简介
     */
    private String info;

    /**
     * 分类所属上级
     */
    private Long parent;
}
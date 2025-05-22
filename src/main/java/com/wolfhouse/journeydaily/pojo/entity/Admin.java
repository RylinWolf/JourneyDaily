package com.wolfhouse.journeydaily.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wolfhouse.journeydaily.common.enums.AdminPurviewEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员表
 *
 * @author linexsong
 * @TableName admin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "admin")
public class Admin {
    /**
     * 管理员 ID
     */
    @TableId(type = IdType.AUTO)
    private Long adminId;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 权限
     */
    private AdminPurviewEnum purview;

    /**
     * 密码
     */
    private String password;
}
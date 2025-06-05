package com.wolfhouse.journeydaily.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author linexsong
 * @TableName user
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    /**
     * 用户ID
     */
    @NotNull(message = "[用户ID]不能为空")
    @TableId
    @ApiModelProperty("用户 ID")
    private Long userId;

    /**
     * 用户名
     */
    @Size(max = 255, message = "编码长度不能超过255")
    @ApiModelProperty("用户名")
    @NotNull
    private String userName;
    /**
     * 邮箱地址
     */
    @Size(max = 255, message = "编码长度不能超过255")
    @ApiModelProperty("邮箱地址")
    @NotNull
    private String email;
    /**
     * 密码哈希
     */
    @NotBlank(message = "[密码哈希]不能为空")
    @Size(max = 255, message = "编码长度不能超过255")
    @ApiModelProperty("密码哈希")
    @JsonProperty("password")
    @NotNull
    private String pwdHash;
    /**
     * 个性签名
     */
    @Size(max = 255, message = "编码长度不能超过255")
    @ApiModelProperty("个性签名")
    private String tagline;
    /**
     * 逻辑删除
     */
    @ApiModelProperty("逻辑删除")
    @TableLogic
    private Integer isDelete;

    /**
     * 是否管理员
     */
    @ApiModelProperty("是否管理员")
    private Integer isAdmin;

}

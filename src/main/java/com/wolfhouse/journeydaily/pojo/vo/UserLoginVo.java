package com.wolfhouse.journeydaily.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author linexsong
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginVo implements Serializable {
    private String userName;
    private String email;
    private Long userId;
    private String tagline;
    private String token;
    private Integer isAdmin;

}

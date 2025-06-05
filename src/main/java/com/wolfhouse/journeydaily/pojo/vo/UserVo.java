package com.wolfhouse.journeydaily.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author linexsong
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo implements Serializable {
    private String userName;
    private String email;
    private Long userId;
    private String tagline;
    private Integer isAdmin;

}

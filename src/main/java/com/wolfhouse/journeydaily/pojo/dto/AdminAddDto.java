package com.wolfhouse.journeydaily.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author linexsong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminAddDto implements Serializable {
    private Long userId;
    private Integer purview;
    private String password;
    private String verifyPassword;
}

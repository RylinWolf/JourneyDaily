package com.wolfhouse.journeydaily.pojo.dto;

import lombok.Data;

/**
 * @author linexsong
 */
@Data
public class AdminDto {
    private Long userId;
    private Integer purview;
    private String password;
}

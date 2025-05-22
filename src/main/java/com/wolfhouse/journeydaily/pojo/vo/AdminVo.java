package com.wolfhouse.journeydaily.pojo.vo;

import com.wolfhouse.journeydaily.common.enums.AdminPurviewEnum;
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
public class AdminVo implements Serializable {
    private Long adminId;
    private AdminPurviewEnum purview;
}

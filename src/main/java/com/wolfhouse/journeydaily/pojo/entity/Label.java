package com.wolfhouse.journeydaily.pojo.entity;

import com.wolfhouse.journeydaily.common.constant.LabelConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author linexsong
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Label implements Serializable {
    protected Long labelId;
    @NotBlank(message = LabelConstant.LABEL_NAME_REQUIRED)
    protected String name;
}

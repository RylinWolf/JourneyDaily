package com.wolfhouse.journeydaily.pojo.dto;

import com.wolfhouse.journeydaily.pojo.entity.Label;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

/**
 * @author linexsong
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class LabelQueryDto extends Label {
    private Long userId;
    private Long journeyId;
}

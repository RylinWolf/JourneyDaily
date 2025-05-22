package com.wolfhouse.journeydaily.pojo.dto;

import com.wolfhouse.journeydaily.pojo.entity.Label;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author linexsong
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
public class LabelEditDto extends Label implements Serializable {
    private Long journeyId;
}

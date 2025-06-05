package com.wolfhouse.journeydaily.pojo.vo;

import com.wolfhouse.journeydaily.pojo.entity.Label;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author linexsong
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class LabelVo extends Label implements Serializable {
    private Long userId;
    private Long journeyId;
}

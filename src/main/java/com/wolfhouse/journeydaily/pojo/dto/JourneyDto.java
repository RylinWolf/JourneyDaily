package com.wolfhouse.journeydaily.pojo.dto;

import io.swagger.annotations.ApiModel;
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
@ApiModel("日记 Dto 类")
public class JourneyDto implements Serializable {
    private Long journeyId;
    private String title;
    private String content;
    private String summary;
    private Long partitionId;
    private Integer visibility;
    private Integer length;
}

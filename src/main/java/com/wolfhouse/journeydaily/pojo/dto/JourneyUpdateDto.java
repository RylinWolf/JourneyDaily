package com.wolfhouse.journeydaily.pojo.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author linexsong
 */
@Data
@Component
@ApiModel("日记更新 Dto")
@AllArgsConstructor
@NoArgsConstructor
public class JourneyUpdateDto implements Serializable {
    private Long journeyId;
    private JsonNullable<String> title = JsonNullable.undefined();
    private JsonNullable<String> content = JsonNullable.undefined();
    private JsonNullable<String> summary = JsonNullable.undefined();
    private JsonNullable<Long> partitionId = JsonNullable.undefined();
    private JsonNullable<Integer> visibility = JsonNullable.undefined();
    private String partition;
    private Integer length;
}

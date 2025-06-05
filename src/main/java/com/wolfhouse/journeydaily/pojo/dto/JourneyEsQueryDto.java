package com.wolfhouse.journeydaily.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author linexsong
 */
@EqualsAndHashCode(callSuper = true)
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JourneyEsQueryDto extends JourneyQueryDto implements Serializable {
    protected Long authorId;
    protected Long journeyId;
    protected Long partitionId;
    protected String title;
    protected Integer visibility;
    protected String content;
    protected JourneyEsQueryOrderDto order;
    protected String[] includes;
    protected String[] excludes;
}

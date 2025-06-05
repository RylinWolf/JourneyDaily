package com.wolfhouse.journeydaily.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author linexsong
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JourneyVo implements Serializable {
    private Long journeyId;
    private Long authorId;
    private String author;
    private String title;
    private String content;
    private String summary;
    private LocalDateTime postTime;
    private LocalDateTime editTime;
    private Integer visibility;
    private Long partitionId;
    private String partition;
    private Integer length;

}

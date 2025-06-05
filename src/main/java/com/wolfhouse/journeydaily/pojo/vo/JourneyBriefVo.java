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
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class JourneyBriefVo implements Serializable {
    private Long journeyId;
    private Long authorId;
    private String author;
    private String title;
    private String summary;
    private LocalDateTime postTime;
    private Integer visibility;
}

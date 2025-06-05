package com.wolfhouse.journeydaily.pojo.doc;

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
public class JourneyDoc implements Serializable {
    private String journeyId;
    private String title;
    private Long authorId;
    private String author;
    private String content;
    private String summary;
    private LocalDateTime postTime;
    private LocalDateTime editTime;
    private Integer visibility;
    private Long partitionId;
    private String partition;
    private Integer length;
}

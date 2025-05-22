package com.wolfhouse.journeydaily.pojo.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author linexsong
 */
@Component
@Data
public class JourneyPartitionBriefVo implements Serializable {
    private Long partitionId;
    private Long parent;
    private String title;
}

package com.wolfhouse.journeydaily.pojo.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author linexsong
 */
@Component
@Data
public class JourneyPartitionDto implements Serializable {
    private Long partitionId;
    private Long parent;
    private String title;
    private String info;
}

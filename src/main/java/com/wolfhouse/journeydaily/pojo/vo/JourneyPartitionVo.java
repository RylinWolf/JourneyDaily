package com.wolfhouse.journeydaily.pojo.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author linexsong
 */
@Component
@Data
public class JourneyPartitionVo implements Serializable {
    private Long partitionId;
    private String title;
    private String info;
    private Long parent;
}

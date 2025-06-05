package com.wolfhouse.journeydaily.pojo.dto;

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
public class JourneyEsQueryOrderDto implements Serializable {
    private Orders[] orders;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Orders {
        String orderBy;
        Boolean isDesc = false;
    }

}

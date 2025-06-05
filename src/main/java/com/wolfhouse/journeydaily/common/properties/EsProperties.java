package com.wolfhouse.journeydaily.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author linexsong
 */
@Component
@Data
@ConfigurationProperties(prefix = "jd.es")
public class EsProperties {
    private String host;
    private Integer port;
}

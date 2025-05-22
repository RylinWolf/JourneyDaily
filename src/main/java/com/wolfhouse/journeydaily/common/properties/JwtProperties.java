package com.wolfhouse.journeydaily.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author linexsong
 */
@Data
@Component
@ConfigurationProperties(prefix = "jd.jwt.user")
public class JwtProperties {
    private String secret;
    private Long ttl;
    private Long refreshTtl;
}

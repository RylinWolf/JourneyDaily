package com.wolfhouse.journeydaily.config;

import com.wolfhouse.journeydaily.common.constant.EsConstant;
import com.wolfhouse.journeydaily.common.properties.EsProperties;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author linexsong
 */
@Configuration
public class EsConfig {
    @Bean(EsConstant.JOURNEY_CLIENT_BEAN)
    public RestHighLevelClient restHighLevelClient(EsProperties properties) {
        return new RestHighLevelClient(
                RestClient.builder(HttpHost.create("http://" + properties.getHost() + ":" + properties.getPort())));
    }
}

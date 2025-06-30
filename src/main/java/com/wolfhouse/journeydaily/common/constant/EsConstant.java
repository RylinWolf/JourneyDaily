package com.wolfhouse.journeydaily.common.constant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author linexsong
 */
@Slf4j
public class EsConstant {
    /** 日记 ES 客户端 Bean 名称 */
    public static final String JOURNEY_CLIENT_BEAN = "journeyEsClient";
    /** 日记索引库相关配置 */
    public static final String JOURNEY_INDEX_MAPPING_FILE = "/es/esJourneyMapping.json";
    public static final String JOURNEY_INDEX_NAME = "journey";
    public static final String JOURNEY_INDEX_STRUCTURE;

    // 初始化索引库映射常量
    static {
        log.info("初始化 ES 常量配置项...");
        log.info("初始化 ES 索引库映射...");
        ClassPathResource res = new ClassPathResource(JOURNEY_INDEX_MAPPING_FILE);
        if (!res.exists()) {
            log.error("未找到 ES Mapping 配置文件");
            JOURNEY_INDEX_STRUCTURE = "";
        } else {
            log.info("加载资源: {}", res.getPath());
            // 文件内容
            StringBuilder json = new StringBuilder();
            try {
                // 创建 Reader，读取文件
                BufferedReader reader = new BufferedReader(new InputStreamReader(res.getInputStream(),
                                                                                 StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line)
                        // 保留换行符
                        .append(System.lineSeparator());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // 设置常量为文件内容
            JOURNEY_INDEX_STRUCTURE = json.toString();
        }
    }
}

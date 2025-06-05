package com.wolfhouse.journeydaily.es.client;

import com.wolfhouse.journeydaily.common.constant.EsConstant;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static com.wolfhouse.journeydaily.common.constant.EsConstant.JOURNEY_CLIENT_BEAN;

/**
 * @author linexsong
 */
@Slf4j
@Component
public class JourneyEsIndexClient {
    private final RestHighLevelClient client;

    public JourneyEsIndexClient(@Qualifier(JOURNEY_CLIENT_BEAN) RestHighLevelClient client) {
        this.client = client;
    }

    /**
     * 根据 Mapping 映射创建索引库
     *
     * @param indexName 索引库名
     * @param mapping   mapping 映射
     * @throws IOException -
     */
    public void createIndexByMapping(String indexName, String mapping) throws IOException {
        // 初始化 Request 对象
        CreateIndexRequest req = new CreateIndexRequest(indexName);
        // 设置索引库映射结构
        req.source(mapping, XContentType.JSON);
        // 调用客户端
        client.indices().create(req, RequestOptions.DEFAULT);
    }

    /**
     * 根据索引库名删除索引库
     *
     * @param indexName 要删除的索引库名称
     * @throws IOException -
     */
    public void deleteIndex(String indexName) throws IOException {
        DeleteIndexRequest req = new DeleteIndexRequest(indexName);
        client.indices().delete(req, RequestOptions.DEFAULT);
    }

    /**
     * 获取指定索引库
     *
     * @param indexName 要获取的索引库名称
     * @throws IOException -
     */
    public Map<String, Object> getIndex(String indexName) throws IOException {
        GetIndexRequest req = new GetIndexRequest(indexName);
        if (!isIndexExist(indexName)) {
            log.error("索引库: {} 不存在！", indexName);
            return Map.of();
        }
        GetIndexResponse resp = client.indices().get(req, RequestOptions.DEFAULT);
        return resp.getMappings().get(EsConstant.JOURNEY_INDEX_NAME).getSourceAsMap();
    }

    /**
     * 获取指定索引库的映射结构
     *
     * @param indexName 指定索引库名
     * @return 映射名列表
     * @throws IOException -
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getMappingPropertiesOf(String indexName) throws IOException {
        return (Map<String, String>) getMappingOf(indexName).get("properties");
    }

    public Map<String, Object> getMappingOf(String indexName) throws IOException {
        GetIndexRequest req = new GetIndexRequest(indexName);
        return client.indices().get(req, RequestOptions.DEFAULT).getMappings().get(indexName).getSourceAsMap();
    }

    /**
     * 判断索引库是否存在
     *
     * @param indexName 索引库名
     * @return 索引库是否存在
     * @throws IOException -
     */
    public Boolean isIndexExist(String indexName) throws IOException {
        GetIndexRequest req = new GetIndexRequest(indexName);
        return client.indices().exists(req, RequestOptions.DEFAULT);
    }
}

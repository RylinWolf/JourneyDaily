package com.wolfhouse.journeydaily.es.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wolfhouse.journeydaily.common.constant.EsConstant;
import com.wolfhouse.journeydaily.common.util.BeanUtil;
import com.wolfhouse.journeydaily.context.BaseContext;
import com.wolfhouse.journeydaily.es.builder.JourneyQueryBuilder;
import com.wolfhouse.journeydaily.pojo.doc.JourneyDoc;
import com.wolfhouse.journeydaily.pojo.dto.JourneyEsQueryDto;
import com.wolfhouse.journeydaily.pojo.dto.JourneyEsQueryOrderDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author linexsong
 */
@Component
@Slf4j
public class JourneyEsDocClient {
    private final RestHighLevelClient client;
    private final JourneyEsIndexClient indexClient;
    private final ObjectMapper jacksonObjectMapper;
    private final ObjectMapper snakeCaseObjectMapper;


    public JourneyEsDocClient(@Qualifier(EsConstant.JOURNEY_CLIENT_BEAN) RestHighLevelClient client,
                              JourneyEsIndexClient indexClient,
                              ObjectMapper jacksonObjectMapper,
                              @Qualifier("jacksonSnakeCaseObjectMapper") ObjectMapper snakeCaseObjectMapper) {
        this.client = client;
        this.indexClient = indexClient;
        this.jacksonObjectMapper = jacksonObjectMapper;
        this.snakeCaseObjectMapper = snakeCaseObjectMapper;
    }

    /**
     * 索引库检查
     *
     * @throws IOException -
     */
    @PostConstruct
    public void indexInit() throws IOException {
        if (!indexClient.isIndexExist(EsConstant.JOURNEY_INDEX_NAME)) {
            log.info("索引库不存在！正在尝试初始化...");
            indexClient.createIndexByMapping(EsConstant.JOURNEY_INDEX_NAME, EsConstant.JOURNEY_INDEX_STRUCTURE);
        }
        log.info("索引库 [{}] 读取成功", EsConstant.JOURNEY_INDEX_NAME);
    }


    /**
     * 根据指定 ID 获取日记文档
     *
     * @param id 指定文档 ID
     * @return 日记文档
     * @throws IOException -
     */
    public JourneyDoc getJourneyDoc(String id) throws IOException {
        GetRequest req = new GetRequest(EsConstant.JOURNEY_INDEX_NAME).id(id);
        GetResponse resp = client.get(req, RequestOptions.DEFAULT);
        String source = resp.getSourceAsString();
        if (BeanUtil.isBlank(source)) {
            return null;
        }
        return snakeCaseObjectMapper.readValue(source, JourneyDoc.class);
    }

    /**
     * 获取指定索引开始的指定数量个日记文档，注意，由于 ES 的限制，总长度不得大于 10000，即 from + size < 10000
     *
     * @param from 开始索引
     * @param size 获取数量
     * @return 日记文档列表
     * @throws IOException -
     */
    public List<JourneyDoc> getJourneyDocs(Integer from, Integer size) throws IOException {
        List<JourneyDoc> list = new ArrayList<>();
        // 构建查询条件
        SearchRequest req = new SearchRequest(EsConstant.JOURNEY_INDEX_NAME);
        req.source().from(from).size(size);
        SearchResponse res = client.search(req, RequestOptions.DEFAULT);
        // 获取命中结果
        SearchHit[] hits = res.getHits().getHits();
        for (SearchHit hit : hits) {
            list.add(snakeCaseObjectMapper.readValue(hit.getSourceAsString(), JourneyDoc.class));
        }

        return list;
    }

    @Data
    public static class SearchResult {
        Long total;
        List<JourneyDoc> docs;
    }

    /**
     * 添加文档
     *
     * @param doc 要添加的文档
     * @throws IOException -
     */
    public void postJourneyDoc(JourneyDoc doc) throws IOException {
        IndexRequest req = new IndexRequest(EsConstant.JOURNEY_INDEX_NAME).id(doc.getJourneyId());
        req.source(snakeCaseObjectMapper.writeValueAsString(doc), XContentType.JSON);
        client.index(req, RequestOptions.DEFAULT);
    }

    /**
     * 批量添加日记文档
     *
     * @param docs 日记文档列表
     * @throws IOException -
     */
    public void bulkPostJourneyDocs(List<JourneyDoc> docs) throws IOException {
        log.info("添加文档... 共 {} 条", docs.size());
        BulkRequest req = new BulkRequest();
        docs.forEach(doc -> {
            try {
                req.add(new IndexRequest(EsConstant.JOURNEY_INDEX_NAME).id(doc.getJourneyId())
                                                                       .source(
                                                                               snakeCaseObjectMapper.writeValueAsString(doc),
                                                                               XContentType.JSON));
            } catch (JsonProcessingException e) {
                // JSON 处理异常
                throw new RuntimeException(e);
            }
        });
        client.bulk(req, RequestOptions.DEFAULT);
        log.info("文档添加完成");
    }

    /**
     * 根据文档 ID 删除文档
     *
     * @param id 文档 ID
     * @throws IOException -
     */
    public void deleteJourneyDoc(String id) throws IOException {
        DeleteRequest req = new DeleteRequest(EsConstant.JOURNEY_INDEX_NAME).id(id);
        client.delete(req, RequestOptions.DEFAULT);
    }

    /**
     * 更新指定文档的指定字段
     *
     * @param id        文档 ID
     * @param updateMap 更新字段字典
     * @throws IOException -
     */
    public void updateJourneyDoc(String id, Map<String, Object> updateMap) throws IOException {
        UpdateRequest req = new UpdateRequest(EsConstant.JOURNEY_INDEX_NAME, id);
        req.doc(updateMap);
        client.update(req, RequestOptions.DEFAULT);
    }

    /**
     * 根据日记文档查询 DTO 进行复合查询，可指定包含和排除字段
     *
     * @param dto      日记 ES 查询 DTO
     * @param orders   是否排序
     * @param includes 包含字段数组
     * @param excludes 排除字段数组
     * @return 查询结果
     * @throws NoSuchFieldException   -
     * @throws IllegalAccessException -
     * @throws IOException            -
     */
    public SearchResult searchJourneyDocs(JourneyEsQueryDto dto, Boolean orders, String[] includes, String[] excludes)
            throws IOException, NoSuchFieldException, IllegalAccessException {
        // 创建查询请求
        SearchRequest req = new SearchRequest(EsConstant.JOURNEY_INDEX_NAME);
        // 构建复合查询条件
        BoolQueryBuilder boolQuery = JourneyQueryBuilder.buildBoolQuery(dto, BaseContext.getUserId());
        List<JourneyDoc> docs = new ArrayList<>();
        // 查询
        req.source().fetchSource(includes, excludes).query(boolQuery);

        // 构建排序条件
        if (orders) {
            for (JourneyEsQueryOrderDto.Orders order : dto.getOrder().getOrders()) {
                req.source().sort(order.getOrderBy(), order.getIsDesc() ? SortOrder.DESC : SortOrder.ASC);
            }
        }

        // 构建分页条件
        int pageSize, pageNo;
        if ((pageSize = dto.getPageSize()) != 0 && (pageNo = dto.getPageNo()) != 0) {
            req.source().size(pageSize).from((pageNo - 1) * pageSize);
        }

        // 执行查询
        SearchResponse res = client.search(req, RequestOptions.DEFAULT);
        SearchHits searchHits = res.getHits();
        TotalHits totalHits = searchHits.getTotalHits();

        // 无命中结果
        if (totalHits == null) {
            return null;
        }

        // 处理结果
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            docs.add(snakeCaseObjectMapper.readValue(hit.getSourceAsString(), JourneyDoc.class));
        }

        // 封装结果
        SearchResult searchResult = new SearchResult();
        searchResult.setDocs(docs);
        searchResult.setTotal(totalHits.value);
        return searchResult;
    }

    /**
     * 根据日记文档查询 DTO 进行复合查询
     *
     * @param dto 日记 ES 查询 DTO
     * @return 查询结果
     * @throws NoSuchFieldException   -
     * @throws IllegalAccessException -
     * @throws IOException            -
     */
    public SearchResult searchJourneyDocs(JourneyEsQueryDto dto)
            throws NoSuchFieldException, IllegalAccessException, IOException {
        return searchJourneyDocs(dto, true, null, null);
    }
}

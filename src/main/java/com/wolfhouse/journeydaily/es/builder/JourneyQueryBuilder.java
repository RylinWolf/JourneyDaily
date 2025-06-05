package com.wolfhouse.journeydaily.es.builder;

import com.wolfhouse.journeydaily.common.constant.JourneyConstant;
import com.wolfhouse.journeydaily.common.util.BeanUtil;
import com.wolfhouse.journeydaily.pojo.dto.JourneyEsQueryDto;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author linexsong
 */
public class JourneyQueryBuilder {
    public static BoolQueryBuilder buildBoolQuery(JourneyEsQueryDto dto)
            throws NoSuchFieldException, IllegalAccessException {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 构建查询条件
        Map<String, Consumer<Object>> queryMapping = Map.ofEntries(
                Map.entry(
                        JourneyConstant.JOURNEY_ID,
                        value -> boolQuery.filter(QueryBuilders.termQuery(JourneyConstant.JOURNEY_ID_DB, value))),
                Map.entry(
                        JourneyConstant.AUTHOR_ID,
                        value -> boolQuery.filter(QueryBuilders.termQuery(JourneyConstant.AUTHOR_ID_DB, value))),
                Map.entry(
                        JourneyConstant.CONTENT,
                        value -> boolQuery.must(QueryBuilders.matchQuery(JourneyConstant.CONTENT, value))),
                Map.entry(
                        JourneyConstant.TITLE,
                        value -> boolQuery.must(QueryBuilders.matchQuery(JourneyConstant.TITLE, value))),
                Map.entry(
                        JourneyConstant.PARTITION_ID,
                        value -> boolQuery.filter(QueryBuilders.termQuery(JourneyConstant.PARTITION_ID_DB, value))));

        // 构建查询体
        for (String k : queryMapping.keySet()) {
            Field field = dto.getClass().getDeclaredField(k);
            field.setAccessible(true);
            // 获取字段值并执行 boolQuery 方法
            Object fieldBody = field.get(dto);
            if (!BeanUtil.isBlank(fieldBody)) {
                queryMapping.get(k).accept(fieldBody);
            }
        }

        return boolQuery;
    }

}

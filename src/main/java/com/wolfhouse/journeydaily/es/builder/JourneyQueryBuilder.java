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
    public static BoolQueryBuilder buildBoolQuery(JourneyEsQueryDto dto, Long loginUser)
            throws NoSuchFieldException, IllegalAccessException {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 仅包含公开及当前登录用户的私人日记
        BoolQueryBuilder visibilityQuery = QueryBuilders.boolQuery();
        // 查询公开日记
        boolQuery.should(QueryBuilders.termQuery(JourneyConstant.VISITABLE, JourneyConstant.VISIBILITY_PUBLIC));

        // 若登录，则构造查询登录用户的私人日记条件
        if (!BeanUtil.isBlank(loginUser)) {
            visibilityQuery.filter(QueryBuilders.termQuery(JourneyConstant.AUTHOR_ID_DB, loginUser));
            visibilityQuery.filter(QueryBuilders.termQuery(JourneyConstant.VISITABLE, JourneyConstant.VISIBILITY_PRIVATE));
            boolQuery.should(visibilityQuery);
        }
        // 构建查询条件
        Map<String, Consumer<Object>> queryMapping = Map.ofEntries(
                Map.entry(
                        JourneyConstant.JOURNEY_ID,
                        value -> boolQuery.filter(QueryBuilders.termQuery(JourneyConstant.JOURNEY_ID_DB, value))),
                Map.entry(JourneyConstant.AUTHOR_ID,
                          value -> boolQuery.filter(QueryBuilders.termQuery(JourneyConstant.AUTHOR_ID_DB, value))),
                Map.entry(JourneyConstant.CONTENT,
                          value -> boolQuery.must(QueryBuilders.matchQuery(JourneyConstant.CONTENT, value))),
                Map.entry(JourneyConstant.TITLE, value -> boolQuery.must(QueryBuilders.matchQuery(JourneyConstant.TITLE, value))),
                Map.entry(
                        JourneyConstant.PARTITION_ID, value -> {
                            // 按照分区查询时，需要保证作者为当前登录用户
                            boolQuery.filter(QueryBuilders.termQuery(JourneyConstant.AUTHOR_ID_DB, loginUser));
                            boolQuery.filter(QueryBuilders.termQuery(JourneyConstant.PARTITION_ID_DB, value));
                        }));

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

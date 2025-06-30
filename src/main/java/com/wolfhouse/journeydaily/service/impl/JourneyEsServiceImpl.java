package com.wolfhouse.journeydaily.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.wolfhouse.journeydaily.common.annotation.NotBlankArgs;
import com.wolfhouse.journeydaily.common.constant.AdminConstant;
import com.wolfhouse.journeydaily.common.constant.EsConstant;
import com.wolfhouse.journeydaily.common.constant.JourneyConstant;
import com.wolfhouse.journeydaily.common.exceptions.ServiceException;
import com.wolfhouse.journeydaily.common.util.BeanUtil;
import com.wolfhouse.journeydaily.context.BaseContext;
import com.wolfhouse.journeydaily.es.client.JourneyEsDocClient;
import com.wolfhouse.journeydaily.es.client.JourneyEsIndexClient;
import com.wolfhouse.journeydaily.mapper.JourneyMapper;
import com.wolfhouse.journeydaily.pojo.doc.JourneyDoc;
import com.wolfhouse.journeydaily.pojo.dto.JourneyEsQueryDto;
import com.wolfhouse.journeydaily.pojo.dto.JourneyEsQueryOrderDto;
import com.wolfhouse.journeydaily.pojo.dto.JourneyUpdateDto;
import com.wolfhouse.journeydaily.pojo.entity.Journey;
import com.wolfhouse.journeydaily.pojo.vo.JourneyVo;
import com.wolfhouse.journeydaily.service.JourneyEsService;
import com.wolfhouse.journeydaily.service.JourneyService;
import com.wolfhouse.pagehelper.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author linexsong
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class JourneyEsServiceImpl implements JourneyEsService {
    private final JourneyEsDocClient docClient;
    private final JourneyEsIndexClient indexClient;
    private final JourneyMapper journeyMapper;
    private final JourneyService journeyService;

    @Override
    public List<JourneyDoc> getJourneyDocs(Integer from, Integer size) throws IOException {
        return docClient.getJourneyDocs(from, size);
    }

    @Override
    public List<JourneyDoc> getJourneyDocs() throws IOException {
        return getJourneyDocs(null, null);
    }

    @Override
    @NotBlankArgs
    public JourneyDoc getJourneyDocById(String journeyId) throws IOException {
        return docClient.getJourneyDoc(journeyId);
    }

    @Override
    @NotBlankArgs
    public PageResult<JourneyDoc> searchJourneyDocs(JourneyEsQueryDto dto)
            throws IOException, NoSuchFieldException, IllegalAccessException {
        // 获取排序字典
        Map<String, Boolean> orderMap;
        JourneyEsQueryOrderDto order = dto.getOrder();
        boolean isOrder = order != null;
        if (isOrder) {
            orderMap = Arrays.stream(order.getOrders())
                             .collect(Collectors.toMap(JourneyEsQueryOrderDto.Orders::getOrderBy,
                                                       JourneyEsQueryOrderDto.Orders::getIsDesc));
            // 获取索引库映射字段，检查排序字段是否非法
            Map<String, String> indexProperties = indexClient.getMappingPropertiesOf(EsConstant.JOURNEY_INDEX_NAME);
            if (!orderMap.keySet().stream().allMatch(indexProperties::containsKey)) {
                throw new ServiceException(JourneyConstant.ORDER_FIELD_NOT_ALLOWED + orderMap.keySet());
            }
        }

        // 调用客户端方法, 执行查询
        JourneyEsDocClient.SearchResult result = docClient.searchJourneyDocs(dto,
                                                                             isOrder,
                                                                             dto.getIncludes(),
                                                                             dto.getExcludes());
        // 封装分页结果
        return PageResult.<JourneyDoc>builder()
                         .total(result.getTotal())
                         .list(result.getDocs())
                         .pages((long) Math.ceil(result.getTotal() * 1.0 / dto.getPageSize()))
                         .build();
    }

    @Override
    @NotBlankArgs
    public Boolean postJourneyDoc(JourneyDoc journeyDoc) throws IOException {
        docClient.postJourneyDoc(journeyDoc);
        return true;
    }

    @Override
    public Boolean postJourneyDocs(List<JourneyDoc> journeyDocs) throws IOException {
        docClient.bulkPostJourneyDocs(journeyDocs);
        return true;
    }

    @Override
    @NotBlankArgs
    public Boolean deleteJourneyById(String journeyId) throws IOException {
        docClient.deleteJourneyDoc(journeyId);
        return true;
    }

    @Override
    @NotBlankArgs
    public Boolean updateJourney(Long id, JourneyUpdateDto dto) throws IOException {
        Field[] fields = dto.getClass().getDeclaredFields();

        PropertyNamingStrategies.SnakeCaseStrategy strategy = new PropertyNamingStrategies.SnakeCaseStrategy();
        Map<String, Object> updateMap = new HashMap<>(fields.length);

        // 根据 updateDto 构建增量更新 Map
        Arrays.stream(fields).forEach(field -> {
            field.setAccessible(true);
            // 转换为蛇形格式
            String fName = strategy.translate(field.getName());

            try {
                Object o = field.get(dto);
                // JsonNullable 字段 若存在内容则添加入更新字典
                if (o instanceof JsonNullable<?> nullableField) {
                    nullableField.ifPresent(f -> {
                        // 标题或内容不得为空
                        if (List.of(JourneyConstant.TITLE, JourneyConstant.CONTENT).contains(fName) &&
                            BeanUtil.isBlank(nullableField.get())) {
                            return;
                        }
                        updateMap.put(fName, nullableField.get());
                    });
                    return;
                }
                // 其他字段均更新
                updateMap.put(fName, o);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        // 执行更新
        docClient.updateJourneyDoc(String.valueOf(id), updateMap);
        return true;
    }

    @Override
    public Boolean fullySyncDb() throws IOException {
        // 分批查询数据库数据 先查询日记 ID，再获取 VO，将 VO 转为 Doc 后保存入 ES

        // 设置权限
        BaseContext.setUserId(AdminConstant.DEFAULT_ADMIN_ID);

        // 构建分页条件
        int pageSize = 100;
        Page<Journey> page = new Page<>(1, pageSize);
        // 查询条件
        QueryWrapper<Journey> wrapper = new QueryWrapper<>();
        wrapper.select(JourneyConstant.JOURNEY_ID_DB);

        // 日记列表
        List<Journey> jList;

        try {
            // 循环获取日记 ID
            while (!(jList = journeyMapper.selectList(page, wrapper)).isEmpty()) {
                // 更新分页条件
                page.setCurrent(page.getCurrent() + 1);

                // 获取 VO 列表
                List<JourneyVo> jVoList = jList.stream()
                                               .map(j -> journeyService.getJourneyVoById(j.getJourneyId()))
                                               .collect(Collectors.toList());
                // 提交至 ES
                docClient.bulkPostJourneyDocs(BeanUtil.copyList(jVoList, JourneyDoc.class));
            }
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            // 移除权限
            BaseContext.removeUserId();
        }
        return true;
    }
}

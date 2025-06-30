package com.wolfhouse.journeydaily.mq.listener;

import com.wolfhouse.journeydaily.common.constant.UserConstant;
import com.wolfhouse.journeydaily.common.constant.mq.JourneyMqConstant;
import com.wolfhouse.journeydaily.common.constant.mq.MqConstant;
import com.wolfhouse.journeydaily.common.util.BeanUtil;
import com.wolfhouse.journeydaily.context.BaseContext;
import com.wolfhouse.journeydaily.pojo.doc.JourneyDoc;
import com.wolfhouse.journeydaily.pojo.dto.JourneyDto;
import com.wolfhouse.journeydaily.pojo.dto.JourneyUpdateDto;
import com.wolfhouse.journeydaily.pojo.dto.MqDto;
import com.wolfhouse.journeydaily.pojo.vo.JourneyVo;
import com.wolfhouse.journeydaily.service.AiService;
import com.wolfhouse.journeydaily.service.JourneyEsService;
import com.wolfhouse.journeydaily.service.JourneyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author linexsong
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JourneyListener {
    private final JourneyService service;
    private final AiService aiService;
    private final JourneyEsService esService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            name = JourneyMqConstant.JOURNEY_UPDATE_QUEUE,
                            arguments = @Argument(name = MqConstant.QUEUE_MODE, value = MqConstant.LAZY_QUEUE)),
                    exchange = @Exchange(name = JourneyMqConstant.JOURNEY_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
                    key = {JourneyMqConstant.JOURNEY_UPDATE_KEY}))
    public void updateJourney(JourneyUpdateDto dto) throws IOException {
        log.info("收到日记更新消息, dto: {}", dto);
        esService.updateJourney(dto.getJourneyId(), dto);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            name = JourneyMqConstant.JOURNEY_POST_QUEUE,
                            arguments = @Argument(name = MqConstant.QUEUE_MODE, value = MqConstant.LAZY_QUEUE)),
                    exchange = @Exchange(name = JourneyMqConstant.JOURNEY_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
                    key = {JourneyMqConstant.JOURNEY_POST_KEY}))
    public void postJourney(JourneyVo vo) throws IOException {
        log.info("收到日记发布消息, vo: {}", vo);
        esService.postJourneyDoc(BeanUtil.copyProperties(vo, JourneyDoc.class));
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            name = JourneyMqConstant.JOURNEY_DELETE_QUEUE,
                            arguments = @Argument(name = MqConstant.QUEUE_MODE, value = MqConstant.LAZY_QUEUE)),
                    exchange = @Exchange(
                            name = JourneyMqConstant.JOURNEY_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
                    key = {JourneyMqConstant.JOURNEY_DELETE_KEY}))
    public void deleteJourney(Long jid) throws IOException {
        log.info("收到日记删除消息, id: {}", jid);
        esService.deleteJourneyById(String.valueOf(jid));
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            name = JourneyMqConstant.JOURNEY_PARTITION_REMOVE_QUEUE,
                            arguments = @Argument(name = MqConstant.QUEUE_MODE, value = MqConstant.LAZY_QUEUE)),
                    exchange = @Exchange(name = JourneyMqConstant.JOURNEY_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
                    key = {JourneyMqConstant.JOURNEY_PARTITION_REMOVE_KEY}))
    public void removePartition(Map<String, Long> ids) {
        log.info("收到分区改变消息, ids: {}", ids);
        BaseContext.setUserId(ids.get(UserConstant.USER_ID));
        Long partitionId = ids.get("partitionId");
        Long newPartitionId = ids.get("newPartitionId");
        // 更新日记分区，并得到更新后的日记列表
        List<Long> changedIds;
        if ((changedIds = service.changePartition(partitionId, newPartitionId)).isEmpty()) {
            return;
        }

        // 获取日记 Vo，并构造更新 Dto，用于更新日记文档
        changedIds.forEach(id -> {
            JourneyVo vo = service.getJourneyVoById(id);
            JourneyUpdateDto dto = new JourneyUpdateDto();
            dto.setPartition(JsonNullable.of(vo.getPartition()));
            dto.setPartitionId(JsonNullable.of(vo.getPartitionId()));

            try {
                esService.updateJourney(vo.getJourneyId(), dto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            name = JourneyMqConstant.JOURNEY_AI_SUMMARY_QUEUE,
                            arguments = @Argument(name = MqConstant.QUEUE_MODE, value = MqConstant.LAZY_QUEUE)),
                    exchange = @Exchange(
                            name = JourneyMqConstant.JOURNEY_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
                    key = {JourneyMqConstant.JOURNEY_AI_SUMMARY_KEY}))
    public void journeyAiSummary(MqDto<JourneyDto> mqDto) throws IOException {
        var dto = mqDto.getObj();

        log.info("收到 AI 生成摘要信息, id: {}", dto.getJourneyId());
        BaseContext.setUserId(mqDto.getUserId());
        // 摘要不为空
        if (!BeanUtil.isBlank(dto.getSummary())) {
            return;
        }
        // 生成摘要并填充
        String summary = aiService.getJourneySummary(dto.getContent());
        if (BeanUtil.isBlank(summary)) {
            return;
        }
        dto.setSummary(summary);
        // 初始化更新 Dto
        var updateDto = new JourneyUpdateDto();
        updateDto.setSummary(JsonNullable.of(dto.getSummary()));
        updateDto.setJourneyId(dto.getJourneyId());

        // 增量更新日记与日记文档
        service.updatePatch(updateDto);
        esService.updateJourney(dto.getJourneyId(), updateDto);
    }
}



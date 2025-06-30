package com.wolfhouse.journeydaily.mq.service;

import com.wolfhouse.journeydaily.common.constant.mq.JourneyMqConstant;
import com.wolfhouse.journeydaily.context.BaseContext;
import com.wolfhouse.journeydaily.pojo.dto.JourneyDto;
import com.wolfhouse.journeydaily.pojo.dto.JourneyUpdateDto;
import com.wolfhouse.journeydaily.pojo.dto.MqDto;
import com.wolfhouse.journeydaily.pojo.vo.JourneyVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author linexsong
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JourneyMqService {
    private final RabbitTemplate template;

    public void postJourney(JourneyVo dto) {
        log.info("发布日记，通知日记发送队列");
        template.convertAndSend(JourneyMqConstant.JOURNEY_EXCHANGE_NAME, JourneyMqConstant.JOURNEY_POST_KEY, dto);
    }

    public void updateJourney(Long jid, JourneyUpdateDto dto) {
        log.info("更新日记，通知日记更新队列");
        dto.setJourneyId(jid);
        template.convertAndSend(JourneyMqConstant.JOURNEY_EXCHANGE_NAME, JourneyMqConstant.JOURNEY_UPDATE_KEY, dto);
    }

    public void deleteJourney(Long jid) {
        log.info("删除日记，通知日记删除队列");
        template.convertAndSend(JourneyMqConstant.JOURNEY_EXCHANGE_NAME, JourneyMqConstant.JOURNEY_DELETE_KEY, jid);
    }

    public void changePartition(Long partitionId, Long newPartitionId) {
        log.info("移除分区，通知日记分区队列");
        template.convertAndSend(JourneyMqConstant.JOURNEY_EXCHANGE_NAME,
                                JourneyMqConstant.JOURNEY_PARTITION_REMOVE_KEY,
                                Map.of("userId",
                                       BaseContext.getUserId(),
                                       "partitionId",
                                       partitionId,
                                       "newPartitionId",
                                       newPartitionId));

    }

    public void getAiSummary(JourneyDto dto) {
        log.info("通知生成 AI 摘要, id: {}", dto.getJourneyId());
        template.convertAndSend(JourneyMqConstant.JOURNEY_EXCHANGE_NAME,
                                JourneyMqConstant.JOURNEY_AI_SUMMARY_KEY,
                                new MqDto<>(BaseContext.getUserId(), dto));

    }
}

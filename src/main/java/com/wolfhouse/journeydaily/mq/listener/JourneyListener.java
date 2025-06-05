package com.wolfhouse.journeydaily.mq.listener;

import com.wolfhouse.journeydaily.common.constant.mq.JourneyMqConstant;
import com.wolfhouse.journeydaily.common.constant.mq.MqConstant;
import com.wolfhouse.journeydaily.common.util.BeanUtil;
import com.wolfhouse.journeydaily.pojo.doc.JourneyDoc;
import com.wolfhouse.journeydaily.pojo.dto.JourneyUpdateDto;
import com.wolfhouse.journeydaily.pojo.vo.JourneyVo;
import com.wolfhouse.journeydaily.service.JourneyEsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author linexsong
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JourneyListener {
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
}



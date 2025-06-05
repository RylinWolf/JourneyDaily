package com.wolfhouse.journeydaily.common.constant.mq;

/**
 * @author linexsong
 */
public class JourneyMqConstant {
    public static final String JOURNEY_BASE_NAME = "journey";
    public static final String JOURNEY_EXCHANGE_NAME = JOURNEY_BASE_NAME + MqConstant.SEPARATOR + MqConstant.EXCHANGE;

    // binding key

    public static final String JOURNEY_POST_KEY = JOURNEY_BASE_NAME + MqConstant.SEPARATOR + "post";
    public static final String JOURNEY_UPDATE_KEY = JOURNEY_BASE_NAME + MqConstant.SEPARATOR + "update";
    public static final String JOURNEY_DELETE_KEY = JOURNEY_BASE_NAME + MqConstant.SEPARATOR + "delete";

    // queue

    public static final String JOURNEY_UPDATE_QUEUE = JOURNEY_UPDATE_KEY + MqConstant.SEPARATOR + MqConstant.QUEUE;
    public static final String JOURNEY_POST_QUEUE = JOURNEY_POST_KEY + MqConstant.SEPARATOR + MqConstant.QUEUE;
    public static final String JOURNEY_DELETE_QUEUE = JOURNEY_DELETE_KEY + MqConstant.SEPARATOR + MqConstant.QUEUE;
    
}

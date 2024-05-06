package com.fishman.welder_management_backend.utils;

import com.fishman.welder_management_backend.properties.FishmanProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 消息utils
 *
 */
@Log4j2
@Component
public class MessageUtils {
    private static FishmanProperties fishmanProperties;

    @Resource
    private FishmanProperties tempProperties;


    /**
     * 发送消息
     *
     * @param phoneNum 电话号码
     * @param code     密码
     */
    public static void sendMessage(String phoneNum, String code) {
        if (fishmanProperties.isUseShortMessagingService()) {
            SMSUtils.sendMessage(phoneNum, code);
        } else {
            log.info("验证码: " + code);
        }
    }

    /**
     * init属性
     */
    @PostConstruct
    public void initProperties() {
        fishmanProperties = tempProperties;
    }
}

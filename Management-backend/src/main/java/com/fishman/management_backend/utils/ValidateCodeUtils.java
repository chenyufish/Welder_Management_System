package com.fishman.management_backend.utils;

import java.util.Random;

import static com.fishman.management_backend.contant.SystemConstants.MAXIMUM_VERIFICATION_CODE_NUM;
import static com.fishman.management_backend.contant.SystemConstants.MINIMUM_VERIFICATION_CODE_NUM;


/**
 * 验证码生成工具
 *
 * @author fishman
 */
public final class ValidateCodeUtils {
    private ValidateCodeUtils() {
    }

    /**
     * 生成验证代码
     *
     * @return {@link Integer}
     */
    public static Integer generateValidateCode() {
        int code = new Random().nextInt(MAXIMUM_VERIFICATION_CODE_NUM); //生成随机数，最大为999999
        if (code < MINIMUM_VERIFICATION_CODE_NUM) {
            code = code + MINIMUM_VERIFICATION_CODE_NUM; //保证随机数为6位数字
        }
        return code;
    }
}

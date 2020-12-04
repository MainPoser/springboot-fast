package cn.com.datu.springboot.arcsoft.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import sun.misc.BASE64Decoder;

import java.io.IOException;

/**
 * @author tianyao
 */
@Slf4j
public class Base64Utils {
    public static byte[] base64StrToBytes(String imgBase64) {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = new byte[0];

        try {
            bytes = decoder.decodeBuffer(imgBase64);
        } catch (IOException var4) {
            log.error("base64StrToBytes:{}", ExceptionUtils.getFullStackTrace(var4));
        }
        for(int i = 0; i < bytes.length; ++i) {
            if (bytes[i] < 0) {
                bytes[i] = (byte)(bytes[i] + 256);
            }
        }

        return bytes;
    }
}

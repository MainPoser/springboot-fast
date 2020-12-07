package cn.com.datu.springboot.arcsoft.common.util;

import cn.com.datu.springboot.arcsoft.common.constant.ConstantImage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.exception.ExceptionUtils;
import sun.misc.BASE64Decoder;

import java.io.IOException;

/**
 * @author tianyao
 */
@Slf4j
public class Base64Utils {
    public static byte[] base64StrToBytes(String imgBase64) {
        byte[] decodeImageData = null;
        if (imgBase64.lastIndexOf(ConstantImage.BASE64_GNU_SED) != -1) {
            decodeImageData = Base64.decodeBase64(imgBase64.split(ConstantImage.BASE64_GNU_SED)[1]);
        } else {
            decodeImageData = Base64.decodeBase64(imgBase64);
        }
        return decodeImageData;
    }
}

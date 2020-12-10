package cn.com.datu.springboot.arcsoft.sdk.model;

import lombok.Data;

/**
 * @author tianyao
 */
@Data
public class UserCompareInfo extends UserRamCache.UserInfo {
    /**
     * 人脸比对后的相似值
     */
    private Float similar;
}
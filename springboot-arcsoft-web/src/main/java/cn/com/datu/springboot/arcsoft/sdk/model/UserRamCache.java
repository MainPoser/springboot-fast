package cn.com.datu.springboot.arcsoft.sdk.model;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tianyao
 */
public class UserRamCache {
    private static ConcurrentHashMap<String, UserInfo> userInfoMap = new ConcurrentHashMap<>();
    public static void addUser(UserInfo userInfo) {
        userInfoMap.put(userInfo.getFaceId(), userInfo);
    }
    public static void removeUser(String faceId) {
        userInfoMap.remove(faceId);
    }
    public static List<UserInfo> getUserList() {
        List<UserInfo> userInfoList = Lists.newLinkedList();
        for (UserInfo value : userInfoMap.values()) {
            userInfoList.add(value);
        }
        return userInfoList;
    }
    @Data
    public static class UserInfo {
        //人脸Id
        private String faceId;
        //人脸名称
        private String name;
        //人脸特征值
        private byte[] faceFeature;
    }
}
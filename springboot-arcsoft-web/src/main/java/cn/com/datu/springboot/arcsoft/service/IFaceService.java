package cn.com.datu.springboot.arcsoft.service;

import cn.com.datu.springboot.arcsoft.dto.FaceDetectResDTO;
import cn.com.datu.springboot.arcsoft.dto.FaceRecognitionResDTO;
import cn.com.datu.springboot.arcsoft.sdk.model.ProcessInfo;
import cn.com.datu.springboot.arcsoft.sdk.model.UserCompareInfo;
import cn.com.datu.springboot.arcsoft.sdk.model.UserRamCache;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageInfo;

import java.util.List;

/**
 * @author tianyao
 */
public interface IFaceService {
    /**
     * 人脸检测
     *
     * @param imageInfo
     * @return
     */
    List<FaceInfo> detectFaces(ImageInfo imageInfo);

    /**
     * 人脸检测
     *
     * @param imageBase64
     * @return
     */
    List<FaceDetectResDTO> detectFaces(String imageBase64);

    /**
     * 人脸特征提取
     *
     * @param imageInfo
     * @param faceInfo
     * @return
     */
    byte[] extractFaceFeature(ImageInfo imageInfo, FaceInfo faceInfo);

    /**
     * 人脸识别
     *
     * @param feature
     * @param userList
     * @param v
     * @return
     */
    List<UserCompareInfo> faceRecognition(byte[] feature, List<UserRamCache.UserInfo> userList, float v);

    /**
     * 人脸识别
     *
     * @param imageBase64
     * @return
     */
    List<FaceRecognitionResDTO> faceRecognition(String imageBase64);

    /**
     * @param imageInfo
     * @param faceInfoList
     * @return 人脸属性检测
     */
    List<ProcessInfo> process(ImageInfo imageInfo, List<FaceInfo> faceInfoList);

    /**
     * @param imageInfo1
     * @param imageInfo2
     * @return
     */
    Float compareFaces(ImageInfo imageInfo1, ImageInfo imageInfo2);
    /**
     * @param imageBase641
     * @param imageBase642
     * @return
     */
    Float compareFaces(String imageBase641, String imageBase642);
}

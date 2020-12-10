package cn.com.datu.springboot.arcsoft.service.impl;

import cn.com.datu.springboot.arcsoft.common.util.Base64Utils;
import cn.com.datu.springboot.arcsoft.dto.FaceDetectResDTO;
import cn.com.datu.springboot.arcsoft.dto.FaceRecognitionResDTO;
import cn.com.datu.springboot.arcsoft.sdk.model.ProcessInfo;
import cn.com.datu.springboot.arcsoft.sdk.model.UserCompareInfo;
import cn.com.datu.springboot.arcsoft.sdk.model.UserRamCache;
import cn.com.datu.springboot.arcsoft.sdk.IFaceEngineSdk;
import cn.com.datu.springboot.arcsoft.service.IFaceService;
import com.arcsoft.face.*;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tianyao
 */
@Slf4j
@Service
public class FaceServiceImpl implements IFaceService {
    @Autowired
    private IFaceEngineSdk faceEngineSdk;

    @Override
    public List<FaceInfo> detectFaces(ImageInfo imageInfo) {
        return faceEngineSdk.detectFaces(imageInfo);
    }

    @Override
    public List<FaceDetectResDTO> detectFaces(String imageBase64) {
        byte[] bytes = Base64Utils.base64StrToBytes(imageBase64);
        ImageInfo imageInfo = ImageFactory.getRGBData(bytes);
        List<FaceDetectResDTO> faceDetectResDTOS = Lists.newLinkedList();
        List<FaceInfo> faceInfoList = faceEngineSdk.detectFaces(imageInfo);
        if (CollectionUtils.isNotEmpty(faceInfoList)) {
            List<ProcessInfo> process = faceEngineSdk.process(imageInfo, faceInfoList);
            for (int i = 0; i < faceInfoList.size(); i++) {
                FaceDetectResDTO faceDetectResDTO = new FaceDetectResDTO();
                FaceInfo faceInfo = faceInfoList.get(i);
                faceDetectResDTO.setRect(faceInfo.getRect());
                faceDetectResDTO.setOrient(faceInfo.getOrient());
                faceDetectResDTO.setFaceId(faceInfo.getFaceId());
                if (CollectionUtils.isNotEmpty(process)) {
                    ProcessInfo processInfo = process.get(i);
                    faceDetectResDTO.setAge(processInfo.getAge());
                    faceDetectResDTO.setGender(processInfo.getGender());
                    faceDetectResDTO.setLiveness(processInfo.getLiveness());
                }
                faceDetectResDTOS.add(faceDetectResDTO);
            }
        }
        return faceDetectResDTOS;
    }

    @Override
    public byte[] extractFaceFeature(ImageInfo imageInfo, FaceInfo faceInfo) {
        return faceEngineSdk.extractFaceFeature(imageInfo, faceInfo);
    }

    @Override
    public Float compareFaces(ImageInfo imageInfo1, ImageInfo imageInfo2) {
        return faceEngineSdk.compareFaces(imageInfo1, imageInfo2);
    }

    @Override
    public Float compareFaces(String imageBase641, String imageBase642) {
        byte[] bytes1 = Base64Utils.base64StrToBytes(imageBase641);
        byte[] bytes2 = Base64Utils.base64StrToBytes(imageBase642);
        ImageInfo imageInfo1 = ImageFactory.getRGBData(bytes1);
        ImageInfo imageInfo2 = ImageFactory.getRGBData(bytes2);
        Float similar = faceEngineSdk.compareFaces(imageInfo1, imageInfo2);
        return similar;
    }

    @Override
    public List<UserCompareInfo> faceRecognition(byte[] faceFeature, List<UserRamCache.UserInfo> userInfoList, float passRate) {
        return faceEngineSdk.faceRecognition(faceFeature, userInfoList, passRate);
    }

    @Override
    public List<FaceRecognitionResDTO> faceRecognition(String imageBase64) {
        List<FaceRecognitionResDTO> faceRecognitionResDTOList = Lists.newLinkedList();
        byte[] bytes = Base64Utils.base64StrToBytes(imageBase64);
        ImageInfo imageInfo = ImageFactory.getRGBData(bytes);
        List<FaceInfo> faceInfoList = faceEngineSdk.detectFaces(imageInfo);
        if (CollectionUtils.isNotEmpty(faceInfoList)) {
            for (FaceInfo faceInfo : faceInfoList) {
                FaceRecognitionResDTO faceRecognitionResDTO = new FaceRecognitionResDTO();
                faceRecognitionResDTO.setRect(faceInfo.getRect());
                byte[] feature = faceEngineSdk.extractFaceFeature(imageInfo, faceInfo);
                if (feature != null) {
                    List<UserCompareInfo> userCompareInfos = faceEngineSdk.faceRecognition(feature, UserRamCache.getUserList(), 0.8f);
                    if (CollectionUtils.isNotEmpty(userCompareInfos)) {
                        faceRecognitionResDTO.setName(userCompareInfos.get(0).getName());
                        faceRecognitionResDTO.setSimilar(userCompareInfos.get(0).getSimilar());
                    }
                }
                faceRecognitionResDTOList.add(faceRecognitionResDTO);
            }
        }
        return faceRecognitionResDTOList;
    }

    @Override
    public List<ProcessInfo> process(ImageInfo imageInfo, List<FaceInfo> faceInfoList) {
        return faceEngineSdk.process(imageInfo, faceInfoList);
    }
}

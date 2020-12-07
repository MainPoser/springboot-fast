package cn.com.datu.springboot.arcsoft.service.impl;

import cn.com.datu.springboot.arcsoft.common.constant.ConstantImage;
import cn.com.datu.springboot.arcsoft.common.util.Base64Utils;
import cn.com.datu.springboot.arcsoft.model.enums.IrLiveness;
import cn.com.datu.springboot.arcsoft.model.enums.PersonSex;
import cn.com.datu.springboot.arcsoft.service.IArcSoftService;
import cn.com.datu.springboot.arcsoft.vo.*;
import com.alibaba.fastjson.JSON;
import com.arcsoft.face.*;
import com.arcsoft.face.enums.CompareModel;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.toolkit.ImageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

/**
 * @author tianyao
 */
@Service
@Slf4j
public class ArcSoftServiceImpl implements IArcSoftService {
    @Autowired
    private FaceEngine faceEngine;
    private static final Pattern PATTERN = Pattern.compile("[a-zA-z]");


    @Override
    public List<FaceInfo> detectFaces(DetectFacesReqVo detectFacesReqVo) {
        List<FaceInfo> faceInfoList = new ArrayList<>();
        byte[] decodeImageData = Base64Utils.base64StrToBytes(detectFacesReqVo.getImgBase64());
        ImageInfo imageInfo = getRGBData(decodeImageData);
        int errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            log.error("人脸检测失败,request:<{}>,response:<{}>", JSON.toJSONString(detectFacesReqVo), errorCode);
            throw new UnsupportedOperationException("人脸检测失败");
        }
        return faceInfoList;
    }

    @Override
    public FaceFeature extractFaceFeature(ExtractFaceFeatureReqVo extractFaceFeatureReqVo) {
        FaceFeature faceFeature = new FaceFeature();
        byte[] decodeImageData = Base64Utils.base64StrToBytes(extractFaceFeatureReqVo.getImgBase64());
        ImageInfo imageInfo = getRGBData(decodeImageData);
        //获取人脸信息
        DetectFacesReqVo detectFacesReqVo = new DetectFacesReqVo();
        detectFacesReqVo.setImgBase64(extractFaceFeatureReqVo.getImgBase64());
        List<FaceInfo> faceInfos = detectFaces(detectFacesReqVo);
        int errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfos.get(0), faceFeature);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            log.error("人脸特征提取失败,request:<{}>,response:<{}>", JSON.toJSONString(extractFaceFeatureReqVo), errorCode);
            throw new UnsupportedOperationException("人脸特征提取失败");
        }
        return faceFeature;
    }

    @Override
    public FaceSimilar compareFaceFeature(CompareFaceFeatureReqVo compareFaceFeatureReqVo) {
        FaceSimilar faceSimilar = new FaceSimilar();
        //比对
        CompareModel compareModel = CompareModel.ID_PHOTO;
        if (StringUtils.isNotBlank(compareFaceFeatureReqVo.getCompareModel())){
            for (CompareModel value : CompareModel.values()) {
                if (compareFaceFeatureReqVo.getCompareModel().equals(value.name())){
                    compareModel = value;
                    break;
                }
            }
        }
        int errorCode = faceEngine.compareFaceFeature(compareFaceFeatureReqVo.getFaceFeature1(), compareFaceFeatureReqVo.getFaceFeature2(), compareModel, faceSimilar);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            log.error("人脸特征比对失败,request:<{}>,response:<{}>", JSON.toJSONString(compareFaceFeatureReqVo), errorCode);
            throw new UnsupportedOperationException("人脸特征比对失败");
        }
        return faceSimilar;
    }

    @Override
    public ProcessResVo process(ProcessReqVo processReqVo) {
        ProcessResVo processResVo = new ProcessResVo();
        byte[] decodeImageData = Base64Utils.base64StrToBytes(processReqVo.getImgBase64());
        //提取图片信息
        ImageInfo imageInfo = getRGBData(decodeImageData);
        //提取图片人脸信息
        DetectFacesReqVo detectFacesReqVo = new DetectFacesReqVo();
        detectFacesReqVo.setImgBase64(processReqVo.getImgBase64());
        List<FaceInfo> faceInfos = detectFaces(detectFacesReqVo);
        //设置检查配置
        FunctionConfiguration configuration = new FunctionConfiguration();
        configuration.setSupportAge(true);
        configuration.setSupportFace3dAngle(true);
        configuration.setSupportGender(true);
        configuration.setSupportLiveness(true);
        int errorCode = faceEngine.process(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfos, configuration);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            log.error("人脸属性检测失败,request:<{}>,response:<{}>", JSON.toJSONString(processReqVo), errorCode);
            throw new UnsupportedOperationException("人脸属性检测失败");
        }
        List<AgeInfo> ageInfoList = new ArrayList<>();
        errorCode = faceEngine.getAge(ageInfoList);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            log.error("获取人脸年龄失败,request:<{}>,response:<{}>", JSON.toJSONString(processReqVo), errorCode);
            throw new UnsupportedOperationException("获取人脸年龄失败");
        }
        //性别检测
        List<GenderInfo> genderInfoList = new ArrayList<>();
        errorCode = faceEngine.getGender(genderInfoList);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            log.error("获取人脸性别失败,request:<{}>,response:<{}>", JSON.toJSONString(processReqVo), errorCode);
            throw new UnsupportedOperationException("获取人脸性别失败");
        }
        List<Face3DAngle> face3DAngleList = new ArrayList<>();
        errorCode = faceEngine.getFace3DAngle(face3DAngleList);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            log.error("获取人脸3D角度失败,request:<{}>,response:<{}>", JSON.toJSONString(processReqVo), errorCode);
            throw new UnsupportedOperationException("获取人脸3D角度失败");
        }
        //活体检测
        List<LivenessInfo> livenessInfoList = new ArrayList<LivenessInfo>();
        errorCode = faceEngine.getLiveness(livenessInfoList);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            log.error("获取活体检测失败,request:<{}>,response:<{}>", JSON.toJSONString(processReqVo), errorCode);
            throw new UnsupportedOperationException("获取活体检测失败");
        }
        if (CollectionUtils.isNotEmpty(ageInfoList)) {
            int age = ageInfoList.get(0).getAge();
            processResVo.setAge(age);
            log.info("年龄：<{}>",age);
        }
        if (CollectionUtils.isNotEmpty(genderInfoList)) {
            int gender = genderInfoList.get(0).getGender();
            String genderString = PersonSex.getValueByKey(gender);
            processResVo.setGender(gender);
            log.info("性别：<{}>",genderString);
        }
        if (CollectionUtils.isNotEmpty(face3DAngleList)) {
            Face3DAngle face3DAngle = face3DAngleList.get(0);
            processResVo.setFace3DAngle(face3DAngle);
            log.info("3D角度：<{}>,<{}>,<{}>", face3DAngle.getPitch(), face3DAngle.getRoll(), face3DAngle.getYaw());
        }
        if (CollectionUtils.isNotEmpty(livenessInfoList)) {
            int liveness = livenessInfoList.get(0).getLiveness();
            processResVo.setLiveness(liveness);
            String livenessString = IrLiveness.getValueByKey(liveness);
            log.info("活体：<{}>,",livenessString);
        }
        return processResVo;
    }

}

package cn.com.datu.springboot.arcsoft.service.impl;

import cn.com.datu.springboot.arcsoft.service.IArcSoftService;
import cn.com.datu.springboot.arcsoft.vo.DetectFacesReqVo;
import cn.com.datu.springboot.arcsoft.vo.ExtractFaceFeatureReqVo;
import com.alibaba.fastjson.JSON;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.enums.ImageFormat;
import com.arcsoft.face.toolkit.ImageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

/**
 * @author tianyao
 */
@Service
@Slf4j
public class ArcSoftServiceImpl implements IArcSoftService {
    @Autowired
    private FaceEngine faceEngine;

    @Override
    public List<FaceInfo> detectFaces(DetectFacesReqVo detectFacesReqVo) {
        List<FaceInfo> faceInfoList = new ArrayList<>();
        byte[] decodeImageData = Base64.getDecoder().decode(detectFacesReqVo.getImgBase64());
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
        byte[] decodeImageData = Base64.getDecoder().decode(extractFaceFeatureReqVo.getImgBase64());
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
        return null;
    }
}

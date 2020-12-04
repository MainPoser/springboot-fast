package cn.com.datu.springboot.arcsoft.service;

import cn.com.datu.springboot.arcsoft.vo.DetectFacesReqVo;
import cn.com.datu.springboot.arcsoft.vo.ExtractFaceFeatureReqVo;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;

import java.util.List;

/**
 * @author tianyao
 */
public interface IArcSoftService {
    /**
     * 传入分离的图像信息数据（base64），人脸检测
     * @param detectFacesReqVo
     * @return
     */
    List<FaceInfo> detectFaces(DetectFacesReqVo detectFacesReqVo);

    /**
     * 人脸特征提取
     * @param extractFaceFeatureReqVo
     * @return
     */
    FaceFeature extractFaceFeature(ExtractFaceFeatureReqVo extractFaceFeatureReqVo);
}

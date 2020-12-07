package cn.com.datu.springboot.arcsoft.vo;

import com.arcsoft.face.FaceFeature;
import lombok.Data;

/**
 * @author tianyao
 */
@Data
public class CompareFaceFeatureReqVo {
    private FaceFeature faceFeature1;
    private FaceFeature faceFeature2;
    /**
     * LIFE_PHOTO	用于生活照之间的特征比对，推荐阈值0.80
     * ID_PHOTO	用于证件照或生活照与证件照之间的特征比对，推荐阈值0.82
     */
    private String compareModel;
}

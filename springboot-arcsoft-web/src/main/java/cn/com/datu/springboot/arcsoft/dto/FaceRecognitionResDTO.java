package cn.com.datu.springboot.arcsoft.dto;

import com.arcsoft.face.Rect;
import lombok.Data;

/**
 * @author tianyao
 * 人脸识别DTO封装类
 */
@Data
public class FaceRecognitionResDTO {
    //人脸框
    private Rect rect;
    //人脸名称
    private String name;
    //人脸比对相似值
    private float similar;
}
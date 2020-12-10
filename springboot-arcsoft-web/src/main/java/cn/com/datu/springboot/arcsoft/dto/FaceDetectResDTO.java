package cn.com.datu.springboot.arcsoft.dto;

import com.arcsoft.face.Rect;
import lombok.Data;

/**
 * @author tianyao
 * 人脸检测DTO封装类
 */
@Data
public class FaceDetectResDTO {
    //人脸框
    private Rect rect;
    //人脸角度
    private int orient;
    //人脸Id
    private int faceId = -1;
    //年龄
    private int age = -1;
    //性别
    private int gender = -1;
    //是否为活体
    private int liveness = -1;
}
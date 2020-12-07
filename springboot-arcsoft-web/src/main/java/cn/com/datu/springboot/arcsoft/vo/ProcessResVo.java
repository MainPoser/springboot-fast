package cn.com.datu.springboot.arcsoft.vo;

import com.arcsoft.face.Face3DAngle;
import lombok.Data;

/**
 * @author tianyao
 */
@Data
public class ProcessResVo {
    /**
     * 图片base64
     */
    private int gender;
    /**
     * 活体
     */
    private int liveness;
    /**
     * 年龄
     */
    private int age;
    /**
     * 3D角度
     */
    private Face3DAngle face3DAngle;
}

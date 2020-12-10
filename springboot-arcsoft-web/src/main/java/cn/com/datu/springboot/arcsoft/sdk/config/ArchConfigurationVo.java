package cn.com.datu.springboot.arcsoft.sdk.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tianyao
 */
@Component
@Data
public class ArchConfigurationVo {
    @Value("${spring.archSoft.faceEngine.appId}")
    private String appId;
    @Value("${spring.archSoft.faceEngine.sdkKey}")
    private String sdkKey;
    @Value("${spring.archSoft.faceEngine.sysType}")
    private String sysType;
    /**
     * ASF_DETECT_MODE_VIDEO  VIDEO模式
     * ASF_DETECT_MODE_IMAGE  IMAGE模式
     */
    @Value("${spring.archSoft.engineConfiguration.detectMode}")
    private String detectMode;
    @Value("${spring.archSoft.engineConfiguration.detectOrient}")
    private String detectOrient;
    @Value("${spring.archSoft.engineConfiguration.detectFaceMaxNum:10}")
    private Integer detectFaceMaxNum;
    @Value("${spring.archSoft.engineConfiguration.detectFaceScaleVal:16}")
    private Integer detectFaceScaleVal;
    @Value("${spring.archSoft.engineConfiguration.functionConfiguration.supportAge:true}")
    private boolean supportAge;
    @Value("${spring.archSoft.engineConfiguration.functionConfiguration.supportFace3dAngle:true}")
    private boolean supportFace3dAngle;
    @Value("${spring.archSoft.engineConfiguration.functionConfiguration.supportFaceDetect:true}")
    private boolean supportFaceDetect;
    @Value("${spring.archSoft.engineConfiguration.functionConfiguration.supportFaceRecognition:true}")
    private boolean supportFaceRecognition;
    @Value("${spring.archSoft.engineConfiguration.functionConfiguration.supportGender:true}")
    private boolean supportGender;
    @Value("${spring.archSoft.engineConfiguration.functionConfiguration.supportLiveness:true}")
    private boolean supportLiveness;
    @Value("${spring.archSoft.engineConfiguration.functionConfiguration.supportIRLiveness:true}")
    private boolean supportIRLiveness;

    /**
     * 识别线程池数量
     */
    @Value("${spring.archSoft.detectPoolSize}")
    public Integer detectPooSize;
    /**
     * 比对线程池数量
     */
    @Value("${spring.archSoft.comparePoolSize}")
    public Integer comparePooSize;
}

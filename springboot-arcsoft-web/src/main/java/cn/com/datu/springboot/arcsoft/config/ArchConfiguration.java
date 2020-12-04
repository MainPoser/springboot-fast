package cn.com.datu.springboot.arcsoft.config;

import cn.com.datu.springboot.arcsoft.common.enums.SysType;
import com.arcsoft.face.ActiveFileInfo;
import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FunctionConfiguration;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * @author tianyao
 */
@Configuration
@Slf4j
public class ArchConfiguration {
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


    @Bean
    public FaceEngine getFaceEngine() {
        //初始化引擎
        FaceEngine faceEngine = initFaceEngine();
        //引擎初始化配置
        EngineConfiguration engineConfiguration = getEngineConfiguration();
        //初始化引擎
        int errorCode;
        errorCode = faceEngine.init(engineConfiguration);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            log.error("初始化引擎失败,appId:<{}>,sdkKey:<{}>", appId, sdkKey);
            throw new UnsupportedOperationException("初始化引擎失败");
        }
        return faceEngine;
    }

    private EngineConfiguration getEngineConfiguration() {
        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        //默认图片模式
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        DetectMode[] detectModes = DetectMode.values();
        for (DetectMode modeEnum : detectModes) {
            if (detectMode.equals(modeEnum.name())) {
                engineConfiguration.setDetectMode(modeEnum);
                break;
            }
        }
        //默认全角度模式
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
        DetectOrient[] detectOrients = DetectOrient.values();
        for (DetectOrient detectOrientEnum : detectOrients) {
            if (detectOrient.equals(detectOrientEnum.name())) {
                engineConfiguration.setDetectFaceOrientPriority(detectOrientEnum);
                break;
            }
        }
        engineConfiguration.setDetectFaceMaxNum(detectFaceMaxNum);
        engineConfiguration.setDetectFaceScaleVal(detectFaceScaleVal);
        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(supportAge);
        functionConfiguration.setSupportFace3dAngle(supportFace3dAngle);
        functionConfiguration.setSupportFaceDetect(supportFaceDetect);
        functionConfiguration.setSupportFaceRecognition(supportFaceRecognition);
        functionConfiguration.setSupportGender(supportGender);
        functionConfiguration.setSupportLiveness(supportLiveness);
        functionConfiguration.setSupportIRLiveness(supportIRLiveness);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);
        return engineConfiguration;
    }

    private FaceEngine initFaceEngine() {
        //默认windows
        String libPath = SysType.WIN64.name();
        for (SysType sysTypeEnum : SysType.values()) {
            if (sysType.equals(sysTypeEnum.name())){
                libPath = sysType;
            }
        }
        //获取认证文件
        String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath()+libPath;
        FaceEngine faceEngine = new FaceEngine(path);
        //激活引擎
        int errorCode = faceEngine.activeOnline(appId, sdkKey);
        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            log.error("引擎激活失败,appId:<{}>,sdkKey:<{}>", appId, sdkKey);
            throw new UnsupportedOperationException("引擎激活失败");
        }
        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
        errorCode = faceEngine.getActiveFileInfo(activeFileInfo);
        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            log.error("获取激活文件信息失败,appId:<{}>,sdkKey:<{}>,libPath:<{}>", appId, sdkKey, path);
            throw new UnsupportedOperationException("获取激活文件信息失败");
        }
        return faceEngine;
    }
}

package cn.com.datu.springboot.arcsoft.sdk.config;


import com.arcsoft.face.EngineConfiguration;

import com.arcsoft.face.FunctionConfiguration;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tianyao
 */
@Configuration
@Slf4j
public class ArchConfiguration {
    @Autowired
    private ArchConfigurationVo archConfigurationVo;

    /**
     * 引擎初始化配置
     * @return
     */
    @Bean
    public EngineConfiguration getEngineConfiguration() {
        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        //默认图片模式
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        DetectMode[] detectModes = DetectMode.values();
        for (DetectMode modeEnum : detectModes) {
            if (archConfigurationVo.getDetectMode().equals(modeEnum.name())) {
                engineConfiguration.setDetectMode(modeEnum);
                break;
            }
        }
        //默认全角度模式
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
        DetectOrient[] detectOrients = DetectOrient.values();
        for (DetectOrient detectOrientEnum : detectOrients) {
            if (archConfigurationVo.getDetectOrient().equals(detectOrientEnum.name())) {
                engineConfiguration.setDetectFaceOrientPriority(detectOrientEnum);
                break;
            }
        }
        engineConfiguration.setDetectFaceMaxNum(archConfigurationVo.getDetectFaceMaxNum());
        engineConfiguration.setDetectFaceScaleVal(archConfigurationVo.getDetectFaceScaleVal());
        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(archConfigurationVo.isSupportAge());
        functionConfiguration.setSupportFace3dAngle(archConfigurationVo.isSupportFace3dAngle());
        functionConfiguration.setSupportFaceDetect(archConfigurationVo.isSupportFaceDetect());
        functionConfiguration.setSupportFaceRecognition(archConfigurationVo.isSupportFaceRecognition());
        functionConfiguration.setSupportGender(archConfigurationVo.isSupportGender());
        functionConfiguration.setSupportLiveness(archConfigurationVo.isSupportLiveness());
        functionConfiguration.setSupportIRLiveness(archConfigurationVo.isSupportIRLiveness());
        engineConfiguration.setFunctionConfiguration(functionConfiguration);
        return engineConfiguration;
    }
}

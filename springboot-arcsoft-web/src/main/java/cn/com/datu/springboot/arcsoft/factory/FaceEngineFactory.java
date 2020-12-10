package cn.com.datu.springboot.arcsoft.factory;

import cn.com.datu.springboot.arcsoft.common.enums.SysType;
import cn.com.datu.springboot.arcsoft.sdk.config.ArchConfigurationVo;
import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.enums.ErrorInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author tianyao
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaceEngineFactory extends BasePooledObjectFactory<FaceEngine> {
    @Autowired
    private ArchConfigurationVo archConfigurationVo;
    //引擎配置类
    @Autowired
    private EngineConfiguration engineConfiguration;

    @Override
    public FaceEngine create() {
        String libPath = SysType.WIN64.name();
        for (SysType sysTypeEnum : SysType.values()) {
            if (archConfigurationVo.getSysType().equals(sysTypeEnum.name())){
                libPath = archConfigurationVo.getSysType();
            }
        }
        FaceEngine faceEngine = new FaceEngine(libPath);
        int activeCode = faceEngine.activeOnline(archConfigurationVo.getAppId(), archConfigurationVo.getSdkKey());
        if (activeCode != ErrorInfo.MOK.getValue() && activeCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            log.error("引擎激活失败" + activeCode);
            throw new UnsupportedOperationException("引擎激活失败" + activeCode);
        }
        int initCode = faceEngine.init(engineConfiguration);
        if (initCode != ErrorInfo.MOK.getValue()) {
            log.error("引擎初始化失败" + initCode);
            throw new UnsupportedOperationException("引擎激活失败" + activeCode);
        }
        return faceEngine;
    }

    @Override
    public PooledObject<FaceEngine> wrap(FaceEngine faceEngine) {
        return new DefaultPooledObject<>(faceEngine);
    }
    @Override
    public void destroyObject(PooledObject<FaceEngine> p) throws Exception {
        FaceEngine faceEngine = p.getObject();
        int result = faceEngine.unInit();
        super.destroyObject(p);
    }
}

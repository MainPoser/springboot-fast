package cn.com.datu.springboot.arcsoft;

import cn.com.datu.springboot.arcsoft.sdk.model.UserCompareInfo;
import cn.com.datu.springboot.arcsoft.sdk.model.UserRamCache;
import cn.com.datu.springboot.arcsoft.service.IFaceService;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author tianyao
 */
@Component
@Slf4j
public class arcsoftApplicationRunner implements ApplicationRunner {
    @Autowired
    private IFaceService faceEngineService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //初始化人脸库到内存
        //initFace();
    }

    public void initFace() throws FileNotFoundException {
        //初始化将人像库特征信息存放在内存中
        Map<String, String> fileMap = Maps.newHashMap();
        fileMap.put("zhao1", "赵丽颖");
        fileMap.put("yang1", "杨紫");

        for (String filePath : fileMap.keySet()) {
            ClassPathResource resource = new ClassPathResource("static/images/" + filePath + ".jpg");
            InputStream inputStream = null;
            try {
                inputStream = resource.getInputStream();
            } catch (IOException e) {
                log.info(ExceptionUtils.getFullStackTrace(e));
                throw new UnsupportedOperationException(ExceptionUtils.getFullStackTrace(e));
            }
            ImageInfo imageInfo = ImageFactory.getRGBData(inputStream);
            List<FaceInfo> faceInfoList = faceEngineService.detectFaces(imageInfo);
            if (CollectionUtils.isNotEmpty(faceInfoList)) {
                byte[] feature = faceEngineService.extractFaceFeature(imageInfo, faceInfoList.get(0));
                UserRamCache.UserInfo userInfo = new UserCompareInfo();
                userInfo.setFaceId(filePath);
                userInfo.setName(fileMap.get(filePath));
                userInfo.setFaceFeature(feature);
                UserRamCache.addUser(userInfo);
            }
        }
    }
}

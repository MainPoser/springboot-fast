package cn.com.datu.springboot.arcsoft.sdk.impl;

import cn.com.datu.springboot.arcsoft.sdk.config.ArchConfigurationVo;
import cn.com.datu.springboot.arcsoft.factory.FaceEngineFactory;
import cn.com.datu.springboot.arcsoft.sdk.model.ProcessInfo;
import cn.com.datu.springboot.arcsoft.sdk.model.UserCompareInfo;
import cn.com.datu.springboot.arcsoft.sdk.model.UserRamCache;
import cn.com.datu.springboot.arcsoft.sdk.IFaceEngineSdk;
import com.arcsoft.face.*;
import com.arcsoft.face.toolkit.ImageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author tianyao
 */
@Slf4j
@Service
public class FaceEngineSdk3 implements IFaceEngineSdk {
    @Autowired
    private ArchConfigurationVo archConfigurationVo;


    private ExecutorService compareExecutorService;

    //通用人脸识别引擎池
    private GenericObjectPool<FaceEngine> faceEngineGeneralPool;

    //人脸比对引擎池
    private GenericObjectPool<FaceEngine> faceEngineComparePool;

    @PostConstruct
    public void init() {
        GenericObjectPoolConfig detectPoolConfig = new GenericObjectPoolConfig();
        detectPoolConfig.setMaxIdle(archConfigurationVo.getDetectPooSize());
        detectPoolConfig.setMaxTotal(archConfigurationVo.getDetectPooSize());
        detectPoolConfig.setMinIdle(archConfigurationVo.getDetectPooSize());
        detectPoolConfig.setLifo(false);
        //底层库算法对象池
        faceEngineGeneralPool = new GenericObjectPool(new FaceEngineFactory(), detectPoolConfig);
        //初始化特征比较线程池
        GenericObjectPoolConfig comparePoolConfig = new GenericObjectPoolConfig();
        comparePoolConfig.setMaxIdle(archConfigurationVo.getComparePooSize());
        comparePoolConfig.setMaxTotal(archConfigurationVo.getComparePooSize());
        comparePoolConfig.setMinIdle(archConfigurationVo.getComparePooSize());
        comparePoolConfig.setLifo(false);
        //底层库算法对象池
        faceEngineComparePool = new GenericObjectPool(new FaceEngineFactory(), comparePoolConfig);
        compareExecutorService = Executors.newFixedThreadPool(archConfigurationVo.getComparePooSize());
    }

    @Override
    public List<FaceInfo> detectFaces(ImageInfo imageInfo) {
        FaceEngine faceEngine = null;
        try {
            faceEngine = faceEngineGeneralPool.borrowObject();
            if (faceEngine == null) {
                throw new UnsupportedOperationException("获取引擎失败");
            }
            //人脸检测得到人脸列表
            List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
            //人脸检测
            int errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
            if (errorCode == 0) {
                return faceInfoList;
            } else {
                log.error("人脸检测失败，errorCode：" + errorCode);
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineGeneralPool.returnObject(faceEngine);
            }
        }
        return null;
    }


    @Override
    public byte[] extractFaceFeature(ImageInfo imageInfo, FaceInfo faceInfo) {
        FaceEngine faceEngine = null;
        try {
            faceEngine = faceEngineGeneralPool.borrowObject();
            if (faceEngine == null) {
                throw new UnsupportedOperationException("获取引擎失败");
            }
            FaceFeature faceFeature = new FaceFeature();
            //提取人脸特征
            int errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfo, faceFeature);
            if (errorCode == 0) {
                return faceFeature.getFeatureData();
            } else {
                log.error("特征提取失败，errorCode：" + errorCode);
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineGeneralPool.returnObject(faceEngine);
            }
        }
        return null;
    }

    @Override
    public Float compareFaces(ImageInfo imageInfo1, ImageInfo imageInfo2) {
        List<FaceInfo> faceInfoList1 = detectFaces(imageInfo1);
        List<FaceInfo> faceInfoList2 = detectFaces(imageInfo2);
        if (CollectionUtils.isEmpty(faceInfoList1) || CollectionUtils.isEmpty(faceInfoList2)) {
            throw new UnsupportedOperationException("未检测到人脸");
        }
        byte[] feature1 = extractFaceFeature(imageInfo1, faceInfoList1.get(0));
        byte[] feature2 = extractFaceFeature(imageInfo2, faceInfoList2.get(0));
        FaceEngine faceEngine = null;
        try {
            faceEngine = faceEngineGeneralPool.borrowObject();
            if (faceEngine == null) {
                throw new UnsupportedOperationException("获取引擎失败");
            }
            FaceFeature faceFeature1 = new FaceFeature();
            faceFeature1.setFeatureData(feature1);
            FaceFeature faceFeature2 = new FaceFeature();
            faceFeature2.setFeatureData(feature2);
            //提取人脸特征
            FaceSimilar faceSimilar = new FaceSimilar();
            int errorCode = faceEngine.compareFaceFeature(faceFeature1, faceFeature2, faceSimilar);
            if (errorCode == 0) {
                return faceSimilar.getScore();
            } else {
                log.error("特征提取失败，errorCode：" + errorCode);
            }

        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineGeneralPool.returnObject(faceEngine);
            }
        }
        return null;
    }


    @Override
    public List<UserCompareInfo> faceRecognition(byte[] faceFeature, List<UserRamCache.UserInfo> userInfoList, float passRate) {
        List<UserCompareInfo> resultUserInfoList = Lists.newLinkedList();//识别到的人脸列表
        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(faceFeature);
        //分成1000一组，多线程处理
        List<List<UserRamCache.UserInfo>> faceUserInfoPartList = Lists.partition(userInfoList, 1000);
        CompletionService<List<UserCompareInfo>> completionService = new ExecutorCompletionService(compareExecutorService);
        for (List<UserRamCache.UserInfo> part : faceUserInfoPartList) {
            completionService.submit(new CompareFaceTask(part, targetFaceFeature, passRate));
        }
        for (int i = 0; i < faceUserInfoPartList.size(); i++) {
            List<UserCompareInfo> faceUserInfoList = new ArrayList<>();
            try {
                faceUserInfoList = completionService.take().get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("多线程执行异常:<{}>", ExceptionUtils.getFullStackTrace(e));
            }
            if (CollectionUtils.isNotEmpty(userInfoList)) {
                resultUserInfoList.addAll(faceUserInfoList);
            }
        }

        resultUserInfoList.sort((h1, h2) -> h2.getSimilar().compareTo(h1.getSimilar()));//从大到小排序

        return resultUserInfoList;
    }


    @Override
    public List<ProcessInfo> process(ImageInfo imageInfo, List<FaceInfo> faceInfoList) {
        FaceEngine faceEngine = null;
        try {
            //获取引擎对象
            faceEngine = faceEngineGeneralPool.borrowObject();
            if (faceEngine == null) {
                throw new UnsupportedOperationException("获取引擎失败");
            }
            int errorCode = faceEngine.process(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList, FunctionConfiguration.builder().supportAge(true).supportGender(true).supportLiveness(true).build());
            if (errorCode == 0) {
                List<ProcessInfo> processInfoList = Lists.newLinkedList();
                //性别列表
                List<GenderInfo> genderInfoList = new ArrayList<GenderInfo>();
                faceEngine.getGender(genderInfoList);
                //年龄列表
                List<AgeInfo> ageInfoList = new ArrayList<AgeInfo>();
                faceEngine.getAge(ageInfoList);
                //活体结果列表
                List<LivenessInfo> livenessInfoList = new ArrayList<LivenessInfo>();
                faceEngine.getLiveness(livenessInfoList);

                for (int i = 0; i < genderInfoList.size(); i++) {
                    ProcessInfo processInfo = new ProcessInfo();
                    processInfo.setGender(genderInfoList.get(i).getGender());
                    processInfo.setAge(ageInfoList.get(i).getAge());
                    processInfo.setLiveness(livenessInfoList.get(i).getLiveness());
                    processInfoList.add(processInfo);
                }
                return processInfoList;

            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineGeneralPool.returnObject(faceEngine);
            }
        }
        return null;
    }

    private class CompareFaceTask implements Callable<List<UserCompareInfo>> {
        //存储的人脸信息列表
        private List<UserRamCache.UserInfo> userInfoList;
        //目标特征值
        private FaceFeature targetFaceFeature;
        //相似度预期值
        private float passRate;

        public CompareFaceTask(List<UserRamCache.UserInfo> userInfoList, FaceFeature targetFaceFeature, float passRate) {
            this.userInfoList = userInfoList;
            this.targetFaceFeature = targetFaceFeature;
            this.passRate = passRate;
        }
        @Override
        public List<UserCompareInfo> call() throws Exception {
            FaceEngine faceEngine = null;
            List<UserCompareInfo> resultUserInfoList = Lists.newLinkedList();//识别到的人脸列表
            try {
                faceEngine = faceEngineComparePool.borrowObject();
                for (UserRamCache.UserInfo userInfo : userInfoList) {
                    FaceFeature sourceFaceFeature = new FaceFeature();
                    sourceFaceFeature.setFeatureData(userInfo.getFaceFeature());
                    FaceSimilar faceSimilar = new FaceSimilar();
                    faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
                    if (faceSimilar.getScore() > passRate) {//相似值大于配置预期，加入到识别到人脸的列表
                        UserCompareInfo info = new UserCompareInfo();
                        info.setName(userInfo.getName());
                        info.setFaceId(userInfo.getFaceId());
                        info.setSimilar(faceSimilar.getScore());
                        resultUserInfoList.add(info);
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            } finally {
                if (faceEngine != null) {
                    faceEngineComparePool.returnObject(faceEngine);
                }
            }
            return resultUserInfoList;
        }
    }
}

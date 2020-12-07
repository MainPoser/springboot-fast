package cn.com.datu.springboot.arcsoft.controller;


import cn.com.datu.springboot.arcsoft.common.constant.ConstantBusinessType;
import cn.com.datu.springboot.arcsoft.common.constant.ConstantOperatorType;
import cn.com.datu.springboot.arcsoft.common.entity.ApiResponse;
import cn.com.datu.springboot.arcsoft.service.IArcSoftService;
import cn.com.datu.springboot.arcsoft.vo.CompareFaceFeatureReqVo;
import cn.com.datu.springboot.arcsoft.vo.DetectFacesReqVo;
import cn.com.datu.springboot.arcsoft.vo.ExtractFaceFeatureReqVo;
import cn.com.datu.springboot.arcsoft.vo.ProcessReqVo;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author tianyao
 */
@Api("arcSoft Api")
@RestController
@CrossOrigin
@RequestMapping("api/rest/v1/arcSoft")
@Slf4j
public class ArcSoftController {
    @Autowired
    private IArcSoftService arcSoftService;

    /**
     * 检测人脸信息
     * @param detectFacesReqVo
     * @return
     */
    @ApiOperation(value = "检测人脸信息,该功能依赖初始化的模式选择，VIDEO模式下使用的是人脸追踪功能，IMAGE模式下使用的是人脸检测功能。初始化中detectFaceOrientPriority、detectFaceScaleVal、detectFaceMaxNum参数的设置，对能否检测到人脸以及检测到几张人脸都有决定性的作用", notes = ConstantOperatorType.MANAGE,produces = ConstantBusinessType.SELECT)
    @PostMapping("/detectFaces")
    public ApiResponse<List<FaceInfo>> detectFaces(@Validated @RequestBody DetectFacesReqVo detectFacesReqVo) {
        return ApiResponse.ok(arcSoftService.detectFaces(detectFacesReqVo));
    }
    /**
     * 人脸特征提取
     * @param extractFaceFeatureReqVo
     * @return
     */
    @ApiOperation(value = "人脸特征提取", notes = ConstantOperatorType.MANAGE,produces = ConstantBusinessType.SELECT)
    @PostMapping("/extractFaceFeature")
    public ApiResponse<FaceFeature> extractFaceFeature(@Validated @RequestBody ExtractFaceFeatureReqVo extractFaceFeatureReqVo) {
        return ApiResponse.ok(arcSoftService.extractFaceFeature(extractFaceFeatureReqVo));
    }
    /**
     * 人脸特征比对
     * @param compareFaceFeatureReqVo
     * @return
     */
    @ApiOperation(value = "人脸特征提取", notes = ConstantOperatorType.MANAGE,produces = ConstantBusinessType.SELECT)
    @PostMapping("/compareFaceFeature")
    public ApiResponse<FaceSimilar> compareFaceFeature(@Validated @RequestBody CompareFaceFeatureReqVo compareFaceFeatureReqVo) {
        return ApiResponse.ok(arcSoftService.compareFaceFeature(compareFaceFeatureReqVo));
    }
    /**
     * 人脸属性检测
     * @param processReqVo
     * @return
     */
    @ApiOperation(value = "人脸属性检测", notes = ConstantOperatorType.MANAGE,produces = ConstantBusinessType.SELECT)
    @PostMapping("/process")
    public ApiResponse<Object> process(@Validated @RequestBody ProcessReqVo processReqVo) {
        return ApiResponse.ok(arcSoftService.process(processReqVo));
    }

}

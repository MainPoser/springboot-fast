package cn.com.datu.springboot.arcsoft.controller;


import cn.com.datu.api.bean.ApiResponse;
import cn.com.datu.open.api.common.constant.ConstantBusinessType;
import cn.com.datu.open.api.common.constant.ConstantOperatorType;
import cn.com.datu.springboot.arcsoft.service.IArcSoftService;
import cn.com.datu.springboot.arcsoft.service.ITestService;
import cn.com.datu.springboot.arcsoft.vo.DetectFacesReqVo;
import cn.com.datu.springboot.arcsoft.vo.ExtractFaceFeatureReqVo;
import cn.com.datu.springboot.arcsoft.vo.TestReqVo;
import com.alibaba.fastjson.JSON;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
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
@RequestMapping("api/rest/v1/ArcSoft")
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
    @GetMapping("/detectFaces")
    public ApiResponse<List<FaceInfo>> detectFaces(@Validated @RequestBody DetectFacesReqVo detectFacesReqVo) {
        return ApiResponse.ok(arcSoftService.detectFaces(detectFacesReqVo));
    }
    /**
     * 人脸特征提取
     * @param extractFaceFeatureReqVo
     * @return
     */
    @ApiOperation(value = "人脸特征提取", notes = ConstantOperatorType.MANAGE,produces = ConstantBusinessType.SELECT)
    @GetMapping("/extractFaceFeature")
    public ApiResponse<FaceFeature> extractFaceFeature(@Validated @RequestBody ExtractFaceFeatureReqVo extractFaceFeatureReqVo) {
        return ApiResponse.ok(arcSoftService.extractFaceFeature(extractFaceFeatureReqVo));
    }

}

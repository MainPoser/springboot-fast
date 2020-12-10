package cn.com.datu.springboot.arcsoft.controller;

import cn.com.datu.springboot.arcsoft.common.constant.ConstantBusinessType;
import cn.com.datu.springboot.arcsoft.common.constant.ConstantOperatorType;
import cn.com.datu.springboot.arcsoft.common.entity.ApiResponse;
import cn.com.datu.springboot.arcsoft.dto.FaceDetectResDTO;
import cn.com.datu.springboot.arcsoft.dto.FaceRecognitionResDTO;
import cn.com.datu.springboot.arcsoft.service.IFaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author tianyao
 */
@Api("arcSoft Api")
@RestController
@CrossOrigin
@RequestMapping("api/rest/v1/Face")
@Slf4j
public class FaceController {
    @Autowired
    private IFaceService faceEngineService;

    @RequestMapping(value = "/faceAdd", method = RequestMethod.POST)
    @ApiOperation(value = "注册人脸", notes = ConstantOperatorType.MANAGE, produces = ConstantBusinessType.INSERT)
    @ResponseBody
    public ApiResponse<String> faceAdd(String file, String faceId, String name) {
        return null;
    }

    @RequestMapping(value = "/faceRecognition", method = RequestMethod.POST)
    @ApiOperation(value = "人脸识别", notes = ConstantOperatorType.MANAGE, produces = ConstantBusinessType.SELECT)
    @ResponseBody
    public ApiResponse<List<FaceRecognitionResDTO>> faceRecognition(String image) {
        return ApiResponse.ok(faceEngineService.faceRecognition(image));
    }

    @RequestMapping(value = "/detectFaces", method = RequestMethod.POST)
    @ApiOperation(value = "人脸属性检测", notes = ConstantOperatorType.MANAGE, produces = ConstantBusinessType.SELECT)
    @ResponseBody
    public ApiResponse<List<FaceDetectResDTO>> detectFaces(String image) {
        return ApiResponse.ok(faceEngineService.detectFaces(image));
    }

    @RequestMapping(value = "/compareFaces", method = RequestMethod.POST)
    @ApiOperation(value = "人脸比对", notes = ConstantOperatorType.MANAGE, produces = ConstantBusinessType.SELECT)
    @ResponseBody
    public ApiResponse<Float> compareFaces(String image1, String image2) {
        return ApiResponse.ok(faceEngineService.compareFaces(image1, image2));
    }

}

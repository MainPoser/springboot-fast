package cn.com.datu.springboot.oauth2server.controller;

import cn.com.datu.springboot.oauth2server.common.constant.ConstantBusinessType;
import cn.com.datu.springboot.oauth2server.common.constant.ConstantOperatorType;
import cn.com.datu.springboot.oauth2server.common.entity.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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


    @RequestMapping(value = "/faceAdd", method = RequestMethod.POST)
    @ApiOperation(value = "注册人脸", notes = ConstantOperatorType.MANAGE, produces = ConstantBusinessType.INSERT)
    @ResponseBody
    public ApiResponse<String> faceAdd(String file, String faceId, String name) {
        return null;
    }


}

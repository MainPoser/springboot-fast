package cn.com.datu.springboot.fast.controller.app.v1;


import cn.com.datu.api.bean.ApiResponse;
import cn.com.datu.open.api.common.constant.ConstantBusinessType;
import cn.com.datu.open.api.common.constant.ConstantOperatorType;
import cn.com.datu.springboot.fast.service.ITestService;
import cn.com.datu.springboot.fast.vo.TestReqVo;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author tianyao
 */
@Api("北向测试接口")
@RestController
@CrossOrigin
@RequestMapping("api/rest/v1/test")
@Slf4j
public class TestController {
    @Autowired
    private ITestService testService;

    @ApiOperation(value = "this is a demo controller", notes = ConstantOperatorType.MOBILE,produces = ConstantBusinessType.INSERT)
    @GetMapping("/testServer")
    public ApiResponse<String> testServer(@Validated @RequestBody TestReqVo testReqVo) {
        log.info("{}", JSON.toJSONString(testReqVo));
        return ApiResponse.ok(testService.testServer(testReqVo));
    }
}

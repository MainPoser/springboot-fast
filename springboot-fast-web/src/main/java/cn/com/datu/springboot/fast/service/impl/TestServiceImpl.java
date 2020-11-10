package cn.com.datu.springboot.fast.service.impl;

import cn.com.datu.springboot.fast.service.ITestService;
import cn.com.datu.springboot.fast.vo.TestReqVo;
import org.springframework.stereotype.Service;

/**
 * testserver实现
 * @author tianyao
 */
@Service
public class TestServiceImpl implements ITestService {
    @Override
    public String testServer(TestReqVo testReqVo) {

        String hello = "helloWorld";
        //todo
        return hello;
    }
}

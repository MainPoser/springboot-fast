package cn.com.datu.springboot.arcsoft.service.impl;

import cn.com.datu.springboot.arcsoft.service.ITestService;
import cn.com.datu.springboot.arcsoft.vo.TestReqVo;
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

package cn.com.datu.springboot.fast.service;

import cn.com.datu.springboot.fast.vo.TestReqVo;

/**
 * @author tianyao
 */
public interface ITestService {
    /**
     * 测试接口
     * @return
     */
    public String testServer(TestReqVo testReqVo);
}

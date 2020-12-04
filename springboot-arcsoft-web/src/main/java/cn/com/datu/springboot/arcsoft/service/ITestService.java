package cn.com.datu.springboot.arcsoft.service;

import cn.com.datu.springboot.arcsoft.vo.TestReqVo;

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

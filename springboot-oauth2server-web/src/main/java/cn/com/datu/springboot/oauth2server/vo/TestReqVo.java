package cn.com.datu.springboot.oauth2server.vo;

import cn.com.datu.springboot.oauth2server.anno.ValidTestVoReq;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author tianyao
 */
@Data
@ValidTestVoReq(message = "年龄不符合校验")
public class TestReqVo {
    @Min(value = 15,message = "年龄不能小于15")
    private int age;
    @Max(value = 30,message = "孩子年龄不能大于30")
    private int childrenAge;
}

package cn.com.datu.springboot.oauth2server.validator;



import cn.com.datu.springboot.oauth2server.anno.ValidTestVoReq;
import cn.com.datu.springboot.oauth2server.vo.TestReqVo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * @author tianyao
 */
public class TestReqVoValidator implements ConstraintValidator<ValidTestVoReq, TestReqVo> {
    @Override
    public boolean isValid(TestReqVo testReqVo, ConstraintValidatorContext constraintValidatorContext) {
        if (testReqVo.getAge()-testReqVo.getChildrenAge()<10){
            return false;
        }
        return true;
    }
}

package cn.com.datu.springboot.oauth2server.anno;


import cn.com.datu.springboot.oauth2server.validator.TestReqVoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 测试controller校验
 * @author tianyao
 */
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = { TestReqVoValidator.class })
public @interface ValidTestVoReq {

    String message() default "ValidNationPassport Validator fail";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

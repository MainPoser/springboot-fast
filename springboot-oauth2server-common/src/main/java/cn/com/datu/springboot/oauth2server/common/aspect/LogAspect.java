package cn.com.datu.springboot.oauth2server.common.aspect;

import cn.com.datu.springboot.oauth2server.common.disruptors.producer.SysOperLogEventProducer;
import cn.com.datu.springboot.oauth2server.common.entity.SysOperLog;
import cn.com.datu.springboot.oauth2server.common.util.RequestUtil;
import cn.com.datu.springboot.oauth2server.common.util.ServletUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 日志aop实现
 *
 * @author tianyao
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    /**
     * 开始时间
     */
    private long startTime = 0L;
    /**
     * 结束时间
     */
    private long endTime = 0L;

    @Autowired
    private SysOperLogEventProducer sysOperLogEventProducer;
    /**
     * 配置织入点
     */
    @Pointcut("@annotation(io.swagger.annotations.ApiOperation)")
    public void logPointCut() {
    }

    @Before("logPointCut()")
    public void doBeforeInServiceLayer(JoinPoint joinPoint) {
        log.debug("doBeforeInServiceLayer");
        startTime = System.currentTimeMillis();
    }


    @After("logPointCut()")
    public void doAfterInServiceLayer(JoinPoint joinPoint) {
        log.debug("doAfterInServiceLayer");
    }

    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //拦截的实体类
        Object target = joinPoint.getTarget();
        Class<?> aClass = target.getClass();
        String name = aClass.getName();
        // 获取request
        HttpServletRequest request = ServletUtils.getRequest();
        SysOperLog sysOperLog = new SysOperLog();
        sysOperLog.setSuccesses(true);
        Throwable throwabled = null;
        // 从注解中获取操作名称、获取响应结果
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwabled = throwable;
            throw throwable;
        } finally {
            //无论是否正常执行，都要执行保存日志
            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            //查找ApiOperation
            if (method.isAnnotationPresent(ApiOperation.class)) {
                ApiOperation log = method.getAnnotation(ApiOperation.class);
                sysOperLog.setDescription(log.value());
                //设置操作类型，本系统设置为从ApiOperation的httpMethod获取
                sysOperLog.setOprType(log.produces());
                //获取当前接口访问的客户端，从ApiOperation注解中的notes中获取，待定
                sysOperLog.setUserAgent(log.notes());
            }
            //获取异常
            if (throwabled != null){
                //说明接口异常
                sysOperLog.setSuccesses(false);
                sysOperLog.setExceptionMsg(ExceptionUtils.getFullStackTrace(throwabled).substring(0,2000));
            }
            endTime = System.currentTimeMillis();
            if (log.isDebugEnabled()) {
                log.debug("doAround>>>result={},spend time：{}", result, endTime - startTime);
            }
            sysOperLog.setBasePath(RequestUtil.getBasePath(request));
            sysOperLog.setIp(RequestUtil.getIpAddr(request));
            sysOperLog.setMethod(request.getMethod());
            if (request.getMethod().equalsIgnoreCase(HttpMethod.GET.toString())) {
                sysOperLog.setParameter(request.getQueryString());
            } else {
                sysOperLog.setParameter(ObjectUtils.toString(request.getParameterMap()));
            }
            if (result != null) {
                sysOperLog.setResult(String.valueOf(HttpStatus.SC_OK));
            }
            //设置请求参数
            setRequestValue(sysOperLog);
            sysOperLog.setSpendTime((int) (endTime - startTime));
            sysOperLog.setStartTime(startTime);
            sysOperLog.setUri(request.getRequestURI());
            sysOperLog.setUrl(ObjectUtils.toString(request.getRequestURL()));
            sysOperLog.setSystemId(name);
            sysOperLog.setUsername(ObjectUtils.toString(request.getUserPrincipal()));
            //todo 获取当前用户，待实现
            sysOperLog.setUsername("username");
            log.info("日志:<{}>", JSON.toJSONString(sysOperLog));
            sysOperLogEventProducer.publish(sysOperLog);
        }
        return result;
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(SysOperLog operLog) throws Exception {
        Map<String, String[]> map = ServletUtils.getRequest().getParameterMap();
        if (MapUtils.isNotEmpty(map)) {
            PropertyPreFilters.MySimplePropertyPreFilter excludefilter = new PropertyPreFilters().addFilter();
            //设置需要忽略的字段
            //excludefilter.addExcludes(EXCLUDE_PROPERTIES);
            String params = JSONObject.toJSONString(map, excludefilter);
            operLog.setParameter(StringUtils.substring(params, 0, 2000));
        }
    }
}

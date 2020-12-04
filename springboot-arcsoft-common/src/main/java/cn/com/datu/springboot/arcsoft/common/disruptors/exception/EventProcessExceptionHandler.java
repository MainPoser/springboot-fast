package cn.com.datu.springboot.arcsoft.common.disruptors.exception;

import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.ExceptionHandler;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author: zhengwq
 * datetime: 2018-11-09 0:28
 * description:
 */
@Component
public class EventProcessExceptionHandler implements ExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(EventProcessExceptionHandler.class);

    @Override
    public void handleEventException(Throwable ex, long sequence, Object event) {
        logger.error("handleEventException:<{}>,sequence:<{}>,event:<{}>",
                ExceptionUtils.getFullStackTrace(ex),
                sequence,
                JSON.toJSONString(event));
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        logger.error("handleEventException:<{}>",
                ExceptionUtils.getFullStackTrace(ex));
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        logger.error("handleEventException:<{}>",
                ExceptionUtils.getFullStackTrace(ex));
    }

}

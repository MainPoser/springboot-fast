package cn.com.datu.springboot.arcsoft.common.disruptors.consumer;

import cn.com.datu.springboot.arcsoft.common.disruptors.event.SysOperLogEvent;
import cn.com.datu.springboot.arcsoft.common.entity.SysOperLog;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tianyao
 */
@Component
@Slf4j
public class SysOperLogRdbEventHandler extends AbstractEventHandler<SysOperLogEvent, SysOperLog> {

    @Override
    protected SysOperLog getValue(SysOperLogEvent sysOperLogEvent) {
        return sysOperLogEvent.getValue();
    }

    @Override
    protected void doBatchHandle(List<SysOperLog> sycList) {
        log.info("storage Rdb:<{}>", JSON.toJSONString(sycList));
    }

    @Override
    public String getName() {
        return "SysOperLogRdbEventHandler";
    }
}

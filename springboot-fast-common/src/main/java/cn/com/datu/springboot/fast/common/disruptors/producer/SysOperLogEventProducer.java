package cn.com.datu.springboot.fast.common.disruptors.producer;

import cn.com.datu.springboot.fast.common.disruptors.event.SysOperLogEvent;
import cn.com.datu.springboot.fast.common.entity.SysOperLog;
import com.lmax.disruptor.RingBuffer;
import lombok.extern.slf4j.Slf4j;

/**
 * 8.创建生产者
 * @author tianyao
 */
@Slf4j
public class SysOperLogEventProducer {

    private final RingBuffer<SysOperLogEvent> ringBuffer;

    public SysOperLogEventProducer(RingBuffer<SysOperLogEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void publish(SysOperLog sysOperLog) {
        // 1.ringBuffer 事件队列 下一个槽
        long sequence = ringBuffer.next();
        try {
            //2.取出空的事件队列
            SysOperLogEvent event = ringBuffer.get(sequence);
            //3.获取事件队列传递的数据
            event.setValue(sysOperLog);
            if (log.isTraceEnabled()) {
                log.trace("operate event. num:{} content:{} ", sequence, event);
            }
        } finally {
            //4.发布事件
            ringBuffer.publish(sequence);
        }
    }
    // step_8 Disruptor 还提供另外一种形式的调用来简化以上操作，并确保 publish 总是得到调用。
   /* static class Translator implements EventTranslatorOneArg<SysOperLogEvent, SysOperLog> {

        @Override
        public void translateTo(SysOperLogEvent sysOperLogEvent, long sequence, SysOperLog sysOperLog) {
            sysOperLogEvent.setValue(sysOperLog);
        }
    }*/

}

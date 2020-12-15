package cn.com.datu.springboot.oauth2server.common.disruptors.config;


import cn.com.datu.springboot.oauth2server.common.disruptors.event.SysOperLogEvent;
import cn.com.datu.springboot.oauth2server.common.disruptors.exception.EventProcessExceptionHandler;
import cn.com.datu.springboot.oauth2server.common.disruptors.factory.SysOperLogEventFactory;
import cn.com.datu.springboot.oauth2server.common.disruptors.producer.SysOperLogEventProducer;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author tianyao
 */
@Component
@Slf4j
public class SysOperLogEventDisruptorConfig {


    @Autowired
    private DisruptorConfig disruptorConfig;

    /**
     * 事件消费者
     */
    @Autowired
    private List<EventHandler<SysOperLogEvent>> eventHandlers;
    /**
     * 异常处理器
     */
    @Autowired
    private EventProcessExceptionHandler eventProcessExceptionHandler;
    /**
     * 生成Disruptor的线程池对象
     */
    private ExecutorService executor;
    /**
     * disruptor对象
     */
    private Disruptor<SysOperLogEvent> sysOperLogEventDisruptor;

    /**
     * 3.创建ringBuffer 大小 ringBufferSize大小一定要是2的N次方
     */

    public static int RING_BUFFER_SIZE;

    private static int CORE_POOL_SIZE;

    private static int MAX_POOL_SIZE;

    private static int QUEUE_SIZE;

    @PostConstruct
    public void init() {
        log.info("=======================================");
        log.info("IdentifyResultOperationEventDisruptorConfig");
        RING_BUFFER_SIZE = disruptorConfig.getRingBufferSize();
        log.info("RING_BUFFER_SIZE:" + RING_BUFFER_SIZE);
        CORE_POOL_SIZE = disruptorConfig.getCorePoolSize();
        log.info("CORE_POOL_SIZE:" + CORE_POOL_SIZE);
        MAX_POOL_SIZE = disruptorConfig.getMaxPoolSize();
        log.info("MAX_POOL_SIZE:" + MAX_POOL_SIZE);
        QUEUE_SIZE = disruptorConfig.getPoolQueueSize();
        log.info("QUEUE_SIZE:" + QUEUE_SIZE);
        log.info("=======================================");
    }

    /**
     * 事件工厂
     *
     * @return
     */
    @Bean
    public SysOperLogEventFactory sysOperLogEventFactory() {
        return new SysOperLogEventFactory();
    }

    @Bean
    public SysOperLogEventProducer sysOperLogEventProducer(RingBuffer<SysOperLogEvent> sysOperLogEventRingBuffer) {
        return new SysOperLogEventProducer(sysOperLogEventRingBuffer);
    }

    @Bean
    public Disruptor<SysOperLogEvent> sysOperLogEventDisruptor(SysOperLogEventFactory sysOperLogEventFactory) {
        // 1.创建一个可缓存的线程 提供线程来出发Consumer 的事件处理
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(QUEUE_SIZE), new ThreadFactoryBuilder().setNameFormat("handle-IdentifyResultOperationEvent-pool-%d").build());
        // 4.创建Disruptor
        sysOperLogEventDisruptor = new Disruptor<>(sysOperLogEventFactory, RING_BUFFER_SIZE, executor,
                ProducerType.MULTI, new SleepingWaitStrategy());
        // 5.连接消费端方法
        //消费者转换为数组
        EventHandler[] handlers = eventHandlers.toArray(new EventHandler[eventHandlers.size()]);
        //多消费者模型,重复消费
        sysOperLogEventDisruptor.handleEventsWith(handlers);
        //多消费者不重复消费模型
        //sysOperLogEventDisruptor.handleEventsWithWorkerPool(sysOperLogEventHandlers);
        sysOperLogEventDisruptor.handleExceptionsWith(eventProcessExceptionHandler);
        // 6.启动
        sysOperLogEventDisruptor.start();
        return sysOperLogEventDisruptor;
    }

    /**
     * 7.创建RingBuffer容器
     *
     * @param sysOperLogEventDisruptor
     * @return
     */
    @Bean
    public RingBuffer<SysOperLogEvent> sysOperLogEventRingBuffer(Disruptor<SysOperLogEvent> sysOperLogEventDisruptor) {
        // 7.创建RingBuffer容器
        return this.sysOperLogEventDisruptor.getRingBuffer();
    }

    /**
     * 10.关闭disruptor和executor
     */
    @PreDestroy
    private void destroy() {
        sysOperLogEventDisruptor.shutdown();
        executor.shutdown();
    }
}

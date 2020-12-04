package cn.com.datu.springboot.arcsoft.common.disruptors.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author tianyao
 */
@Data
@Configuration
public class DisruptorConfig {

    @Value("${spring.disruptor.thread-pool.ring_buffer_size:1024}")
    private int ringBufferSize;

    @Value("${spring.disruptor.thread-pool.core_pool_size:5}")
    private int corePoolSize;

    @Value("${spring.disruptor.thread-pool.max_pool_size:10}")
    private int maxPoolSize;

    @Value("${spring.disruptor.thread-pool.queue_size:512}")
    private int poolQueueSize;

}

package cn.com.datu.springboot.arcsoft.common.disruptors.consumer;

import cn.com.datu.springboot.arcsoft.common.disruptors.config.DisruptorConfig;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author coffee
 * @Classname AbstractEventHandler
 * @Description TODO
 * @Date 2019/9/6 13:30
 */
@Slf4j
public abstract class AbstractEventHandler<EVENT, VALUE> implements EventHandler<EVENT> {


    @Autowired
    protected DisruptorConfig disruptorConfig;

    public static int BATCH_SIZE = 100;

    protected static int RING_BATCH_SIZE;

    protected AtomicLong countSize = new AtomicLong();

    protected AtomicLong errorSize = new AtomicLong();

    protected AtomicLong consumeTime = new AtomicLong();

    protected List<VALUE> sycList = Collections.synchronizedList(new ArrayList<>(BATCH_SIZE));

    @PostConstruct
    public void init() {
        RING_BATCH_SIZE = disruptorConfig.getRingBufferSize();
        doInit();
    }

    protected void doInit() {

    }

    @Override
    public void onEvent(EVENT event, long sequence, boolean endOfBatch) throws Exception {

        if (log.isTraceEnabled()) {
            log.trace("EventHandler[{}] num:{} text:{},sysList:{},endOfBatch:{}", getName(),
                    sequence, event, sycList.size(), endOfBatch);
        }

        VALUE value = getValue(event);
        sycList.add(value);

        if (sycList.size() == BATCH_SIZE) {
            if (log.isTraceEnabled()) {
                log.trace("EventHandler[{}] Begin", getName());
            }
            doBatchHandle(sycList);
            log();
            sycList.clear();
        }

        if (endOfBatch && !sycList.isEmpty()) {
            doBatchHandle(sycList);
            log();
            sycList.clear();
        }

     /*   if(countSize.get()%1000==0 || endOfBatch){
            logger.trace("EventHandler[{}]共计完成:[{}]条",getName(),countSize.get());
        }*/

    }

    protected void log() {
        countSize.addAndGet(sycList.size());
        if (log.isTraceEnabled()) {
            log.trace("EventHandler[{}]complete ,current [{}]", getName()
                    , sycList.size());
        }
    }

    protected abstract VALUE getValue(EVENT event);


    protected abstract void doBatchHandle(List<VALUE> sycList);


    public abstract String getName();
}

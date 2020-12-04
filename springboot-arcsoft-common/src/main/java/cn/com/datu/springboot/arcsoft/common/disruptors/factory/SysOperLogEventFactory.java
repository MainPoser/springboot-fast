package cn.com.datu.springboot.arcsoft.common.disruptors.factory;

import cn.com.datu.springboot.arcsoft.common.disruptors.event.SysOperLogEvent;
import com.lmax.disruptor.EventFactory;
import lombok.Data;

/**
 * @author tianyao
 */
@Data
public class SysOperLogEventFactory implements EventFactory<SysOperLogEvent> {

	@Override
	public SysOperLogEvent newInstance() {
		return new SysOperLogEvent();
	}
}
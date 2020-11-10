package cn.com.datu.springboot.fast.common.disruptors.event;

import cn.com.datu.springboot.fast.common.entity.SysOperLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tianyao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysOperLogEvent {
	private SysOperLog value;
}
package cn.com.datu.springboot.fast.common.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
/**
 * @author tianyao
 */
@Data
@ToString
public class SysOperLog implements Serializable {
    /**
     * 编号
     *
     * @mbg.generated
     */
    private Integer logId;

    /**
     * 操作描述
     *
     * @mbg.generated
     */
    private String description;

    /**
     * 操作用户
     *
     * @mbg.generated
     */
    private String username;

    /**
     * 操作时间
     *
     * @mbg.generated
     */
    private Long startTime;

    /**
     * 消耗时间
     *
     * @mbg.generated
     */
    private Integer spendTime;

    /**
     * 根路径
     *
     * @mbg.generated
     */
    private String basePath;

    /**
     * URI
     *
     * @mbg.generated
     */
    private String uri;

    /**
     * URL
     *
     * @mbg.generated
     */
    private String url;

    /**
     * 请求类型
     *
     * @mbg.generated
     */
    private String method;

    /**
     * 用户标识
     *
     * @mbg.generated
     */
    private String userAgent;

    /**
     * IP地址
     *
     * @mbg.generated
     */
    private String ip;


    private String parameter;

    private String result;

    /**
     * 操作类型
     */
    private String oprType;

    /**
     * 所属系统
     */
    private String systemId;


    /**
     * 会话标识
     */
    private String sessionId;
    /**
     * 是否调用成功
     */
    private boolean successes;
    /**
     * 出错异常
     */
    private String exceptionMsg;

    private static final long serialVersionUID = 1L;
}
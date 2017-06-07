package com.ziroom.ferrari.exception;

/**
 * 数据变更消息发送异常
 */
@SuppressWarnings("serial")
public class DataChangeMessageSendException extends RuntimeException {

    public DataChangeMessageSendException(String message) {
	super(message);
    }

    public DataChangeMessageSendException(String message, Throwable cause) {
	super(message, cause);
    }
}

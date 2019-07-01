package com.jimi.ozone_server.comm.exception;

/**
 * 自定义异常类
 * <br>
 * <b>2019年6月29日</b>
 * @author 几米物联自动化部-韦姚忠
 *
 */
public class CustomException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private int code;
	
	private String message;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CustomException(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
	public CustomException() {
		
	}
	
	
	

}

package com.jimi.ozone_server.comm.model;

import java.io.Serializable;
/**
 * 返回结果实体类
 * <br>
 * <b>2019年6月21日</b>
 * @author 几米物联自动化部-韦姚忠
 *
 */
public class Result implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer code;
	
	private Object data;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public Result() {
		
	}

	public Result(Integer code, Object data) {
		super();
		this.code = code;
		this.data = data;
	}

	@Override
	public String toString() {
		return "Result [code=" + code + ", data=" + data + "]";
	}
	
	
}

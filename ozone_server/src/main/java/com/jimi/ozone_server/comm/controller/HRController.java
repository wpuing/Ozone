package com.jimi.ozone_server.comm.controller;

import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jimi.ozone_server.comm.model.Result;
import com.jimi.ozone_server.comm.service.HRService;
/**
 * 人力资源显示控制器
 * <br>
 * <b>2019年6月21日</b>
 * @author 几米物联自动化部-韦姚忠
 *
 */

public class HRController extends Controller {
	
	public static HRService hrService = Enhancer.enhance(HRService.class);
	
	
	/*
	 * 查询人力资源
	 */
	public void findHrInfo() {
		Result result = hrService.findHrInfo();
		renderJson(result);
		
	}

}

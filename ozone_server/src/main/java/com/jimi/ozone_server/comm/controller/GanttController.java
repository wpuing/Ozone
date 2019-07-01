package com.jimi.ozone_server.comm.controller;

import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jimi.ozone_server.comm.model.Result;
import com.jimi.ozone_server.comm.service.GanttService;
import com.jimi.ozone_server.comm.service.base.BaseMethodService;

/**
 * 甘特图管理控制器
 * <br>
 * <b>2019年6月21日</b>
 * @author 几米物联自动化部-韦姚忠
 *
 */
public class GanttController extends Controller {

	private static GanttService ganttService = Enhancer.enhance(GanttService.class);
	
	
	/*
	 * 甘特图显示
	 */
	public void findPojoGantt() {
		int id = Integer.parseInt(getPara("ganttId"));
		Result result = ganttService.findPojoGantt(id);
		renderJson(result);
		
	}
	
	/*
	 * 查询甘特图
	 */
	public void findGanttAll() {
		String name = getPara("name");
		System.out.println("控制器name："+name);
		Result result = ganttService.findAllGantt(name);
		renderJson(result);
		
	}
	
	
	/*
	 * 删除甘特图
	 */
	public void deleteGantt() {
		Result result = new Result(400, "删除失败");
		String ids = getPara("ids");
		String[] newNumbers = ids.split(",");
		for (String idStr : newNumbers) {
			Integer newId = BaseMethodService.isParameterType(idStr);
			if (newId!=null) {
				result = ganttService.deleteGantt(newId);
			}
		}
		renderJson(result);
		
	}
	
	
	/*
	 * 添加甘特图
	 */
	public void addGantt() {
		String name = getPara("name");
		String responsible = getPara("responsible");
		String cycleStr = getPara("cycle");
		int cycle = 0;
		if (cycleStr!=null) {
			Integer newCycle =BaseMethodService.isParameterType(cycleStr);
			if (newCycle!=null) {
				cycle = newCycle;
			}
		}
		String remark = getPara("remark");
		System.out.println("控制器responsible："+responsible);
		System.out.println("控制器name："+name);
		System.out.println("控制器cycle："+cycleStr);
		System.out.println("控制器remark："+remark);
		Result result = ganttService.addGantt(name, responsible, cycle, remark);
		renderJson(result);
	}
	
	
	/*
	 * 修改甘特图
	 */
	public void updateGantt() {
		String idStr = getPara("id");
		int id = 0;
		if (idStr!=null) {
			Integer newId =BaseMethodService.isParameterType(idStr);
			if (newId!=null) {
				id = newId;
			}
		}
		String name = getPara("name");
		String responsible = getPara("responsible");
		String cycleStr = getPara("cycle");
		int cycle = 0;
		if (cycleStr!=null) {
			Integer newCycle =BaseMethodService.isParameterType(cycleStr);
			if (newCycle!=null) {
				cycle = newCycle;
			}
		}
		String remark = getPara("remark");
		System.out.println("控制器responsible："+responsible);
		System.out.println("控制器name："+name);
		System.out.println("控制器cycle："+cycleStr);
		System.out.println("控制器remark："+remark);
		Result result = ganttService.updateGantt(id, name, responsible, cycle, remark);
		renderJson(result);
	}
}

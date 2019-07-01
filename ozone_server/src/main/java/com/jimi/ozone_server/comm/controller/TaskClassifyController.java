package com.jimi.ozone_server.comm.controller;

import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jimi.ozone_server.comm.model.Result;
import com.jimi.ozone_server.comm.service.TaskClassifyService;
import com.jimi.ozone_server.comm.service.base.BaseMethodService;
/**
 * 任务分类管理控制器
 * <br>
 * <b>2019年6月21日</b>
 * @author 几米物联自动化部-韦姚忠
 *
 */
public class TaskClassifyController extends Controller {

	private static TaskClassifyService taskClassifyService = Enhancer.enhance(TaskClassifyService.class);
	
	
	/*
	 * 查询任务分类
	 */
	public void findTaskClassifyAll() {
		String name = getPara("name");
		String ganttStr = getPara("gantt");
		int gantt = 0;
		if (ganttStr!=null&&!"".equals(ganttStr)) {
			gantt = Integer.parseInt(ganttStr);
		}
		renderJson(taskClassifyService.findAllTaskClassify(name, gantt));
	}
	
	
	/*
	 * 删除任务分类
	 */
	public void deleteTaskClassify() {
		
		Result result = new Result(400, "删除失败");
		String ids = getPara("ids");
		String[] newNumbers = ids.split(",");
		for (String idStr : newNumbers) {
			Integer newId = BaseMethodService.isParameterType(idStr);
			if (newId!=null) {
				result = taskClassifyService.deleteTaskClassify(newId);
			}
		}
		renderJson(result);
		
		
		
//		String id_str = getPara("ids");
//		String[] ids = id_str.split(",");
//		Result result = null;
//		for (String id : ids) {
//			Integer newId = BaseMethodService.isParameterType(id);
//			if (newId!=null) {
//				result = taskClassifyService.deleteTaskClassify(newId);
//			}else {
//				result = new Result(400, "格式不正确");
//			}
//			System.out.println("控制层返回值："+result.getCode()+",信息: " +result.getData()) ;
//		}
//		renderJson(result);
	}


	
	
	
	/*
	 * 添加任务分类
	 */
	public void addTaskClassify() {
		
		String name = getPara("name");
		String remark = getPara("remark");
		String ganttIdStr = getPara("gantt");
		int gantt = 0;
		if (ganttIdStr!=null&&!"".equals(ganttIdStr)) {
			gantt = Integer.parseInt(ganttIdStr);
		}
		Result result = taskClassifyService.addTaskClassify(name, remark, gantt);
		renderJson(result);
		
	}
	
	
	/*
	 * 修改任务分类
	 */
	public void updateTaskClassify() {
		int taskClassifyId = 0;
		String taskClassifyIdStr = getPara("id");
		if (taskClassifyIdStr!=null&&!"".equals(taskClassifyIdStr)) {
			taskClassifyId = Integer.parseInt(taskClassifyIdStr);
		}
		String name=getPara("name");;
		String remark=getPara("remark");;
		String ganttIdStr = getPara("gantt");
		int gantt = 0;
		if (ganttIdStr!=null&&!"".equals(ganttIdStr)) {
			gantt = Integer.parseInt(ganttIdStr);
		}
		Result result = taskClassifyService.updateTaskClassify(taskClassifyId, name, remark, gantt);
		renderJson(result);
	}
}

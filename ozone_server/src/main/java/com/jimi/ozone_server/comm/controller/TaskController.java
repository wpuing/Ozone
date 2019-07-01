package com.jimi.ozone_server.comm.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jimi.ozone_server.comm.model.Result;
import com.jimi.ozone_server.comm.service.TaskService;
import com.jimi.ozone_server.comm.service.base.BaseMethodService;
/**
 * 任务管理控制器
 * <br>
 * <b>2019年6月21日</b>
 * @author 几米物联自动化部-韦姚忠
 *
 */
public class TaskController extends Controller {
	private static TaskService taskService = Enhancer.enhance(TaskService.class);
	
	/*
	 * 添加任务
	 */
	public void addTaskInfoAll() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		
		boolean is_coerce = Boolean.parseBoolean(getPara("is_coerce"));
		System.out.println("控制器接收is_coerce："+is_coerce);
		
		String predecessorTasks = getPara("predecessorTasks");
		System.out.println("控制器接收predecessorTasks："+predecessorTasks);
		
		String executor = getPara("executor");
		System.out.println("控制器接收executor："+executor);
		
		String name = getPara("name");
		System.out.println("控制器接收name："+name);
		
		int gantt = Integer.parseInt(getPara("gantt"));
		System.out.println("控制器接收gantt："+gantt);
		
		int task_classify = Integer.parseInt(getPara("task_classify"));
		System.out.println("控制器接收task_classify："+task_classify);
		
		String remark = getPara("remark");
		System.out.println("控制器接收remark："+remark);
		
		Date actual_end_date = null;
		Date plans_end_date = null;
		Date start_date = null;
		try {
			actual_end_date = sdf.parse(getPara("actual_end_date"));
			System.out.println("控制器接收actual_end_date："+actual_end_date);
			start_date = sdf.parse(getPara("start_date"));
			System.out.println("控制器接收start_date："+start_date);
			plans_end_date = sdf.parse(getPara("plans_end_date"));
			System.out.println("控制器接收plans_end_date："+plans_end_date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Result result = taskService.addTask(name, start_date, plans_end_date, actual_end_date, task_classify, gantt, remark, predecessorTasks, executor, is_coerce);
		
		renderJson(result);
	}


	
	
	/*
	 * 修改任务
	 */
	public void updateTaskInfo(Object taskId, String numbers) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		
		boolean is_coerce = Boolean.parseBoolean(getPara("is_coerce"));
		System.out.println("控制器接收is_coerce："+is_coerce);
		
		String predecessorTasks = getPara("predecessorTasks");
		System.out.println("控制器接收predecessorTasks："+predecessorTasks);
		
		String executor = getPara("executor");
		System.out.println("控制器接收executor："+executor);
		int id = 0;
		String idStr = getPara("id");
		if (idStr!=null) {
			Integer newId =BaseMethodService.isParameterType(idStr);
			if (newId!=null) {
				id = newId;
			}
		}
		System.out.println("控制器接收name："+id);
		
		String name = getPara("name");
		System.out.println("控制器接收name："+name);
		
		int gantt = Integer.parseInt(getPara("gantt"));
		System.out.println("控制器接收gantt："+gantt);
		
		int task_classify = Integer.parseInt(getPara("task_classify"));
		System.out.println("控制器接收task_classify："+task_classify);
		
		String remark = getPara("remark");
		System.out.println("控制器接收remark："+remark);
		
		Date actual_end_date = null;
		Date plans_end_date = null;
		Date start_date = null;
		try {
			actual_end_date = sdf.parse(getPara("actual_end_date"));
			System.out.println("控制器接收actual_end_date："+actual_end_date);
			start_date = sdf.parse(getPara("start_date"));
			System.out.println("控制器接收start_date："+start_date);
			plans_end_date = sdf.parse(getPara("plans_end_date"));
			System.out.println("控制器接收plans_end_date："+plans_end_date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Result result = taskService.updateTask(id,name, start_date, plans_end_date, actual_end_date, task_classify, gantt, remark, predecessorTasks, executor, is_coerce);
		
		renderJson(result);
		
		
	}




	
	
	
	/*
	 * 查询任务
	 */
	public void findTaskInfoAll() {
		String name = getPara("name");
		Result result = taskService.findAllTask(name);
		renderJson(result);
		
	}
	
	
	/*
	 * 删除任务
	 */
	public void deleteTaskInfoAll() {
		Result result = new Result(400, "删除失败");
		String ids = getPara("ids");
		String[] newNumbers = ids.split(",");
		for (String idStr : newNumbers) {
			Integer newId = BaseMethodService.isParameterType(idStr);
			if (newId!=null) {
				result = taskService.deleteTask(newId);
			}
		}
		renderJson(result);
		
		
//		String id_str = getPara("ids");
//		String[] ids = id_str.split(",");
//		Result result = null;
//		for (String id : ids) {
//			result = taskService.deleteTask(Integer.parseInt(id));
//			System.out.println("控制层返回值："+result.getCode()+",信息: " +result.getData()) ;
//		}
//		renderJson(result);
		
		
	}
	





	

}

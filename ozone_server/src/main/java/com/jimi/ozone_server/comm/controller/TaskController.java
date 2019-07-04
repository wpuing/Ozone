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
		boolean isCoerce = Boolean.parseBoolean(getPara("is_coerce"));
		String predecessorTasks = getPara("predecessorTasks");
		String executor = getPara("executor");
		String name = getPara("name");
		int gantt = Integer.parseInt(getPara("gantt"));
		int taskClassify = Integer.parseInt(getPara("task_classify"));
		String remark = getPara("remark");
		Date actualEndDate = null;
		Date plansEndDate = null;
		Date startDate = null;
		String startDateStr = getPara("start_date");
		String actualEndDateStr = getPara("actual_end_date");
		String plansEndDateStr = getPara("plans_end_date");
		try {
			if (startDateStr !=null) {
				startDate = sdf.parse(getPara("start_date"));
			}
			if (actualEndDateStr !=null) {
				actualEndDate = sdf.parse(getPara("actual_end_date"));
			}
			if (plansEndDateStr !=null) {
				plansEndDate = sdf.parse(getPara("plans_end_date"));
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Result result = taskService.addTask(name, startDate, plansEndDate, actualEndDate, taskClassify, gantt, remark, predecessorTasks, executor, isCoerce);
		renderJson(result);
	}

	
	/*
	 * 修改任务
	 */
	public void updateTaskInfo() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		boolean isCoerce = Boolean.parseBoolean(getPara("is_coerce"));
		String predecessorTasks = getPara("predecessorTasks");
		String executor = getPara("executor");
		int id = 0;
		String idStr = getPara("id");
		if (idStr!=null) {
			Integer newId =BaseMethodService.handleParameterType(idStr);
			if (newId!=null) {
				id = newId;
			}
		}
		String name = getPara("name");
		int gantt = 0;
		String ganttStr = getPara("gantt");
		if (ganttStr!=null) {
			Integer newId =BaseMethodService.handleParameterType(ganttStr);
			if (newId!=null) {
				gantt = newId;
			}
		}
		int taskClassify = 0;
		String taskClassifytStr = getPara("task_classify");
		if (taskClassifytStr!=null) {
			Integer newId =BaseMethodService.handleParameterType(taskClassifytStr);
			if (newId!=null) {
				taskClassify = newId;
			}
		}
		String remark = getPara("remark");
		Date actualEndDate = null;
		Date plansEndDate = null;
		Date startDate = null;
		String startDateStr = getPara("start_date");
		String actualEndDateStr = getPara("actual_end_date");
		String plansEndDateStr = getPara("plans_end_date");
		try {
			if (startDateStr !=null) {
				startDate = sdf.parse(getPara("start_date"));
			}
			if (actualEndDateStr !=null) {
				actualEndDate = sdf.parse(getPara("actual_end_date"));
			}
			if (plansEndDateStr !=null) {
				plansEndDate = sdf.parse(getPara("plans_end_date"));
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Result result = taskService.updateTask(id,name, startDate, plansEndDate, actualEndDate, taskClassify, gantt, remark, predecessorTasks, executor, isCoerce);
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
			Integer newId = BaseMethodService.handleParameterType(idStr);
			if (newId!=null) {
				result = taskService.deleteTask(newId);
			}
		}
		renderJson(result);
	}
	
}

package com.jimi.ozone_server.comm.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jimi.ozone_server.comm.constant.DeleteStatus;
import com.jimi.ozone_server.comm.model.PersonnelTask;
import com.jimi.ozone_server.comm.model.Result;
import com.jimi.ozone_server.comm.model.Task;
import com.jimi.ozone_server.comm.model.TaskRelation;
import com.jimi.ozone_server.comm.service.base.BaseMethodService;

/**
 * 任务管理业务类
 * <br>
 * <b>2019年6月21日</b>
 * @author 几米物联自动化部-韦姚忠
 *
 */
public class TaskService{
	

	/**
	 * 删除任务
	 * @param id 任务id
	 * @return 提示用户
	 */
	public Result deleteTask(int id) {
		String sql = "SELECT id,name FROM task WHERE is_delete < 1 AND id = "+id;
		String tableName = "task";
		//调用公共删除方法
		Result result = BaseMethodService.deleteTableRecord(tableName, sql);
		//调用删除前置任务方法
		deleteTaskRelation(id);
		//调用删除人员任务方法
		deletePersonnelTask(id);
		return result;
	}
	
	
	/**
	 * 查询任务
	 * @param name 任务名称
	 * @return    返回信息提示用户
	 */
	public Result findAllTask(String name) {
		String noValueSql = "SELECT id,name,start_date,plans_end_date,actual_end_date,task_classify FROM task WHERE is_delete <1";
		String  valueSql= "SELECT id,name,start_date,plans_end_date,actual_end_date,task_classify FROM task WHERE is_delete <1 AND name LIKE '%"+name+"%'";
		List<Record> personnels = null;
		// 判断name不为空
		if (!"".equals(name) && name != null) {
			// 带值查询
			personnels = Db.find(valueSql);
		} else {
			personnels = Db.find(noValueSql);
		}
		List<Record> tasks = new ArrayList<>();
		for (int i = 0; i < personnels.size(); i++) {
			Record record = personnels.get(i);
			int taskId = record.get("id");
			int taskClassifyId = record.get("task_classify");
			List<Record> taskClassifys = Db.find("SELECT name,remark FROM task_classify WHERE is_delete<1 AND id ="+taskClassifyId);
			List<Record> taskRelations = Db.find("SELECT id,predecessor_task,current_task FROM task_relation WHERE is_delete<1 AND current_task ="+taskId);
			List<Record> personnelTasks = Db.find("SELECT id,personnel,task,remark FROM personnel_task WHERE is_delete<1 AND task ="+taskId);
			if (taskClassifys.size()>0) {
				record.set("task_classify_name", taskClassifys.get(0).get("name"));
			}else {
				record.set("task_classify_name", new ArrayList<>());
			}
			record.set("predecessorTasks", personnelTasks);
			record.set("taskRelations", taskRelations);
			tasks.add(record);
		}
		return new Result(200, personnels);
	}
	
	
	/**
	 * 添加任务
	 * @param name            任务名称
	 * @param start_date      开始时间
	 * @param plans_end_date  计划结束时间
	 * @param actual_end_date 实际结束时间
	 * @param task_classify   任务外键
	 * @param gantt           甘特图外键
	 * @param remark          备注
	 * @param is_coerce       提示用户是否强制添加任务到执行人		
	 * @param predecessorTasks人员任务		
	 * @param executor        前置任务		
	 * @return
	 */
	public Result addTask(String name,Date startDate,Date plansEndDate,
			Date actualEndDate,int taskClassifyId,int gantt,String remark,
			String predecessorTasks,String executor,boolean isCoerce) {
		if (name==null&&"".equals(name)&&startDate==null&&plansEndDate==null&&taskClassifyId<=0&&gantt<=0) {
			return new Result(400, "数据不完整");
		}
		
		List<Record> tasks = Db.find("SELECT * from task WHERE is_delete < 1 AND name ='"+name+"'");
		if (tasks.size()>0) {
			return new Result(400, "存在该任务");
		}
		boolean code = false;
		Result result = new Result(400, "添加失败");
		//遍历添加人员任务
		if (executor!=null&&!"".equals(executor)) {
			//存在人员任务
			String[] newExecutor = executor.split(",");
			if (newExecutor.length>0) {
				for (String personnel : newExecutor) {
					Integer newId = BaseMethodService.handleParameterType(personnel);
					if (newId!=null) {
						result = handleDateInfo( newId, startDate, plansEndDate, isCoerce);
						code = true;
					}
				}
			}
		}else {
			//不存在人员任务
			result = addTaskInfo(name, startDate, plansEndDate, actualEndDate, taskClassifyId, gantt, remark, predecessorTasks, executor);
			code = false;
		}
		if (result!=null&&code==true) {
			if (result.getCode()==200) {
				result = addTaskInfo(name, startDate, plansEndDate, actualEndDate, taskClassifyId, gantt, remark, predecessorTasks, executor);
				}
			}
		return result;
	}
	
	
	/**
	 * 修改任务
	 * @param name            任务名称
	 * @param start_date      开始时间
	 * @param plans_end_date  计划结束时间
	 * @param actual_end_date 实际结束时间
	 * @param task_classify   任务外键
	 * @param gantt           甘特图外键
	 * @param remark          备注
	 * @param is_coerce       提示用户是否强制添加任务到执行人		
	 * @param predecessorTasks人员任务		
	 * @param executor        前置任务		
	 * @return
	 */
	public Result updateTask(int id,String name,Date startDate,Date plansEndDate,
			Date actualEndDate,int taskClassify,int gantt,String remark,
			String predecessorTasks,String executor,boolean isCoerce) {
		if (name==null&&"".equals(name)&&startDate==null&&plansEndDate==null&&taskClassify<=0&&gantt<=0) {
			return new Result(400, "数据不完整");
		}
		Task task = Task.dao.findById(id);
		if (task==null) {
			return new Result(400, "不存在该任务");
		}
		Record taskName = Db.findFirst("SELECT * from task WHERE is_delete < 1 AND name ='"+name+"'");
		if (taskName!=null) {
			return new Result(400, "存在该任务");
		}
		Result result = new Result(400, "修改失败");
		//判断时间是否为空
		if (startDate==null) {
			startDate = task.getStartDate();
		}
		if (plansEndDate==null) {
			plansEndDate = task.getPlansEndDate();
		}
		//遍历添加人员任务
		if (executor!=null&&!"".equals(executor)) {
			//存在人员任务
			String[] newExecutor = executor.split(",");
			for (String personnel : newExecutor) {
				Integer newId = BaseMethodService.handleParameterType(personnel);
				if (newId!=null) {
					result = handleDateInfo(newId, startDate, plansEndDate, isCoerce);
				}
			}
		}
		else {
			//将当前任务的人员任务删除
			updatePersonnelTask(id);
			result.setCode(200);
		}
		if (result!=null) {
			if (result.getCode()==200) {
				//依次将之前的人员任务和前置任务删除
				updatePersonnelTask(id);
				updateTaskRelation(id);
				//执行修改
				result = updateTaskInfo(id,name, startDate, plansEndDate, actualEndDate, taskClassify, gantt, remark, predecessorTasks, executor);
			}
		}
		return result;
	}
	
	
	/*
	 *添加任务信息，前置任务信息，人员任务信息
	 */
	public Result addTaskInfo(String name,Date startDate,Date plansEndDate,Date actualEndDate,
			int taskClassify,int gantt,String remark,String predecessorTasks,String executor) {
		Task task = handleTaskInfo(0, name, startDate, plansEndDate, actualEndDate, taskClassify, gantt, remark);
		boolean b = task.save();
		//判断添加任务成功
		if (b) {
			//得到当前任务的id
			Record record = findOneTask(name);
			Object taskId = record.get("id");
			if (predecessorTasks!=null&&!"".equals(predecessorTasks)) {
				String[]  taskRelations= predecessorTasks.split(",");
				//遍历添加前置任务
				for (String taskRelation : taskRelations) {
					addTaskRelation(Integer.parseInt(taskRelation),(int) taskId);
				}
			}
			if (executor!=null&&!"".equals(executor)) {
				//遍历添加人员任务
				String[] newExecutor = executor.split(",");
				for (String personnel : newExecutor) {
					addPersonnelTask((int) taskId, Integer.parseInt(personnel));
				}
				
			}
			return new Result(200, "添加任务信息成功");
		}else {
			return new Result(400, "添加任务信息失败");
		}
	}
	
	
	/*
	 *修改任务信息，前置任务信息，人员任务信息
	 */
	public Result updateTaskInfo(int id,String name,Date startDate,Date plansEndEate,Date actualEndDate,
			int taskClassifyId,int gantt,String remark,String taskR,String executor) {
		Task task = handleTaskInfo(id, name, startDate, plansEndEate, actualEndDate, taskClassifyId, gantt, remark);
		boolean b = task.update();
		if (b) {
			if (taskR!=null&&!"".equals(taskR)) {
				String[]  newTaskR= taskR.split(",");
				//遍历添加前置任务
				for (String taskRelation : newTaskR) {
					Integer newId = BaseMethodService.handleParameterType(taskRelation);
					if (newId!=null) {
						addTaskRelation(newId,id);
					}
				}
			}
			//遍历添加人员任务
			if (executor!=null&&!"".equals(executor)) {
				String[] newPredecessorTasks = executor.split(",");
				for (String personnel : newPredecessorTasks) {
					Integer newId = BaseMethodService.handleParameterType(personnel);
					if (newId!=null) {
						addPersonnelTask(id, newId);
					}
				}
			}
		}
		return new Result(200, "修改任务信息成功");
	
	}
	
	
	/**
	 * 删除前置任务
	 * @param id 任务id
	 * @return
	 */
	public Result deleteTaskRelation(int id) {
		List<Record> taskRelations = Db.find("SELECT * FROM task_relation WHERE is_delete <1 AND current_task = "+id);
		if (taskRelations.size()<=0) {
			return new Result(400, "该任务没有前置任务！");
		}
		boolean b = false;
		for (int i = 0; i < taskRelations.size(); i++) {
			Record record = taskRelations.get(i);
			//有值就执行删除方法
			record .set("is_delete", DeleteStatus.IS_DELETE_OFF);
			b = Db.update("task_relation", record);
		}
		if (b) {
			return new Result(200, "删除成功");
		}else {
			return new Result(400, "删除失败");
		}
		
	}
	
	
	/**
	 * 删除人员任务
	 * @param id 任务id
	 * @return
	 */
	public Result deletePersonnelTask(int id) {
		List<Record> personnelTasks = Db.find("SELECT id FROM personnel_task WHERE is_delete < 1 AND task = "+id);
		if (personnelTasks.size()<=0) {
			return new Result(400, "该任务没有人员任务！");
		}
		boolean b = false;
		for (int i = 0; i < personnelTasks.size(); i++) {
			Record record = personnelTasks.get(i);
			//有值就执行删除方法
			record .set("is_delete", DeleteStatus.IS_DELETE_OFF);
			b = Db.update("personnel_task", record);
		}
		if (b) {
			return new Result(200, "删除成功");
		}else {
			return new Result(400, "删除失败");
		}
		
	}
	
	
	/**
	 * 添加前置任务
	 * @param predecessor_task 前置任务id（任务外键）
	 * @param current_task     当前任务id（任务外键）
	 * @return  返回信息提示用户
	 */
	public Result addTaskRelation(int predecessorTaskId,int currentTask) {
		List<Record> taskRelations = Db.find("SELECT * FROM task_relation WHERE is_delete <1 AND current_task = "+currentTask);
		if (taskRelations.size()>0) {
			for (int i = 0; i < taskRelations.size(); i++) {
				Object findPredecessorTask = taskRelations.get(i).get("predecessor_task");
				//当查询出来的前置任务id是空，转为0
				if (findPredecessorTask ==null) {
					predecessorTaskId = 0;
				}
				boolean b = findPredecessorTask.equals(predecessorTaskId);
				if (b) {
					return new Result(400, "存在该该前置任务");
				}
			}
		}
		TaskRelation taskRelation = new TaskRelation();
		taskRelation.setPredecessorTask(predecessorTaskId);
		taskRelation.setCurrentTask(currentTask);
		taskRelation.setIsDelete(DeleteStatus.IS_DELETE_ON);
		boolean b = taskRelation.save();
		if (b) {
			return new Result(200, "添加成功");
		}else {
			return new Result(400, "添加失败");
		}
		
		
	}
	
	
	/**
	 * 修改前置任务
	 * @param predecessor_task 前置任务id（任务外键）
	 * @param current_task     当前任务id（任务外键）
	 * @return  返回信息提示用户
	 */
	public Result updateTaskRelation(int currentTask) {
		Result result = new Result(400, "修改前置任务失败");
		List<Record> taskRelations = Db.find("SELECT * FROM task_relation WHERE is_delete <1 AND current_task = "+currentTask);
		if (taskRelations.size()>0) {
				for (Record record : taskRelations) {
					record.set("is_delete", DeleteStatus.IS_DELETE_OFF);
					boolean b = Db.update("task_relation",record);
					if (b) {
						result = new Result(200, "修改前置任务成功");
					}
				}
			
		}
		return result;
	}
	
	
	/**
	 * 添加人员任务
	 * @param task      任务id
	 * @param personnel 人员id
	 * @return   返回信息提示用户
	 */
	public Result addPersonnelTask(int task,int personnel) {
		List<Record> personnelTasks = Db.find("SELECT * FROM personnel_task WHERE is_delete < 1 AND task = "+task);
		if (personnelTasks.size()>0) {
			for (int i = 0; i < personnelTasks.size(); i++) {
				boolean b = personnelTasks.get(i).get("personnel").equals(personnel);
				if (b) {
					return new Result(400, "存在该人员任务");
				}
			}
		}
		PersonnelTask personnelTask = new PersonnelTask();
		personnelTask.setTask(task);
		personnelTask.setPersonnel(personnel);
		personnelTask.setIsDelete(DeleteStatus.IS_DELETE_ON);
		boolean b = personnelTask.save();
		if (b) {
			return new Result(200, "添加成功");
		}else {
			return new Result(400, "添加失败");
		}
		
	}
	
	
	/**
	 * 修改人员任务
	 * @param task      任务id
	 * @param personnel 人员id
	 * @return   返回信息提示用户
	 */
	public Result updatePersonnelTask(int task) {
		Result result = new Result(400, "修改人员任务失败");
		List<Record> personnelTasks = Db.find("SELECT * FROM personnel_task WHERE is_delete < 1 AND task = "+task);
		if (personnelTasks.size()>0) {
			for (int i = 0; i < personnelTasks.size(); i++) {
				Record record = personnelTasks.get(i);
				record.set("is_delete", DeleteStatus.IS_DELETE_OFF);
				boolean b = Db.update("personnel_task" , record);
				if (b) {
					result = new Result(200, "修改人员任务成功");
				}
			}
		}
		return result;
		
	}
	
	
	/**
	 * 查询出人员任务表不为空时
	 * @param personnel 人员id
	 * @param front_start_date     要添加任务开始时间
	 * @param front_plans_end_date 要添加任务计划结束时间
	 * @return 返回布尔值判断
	 */
	public Result handleDateInfo(int personnel,Date frontStartDate,Date frontPlansEndEate,boolean isCoerce) {
		//查询人员任务
		List<Record> personnelTasks = Db.find("SELECT * FROM personnel_task WHERE is_delete < 1 AND personnel = " +personnel);
		//任务id
		int taskId = 0;
		//查询出的任务开始时间
		Date startDate = null;
		//查询出的任务计划结束时间
		Date plansEndDate = null;
		//时间判断值
		boolean dateCode = false;
		for (int i = 0; i < personnelTasks.size(); i++) {
			taskId = personnelTasks.get(i).get("task");
			//查询任务
			List<Record> tasks = Db.find("SELECT id,start_date,plans_end_date from task WHERE is_delete < 1 AND id = "+taskId);
			for (Record task : tasks) {
				startDate = task.get("start_date");
				plansEndDate = task.get("plans_end_date");
				//调用判断时间方法
				dateCode = compareDate(frontStartDate, frontPlansEndEate, startDate, plansEndDate);
				if (dateCode==false) {
					if (dateCode==isCoerce) {
						return new Result(210,"时间冲突！");
					}
				}
			}
		}
		return new Result(200,"操作成功");
	}
	
	
	/**
	 * 根据任务名称信息
	 * @param name 任务名称
	 * @return 任务信息
	 */
	public Record findOneTask(String name) {
		Record task = Db.findFirst("SELECT id,name FROM task WHERE is_delete <1 AND name ='"+name+"'");
		return task;
	}
	
	
	/*
	 * 判断前端要修改的任务信息
	 */
	private Task handleTaskInfo(int id,String name,Date startDate,Date plansEndDate,
			Date actualEndDate,int taskClassify,int gantt,String remark) {
		Task task = new Task();
		if (id>0) {
			task = Task.dao.findById(id);
		}
		if (id>0) {
			task.setId(id);
		}
		if (name!=null&&!"".equals(name)) {
			task.setName(name);
		}
		if (startDate!=null) {
			task.setStartDate(startDate);
		}
		if (plansEndDate!=null) {
			task.setPlansEndDate(plansEndDate);
		}
		if (actualEndDate!=null) {
			task.setActualEndDate(actualEndDate);
		}
		if (taskClassify>0) {
			task.setTaskClassify(taskClassify);
		}
		if (gantt>0) {
			task.setGantt(gantt);
		}
		if (remark!=null&&!"".equals(remark)) {
			task.setRemark(remark);
		}
		task.setIsDelete(DeleteStatus.IS_DELETE_ON);
		return task;
	}
	
	
	/**
	 * 将时间进行判断是否冲突
	 * @param frontStartDate   添加的任务的开始时间
	 * @param frontPlansEndEate添加的任务的结束时间
	 * @param startDate        查询的任务的开始时间
	 * @param plansEndDate     查询的任务的结束时间
	 * @return
	 */
	private boolean compareDate(Date frontStartDate,Date frontPlansEndEate,Date startDate,Date plansEndDate) {
		//判断添加的开始时间大于等于查询的结束时间
		//或
		//判断添加的任务的结束时间大于等于查询的任务的开始时间
		if (frontStartDate.compareTo(plansEndDate)>=0||frontPlansEndEate.compareTo(startDate)<=0) {
//			System.out.println("--右边-->"+frontStartDate.compareTo(plansEndDate));
//			System.out.println("--左边-->"+frontPlansEndEate.compareTo(startDate));
//			System.out.println("添加的开始时间大于等于查询的结束时间就表示");
//			System.out.println("添加任务在查询的任务的右边");
//			System.out.println("添加的结束时间小于等于查询的开始时间就表示");
//			System.out.println("添加任务在查询的任务的左边");
//			System.out.println("-判断时间方法-返回值--true");
			return true;
		}
		return false;
	}
		

	
}

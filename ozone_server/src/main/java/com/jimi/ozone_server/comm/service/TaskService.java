package com.jimi.ozone_server.comm.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jimi.ozone_server.comm.constant.IsDelete;
import com.jimi.ozone_server.comm.model.Result;
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
		System.out.println("业务层id："+id);
		String sql = "SELECT id,name FROM task WHERE is_delete < 1 AND id = "+id;
		String tableName = "task";
		//调用公共删除方法
		Result result = BaseMethodService.deleteTableRecord(tableName, sql);
		//调用删除前置任务方法
		deleteTaskRelation(id);
		//调用删除人员任务方法
		deletePersonnelTask(id);
		System.out.println("业务层返回值："+result.getCode()+",信息: " +result.getData()) ;
		return result;
	}
	
	
	/**
	 * 查询任务
	 * @param name 任务名称
	 * @return    返回信息提示用户
	 */
	public Result findAllTask(String name) {
		System.out.println("业务层name："+name);
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
			List<Record> taskClassifys = Db.find("SELECT name,remark FROM task_classify WHERE is_delete<1 AND id ="+taskId);
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
		System.out.println("业务层集合--ps：" + personnels);
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
	public Result addTask(String name,Date start_date,Date plans_end_date,
			Date actual_end_date,int task_classify,int gantt,String remark,
			String predecessorTasks,String executor,boolean is_coerce) {
		System.out.println("业务层name："+name+",start_date:"+start_date+",plans_end_date:"+plans_end_date+",actual_end_date:"+actual_end_date+",task_classify:"+",gantt:"+gantt+",remark:"+remark);
		if (name==null&&"".equals(name)&&start_date==null&&plans_end_date==null&&task_classify<=0&&gantt<=0) {
			return new Result(400, "数据不完整");
		}
		List<Record> tasks = Db.find("SELECT * from task WHERE is_delete < 1 AND name ='"+name+"'");
		if (tasks.size()>0) {
			return new Result(400, "存在该任务");
		}
		Result result = null;
		//遍历添加人员任务
		String[] newExecutor = executor.split(",");
		for (String personnel : newExecutor) {
			
			result = isDate(name, Integer.parseInt(personnel), start_date, plans_end_date, is_coerce, actual_end_date, task_classify, gantt, remark, predecessorTasks, executor);
		}
		if (result.getCode()==200) {
			result = addTaskInfo(name, start_date, plans_end_date, actual_end_date, task_classify, gantt, remark, predecessorTasks, executor);
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
	public Result updateTask(int id,String name,Date start_date,Date plans_end_date,
			Date actual_end_date,int task_classify,int gantt,String remark,
			String predecessorTasks,String executor,boolean is_coerce) {
		System.out.println("业务层name："+name+",start_date:"+start_date+",plans_end_date:"+plans_end_date+",actual_end_date:"+actual_end_date+",task_classify:"+",gantt:"+gantt+",remark:"+remark);
		if (name==null&&"".equals(name)&&start_date==null&&plans_end_date==null&&task_classify<=0&&gantt<=0) {
			return new Result(400, "数据不完整");
		}
		Record task_id = Db.findFirst("SELECT * from task WHERE is_delete < 1 AND id ="+id);
		if (task_id==null) {
			return new Result(400, "不存在该任务");
		}
		Record task_name = Db.findFirst("SELECT * from task WHERE is_delete < 1 AND name ='"+name+"'");
		if (task_name!=null) {
			return new Result(400, "存在该任务");
		}
		Result result = null;
		//遍历添加人员任务
		String[] newExecutor = executor.split(",");
		for (String personnel : newExecutor) {
			result = isDate(name, Integer.parseInt(personnel), start_date, plans_end_date, is_coerce, actual_end_date, task_classify, gantt, remark, predecessorTasks, executor);
		}
		if (result.getCode()==200) {
			//依次将之前的人员任务和前置任务删除
			updatePersonnelTask(id);
			updateTaskRelation(id);
			//再执行添加
			result = updateTaskInfo(id,name, start_date, plans_end_date, actual_end_date, task_classify, gantt, remark, predecessorTasks, executor);
		}
		return result;
		
	}
	
	
	/*
	 *添加任务信息，前置任务信息，人员任务信息
	 */
	public Result addTaskInfo(String name,Date start_date,Date plans_end_date,Date actual_end_date,
			int task_classify,int gantt,String remark,String predecessorTasks,String executor) {
		Record record = new Record();
		record.set("name", name);
		record.set("start_date", start_date);
		record.set("plans_end_date", plans_end_date);
		record.set("actual_end_date", actual_end_date);
		record.set("task_classify", task_classify);
		record.set("gantt", gantt);
		record.set("remark", remark);
		record.set("is_delete", IsDelete.IS_DELETE_ON);
		System.out.println("---当前要添加的任务信息--"+record);
		boolean b = Db.save("task", record);
		//判断添加任务成功
		if (b) {
			//得到当前任务的id
			Record task = findOneTask(name);
			Object taskId = task.get("id");
			System.out.println("当前任务----"+task);
			String[]  newExecutor= predecessorTasks.split(",");
			//遍历添加前置任务
			for (String taskRelation : newExecutor) {
				Result result = addTaskRelation(Integer.parseInt(taskRelation),(int) taskId);
				System.out.println("----添加前置任务---"+result.getData()) ;
				
			}
			//遍历添加人员任务
			String[] newPredecessorTasks = executor.split(",");
			for (String personnel : newPredecessorTasks) {
				Result result = addPersonnelTask((int) taskId, Integer.parseInt(personnel));
				System.out.println("----添加人员任务---"+result.getData()) ;
			}
			return new Result(200, "添加任务所有信息成功");
		}else {
			return new Result(400, "添加任务所有信息失败");
		}
	}
	
	
	/*
	 *修改任务信息，前置任务信息，人员任务信息
	 */
	public Result updateTaskInfo(int id,String name,Date start_date,Date plans_end_date,Date actual_end_date,
			int task_classify,int gantt,String remark,String predecessorTasks,String executor) {
		Record record = new Record();
		record.set("id", id);
		record.set("name", name);
		record.set("start_date", start_date);
		record.set("plans_end_date", plans_end_date);
		record.set("actual_end_date", actual_end_date);
		record.set("task_classify", task_classify);
		record.set("gantt", gantt);
		record.set("remark", remark);
		record.set("is_delete", IsDelete.IS_DELETE_ON);
		System.out.println("---当前要添加的任务信息--"+record);
		boolean b = Db.update("task", record);
		//判断添加任务成功
		if (b) {
			//得到当前任务的id
			Record task = findOneTask(name);
			Object taskId = task.get("id");
			System.out.println("当前任务----"+task);
			String[]  newExecutor= predecessorTasks.split(",");
			//遍历添加前置任务
			for (String taskRelation : newExecutor) {
				Result result = addTaskRelation(Integer.parseInt(taskRelation),(int) taskId);
				System.out.println("----添加前置任务---"+result.getData()) ;
				
			}
			//遍历添加人员任务
			String[] newPredecessorTasks = executor.split(",");
			for (String personnel : newPredecessorTasks) {
				Result result = addPersonnelTask((int) taskId, Integer.parseInt(personnel));
				System.out.println("----添加人员任务---"+result.getData()) ;
			}
			return new Result(200, "添加任务所有信息成功");
		}else {
			return new Result(400, "添加任务所有信息失败");
		}
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
			record .set("is_delete", IsDelete.IS_DELETE_OFF);
			b = Db.update("task_relation", record);
		}
		if (b) {
			return new Result(200, "删除成功");
		}else {
			throw new RuntimeException("未知异常");
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
			record .set("is_delete", IsDelete.IS_DELETE_OFF);
			b = Db.update("personnel_task", record);
		}
		if (b) {
			return new Result(200, "删除成功");
		}else {
			throw new RuntimeException("未知异常");
		}
		
	}
	
	
	/**
	 * 添加前置任务
	 * @param predecessor_task 前置任务id（任务外键）
	 * @param current_task     当前任务id（任务外键）
	 * @return  返回信息提示用户
	 */
	public Result addTaskRelation(int predecessor_task,int current_task) {
		System.out.println("业务层predecessor_task："+predecessor_task+",current_task："+current_task);
		List<Record> taskRelations = Db.find("SELECT * FROM task_relation WHERE is_delete <1 AND current_task = "+current_task);
		if (taskRelations.size()>0) {
			for (int i = 0; i < taskRelations.size(); i++) {
				Object findPredecessor_task = taskRelations.get(i).get("predecessor_task");
				//当查询出来的前置任务id是空，转为0
				if (findPredecessor_task ==null) {
					predecessor_task = 0;
				}
				boolean b = findPredecessor_task.equals(predecessor_task);
				if (b) {
					return new Result(400, "存在该该前置任务");
				}
			}
		}
		Record record = new Record();
		record.set("predecessor_task", predecessor_task).set("current_task", current_task);
		record.set("is_delete", IsDelete.IS_DELETE_ON);
		boolean b = Db.save("task_relation" , record  );
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
	public Result updateTaskRelation(int current_task) {
		System.out.println("业务层current_task："+current_task);
		Result result = new Result(400, "修改前置任务失败");
		List<Record> taskRelations = Db.find("SELECT * FROM task_relation WHERE is_delete <1 AND current_task = "+current_task);
		if (taskRelations.size()>0) {
			for (int i = 0; i < taskRelations.size(); i++) {
				Record record = taskRelations.get(i);
				
				record.set("is_delete", IsDelete.IS_DELETE_OFF);
				boolean b = Db.update("task_relation",record);
				if (b) {
					System.out.println();
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
		System.out.println("业务层task："+task+",personnel："+personnel);
		List<Record> personnelTasks = Db.find("SELECT * FROM personnel_task WHERE is_delete < 1 AND task = "+task);
		if (personnelTasks.size()>0) {
			for (int i = 0; i < personnelTasks.size(); i++) {
				boolean b = personnelTasks.get(i).get("personnel").equals(personnel);
				if (b) {
					return new Result(400, "存在该人员任务");
				}
			}
		}
		Record record = new Record();
		record.set("task", task).set("personnel", personnel);
		record.set("is_delete", IsDelete.IS_DELETE_ON);
		boolean b = Db.save("personnel_task" , record);
		if (b) {
			return new Result(200, "添加成功");
		}else {
			return new Result(400, "失败成功");
		}
		
	}
	
	
	/**
	 * 修改人员任务
	 * @param task      任务id
	 * @param personnel 人员id
	 * @return   返回信息提示用户
	 */
	public Result updatePersonnelTask(int task) {
		System.out.println("业务层task："+task);
		Result result = new Result(400, "修改人员任务失败");
		List<Record> personnelTasks = Db.find("SELECT * FROM personnel_task WHERE is_delete < 1 AND task = "+task);
		if (personnelTasks.size()>0) {
			for (int i = 0; i < personnelTasks.size(); i++) {
				Record record = personnelTasks.get(i);
				record.set("is_delete", IsDelete.IS_DELETE_OFF);
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
	public Result isDate(String name,int personnel,Date front_start_date,
			Date front_plans_end_date,boolean is_coerce,Date actual_end_date,
			int task_classify,int gantt,String remark,String predecessorTasks,String executor) {
		//查询人员任务
		List<Record> personnelTasks = Db.find("SELECT * FROM personnel_task WHERE is_delete < 1 AND personnel = " +personnel);
		//任务id
		int taskId = 0;
		//查询出的任务开始时间
		Date start_date = null;
		//查询出的任务计划结束时间
		Date plans_end_date = null;
		//比较任务时间
		boolean isTask = false;
		//是否强制添加任务判定值
		boolean code = false;
		for (int i = 0; i < personnelTasks.size(); i++) {
			taskId = personnelTasks.get(i).get("task");
			//查询任务
			List<Record> tasks = Db.find("SELECT id,start_date,plans_end_date from task WHERE is_delete < 1 AND id = "+taskId);
			for (Record task : tasks) {
				start_date = task.get("start_date");
				plans_end_date = task.get("plans_end_date");
				
				System.out.println("要添加的任务的开始时间"+front_start_date);
				System.out.println("要添加的任务的计划结束时间"+front_plans_end_date);
				System.out.println("当前查询的任务的开始时间"+start_date);
				System.out.println("当前查询的任务的计划结束时间"+plans_end_date);
				
				//要添加的任务的开始时间大于等于当前查询出的任务的结束时间？
				System.out.println("---1->"+front_start_date.compareTo(start_date));
				
				if (front_start_date.compareTo(start_date)<0) {
					System.out.println("要添加的任务的开始时间小于当前查询出的任务的结束时间");
					//判断添加的任务的结束时间大于等于当前查询出的任务的开始时间？
					System.out.println("---2->"+front_plans_end_date.compareTo(plans_end_date));
					if(front_plans_end_date.compareTo(plans_end_date)>=0) {
						System.out.println("判断添加的任务的结束时间大于等于当前查询出的任务的开始时间");
						isTask = true;
						code = true;
					}else {
						code = false;
						
					}
				}else {
					System.out.println("--------is_coerce = true-");
					code = true;
					if (code) {
						//判断是否强制添加任务
						if (is_coerce) {
							isTask = true;
						}else {
							return new Result(400, "是否强制添加任务");
						}
					}
					
				}
				
			}
			
		}
		if (isTask) {
			System.out.println("----isTask---true");
			return new Result(200,"查询出人员任务表不为空时处理已完成！");
		}
		return new Result(200,"添加完成");
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

	
}

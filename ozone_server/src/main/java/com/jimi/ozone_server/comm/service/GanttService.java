package com.jimi.ozone_server.comm.service;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jimi.ozone_server.comm.constant.IsDelete;
import com.jimi.ozone_server.comm.model.Result;
import com.jimi.ozone_server.comm.service.base.BaseMethodService;

/**
 * 甘特图管理业务类 <br>
 * <b>2019年6月21日</b>
 * 
 * @author 几米物联自动化部-韦姚忠
 *
 */
public class GanttService {
	
	/*
	 * 甘特图显示
	 */
	public Result findPojoGantt(int id) {
		String ganttSql = "SELECT id,name,responsible,cycle,remark FROM gantt WHERE is_delete <1 AND id ="+id;
		List<Record> gantts = Db.find(ganttSql);
		Record gantt = gantts.get(0);
		String taskClassifySql  = "SELECT id,name FROM task_classify WHERE is_delete <1 AND gantt ="+id;
		List<Record> personnels = new ArrayList<>();
		//前置任务id
		String taskRelas = "";
		//任务分类集合
		List<Record> taskClassifys = Db.find(taskClassifySql);
		for (int i = 0; i < taskClassifys.size(); i++) {
			Record taskClassify = taskClassifys.get(i);
			//任务分类id
			int taskClassifyId = taskClassify.get("id");
			//任务集合
			List<Record> tasks = Db.find("SELECT id,name,start_date,plans_end_date,actual_end_date FROM task WHERE is_delete <1 AND task_classify ="+taskClassifyId);
			for (Record task : tasks) {
				//任务id
				int taskId = task.get("id");
				List<Record> taskRelations = Db.find("SELECT id,predecessor_task,current_task FROM task_relation WHERE is_delete<1 AND current_task ="+taskId);
				List<Record> personnelTasks = Db.find("SELECT id,personnel,task,remark FROM personnel_task WHERE is_delete<1 AND task =" +taskId);
				for (Record personnelTask : personnelTasks) {
					//得到人员id
					int personnelId = personnelTask.get("id");
					 //查询人员
					 List<Record> personnel = Db.find("SELECT id,name FROM personnel WHERE  is_delete<1 AND id ="+personnelId);
					 System.out.println("业务层查询人员："+personnel);
					 if (personnel.size()>0) {
						 personnels.add(personnel.get(0));
						 System.out.println("1.人员集合："+personnels);
					}
					
				}
				for (Record taskRelation : taskRelations) {
					String predecessor_task = taskRelation.getStr("predecessor_task");
					if (predecessor_task == null) {
						taskRelas = "0";
					}else {
						taskRelas = predecessor_task;
					}
					taskRelas = taskRelas+",";
				}
				task.set("personnels",personnels);
				task.set("taskRelas", taskRelas);
				System.out.println("2.人员集合："+personnels);
				System.out.println("任务："+task);
				personnels = new ArrayList<>();
				taskRelas = "";
			
			}
			//添加完第一个任务分类、前置任务之后清空
			taskClassify.set("tasks", tasks);
			
		}
		gantt.set("taskClassifys", taskClassifys);
		
		return new Result(200, gantt);
		
		
	}
	
	
	/**
	 * 删除甘特图
	 * @param id 甘特图id
	 * @return  返回信息提示用户
	 */
	public Result deleteGantt(int id) {
		System.out.println("业务层id："+id);
		String sql = "SELECT id FROM gantt WHERE is_delete <1 AND id = "+id;
		String tableName = "gantt";
		//调用公共删除方法
		Result result = BaseMethodService.deleteTableRecord(tableName, sql);
		System.out.println("业务层返回值："+result.getCode()+",信息: " +result.getData()) ;
		return result;
	}
	
	
	/**
	 * 查询甘特图
	 * @param name 甘特图名字
	 * @return	    返回集合数据显示
	 */
	public Result findAllGantt(String name) {
		System.out.println("业务层参数--name：" + name);
		List<Record> gantts = null;
		// 判断name不为空
		if (!"".equals(name) && name != null) {
			// 带值查询
			gantts = Db.find("SELECT id,name,responsible,cycle,remark FROM gantt WHERE is_delete <1 AND name LIKE '%"+name+"%'");
		} else {
			gantts = Db.find("SELECT id,name,responsible,cycle,remark FROM gantt WHERE is_delete <1");
		}
		System.out.println("业务层集合--gantt：" + gantts);
		return new Result(200, gantts);
		
	}
	
	
	/**
	 * 添加甘特图
	 * @param name        甘特图名称
	 * @param responsible 项目负责人
	 * @param cycle       项目周期
	 * @param remark      备注
	 * @return   返回字符串信息提示用户
	 */
	public Result addGantt(String name,String responsible,int cycle,String remark) {
		System.out.println("业务层参数--name：" + name+",responsoble"+responsible+", cycle"+cycle+",remark"+remark);
		if (name!=null&&!"".equals(name)&&responsible!=null&&!"".equals(responsible)&&cycle>0) {
			List<Record> gantts = Db.find("SELECT id,name FROM gantt WHERE is_delete <1 AND name LIKE '%"+name+"%'");
			if (gantts.size()>0) {
				return new Result(400, "甘特图已存在！");
			}
			Record record = new Record();
			record.set("name", name).set("responsible", responsible).set("cycle", cycle).set("remark", remark);
			record.set("is_delete", IsDelete.IS_DELETE_ON);
			boolean b = Db.save("gantt", record );
			if (b) {
				return new Result(200, "添加成功");
			}
			
		}else {
			return new Result(400, "信息有误！");
		}
		return new Result(400, "添加失败！");
		
	}
	
	
	/**
	 * 修改甘特图
	 * @param id		  甘特图id
	 * @param name        甘特图名称
	 * @param responsible 项目负责人
	 * @param cycle       项目周期
	 * @param remark      备注
	 * @return   返回字符串信息提示用户
	 */
	public Result updateGantt(int id,String name,String responsible,int cycle,String remark) {
		System.out.println("业务层参数--id："+id+",name：" + name+",responsoble"+responsible+", cycle"+cycle+",remark"+remark);
		List<Record> gantts = Db.find("SELECT id FROM gantt WHERE is_delete <1 AND id ="+id);
		if (gantts.size()<=0) {
			return new Result(400, "甘特图不存在！");
		}
		if (name!=null&&!"".equals(name)&&responsible!=null&&!"".equals(responsible)&&cycle>0) {
			Record record = new Record();
			record.set("id", id).set("name", name).set("responsible", responsible).set("cycle", cycle).set("remark", remark);
			record.set("is_delete", IsDelete.IS_DELETE_ON);
			boolean b = Db.update("gantt", record );
			if (b) {
				return new Result(200, "修改成功");
			}
		}
		return new Result(200, "修改失败");
		
	}
	

}

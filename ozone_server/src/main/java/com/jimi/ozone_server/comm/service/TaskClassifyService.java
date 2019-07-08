package com.jimi.ozone_server.comm.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jimi.ozone_server.comm.constant.DeleteStatus;
import com.jimi.ozone_server.comm.model.Result;
import com.jimi.ozone_server.comm.model.TaskClassify;
import com.jimi.ozone_server.comm.service.base.BaseMethodService;

/**
 * 任务分类管理业务类
 * <br>
 * <b>2019年6月21日</b>
 * @author 几米物联自动化部-韦姚忠
 */
public class TaskClassifyService{

	
	/**
	 * 删除任务分类
	 * @param id 任务分类id
	 * @return 提示用户
	 */
	public Result deleteTaskClassify(int id) {
		String sql = "SELECT id,name FROM task_classify WHERE is_delete < 1 AND id = "+id;
		String tableName = "task_classify";
		//调用公共删除方法
		Result result = BaseMethodService.deleteTableRecord(tableName, sql);
		return result;
	}
	
	
	/**
	 * 查询任务分类
	 * @param name 任务分类名称，模糊查询
	 * @return  返回集合
	 */
	public Result findAllTaskClassify(String name,int gantt) {
		List<Record> taskClassifys = null;
		if (gantt>0) {
			//查询甘特图是否存在
			List<Record> gantts = Db.find("SELECT id FROM gantt WHERE is_delete <1 AND id ="+gantt);
			if (gantts.size()<=0) {
				// 判断name不为空
				if (!"".equals(name) && name != null) {
					// 甘特图不存在，name存在
					taskClassifys = Db.find("SELECT id,name,remark,gantt FROM task_classify WHERE is_delete <1 AND name LIKE '%"+name+"%'");
				}else {
					taskClassifys = Db.find("SELECT id,name,remark,gantt FROM task_classify WHERE gantt="+gantt+" AND is_delete <1");
				}
			}else {
				if (!"".equals(name) && name != null) {
					// 甘特图存在，name存在
					taskClassifys = Db.find("SELECT id,name,remark,gantt FROM task_classify WHERE gantt="+gantt+" AND is_delete <1 AND name LIKE '%"+name+"%'");
				}else {
					taskClassifys = Db.find("SELECT id,name,remark,gantt FROM task_classify WHERE gantt="+gantt+" AND is_delete <1");
				}
			}
		}
		//甘特图id无，任务分类为空
		if (gantt==0) {
			if (!"".equals(name) && name != null) {
				taskClassifys = Db.find("SELECT id,name,remark,gantt FROM task_classify WHERE is_delete <1 AND name LIKE '%"+name+"%'");
			}else {
				taskClassifys = Db.find("SELECT id,name,remark,gantt FROM task_classify WHERE  is_delete <1");
			}
		}
		if (gantt<0) {
			return new Result(400, "无效的id");
		}
		return new Result(200, taskClassifys);
	}
	
	
	/**
	 * 修改任务分类
	 * @param name	任务分类名称
	 * @param remark任务分类备注
	 * @param gantt 甘特图id
	 * @return
	 */
	public Result updateTaskClassify(int id,String name,String remark,int gantt) {
		if (id<=0) {
			return new Result(400, "任务分类不存在");
		}else {
			Record taskClassify = Db.findFirst("SELECT id,name,remark,gantt FROM task_classify WHERE is_delete < 1 AND id = "+id);
			if (taskClassify == null) {
				return new Result(400, "任务分类不存在");
			}
			List<Record> gantts = Db.find("SELECT id FROM gantt WHERE is_delete <1 AND id ="+gantt);
			if (gantts.size()<=0) {
				return new Result(400, "甘特图不存在");
			}
			if (name!=null&&!"".equals(name)) {
				List<Record> taskClassifys = Db.find("SELECT id,name,remark,gantt FROM task_classify WHERE name = '"+name+"' AND is_delete < 1; ");
				if (taskClassifys.size()>0) {
					return new Result(400, "存在该任务分类");
				}
				TaskClassify classify = handleTaskClassifyInfo(id, name, remark, gantt);
				boolean b = classify.update();
				if (b) {
					return new Result(200, "修改成功");
				}
			}
		}
		return new Result(400, "修改失败");
	}
	
	
	/**
	 * 添加任务分类
	 * @param id    任务分类id
	 * @param name	任务分类名称
	 * @param remark任务分类备注
	 * @param gantt 甘特图id
	 * @return
	 */
	public Result addTaskClassify(String name,String remark,int gantt) {
		List<Record> gantts = Db.find("SELECT id FROM gantt WHERE is_delete <1 AND id ="+gantt);
		if (gantts.size()<0) {
			return new Result(400, "甘特图不存在");
		}
		if (name!=null&&!"".equals(name)) {
			TaskClassify taskClassify = handleTaskClassifyInfo(0, name, remark, gantt);
			boolean b = taskClassify.save();
			if (b) {
				return new Result(200, "添加成功");
			}
		}
		return new Result(400, "添加失败");
	}
	
	
	/*
	 * 判断任务分类参数
	 */
	private TaskClassify handleTaskClassifyInfo(int id,String name,String remark,int gantt) {
		TaskClassify taskClassify = new TaskClassify();
		if (id>0) {
			taskClassify = TaskClassify.dao.findById(id);
		}
		if (name!=null&&!"".equals(name)) {
			taskClassify.setName(name);
		}
		if (remark!=null&&!"".equals(remark)) {
			taskClassify.setRemark(remark);
		}
		if (gantt>0) {
			taskClassify.setGantt(gantt);
		}
		taskClassify.setIsDelete(DeleteStatus.IS_DELETE_ON);
		return taskClassify;
	}
}

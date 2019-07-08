package com.jimi.ozone_server.comm.service;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jimi.ozone_server.comm.model.Result;
import com.jimi.ozone_server.comm.service.base.SQL;

/**
 * 人力资源显示业务类 <br>
 * <b>2019年6月21日</b>
 * 
 * @author 几米物联自动化部-韦姚忠
 *
 */
public class HRService {
	
	
	/**
	 * 查询人力资源
	 * @return 返回人力资源信息
	 */
	public Result findHrInfo() {
		String personnelClassifySql = SQL.FIND_PERSONNEL_CLASSIFY_ALL;
		List<Record> tasks  = new ArrayList<>();
		List<Record> newPersonnels = new ArrayList<>();
		List<Record> newPersonnelClassifys = new ArrayList<>();
		//1.查询所有的人员分类
		List<Record> personnelClassifys = Db.find(personnelClassifySql);
		for (Record personnelClassify : personnelClassifys) {
			int personnel_classify_id = personnelClassify.get("id");
			//2.根据人员分类id查询该分类下的所有的人员
			List<Record> personnels = Db.find(SQL.FIND_PERSONEL_BY_PERSONNEL_CALSSIFY_ID+personnel_classify_id);
			for (Record personnel : personnels) {
				int personnel_id = personnel.get("id");
				//3.根据人员id查询当前人员的人员任务列表
				List<Record> personnelTasks = Db.find(SQL.FIND_PERSONNEL_CALSSIFY_BY_PERSONNEL_ID+personnel_id);
				for (Record personnelTask : personnelTasks) {
					int task_id = personnelTask.get("task");
					//4.根据得到的人员任务列表的任务id得到该任务
					List<Record> oneTasks = Db.find(SQL.FIND_TASK_BY_ID+task_id);
					//得到该任务
					if (oneTasks.size()>0) {
						Record task = oneTasks.get(0);
						int gantt_id = oneTasks.get(0).get("gantt");
						//5.根据任务的甘特图外键得到甘特图信息
						List<Record> gantts = Db.find(SQL.FINAL_GANTT_HR_BY_ID+gantt_id);
						Record gantt = gantts.get(0);
						//将甘特图的信息放进任务里面
						task.set("task_gantt", gantt);
						//将个人任务放进集合中
						tasks.add(task);
					}
				}
				//将个人任务集合放入人员中
				personnel.set("tasks", tasks);
				tasks = new ArrayList<>();
				//将人员放入人员集合中
				newPersonnels.add(personnel);
			}
			//将人员集合放进任务分类中
			personnelClassify.set("personnels", newPersonnels);
			newPersonnels = new ArrayList<>();
			//将任务分类放进任务任务分类集合中
			newPersonnelClassifys.add(personnelClassify);
		}
		return new Result(200, newPersonnelClassifys);
	}

}

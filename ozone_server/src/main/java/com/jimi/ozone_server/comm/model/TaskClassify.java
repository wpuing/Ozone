package com.jimi.ozone_server.comm.model;

import java.util.List;

import com.jimi.ozone_server.comm.model.base.BaseTaskClassify;

/**
 * 任务分类实体类
 * <br>
 * <b>2019年6月21日</b>
 * @author 几米物联自动化部-韦姚忠
 *
 */
@SuppressWarnings("serial")
public class TaskClassify extends BaseTaskClassify<TaskClassify> {
	
	public static final TaskClassify dao = new TaskClassify().dao();
	
	private List<Task>  tasks;
	
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	@Override
	public String toString() {
		return "TaskClassify [tasks=" + tasks + "]";
	}
	
	
}

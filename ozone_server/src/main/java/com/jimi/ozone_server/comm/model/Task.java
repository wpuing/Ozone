package com.jimi.ozone_server.comm.model;

import java.util.List;

import com.jimi.ozone_server.comm.model.base.BaseTask;

/**
 * 任务实体类
 * <br>
 * <b>2019年6月21日</b>
 * @author 几米物联自动化部-韦姚忠
 *
 */
@SuppressWarnings("serial")
public class Task extends BaseTask<Task> {
	
	public static final Task dao = new Task().dao();
	
	private List<TaskRelation> taskRelations;
	
	private List<PersonnelTask> personnelTasks;

	public List<TaskRelation> getTaskRelations() {
		return taskRelations;
	}

	public void setTaskRelations(List<TaskRelation> taskRelations) {
		this.taskRelations = taskRelations;
	}

	public List<PersonnelTask> getPersonnelTasks() {
		return personnelTasks;
	}

	public void setPersonnelTasks(List<PersonnelTask> personnelTasks) {
		this.personnelTasks = personnelTasks;
	}

	@Override
	public String toString() {
		return "Task [taskRelations=" + taskRelations + ", personnelTasks=" + personnelTasks + "]";
	}
	
	
	
}

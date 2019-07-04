package com.jimi.ozone_server.comm.model;

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
	
	
}

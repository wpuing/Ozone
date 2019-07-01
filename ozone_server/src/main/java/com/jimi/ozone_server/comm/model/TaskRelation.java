package com.jimi.ozone_server.comm.model;

import com.jimi.ozone_server.comm.model.base.BaseTaskRelation;

/**
 * 前置任务实体类
 * <br>
 * <b>2019年6月21日</b>
 * @author 几米物联自动化部-韦姚忠
 *
 */
@SuppressWarnings("serial")
public class TaskRelation extends BaseTaskRelation<TaskRelation> {
	public static final TaskRelation dao = new TaskRelation().dao();
}

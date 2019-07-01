package com.jimi.ozone_server.comm.model;
/**
 * 自定义甘特图信息增强实体类
 * <br>
 * <b>2019年6月21日</b>
 * @author 几米物联自动化部-韦姚忠
 *
 */

import java.util.List;

public class CustomGantt {
	
	private Gantt gantt;
	
	private List<TaskClassify> taskClassifys;

	public Gantt getGantt() {
		return gantt;
	}

	public void setGantt(Gantt gantt) {
		this.gantt = gantt;
	}

	public List<TaskClassify> getTaskClassifys() {
		return taskClassifys;
	}

	public void setTaskClassifys(List<TaskClassify> taskClassifys) {
		this.taskClassifys = taskClassifys;
	}

	@Override
	public String toString() {
		return "CustomGantt [gantt=" + gantt + ", taskClassifys=" + taskClassifys + "]";
	}

	
}

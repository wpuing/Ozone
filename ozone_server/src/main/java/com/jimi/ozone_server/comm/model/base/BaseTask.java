package com.jimi.ozone_server.comm.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseTask<M extends BaseTask<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}
	
	public java.lang.String getName() {
		return getStr("name");
	}

	public void setStartDate(java.util.Date startDate) {
		set("start_date", startDate);
	}
	
	public java.util.Date getStartDate() {
		return get("start_date");
	}

	public void setPlansEndDate(java.util.Date plansEndDate) {
		set("plans_end_date", plansEndDate);
	}
	
	public java.util.Date getPlansEndDate() {
		return get("plans_end_date");
	}

	public void setActualEndDate(java.util.Date actualEndDate) {
		set("actual_end_date", actualEndDate);
	}
	
	public java.util.Date getActualEndDate() {
		return get("actual_end_date");
	}

	public void setTaskClassify(java.lang.Integer taskClassify) {
		set("task_classify", taskClassify);
	}
	
	public java.lang.Integer getTaskClassify() {
		return getInt("task_classify");
	}

	public void setGantt(java.lang.Integer gantt) {
		set("gantt", gantt);
	}
	
	public java.lang.Integer getGantt() {
		return getInt("gantt");
	}

	public void setIsDelete(java.lang.Integer isDelete) {
		set("is_delete", isDelete);
	}
	
	public java.lang.Integer getIsDelete() {
		return getInt("is_delete");
	}

	public void setRemark(java.lang.String remark) {
		set("remark", remark);
	}
	
	public java.lang.String getRemark() {
		return getStr("remark");
	}

}

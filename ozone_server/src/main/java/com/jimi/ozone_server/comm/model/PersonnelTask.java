package com.jimi.ozone_server.comm.model;

import com.jimi.ozone_server.comm.model.base.BasePersonnelTask;

/**
 * 人员任务实体类
 * <br>
 * <b>2019年6月21日</b>
 * @author 几米物联自动化部-韦姚忠
 *
 */
@SuppressWarnings("serial")
public class PersonnelTask extends BasePersonnelTask<PersonnelTask> {
	
	public static final PersonnelTask dao = new PersonnelTask().dao();
	
	private Personnel personnelPojo;

	public Personnel getPersonnelPojo() {
		return personnelPojo;
	}

	public void setPersonnelPojo(Personnel personnelPojo) {
		this.personnelPojo = personnelPojo;
	}

	@Override
	public String toString() {
		return "PersonnelTask [personnelPojo=" + personnelPojo + "]";
	}
	
	
	
}

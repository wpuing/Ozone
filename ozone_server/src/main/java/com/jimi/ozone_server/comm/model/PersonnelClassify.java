package com.jimi.ozone_server.comm.model;

import com.jimi.ozone_server.comm.model.base.BasePersonnelClassify;

/**
 * 人员分类实体类
 * <br>
 * <b>2019年6月21日</b>
 * @author 几米物联自动化部-韦姚忠
 *
 */
@SuppressWarnings("serial")
public class PersonnelClassify extends BasePersonnelClassify<PersonnelClassify> {
	public static final PersonnelClassify dao = new PersonnelClassify().dao();
}

package com.jimi.ozone_server.comm.model;

import com.jimi.ozone_server.comm.model.base.BaseGantt;

/**
 * 甘特图实体类
 * <br>
 * <b>2019年6月21日</b>
 * @author 几米物联自动化部-韦姚忠
 *
 */
@SuppressWarnings("serial")
public class Gantt extends BaseGantt<Gantt> {
	public static final Gantt dao = new Gantt().dao();
}

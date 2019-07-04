package com.jimi.ozone_server.comm.controller;

import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jimi.ozone_server.comm.model.Result;
import com.jimi.ozone_server.comm.service.PersonnelService;
import com.jimi.ozone_server.comm.service.base.BaseMethodService;
/**
 * 人员管理控制器
 * <br>
 * <b>2019年6月21日</b>
 * @author 几米物联自动化部-韦姚忠
 *
 */
public class PersonnelController extends Controller {
	
	private static PersonnelService personnelService = Enhancer.enhance(PersonnelService.class);

	
	/*
	 * 查询人员
	 */
	public void  findPerAll() {
		String name  = getPara("name");
		renderJson(personnelService.findAllPersonnel(name));
		
	}
	
	
	/*
	 * 删除人员
	 */
	public void deletePerAll() {
		Result result = new Result(400, "删除失败");
		String ids = getPara("ids");
		String[] newNumbers = ids.split(",");
		for (String idStr : newNumbers) {
			Integer newId = BaseMethodService.handleParameterType(idStr);
			if (newId!=null) {
				result = personnelService.deletePersonnel(newId);
			}
		}
		renderJson(result);
	}
	
	
	/*
	 * 添加人员
	 */
	public void insertPer() {
		String name = getPara("name");
		int personnelClassify = Integer.parseInt(getPara("personnel_classify","1"));
		Result result = personnelService.addPersonnel(name, personnelClassify);
		renderJson(result);
	}
	
	
	/*
	 * 修改人员
	 */
	public void updatePer() {
		int id = Integer.parseInt(getPara("id"));
		String name = getPara("name");
		int personnelClassify = Integer.parseInt(getPara("personnel_classify","1"));
		Result result = personnelService.updatePersonnel(id, name, personnelClassify);
		renderJson(result);
	}
	
}

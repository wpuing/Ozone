package com.jimi.ozone_server.comm.controller;

import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jimi.ozone_server.comm.model.Result;
import com.jimi.ozone_server.comm.service.PersonnelClassifyService;
import com.jimi.ozone_server.comm.service.base.BaseMethodService;



/**
 * 人员分类管理控制器 <br>
 * <b>2019年6月21日</b>
 * 
 * @author 几米物联自动化部-韦姚忠
 *
 */
public class PersonnelClassifyController extends Controller {

	private static PersonnelClassifyService personnelClassifyService = Enhancer.enhance(PersonnelClassifyService.class);

	public void index() {
		renderText("this is personnel classify controller");
	}
	

	/*
	 * 查询人员分类
	 * 
	 */
	public void findPerClsAll() {
		String name = getPara("name");
		Result personnelClassifys = personnelClassifyService.getPersonnelClassifys(name);
		renderJson(personnelClassifys);
	}

	
	/*
	 * 添加人员分类
	 */
	public void addClassify() {
		String name = getPara("name");
		String remark = getPara("remark");
		Result result = personnelClassifyService.addPersonnelClassify(name, remark);
		renderJson(result);
	}
	
	
	/*
	 * 修改人员分类
	 */
	public void updateClassify() {
		int id = Integer.parseInt(getPara("id"));
		String name = getPara("name");
		String remark = getPara("remark");
		Result result = personnelClassifyService.findPersonnelClassifyByName(id, name, remark);
		renderJson(result);
		
	}
	
	
	/*
	 * 删除人员分类
	 */
	public void deleteClassify() {
		int id = 0;
		Result result = new Result(400, "删除失败");
		String ids = getPara("ids");
		String[] newNumbers = ids.split(",");
		for (String idStr : newNumbers) {
			Integer newId = BaseMethodService.handleParameterType(idStr);
			if (newId!=null) {
				id=newId;
				result = personnelClassifyService.deletePersonnelClassify(id);
			}
		}
		renderJson(result);
		
	}
}

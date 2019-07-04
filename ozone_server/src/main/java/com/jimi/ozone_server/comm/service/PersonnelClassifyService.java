package com.jimi.ozone_server.comm.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jimi.ozone_server.comm.constant.DeleteStatus;
import com.jimi.ozone_server.comm.model.PersonnelClassify;
import com.jimi.ozone_server.comm.model.Result;
import com.jimi.ozone_server.comm.service.base.BaseMethodService;



/**
 * 人员分类管理业务类 <br>
 * <b>2019年6月21日</b>
 * 
 * @author 几米物联自动化部-韦姚忠
 *
 */
public class PersonnelClassifyService {

	
	/**
	 * 查询人员分类
	 * @param name 人员分类名称，用作模糊查询
	 * @return 人员分类集合
	 */
	public Result getPersonnelClassifys(String name) {
		List<Record> personnelClassifys = null;
		// 判断name不为空
		if (!"".equals(name) && name != null) {
			// 带值查询
			personnelClassifys = Db.find("SELECT id,name,remark FROM personnel_classify WHERE name LIKE '%" + name
					+ "%' AND is_delete < 1");
		} else {
			personnelClassifys = Db.find("SELECT id,name,remark FROM personnel_classify WHERE  is_delete < 1");
		}
		return new Result(200, personnelClassifys);
	}

	
	/**
	 * 添加人员分类
	 * 
	 * @param name   人员分类名称
	 * @param remark 人员分类备注
	 * @return 字符串提示用户
	 */
	public Result addPersonnelClassify(String name, String remark) {
		String data = null;
		boolean isOk = false;
		//判断非空
		if (name!=null&&!"".equals(name)) {
			//查询数据库
			List<Record> findResults = Db.find("SELECT id FROM personnel_classify WHERE name ='" + name + "'");
			//判断该人员分类是否存在
			if (findResults.size() > 0) {
				data = "存在该人员分类！";
				return new Result(400, data);
			}
		}
		
		if (name != null && !"".equals(name)) {
			PersonnelClassify personnelClassify = handlePersonnelClassifyInfo(0, name, remark);
			isOk = personnelClassify.save();
		}
		if (isOk) {
			data = "添加成功！";
			return new Result(200,data);
		}
		return new Result(400, "添加失败");

	}

	
	/**
	 * 修改人员分类
	 * @param id     人员分类的id
	 * @param name	  人员分类名称
	 * @param remark 人员分类备注
	 * @return 字符串提示用户
	 */
	public Result findPersonnelClassifyByName(int id,String name,String remark) {
		List<Record> record = Db.find("SELECT id,name,remark FROM personnel_classify WHERE  is_delete < 1 AND id = "+id);
		if (record.size()>0) {
			PersonnelClassify personnelClassify = handlePersonnelClassifyInfo(id, name, remark);
			boolean b = personnelClassify.update();
			if (b) {
				return new Result(200, "修改成功");
			}
		}
		return new Result(400,"修改失败");
	}
	
	
	/**
	 * 删除人员分类
	 * @param id 人员分类id
	 * @return 提示用户
	 */
	public Result deletePersonnelClassify(int id) {
		String sql = "SELECT id,name,remark FROM personnel_classify WHERE  is_delete < 1 AND id = "+id;
		String tableName = "personnel_classify";
		//调用公共删除方法
		Result result = BaseMethodService.deleteTableRecord(tableName, sql);
		return result;
		
	}
	
	
	/*
	 * 判断人员分类参数
	 */
	private PersonnelClassify handlePersonnelClassifyInfo(int id,String name,String remark) {
		PersonnelClassify personnelClassify = new PersonnelClassify();
		if (id>0) {
			personnelClassify = PersonnelClassify.dao.findById(id);
		}
		if (name!=null&&!"".equals(name)) {
			personnelClassify.setName(name);
		}
		if (remark!=null&&!"".equals(remark)) {
			personnelClassify.setRemark(remark);
		}
		personnelClassify.setIsDelete(DeleteStatus.IS_DELETE_ON);
		return personnelClassify;
		
	}
	
}

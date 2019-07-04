package com.jimi.ozone_server.comm.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jimi.ozone_server.comm.constant.DeleteStatus;
import com.jimi.ozone_server.comm.model.Personnel;
import com.jimi.ozone_server.comm.model.Result;
import com.jimi.ozone_server.comm.service.base.BaseMethodService;

/**
 * 人员管理业务类
 * <br>
 * <b>2019年6月21日</b>
 * @author 几米物联自动化部-韦姚忠
 *
 */
public class PersonnelService{
	
	
	/**
	 * 删除人员
	 * @param id 人员分类id
	 * @return 提示用户
	 */
	public Result deletePersonnel(int id) {
		String sql = "SELECT id,name FROM personnel WHERE is_delete < 1 AND id = "+id;
		String tableName = "personnel";
		//调用公共删除方法
		Result result = BaseMethodService.deleteTableRecord(tableName, sql);
		return result;
	}
	
	
	/**
	 * 查询人员
	 * @param name 人员名称，模糊查询
	 * @return  返回集合
	 */
	public Result findAllPersonnel(String name) {
		List<Record> personnels = null;
		// 判断name不为空
		if (!"".equals(name) && name != null) {
			// 带值查询
			personnels = Db.find("SELECT p.id,p.name,p.personnel_classify,pc.name personnel_classify_name FROM personnel p LEFT JOIN personnel_classify pc ON  p.personnel_classify = pc.id  WHERE p.name LIKE \"%"+name+"%\" AND p.is_delete < 1;");
		} else {
			personnels = Db.find("SELECT p.id,p.name,p.personnel_classify,pc.name personnel_classify_name FROM personnel p LEFT JOIN personnel_classify pc ON  p.personnel_classify = pc.id  WHERE  p.is_delete < 1;");
		}
		return new Result(200, personnels);
		
	}
	
	
	/**
	 * 添加人员
	 * @param name               人员名称
	 * @param personnelClassify 人员分类外键
	 * @return
	 */
	public Result addPersonnel(String name,int personnelClassify) {
		//判断非空
		if (name!=null &&!"".equals(name)) {
			Record record = Db.findFirst("SELECT id,name FROM personnel WHERE name LIKE '"+name+"' and is_delete < 1");
			//查询是否存在该人员
			if (record!=null) {
				return new Result(400, "存在该人员！");
			}
			//添加该人员到数据库
			Personnel personnel = handlePersonnelInfo(0, name, personnelClassify);
			
			boolean b = personnel.save();
			if (b) {
				return new Result(200, "添加成功");
			}
		}
		return new Result(200, "添加失败");
		
	}

	
	/**
	 * 修改人员
	 * @param id				 人员id
	 * @param name               人员名称
	 * @param personnelClassifyId 人员分类外键
	 * @return
	 */
	public Result updatePersonnel(int id,String name,int personnelClassifyId) {
		Record record = Db.findFirst("SELECT id,name FROM personnel WHERE is_delete < 1 AND id = "+id);
		if (record==null) {
			return new Result(400, "该人员不存在");
		}
		Record personnelClassify = Db.findFirst("SELECT id,name,remark FROM personnel_classify WHERE  is_delete < 1 AND id = "+personnelClassifyId);
		if (personnelClassify==null) {
			return new Result(400, "该人员分类不存在");
		}
		if (name!=null &&!"".equals(name)) {
			Record findRecord = Db.findFirst("SELECT id,name FROM personnel WHERE name LIKE '"+name+"' and is_delete < 1");
			if (findRecord!=null) {
				return new Result(400, "存在");
			}
		}
		Personnel personnel = handlePersonnelInfo(id, name, personnelClassifyId);
		boolean b = personnel.update();
		if (b) {
			return new Result(200, "修改成功");
		}
		return new Result(200, "修改失败");
	}
	
	
	/*
	 * 判断人员参数
	 */
	private Personnel handlePersonnelInfo(int id,String name,int personnelClassify) {
		Personnel personnel = new Personnel();
		if (id>0) {
			personnel = Personnel.dao.findById(id);
		}
		if (name!=null&&!"".equals(name)) {
			personnel.setName(name);
		}
		if (personnelClassify>0) {
			personnel.setPersonnelClassify(personnelClassify);
		}
		personnel.setIsDelete(DeleteStatus.IS_DELETE_ON);
		return personnel;
	}
}

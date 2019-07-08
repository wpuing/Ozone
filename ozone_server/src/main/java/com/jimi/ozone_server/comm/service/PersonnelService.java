package com.jimi.ozone_server.comm.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jimi.ozone_server.comm.constant.DeleteStatus;
import com.jimi.ozone_server.comm.model.Personnel;
import com.jimi.ozone_server.comm.model.Result;
import com.jimi.ozone_server.comm.service.base.BaseMethodService;
import com.jimi.ozone_server.comm.service.base.SQL;

/**
 * 人员管理业务类
 * <br>
 * <b>2019年6月21日</b>
 * @author 几米物联自动化部-韦姚忠
 */
public class PersonnelService{
	
	
	/**
	 * 删除人员
	 * @param id 人员分类id
	 * @return 提示用户
	 */
	public Result deletePersonnel(int id) {
		String sql = SQL.FIND_PERSONNEL_BY_ID+id;
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
			personnels = Db.find(SQL.FIND_ALL_PERSONNEL_LIKE_BY_JOIN_CLASSIFY_NAME+"'%"+name+"%'");
		} else {
			personnels = Db.find(SQL.FIND_PERSONNEL_BY_JOIN_CLASSIFY);
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
			Record record = Db.findFirst(SQL.FIND_PERSONNEL_BY_NAME+"'"+name+"'");
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
		Record record = Db.findFirst(SQL.FIND_PERSONNEL_BY_ID+id);
		if (record==null) {
			return new Result(400, "该人员不存在");
		}
		Record personnelClassify = Db.findFirst(SQL.FIND_PERSONNEL_TASK_INFO_BY_TASK_ID+personnelClassifyId);
		if (personnelClassify==null) {
			return new Result(400, "该人员分类不存在");
		}
		if (name!=null &&!"".equals(name)) {
			Record findRecord = Db.findFirst(SQL.FIND_ALL_PERSONNEL_LIKE_BY__NAME+"'"+name+"'");
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

package com.jimi.ozone_server.comm.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jimi.ozone_server.comm.constant.IsDelete;
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
	 * 
	 * @param name 人员分类名称，用作模糊查询
	 * @return 人员分类集合
	 */
	public Result getPersonnelClassifys(String name) {
		System.out.println("业务层参数--name：" + name);
		List<Record> personnelClassifys = null;
		// 判断name不为空
		if (!"".equals(name) && name != null) {
			// 带值查询
			personnelClassifys = Db.find("SELECT id,name,remark FROM personnel_classify WHERE name LIKE '%" + name
					+ "%' AND is_delete < 1");
		} else {
			personnelClassifys = Db.find("SELECT id,name,remark FROM personnel_classify WHERE  is_delete < 1");
		}
		System.out.println("业务层集合--pcs：" + personnelClassifys);
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
		System.out.println("业务类参数--name：" + name + ",remark：" + remark);
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
		Record record = new Record();
		record.set("name", name).set("remark", remark).set("is_delete", IsDelete.IS_DELETE_ON);
		System.out.println("要添加的人员分类：" + record.toString());
		if (name != null && !"".equals(name)) {
			isOk = Db.save("personnel_classify", record);
		}
		System.out.println("业务类判断是否添加成功？---->" + isOk);
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
		System.out.println("业务层id："+id+",name: "+name+",remark: "+remark);
		List<Record> personnelClassify = Db.find("SELECT id,name,remark FROM personnel_classify WHERE  is_delete < 1 AND id = "+id);
		System.out.println(personnelClassify);
		if (personnelClassify.size()>0) {
			if(name!=null&&!"".equals(name)) {
				Record record = new Record();
				record.set("id",id).set("name", name).set("remark", remark).set("is_delete", IsDelete.IS_DELETE_ON);
				boolean b = Db.update("personnel_classify", record);
				if (b) {
					return new Result(200, "修改成功");
				}
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
		System.out.println("业务层id："+id);
		String sql = "SELECT id,name,remark FROM personnel_classify WHERE  is_delete < 1 AND id = "+id;
		String tableName = "personnel_classify";
		//调用公共删除方法
		Result result = BaseMethodService.deleteTableRecord(tableName, sql);
		System.out.println("业务层返回值："+result.getCode()+",信息: " +result.getData()) ;
		return result;
		
	}
	
	
}

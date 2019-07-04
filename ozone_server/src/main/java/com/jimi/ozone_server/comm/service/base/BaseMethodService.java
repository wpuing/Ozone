package com.jimi.ozone_server.comm.service.base;


import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jimi.ozone_server.comm.constant.DeleteStatus;
import com.jimi.ozone_server.comm.model.Result;

/**
 * 公共业务方法基础类 <br>
 * <b>2019年6月24日</b>
 * 
 * @author 几米物联自动化部-韦姚忠
 *
 */
public class BaseMethodService {
	
	
	/**
	 * 删除方法
	 * @param tableName 要删除的记录所在的表名
	 * @param record	对应的实体类
	 * @return 			返回布尔值提示用户
	 */
	public static Result deleteTableRecord(String tableName,String sql) {
		boolean b = false;
		//通过id查询出该人员分类的实体
		List<Record> personnelClassify = Db.find(sql);
		//判断是否有值
		if (personnelClassify.size()>0) {
			//取出实体
			Record record = personnelClassify.get(0);
			//有值就执行删除方法
			record .set("is_delete", DeleteStatus.IS_DELETE_OFF);
			b = Db.update(tableName, record);
			if (b) {
				return new Result(200, "删除成功");
			}else {
				throw new RuntimeException("未知异常");
			}
		}else {
			return new Result(400, "该数据不存在");
		}
	}
	
	
	/**
	 * 提取字符串形式的id集合
	 * @param numbers ids
	 * @return integer元素list集合
	 */
	public List<Integer> ExtractNumber(String numbers) {
		String[] newNumbers = numbers.split(",");
		List<Integer> number_list = new ArrayList<>();
 		for (String number : newNumbers) {
			number_list.add(Integer.parseInt(number));
		}
		return number_list;
	}
	
	
	/**
	 * 判断遍历得到的id类型是否正确
	 * @param id 传入的id
	 * @return 信息提示
	 */
	public static Integer handleParameterType(String id) {
		Integer newId = null;
		try {
			newId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return null;
		}
		if (newId instanceof Integer) {
			return newId;
		}else {
			return null;
		}
		
	}

}

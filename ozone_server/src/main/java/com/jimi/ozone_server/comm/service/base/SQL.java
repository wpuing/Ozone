package com.jimi.ozone_server.comm.service.base;

/**
 * SQL语句类 <br>
 * <b>2019年7月7日</b>
 * 
 * @author 几米物联自动化部-韦姚忠
 *
 */
public class SQL {

	/**
	 * 甘特图业务SQL
	 */
	public static final String FIND_GANTT_BY_ID = "SELECT id,name,responsible,cycle,remark FROM gantt WHERE is_delete <1 AND id =";
	
	public static final String FIND_TASK_CLASSIFY_BY_GANTT_ID="SELECT id,name FROM task_classify WHERE is_delete <1 AND gantt =";
	
	public static final String FIND_TASK_BY_TASK_CLASSIFY_ID = "SELECT id,name,start_date,plans_end_date,actual_end_date FROM task WHERE is_delete <1 AND task_classify =";

	public static final String FIND_TASK_RELATION_BY_CURRENT_TASK = "SELECT id,predecessor_task,current_task FROM task_relation WHERE is_delete<1 AND current_task =";

	public static final String  FIND_PERSONNEL_TASK_BY_TASK_ID ="SELECT id,personnel,task,remark FROM personnel_task WHERE is_delete<1 AND task ="; 

	public static final String FIND_PERSONNEL_BY_ID = "SELECT id,name FROM personnel WHERE  is_delete<1 AND id =";

	public static final String FIND_GANTT_ALL = "SELECT id,name,responsible,cycle,remark FROM gantt WHERE is_delete <1";
	
	/**
	 * 人力资源显示SQL
	 */
	public static final String FIND_PERSONNEL_CLASSIFY_ALL = "SELECT id,name FROM personnel_classify WHERE is_delete < 1";
	
	public static final String FIND_PERSONEL_BY_PERSONNEL_CALSSIFY_ID = "SELECT id,name FROM personnel WHERE is_delete < 1 AND personnel_classify  =";
	
	public static final String FIND_PERSONNEL_CALSSIFY_BY_PERSONNEL_ID = "SELECT id,personnel,task FROM personnel_task WHERE is_delete < 1 AND personnel =";
	
	public static final String FIND_TASK_BY_ID = "SELECT id,name,gantt FROM task WHERE is_delete < 1 AND id = ";
	
	public static final String FINAL_GANTT_HR_BY_ID = "SELECT id,name FROM gantt WHERE is_delete <1 AND id = ";
	
	/**
	 * 人员分类SQL
	 */
	public static final String FIND_PER_CLASSIFY_ALL_IN_REMARK = "SELECT id,name,remark FROM personnel_classify WHERE  is_delete < 1";

	public static final String FIND_PER_CLASSIFY_BY_NAME = "SELECT id FROM personnel_classify WHERE name =";

	public static final String FIND_PER_CLASSIFY_BY_ID = "SELECT id,name,remark FROM personnel_classify WHERE  is_delete < 1 AND id = ";
	
	/**
	 * 人员SQL
	 */
	public static final String FIND_ALL_PERSONNEL_LIKE_BY_JOIN_CLASSIFY_NAME = "SELECT p.id,p.name,p.personnel_classify,pc.name personnel_classify_name FROM personnel p LEFT JOIN personnel_classify pc ON  p.personnel_classify = pc.id  WHERE p.is_delete < 1 AND p.name LIKE";

	public static final String FIND_PERSONNEL_BY_JOIN_CLASSIFY = "SELECT p.id,p.name,p.personnel_classify,pc.name personnel_classify_name FROM personnel p LEFT JOIN personnel_classify pc ON  p.personnel_classify = pc.id  WHERE  p.is_delete < 1";

	public static final String FIND_PERSONNEL_BY_NAME="SELECT id,name FROM personnel WHERE is_delete < 1 AND name =";

	public static final String FIND_PERSONNEL_TASK_INFO_BY_TASK_ID = "SELECT id,name,remark FROM personnel_classify WHERE  is_delete < 1 AND id = ";

	public static final String FIND_ALL_PERSONNEL_LIKE_BY__NAME = "SELECT id,name FROM personnel WHERE is_delete < 1 AND name = ";

	/**
	 * 任务分类SQL
	 */
	public static final String FIND_TASK_CLASSIFY_BY_ID = "SELECT id,name FROM task_classify WHERE is_delete < 1 AND id = ";
	
	public static final String FIND_TASK_CLASSIFY_BY_NAME_LIKE = "SELECT id,name,remark,gantt FROM task_classify WHERE is_delete <1 AND name LIKE ";
	
	public static final String FIND_TASK_CLASSIFY_BY_GANTT = "SELECT id,name,remark,gantt FROM task_classify WHERE is_delete <1  AND gantt =";
	
	public static final String FIND_TASK_CLASSIFY_ALL = "SELECT id,name,remark,gantt FROM task_classify WHERE  is_delete <1";
	
	public static final String FIND_TASK_CLASSIFY_BY_ID_REMARK = "SELECT id,name,remark,gantt FROM task_classify WHERE is_delete < 1 AND id = ";
	
	public static final String FIND_TASK_CLASSIFY_BY_NAME = "SELECT id,name,remark,gantt FROM task_classify WHERE  is_delete < 1 AND name = ";
	
	/**
	 * 任务SQL
	 */
	public static final String FIND_TASK_ALL = "SELECT id,name,start_date,plans_end_date,actual_end_date,task_classify FROM task WHERE is_delete <1";
	
	public static final String FIND_TASK_BY_LIKE_NAME = "SELECT id,name,start_date,plans_end_date,actual_end_date,task_classify FROM task WHERE is_delete <1 AND name LIKE ";
	
	public static final String FIND_TASK_CLASSIFY_NAME_BY_ID = "SELECT name,remark FROM task_classify WHERE is_delete<1 AND id =";
	
	public static final String FIND_PERSONNEL_TASK_BY_TASK = "SELECT id,personnel,task,remark FROM personnel_task WHERE is_delete<1 AND task =";
	
	public static final String FIND_TASK_BY_NAME ="SELECT id,name FROM task WHERE is_delete < 1 AND name =";
	
	public static final String FIND_PERSONNEL_TASK_BY_PERSONNEL = "SELECT * FROM personnel_task WHERE is_delete < 1 AND personnel = ";
	
	public static final String FIND_TASK_BY_ID_ALL_INFO = "SELECT id,start_date,plans_end_date from task WHERE is_delete < 1 AND id = ";
	
}

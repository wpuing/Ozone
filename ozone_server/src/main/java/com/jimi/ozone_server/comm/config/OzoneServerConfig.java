package com.jimi.ozone_server.comm.config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.tx.TxByMethodRegex;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.jimi.ozone_server.comm.controller.GanttController;
import com.jimi.ozone_server.comm.controller.HRController;
import com.jimi.ozone_server.comm.controller.PersonnelClassifyController;
import com.jimi.ozone_server.comm.controller.PersonnelController;
import com.jimi.ozone_server.comm.controller.TaskClassifyController;
import com.jimi.ozone_server.comm.controller.TaskController;
import com.jimi.ozone_server.comm.exception.ExceptionIntoLogInterceptor;
import com.jimi.ozone_server.comm.model.MysqlMappingKit;

public class OzoneServerConfig extends JFinalConfig{
	
	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 80, "/");
	}

	@Override
	public void configConstant(Constants me) {
		me.setDevMode(true);
		
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/gantt", GanttController.class);
		me.add("/perClassify", PersonnelClassifyController.class);
		me.add("/personnel", PersonnelController.class);
		me.add("/task", TaskController.class);
		me.add("/taskClassify", TaskClassifyController.class);
		me.add("/hr", HRController.class);
		
		
		
	}

	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configPlugin(Plugins me) {
		Prop prop = PropKit.use("properties.ini");
		DruidPlugin	dp = new DruidPlugin(prop.get("w_url"), prop.get("w_user"), prop.get("w_password"));
		me.add(dp);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		MysqlMappingKit.mapping(arp);
		me.add(arp);
	}

	@Override
	public void configInterceptor(Interceptors me) {
		//全局拦截器，对所有请求拦截
		 
        //添加控制层全局拦截器
        //interceptors.addGlobalActionInterceptor(new GlobalActionInterceptor());
		me.addGlobalActionInterceptor(new ExceptionIntoLogInterceptor());
        //添加业务层全局拦截器
        //interceptors.addGlobalServiceInterceptor(new GlobalServiceInterceptor());
		//me.addGlobalServiceInterceptor(new ExceptionIntoLogInterceptor());
 
        //兼容老版jfinal写法
        //interceptors.add(new GlobalActionInterceptor());
//		new TxByMethodRegex("(.*save.*|.*update.*|.*delete.*)");
		me.add(new TxByMethodRegex("(.*save.*|.*update.*|.*delete.*)"));
		
	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		
	}

}

package com.jimi.ozone_server.comm.model.gen;

import javax.sql.DataSource;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.druid.DruidPlugin;

public class GeneratorStarter {

	/**
	 * 用于生成表对应的Bean
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		genMysql();
	}

	private static void genMysql() {
		PropKit.use("properties.ini");
		String url = PropKit.get("w_url");
		String user = PropKit.get("w_user");
		String password = PropKit.get("w_password");
		// base model 所使用的包名
		String baseModelPkg = PropKit.get("baseModelPackage");
		// base model 文件保存路径
		String baseModelDir = PathKit.getWebRootPath() + PropKit.get("baseModelPath");
		// model 所使用的包名
		String modelPkg = PropKit.get("modelPackage");
		// model 文件保存路径
		String modelDir = baseModelDir + PropKit.get("modelPath");

		DruidPlugin druidPlugin = new DruidPlugin(url, user, password);
		druidPlugin.start();
		DataSource dataSource = druidPlugin.getDataSource();

		// 创建生成器
		Generator generator = new Generator(dataSource, baseModelPkg, baseModelDir, modelPkg, modelDir);
		// 设置是否生成链式 setter 方法
		generator.setGenerateChainSetter(false);
		// 设置方言
		generator.setDialect(new MysqlDialect());
		// 设置Mapping名字
		generator.setMappingKitClassName("MysqlMappingKit");
		// 设置是否在 Model 中生成 dao 对象
		generator.setGenerateDaoInModel(true);
		// 生成
		generator.generate();
	}
}

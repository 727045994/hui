package com.eim.util;

import java.util.HashMap;
import java.util.Map;
/**
 * 代码生成器
 * 注：运行main方法后，请手动F5刷新。
 */
public class GenCode {
	
	public static void main(String[] args) {
		Map<String,String> map=new HashMap<String,String>();
		/**
		 * 必填项
		 */
		//数据库地址
		map.put("URL", "jdbc:mysql://127.0.0.1:3306/qq?useUnicode=true&amp;characterEncoding=utf-8&amp;zeroDateTimeBehavior=convertToNull");
		//数据库驱动
		map.put("DRIVER", "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
		//数据库登录名(
		map.put("NAME", "root");
		//数据库登录密码
		map.put("PASS", "123456");
		//表名
		map.put("tablename","cust_service");
		/**
		 * 可选项
		 */
		//schema
		map.put("schema", "");
		/*
		 * 实体类配置
		 */
		map.put("entityPath", "com.eim.model");//实体类生成路径,可传空字符串,默认为com.by.common.autoCode
		/*
		 * mapper配置
		 */
		map.put("mapperPath", "com.eim.mapper");//mapper生成路径,可传空字符串,默认为com.by.common.autoCode
		/*
		 * service配置
		 */
//		map.put("servicePath", "com.ssm.biz.label.service");//service生成路径,可传空字符串,默认为com.by.common.autoCode
		/*
		 * controller配置
		 */
//		map.put("controllerPath", "com.emmc.biz.match.controller");//controller生成路径,可传空字符串,默认为com.by.common.autoCode
//		map.put("controllerRequestMappingValue", "/login");//controller访问路径,默认空字符串
//		map.put("controllerFileName", "LoginController");//controller文件名，默认实体名+Controller
		/*
		 * jsp、js配置
		 */
//		map.put("jspPath", "rule");//jsp生成路径,例如：system.test
//		map.put("jsPath", "rule");//js生成路径,例如：system.test
		
		GenCodeUtil util=new GenCodeUtil(map);
		util.genEntity();//执行生成
		util.genMapper();
//		util.genService();
	}
}

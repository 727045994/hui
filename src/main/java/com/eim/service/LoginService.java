package com.eim.service;

import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface LoginService {
	/**
	 * 
	* @author: LUOHUI 
	* @Date:2018年4月24日
	* @Description: TODO(根据账户查询token) 
	* @return Map<String,Object>    返回类型
	 */
	JSONObject getUserToken(String account,String userName);
	
	/**
	 * 
	* @author: LUOHUI 
	* @Date:2018年4月25日
	* @Description: TODO(系统分配客服账户) 
	* @return String    返回类型
	 */
	String Allot();
	/**
	 * 
	* @author: LUOHUI 
	* @Date:2018年4月25日
	* @Description: TODO(发送消息) 
	* @return String    返回类型
	 */
	String sendMsg(String from,String to,String msg);

}

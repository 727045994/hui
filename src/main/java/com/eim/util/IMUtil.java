package com.eim.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

public class IMUtil {
	
	private static Logger logger = LogManager.getLogger(IMUtil.class);
	
	/**
	 * 
	* @author: LUOHUI 
	* @Date:2018年4月25日
	* @Description: TODO(创建IM账户,如果有返回对应信息) 
	* @return String    返回类型
	 */
	public static JSONObject createUser(String account,String userName) {
		
		String url="https://api.netease.im/nimserver/user/create.action";
		String str="";
		JSONObject json=new JSONObject();
		Map<String, String> map=new HashMap<>();
		map.put("accid", account);
		map.put("name", userName);
		str=HttpClientUtil.sendpostJson(url, map);
		logger.debug("创建IM账户返回参数为："+str);
		json=JSONObject.fromObject(str);
		String code=json.getString("code");//返回状态
		
		if(code.equals("414")) {
			String desc=json.getString("desc");//返回详情
			if(desc.equals("already register")) {
				json=refreshToken(userName);
			}
		}
		return json;
	}
	/**
	 * 
	* @author: LUOHUI 
	* @Date:2018年4月25日
	* @Description: TODO(根据用户名查询token) 
	* @return JSONObject    返回类型
	 */
	public static JSONObject refreshToken(String userName) {
		String url="https://api.netease.im/nimserver/user/refreshToken.action";
		String str="";
		JSONObject json=new JSONObject();
		Map<String, String> map=new HashMap<>();
		map.put("accid", userName);
		str=HttpClientUtil.sendpostJson(url, map);
		json=JSONObject.fromObject(str);
		String code=json.getString("code");
		if(code.equals("200")) {
			return  json=json.getJSONObject("info");
		}
		return null;
	}
	/**
	 * 
	 * 	* @author: LUOHUI 
	* @Date:2018年4月25日
	* @Description: TODO(发送不同文本消息) 
	* @return boolean    返回类型
	 */
	public static String sendMsg(String from,String to,String msg) {
		
		JSONObject json=new JSONObject();
		String url="https://api.netease.im/nimserver/msg/sendMsg.action";
		Map<String, String> map=new HashMap<>();
		map.put("from", from);
		map.put("ope", "0");
		map.put("to", to);
		map.put("type", "0");
		JSONObject msgs=new JSONObject();
		msgs.put("msg", msg);
		map.put("body", msgs.toString());
		String str=HttpClientUtil.sendpostJson(url, map);
		json=JSONObject.fromObject(str);
		String code=json.getString("code");
		if(code.equals("200")) {
			return "200";
		}
		return "";
	}

}

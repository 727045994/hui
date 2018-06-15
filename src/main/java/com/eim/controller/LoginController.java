package com.eim.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eim.model.CustServiceInfo;
import com.eim.service.CustService;
import com.eim.service.LoginService;
import com.eim.util.BusinessException;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="/login")

public class LoginController {
	
	@Autowired
	private CustService custService;
	
	@Autowired
	private LoginService loginService;
	
	private static int a=0;
	
	/*@ResponseBody
	@RequestMapping(value="/getToken.do")
	public JSONObject getToken(String account) {
		return loginService.getUserToken(account);
	}*/
	
	
	@ResponseBody
	@RequestMapping(value="/getList.do")
	public List<CustServiceInfo> getList() throws BusinessException{
		return custService.getList(null);
	}
	/**
	 * 
	* @author: LUOHUI 
	* @Date:2018年4月25日
	* @Description: TODO(根据账户和用户名获取对应的账户和token) 
	* @return JSONObject    返回类型
	 */
	@ResponseBody
	@RequestMapping(value="/getToken.do")
	public JSONObject getToken(String account,String userName) {
		return loginService.getUserToken(account,userName);
	}
	
	/**
	 * 
	* @author: LUOHUI 
	* @Date:2018年4月25日
	* @Description: TODO(根据账户和用户名获取对应的账户和token，并返回系统分配的客服) 
	* @return JSONObject    返回类型
	 */
	@ResponseBody
	@RequestMapping(value="/getTokenAndAllot.do")
	public JSONObject getTokenAndAllot(String account,String userName) {
		JSONObject json=loginService.getUserToken(account,userName);
		json.put("to",loginService.Allot() );
		return json;
	}
	/**
	 * 
	* @author: LUOHUI 
	* @Date:2018年4月26日
	* @Description: TODO(发送消息) 
	* @return String    返回类型
	 */
	@ResponseBody
	@RequestMapping(value="/sendMsg.do")
	public String sendMsg(String from,String to,String msg) {
		return loginService.sendMsg(from, to, msg);
	}
	/**
	 * 
	* @author: LUOHUI 
	* @Date:2018年4月26日
	* @Description: TODO(登陆) 
	* @return int    返回类型
	 */
	@ResponseBody
	@RequestMapping(value="/submit.do")
	public Map<String, Object> submit(@RequestParam Map<String, Object> map) {
		try {
			CustServiceInfo info=custService.get(map);
			map.clear();
			if(null!=info) {
				map.put("code", 200);
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	

}

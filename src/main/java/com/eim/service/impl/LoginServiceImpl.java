package com.eim.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eim.mapper.CustServiceMapper;
import com.eim.model.CustServiceInfo;
import com.eim.service.LoginService;
import com.eim.util.IMUtil;

import net.sf.json.JSONObject;
@Service
public class LoginServiceImpl implements LoginService{
	
	@Autowired
	private CustServiceMapper custServiceMapper;
	
	private static  int cunt=0;

	@Override
	public JSONObject getUserToken(String account,String userName) {
		
		/*Map<String, String> map=new HashMap<>();
		map.put("accid", account);
		String str=HttpClientUtil.sendpostJson("https://api.netease.im/nimserver/user/refreshToken.action", map);
		JSONObject json=JSONObject.fromObject(str);*/
		
		JSONObject  json=IMUtil.createUser(account, userName);
		
		
		return json;
	}

	@Override
	public String Allot() {
		String to="";
		cunt++;
		List<CustServiceInfo> list=custServiceMapper.getList(null);
		if(cunt>list.size()) {
			cunt=1;
		}
		CustServiceInfo info=list.get(cunt%list.size());
		to=info.getAccount();
		
		return to;
	}

	@Override
	public String sendMsg(String from, String to, String msg) {
		if(null==from||""==from) {
			return "发送人不能为空";
		}
		if(null==to||""==to) {
			return "接受人不能为空";
		}
		if(null==msg||""==msg) {
			return "消息不能为空";
		}
		String code=IMUtil.sendMsg(from, to, msg);
		return code;
	}

}

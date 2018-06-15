package com.eim.wechat.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eim.wechat.service.WeChatService;

@Controller
@RequestMapping(value="/wechat")
public class WechatController {
	@Autowired
	private WeChatService weChatService;
	
	@ResponseBody
	@RequestMapping(value="/jieru.do")
	public String jieru(HttpServletRequest req,HttpServletResponse res) {
		String meth=req.getMethod();
		if(meth.equals("GET")) {
			  String signature = req.getParameter("signature");// 微信加密签名  
              String timestamp = req.getParameter("timestamp");// 时间戳  
              String nonce = req.getParameter("nonce");// 随机数  
              String echostr = req.getParameter("echostr");//随机字符串  
              return echostr;
		}else {
			
			String msg=weChatService.weixinPost(req);
			return msg;
		}
		
	}
	
	

}

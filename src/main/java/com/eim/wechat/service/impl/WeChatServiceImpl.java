package com.eim.wechat.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.eim.wechat.model.TextMessage;
import com.eim.wechat.service.WeChatService;
import com.eim.wechat.util.MessageUtil;
@Service
public class WeChatServiceImpl implements WeChatService{

	@Override
	public String weixinPost(HttpServletRequest req) {
		 String respMessage = null;
		 
		
		try {
			  // xml请求解析
	         Map<String, String> requestMap = MessageUtil.xmlToMap(req);
			  // 发送方帐号（open_id）
	         String fromUserName = requestMap.get("FromUserName");
	         // 公众帐号
	         String toUserName = requestMap.get("ToUserName");
	         // 消息类型
	         String msgType = requestMap.get("MsgType");
	         // 消息内容
	         String content = requestMap.get("Content");
	         
	         // 文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                //这里根据关键字执行相应的逻辑，只有你想不到的，没有做不到的
                if(content.equals("你好")){
                	 //自动回复
                    TextMessage text = new TextMessage();
                    text.setContent("您好");
                    text.setToUserName(fromUserName);
                    text.setFromUserName(toUserName);
                    text.setCreateTime(new Date().getTime() + "");
                    text.setMsgType(msgType);
                    
                    respMessage = MessageUtil.textMessageToXml(text);
                }else {
                	//转到客服系统
                	 TextMessage text = new TextMessage();
                     //text.setContent("您好");
                     text.setToUserName(fromUserName);
                     text.setFromUserName(toUserName);
                     text.setCreateTime(new Date().getTime() + "");
                     text.setMsgType("transfer_customer_service");
                     
                     respMessage = MessageUtil.textMessageToXml(text);
                }
                
               
               
            }else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
            	  //自动回复
                TextMessage text = new TextMessage();
                text.setContent("欢迎关注");
                text.setToUserName(fromUserName);
                text.setFromUserName(toUserName);
                text.setCreateTime(new Date().getTime() + "");
                text.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
                respMessage = MessageUtil.textMessageToXml(text);
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

       
         
		return respMessage;
	}

}

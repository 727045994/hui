package com.eim.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;



/***
 * http工具类
 * @author Administrator
 *
 */
public class HttpClientUtil {
	private static Logger logger = LogManager.getLogger(HttpClientUtil.class);
	
	 /** 
     * 发送 get请求 
     */  
    public static String sendGet(String url) {  
    	String content = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        try {  
            // 创建httpget.    
            HttpGet httpget = new HttpGet(url);  
            System.out.println("executing request " + httpget.getURI());  
            // 执行get请求.    
            CloseableHttpResponse response = httpclient.execute(httpget);
            response.setHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8") ;
            try {   
                // 获取响应实体    
                HttpEntity entity = response.getEntity();  
                System.out.println("--------------------------------------");  
                // 打印响应状态    
                System.out.println(response.getStatusLine());  
                if (entity != null) {  
                    // 打印响应内容长度    
                	content = EntityUtils.toString(entity,"utf-8");
                    System.out.println("Response content length: " + entity.getContentLength());  
                    // 打印响应内容    
                    System.out.println("Response content: " + content);  
                }  
                System.out.println("------------------------------------");  
            } finally {  
            	response.close();  
            } 
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }
        return content;
    }  
	
	public static String sendPost(String url,Map<String, String> mapParams) {
		String content = "";
		// 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();
       
        //创建httpPost 
        HttpPost httpPost = new HttpPost(url);
       
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000 * 60).setConnectTimeout(1000 * 60).build();//设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        //httpPost.setHeader("Content-Type","application/json") ;
        CloseableHttpResponse response = null;
       
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
        Set<Entry<String, String>> entrySet = mapParams.entrySet();
       	for (Entry<String,String> entry : entrySet) {
       		formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));  
		}
        try {  
        	UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
            httpPost.setEntity(uefEntity);  
            System.out.println("executing request " + httpPost.getURI());  
            response = httpclient.execute(httpPost);  
            HttpEntity entity = response.getEntity();  
            if (entity != null) {
            	content = EntityUtils.toString(entity, "UTF-8");
            	System.out.println("--------------------------------------");
            	System.out.println("Status: "+ response.getStatusLine());
           		System.out.println("Response content: " + content);  
           		System.out.println("--------------------------------------");  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {  
            // 关闭连接,释放资源    
            try {  
            	response.close();
            	httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return content;
	}
	public static String httpRestOfAll(String soap, String httpUrl) {
		String string = null;
		try {
			PostMethod postMethod = new PostMethod(httpUrl);

			byte[] b = soap.getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b, 0, b.length);
			RequestEntity re = new InputStreamRequestEntity(is, b.length, "application/json; charset=utf-8");
			postMethod.setRequestEntity(re);

			HttpClient httpClient = new HttpClient();

			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);

			httpClient.getHttpConnectionManager().getParams().setSoTimeout(10000);
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == 200) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(postMethod.getResponseBodyAsStream(), "UTF-8"));
				StringBuffer stringBuffer = new StringBuffer();
				String str;
				while ((str = reader.readLine()) != null) {
					stringBuffer.append(str);
				}
				string = stringBuffer.toString().trim().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
			}
		} catch (IllegalArgumentException e) {
			logger.error("httpRest接口调用实现方法IllegalArgumentException异常-->>" + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("httpRest接口调用实现方法IOException异常日志-->>" + e.getMessage(), e);
		}
		return string;
	}
	
	public static String sendpostJson(String url,Map<String, String> map) {
		/*String AppKey="357344fe9d851c45a277e9af963fe9c9";
		String appSecret="6bbb355ad05f";
		String Nonce="6666666";
		String CurTime=String.valueOf(new Date().getTime()/1000);
		String CheckSum=CheckSumBuilder.getCheckSum(appSecret, Nonce, CurTime);*/
		String content = "";
		// 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();
       
        //创建httpPost 
        HttpPost httpPost = new HttpPost(url);
       
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000 * 60).setConnectTimeout(1000 * 60).build();//设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
      /*  httpPost.setHeader("AppKey",AppKey);
        httpPost.setHeader("Nonce",Nonce);
        httpPost.setHeader("CurTime",CurTime);
        httpPost.setHeader("CheckSum",CheckSum);*/
       // httpPost.setHeader("Content-Type","application/json") ;
        CloseableHttpResponse response = null;
       
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
        Set<Entry<String, String>> entrySet = map.entrySet();
       	for (Entry<String,String> entry : entrySet) {
       		formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));  
		}
        try {  
        	UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
            httpPost.setEntity(uefEntity);  
            System.out.println("executing request " + httpPost.getURI());  
            response = httpclient.execute(httpPost);  
            HttpEntity entity = response.getEntity();  
            if (entity != null) {
            	content = EntityUtils.toString(entity, "UTF-8");
            	System.out.println("--------------------------------------");
            	System.out.println("Status: "+ response.getStatusLine());
           		System.out.println("Response content: " + content);  
           		System.out.println("--------------------------------------");  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {  
            // 关闭连接,释放资源    
            try {  
            	response.close();
            	httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return content;
	}
	public static String sendPostJson(String url,JSONObject json) {
		String content = "";
		// 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();
       
        //创建httpPost 
        HttpPost httpPost = new HttpPost(url);
       
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000 * 60).setConnectTimeout(1000 * 60).build();//设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        CloseableHttpResponse response = null;
        try {  
            httpPost.setEntity(new StringEntity(json.toString()));  
            System.out.println("executing request " + httpPost.getURI());  
            response = httpclient.execute(httpPost);  
            HttpEntity entity = response.getEntity();  
            if (entity != null) {
            	content = EntityUtils.toString(entity, "UTF-8");
            	System.out.println("--------------------------------------");
            	System.out.println("Status: "+ response.getStatusLine());
           		System.out.println("Response content: " + content);  
           		System.out.println("--------------------------------------");  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {  
            // 关闭连接,释放资源    
            try {  
            	response.close();
            	httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return content;
	}
	 public static JSONObject doPost(String url,JSONObject json){
		    DefaultHttpClient client = new DefaultHttpClient();
		    HttpPost post = new HttpPost(url);
		    JSONObject response = null;
		    try {
		      StringEntity s = new StringEntity(json.toString());
		      s.setContentEncoding("UTF-8");
		      s.setContentType("application/json");//发送json数据需要设置contentType
		      post.setEntity(s);
		      HttpResponse res = client.execute(post);
		      if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
		        HttpEntity entity = res.getEntity();
		        String result = EntityUtils.toString(res.getEntity());// 返回json格式：
		        response = JSONObject.fromObject(result);
		      }
		    } catch (Exception e) {
		      throw new RuntimeException(e);
		    }
		    return response;
		  }
	
	public static void main(String[] args) throws java.text.ParseException {
		/*String url="https://api.netease.im/nimserver/msg/sendMsg.action";
		Map<String, String> map=new HashMap<>();
		map.put("from", "admin");
		map.put("ope", "0");
		map.put("to", "lisi");
		map.put("type", "0");
		map.put("body", "{'msg':'哈哈哈哈'}");
		String str=sendpostJson(url, map);
		System.out.println(str);*/
		
		/*String url="https://api.netease.im/nimserver/user/create.action";
		Map<String, String> map=new HashMap<>();
		map.put("accid", "admin");
		map.put("name", "admin");
		String str=sendpostJson(url, map);
		System.out.println(str);*/
		
		/*String url="https://api.netease.im/nimserver/user/refreshToken.action";
		Map<String, String> map=new HashMap<>();
		map.put("accid", "lisi");
		String str=sendpostJson(url, map);
		System.out.println(str);*/
		
		
	/*	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String url="https://api.netease.im/nimserver/history/querySessionMsg.action";
		Map<String, String> map=new HashMap<>();
		map.put("from", "luohui");
		map.put("to", "lisi");
		map.put("begintime", String.valueOf(sdf.parse("2018-4-23 14:00:00").getTime()));
		map.put("endtime", String.valueOf(sdf.parse("2018-4-24 15:30:00").getTime()));
		map.put("limit", "100");
		String str=sendpostJson(url, map);
		System.out.println(str);*/
		
		//获取token
		/*String appId="wx19eb7b496e9e29cc";
		String secret="ae1479087ca5f5fbf2ba56a80b5cec7b";
		String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appId+"&secret="+secret;
		
		String str=sendGet(url);
		System.out.println(str);*/
		
		//微信
		
		//获取客服基本信息
		/*String token="9_VxLbxCx9KFoCI5DNf7R6AkLejBcATV1w6t3ryZaCD1N_CGj1ova7iN-5jlMPFMRRLUBkWjQEwR33akw6w8QLBY6uFK_rAAWVyhQJlFM-FLxrfslhz2xJ6uCl8E6cd2WO-BS2Zfm1M9833KnSYJCcAHAQBU";
		String url="https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token="+token;
		String str=sendGet(url);
		System.out.println(str);*/
		
		/*String token="9_VxLbxCx9KFoCI5DNf7R6AkLejBcATV1w6t3ryZaCD1N_CGj1ova7iN-5jlMPFMRRLUBkWjQEwR33akw6w8QLBY6uFK_rAAWVyhQJlFM-FLxrfslhz2xJ6uCl8E6cd2WO-BS2Zfm1M9833KnSYJCcAHAQBU";
		String url="https://api.weixin.qq.com/customservice/kfaccount/inviteworker?access_token="+token;
		Map<String, String> map=new HashMap<>();
		map.put("kf_account", "kf2007@i-mineral");
		map.put("invite_wx", "fdsf");
		String str=sendpostJson(url,map);
		System.out.println(str);*/
		
		//获取聊天记录
		/*SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String  starttime=String.valueOf(sdf.parse("2018-04-28 08:00:00").getTime()/1000);
		String endtime=String.valueOf(sdf.parse("2018-04-28 17:50:00").getTime()/1000);
		String token="9_sKri63c8yA531xIRPbUEGjzFvs4L_TrOA7VqGqjVmYrC0rFZj1HxYiDl05HEmuxskuf2ff-PVAeszomGOmGXXzdUoAMGGDYJ4XmthgcmvR2tMcGGjkOgBDFkD2xIZ5C6hcbV_a_ca6TygGwdCZHiAAAKHP";
		String url="https://api.weixin.qq.com/customservice/msgrecord/getmsglist?access_token="+token;
		JSONObject json=new JSONObject();
		json.put("starttime", starttime);
		json.put("endtime", endtime);
		json.put("msgid", "1");
		json.put("number", "10000");
		String str=sendPostJson(url, json);
		//json=sendPostJson(url,json);
		System.out.println(str);*/
		
		String token="24.25f56bbe199731fff8a4d2382df05e2e.2592000.1528357705.282335-11209074";
		String url="https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic?access_token="+token;
		String img=getImgStr("C:\\Users\\Administrator\\Desktop/2.jpg");
		Map<String, String> map=new HashMap<>();
		//map.put("url", "http://img.zcool.cn/community/013e6858146b58a84a0d304f951f9b.jpg@1280w_1l_2o_100sh.jpg");
		map.put("image", img);
		String str=sendpostJson(url, map);
		System.out.println(str);
		
		//System.out.println(getImgStr("C:\\Users\\Administrator\\Desktop/2.jpg"));
	}
	
	 /**
     * 将图片转换成Base64编码
     * @param imgFile 待处理图片
     * @return
     */
    public static String getImgStr(String imgFile){
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
  
        
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try 
        {
            in = new FileInputStream(imgFile);        
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }

}

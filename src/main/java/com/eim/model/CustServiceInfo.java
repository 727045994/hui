package com.eim.model;

import java.util.Date;
import java.math.BigDecimal;
//CUST_SERVICE
public class CustServiceInfo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/**  */
	private int id;
	/** 账号 */
	private String account;
	
	private String password;
	
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id=id;
	}
	public String getAccount(){
		return account;
	}
	public void setAccount(String account){
		this.account=account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}


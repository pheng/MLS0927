package net.chinawuyue.mls.login;

import java.io.Serializable;

/**
 * µÇÂ¼ÐÅÏ¢
 * */
public class LoginInfo implements Serializable{
	public String userCode;
	public String userName;
	public String role;
	public String lastLoginDate;
	public String versionCode;
	public String count1;
	public String count2;
	public String orgId;
	public String orgName;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "userCode:" + userCode + " -userName:" + userName + " -role:" + role + " -orgId:" + orgId + " -orgName:" + orgName;
	}
}


package net.chinawuyue.mls.after_loan;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 贷后管理返回报文数据结构（常规）
 * @author Administrator
 *
 */
public class AfterLoanCommonObject {
	
	//数据列表
	/**
	 * 客户编号
	 */
	private String customerID;
	
	/**
	 * 客户名称
	 */
	private String customerName;
	
	/**
	 * 检查人
	 */
	private String inputUserName;
	
	/**
	 * 检查机构
	 */
	private String inputOrgName;
	
	/**
	 * 上次检查完成日期
	 */
	private String updateDate;
	
	/**
	 * 贷后检查流水号
	 */
	private String iiSerialNo;
	
	public AfterLoanCommonObject() {
		
	}
	
	public AfterLoanCommonObject(String jsonData) {
		paserJsonData(jsonData);
	}
	
	//解析请求回的json数据，初始化数据类表变量
		private void paserJsonData(String jsonData){
			try {
				JSONObject jsonObj = new JSONObject(jsonData);
				this.customerID = jsonObj.optString("CUSTOMERID","");
				this.customerName = jsonObj.optString("CUSTOMERNAME","");
				this.inputOrgName = jsonObj.optString("INPUTORGNAME","");
				this.inputUserName = jsonObj.optString("INPUTUSERNAME","");
				this.inspectDate = jsonObj.optString("INSPECTDATE","");
				this.inspectTypeName = jsonObj.optString("INSPECTTYPENAME","");
				this.inspectUserID = jsonObj.optString("INSPECTUSERID","");
				this.updateDate = jsonObj.optString("UPDATEDATE","");
				this.iiSerialNo = jsonObj.optString("IISERIALNO","");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getInputUserName() {
		return inputUserName;
	}

	public void setInputUserName(String inputUserName) {
		this.inputUserName = inputUserName;
	}

	public String getInputOrgName() {
		return inputOrgName;
	}

	public void setInputOrgName(String inputOrgName) {
		this.inputOrgName = inputOrgName;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getInspectTypeName() {
		return inspectTypeName;
	}

	public void setInspectTypeName(String inspectTypeName) {
		this.inspectTypeName = inspectTypeName;
	}

	public String getInspectDate() {
		return inspectDate;
	}

	public void setInspectDate(String inspectDate) {
		this.inspectDate = inspectDate;
	}

	public String getInspectUserID() {
		return inspectUserID;
	}

	public void setInspectUserID(String inspectUserID) {
		this.inspectUserID = inspectUserID;
	}

	public String getIiSerialNo() {
		return iiSerialNo;
	}

	public void setIiSerialNo(String iiSerialNo) {
		this.iiSerialNo = iiSerialNo;
	}

	/**
	 * 贷后检查频率 
	 */
	private String inspectTypeName;
	
	/**
	 * 贷后检查截止日期
	 */
	private String inspectDate;
	
	/**
	 * 发起人
	 */
	private String inspectUserID;

}

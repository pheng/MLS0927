
package net.chinawuyue.mls.before_loan;


import org.json.JSONObject;

/**
 * 贷款审批业务列表数据结构
 * @author Administrator
 *
 */
public class LoanDataObject {
	
	/**
	 * 申请流水号
	 */
	private String serialNO;

	public String getManageUserName() {
		return manageUserName;
	}

	public void setManageUserName(String manageUserName) {
		this.manageUserName = manageUserName;
	}

	/**
	 * 客户编号
	 */
	private String customerID;
	
	/**
	 * 客户名称
	 */
	private String customerName;
	
	/**
	 * 业务品种
	 */
	private String businessType;
	
	/**
	 * 申请金额
	 */
	private String businessSUM;
	
	/**
	 * 流程类型
	 * not necessary
	 */
	private String flowNO;
	
	/**
	 * 阶段编号
	 * not necessary
	 */
	private String phaseNO;
	
	/**
	 * 对象类型
	 */
	private String objectType;
	
	/**
	 * 管户人
	 */
	private String manageUserName;
	
	/**
	 * 管户机构
	 */
	private String manageOrgName;

	/**
	 * 创建日期
	 */
	private String inputDate;
	
	public LoanDataObject() {
		
	}
	
	public LoanDataObject(String jsonData) {
		parseJsonData(jsonData);
	}
	
	private void parseJsonData(String jsonData) {
		try {
			JSONObject jsonObj = new JSONObject(jsonData);
			this.businessSUM = jsonObj.optString("BUSINESSSUM", "");
			this.businessType = jsonObj.optString("BUSINESSTYPE", "");
			this.customerID = jsonObj.optString("CUSTOMERID", "");
			this.customerName = jsonObj.optString("CUSTOMERNAME", "");
			this.flowNO = jsonObj.optString("FLOWNO", "");
			this.inputDate = jsonObj.optString("INPUTDATE", "");
			this.manageOrgName = jsonObj.optString("MANAGEORGNAME", "");
			this.objectType = jsonObj.optString("OBJECTTYPE", "");
			this.phaseNO = jsonObj.optString("PHASENO", "");
			this.serialNO = jsonObj.optString("SERIALNO", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getSerialNO() {
		return serialNO;
	}

	public void setSerialNO(String serialNO) {
		this.serialNO = serialNO;
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

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getBusinessSUM() {
		return businessSUM;
	}

	public void setBusinessSUM(String businessSUM) {
		this.businessSUM = businessSUM;
	}

	public String getFlowNO() {
		return flowNO;
	}

	public void setFlowNO(String flowNO) {
		this.flowNO = flowNO;
	}

	public String getPhaseNO() {
		return phaseNO;
	}

	public void setPhaseNO(String phaseNO) {
		this.phaseNO = phaseNO;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getManageOrgName() {
		return manageOrgName;
	}

	public void setManageOrgName(String managerOrgName) {
		this.manageOrgName = managerOrgName;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}
}


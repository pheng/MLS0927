
package net.chinawuyue.mls.before_loan;

import org.json.JSONObject;

/**
 * 贷款客户详情数据结构
 * @author Administrator
 *
 */
public class LoanCusInfoObject {

	/**
	 * 客户编号
	 */
	private String customerID;
	
	/**
	 * 客户名称
	 */
	private String customerName;
	
	/**
	 * 客户类型
	 */
	private String customerType;
	
	/**
	 * 证件类型
	 */
	private String certType;
	
	/**
	 * 证件号码
	 */
	private String certID;
	
	/**
	 * 地址
	 */
	private String add;
	
	/**
	 * 联系电话
	 */
	private String tel;
	
	/**
	 * 客户分类
	 */
	private String industryType;
	
	/**
	 * 客户关联人类型
	 */
	private String relativeType;
	
	/**
	 * 客户关联人名称
	 */
	private String relativeName;
	
	/**
	 * 管户人
	 */
	private String manageUserName;
	
	/**
	 * 管户机构
	 */
	private String manageOreName;
	
	/**
	 * 登记机构
	 */
	private String inputOrgName;
	
	/**
	 * 登记人
	 */
	private String inputUserName;
	
	/**
	 * 登记日期
	 */
	private String inputDate;
	
	public LoanCusInfoObject() {
	}
	
	public LoanCusInfoObject(String jsonData) {
		parseJsonData(jsonData);
	}
	
	private void parseJsonData(String jsonData) {
		try {
			JSONObject jsonObj = new JSONObject(jsonData);
			this.add = jsonObj.optString("ADD", "");
			this.certID = jsonObj.optString("CERTID", "");
			this.certType = jsonObj.optString("CERTTYPE", "");
			this.customerID = jsonObj.optString("CUSTOMERID", "");
			this.customerName = jsonObj.optString("CUSTOMERNAME", "");
			this.customerType = jsonObj.optString("CUSTOMERTYPE", "");
			this.industryType = jsonObj.optString("INDUSTRYTYPE", "");
			this.inputDate = jsonObj.optString("INPUTDATE", "");
			this.inputOrgName = jsonObj.optString("INPUTORGNAME", "");
			this.inputUserName = jsonObj.optString("INPUTUSERNAME", "");
			this.manageOreName = jsonObj.optString("MANAGEORGNAME", "");
			this.manageUserName = jsonObj.optString("MANAGEUSERNAME", "");
			this.relativeName = jsonObj.optString("RELATIVETNAME", "");
			this.relativeType = jsonObj.optString("RELATIVETYPE", "");
			this.tel = jsonObj.optString("TEL", "");
		} catch (Exception e) {
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

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getCertID() {
		return certID;
	}

	public void setCertID(String certID) {
		this.certID = certID;
	}

	public String getAdd() {
		return add;
	}

	public void setAdd(String add) {
		this.add = add;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getIndustryType() {
		return industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	public String getRelativeType() {
		return relativeType;
	}

	public void setRelativeType(String relativeType) {
		this.relativeType = relativeType;
	}

	public String getRelativeName() {
		return relativeName;
	}

	public void setRelativeName(String relativeName) {
		this.relativeName = relativeName;
	}

	public String getManageUserName() {
		return manageUserName;
	}

	public void setManageUserName(String manageUserName) {
		this.manageUserName = manageUserName;
	}

	public String getManageOreName() {
		return manageOreName;
	}

	public void setManageOreName(String manageOreName) {
		this.manageOreName = manageOreName;
	}

	public String getInputOrgName() {
		return inputOrgName;
	}

	public void setInputOrgName(String inputOrgName) {
		this.inputOrgName = inputOrgName;
	}

	public String getInputUserName() {
		return inputUserName;
	}

	public void setInputUserName(String inputUserName) {
		this.inputUserName = inputUserName;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}
	
}


package net.chinawuyue.mls.after_loan;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 贷后管理返回报文数据结构（首次）
 * @author Administrator
 *
 */
public class AfterLoanObject {
	
	//数据列表
	/**
	 * 客户编号
	 */
	private String customerID;
	
	/**
	 * 借据编号
	 */
	private String serialNO;
	
	/**
	 * 客户名称
	 */
	private String customerName;
	
	/**
	 * 业务品种
	 */
	private String businessType;
	
	/**
	 * 放款金额
	 */
	private String businessSUM;
	
	/**
	 * 贷款余额
	 */
	private String balance;
	
	/**
	 * 起始日
	 */
	private String putoutDate;
	
	/**
	 * 到期日
	 */
	private String maturity;
	
	/**
	 * 贷后检查截止日期
	 */
	private String inspectDateName;
	
	/**
	 * 检查人
	 */
	private String inputUserName;
	
	/**
	 * 检查机构
	 */
	private String inputOrgName;
	
	/**
	 * 贷后检查流水号
	 */
	private String iiSerialNo;
	

	public AfterLoanObject() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 贷后检查
	 * @param jsonData 必须为json格式的贷后数据
	 */
	public AfterLoanObject(String jsonData){
		//解析请求回的json数据，初始化数据类表变量
		paserJsonData(jsonData);
	}
	
	//解析请求回的json数据，初始化数据类表变量
	private void paserJsonData(String jsonData){
		try {
			JSONObject jsonObj = new JSONObject(jsonData);
			this.balance = jsonObj.optString("BALANCE", "");
			this.businessSUM = jsonObj.optString("BUSINESSSUM", "");
			this.businessType = jsonObj.optString("BUSINESSTYPE", "");
			this.customerID = jsonObj.optString("CUSTOMERID", "");
			this.customerName = jsonObj.optString("CUSTOMERNAME", "");
			this.inputOrgName = jsonObj.optString("INPUTORGNAME", "");
			this.inputUserName = jsonObj.optString("INPUTUSERNAME", "");
			this.inspectDateName = jsonObj.optString("INSPECTDATENAME", "");
			this.maturity = jsonObj.optString("MATURITY", "");
			this.putoutDate = jsonObj.optString("PUTOUTDATE", "");
			this.serialNO = jsonObj.optString("SERIALNO", "");
			this.iiSerialNo = jsonObj.optString("IISERIALNO", "");
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


	public String getSerialNO() {
		return serialNO;
	}


	public void setSerialNO(String serialNO) {
		this.serialNO = serialNO;
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


	public String getBalance() {
		return balance;
	}


	public void setBalance(String balance) {
		this.balance = balance;
	}


	public String getPutoutDate() {
		return putoutDate;
	}


	public void setPutoutDate(String putoutDate) {
		this.putoutDate = putoutDate;
	}


	public String getMaturity() {
		return maturity;
	}


	public void setMaturity(String maturity) {
		this.maturity = maturity;
	}


	public String getInspectDateName() {
		return inspectDateName;
	}


	public void setInspectDateName(String inspectDateName) {
		this.inspectDateName = inspectDateName;
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

	public String getIiSerialNo() {
		return iiSerialNo;
	}

	public void setIiSerialNo(String iiSerialNo) {
		this.iiSerialNo = iiSerialNo;
	}
	
	
}


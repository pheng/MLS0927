package net.chinawuyue.mls.before_loan;

import org.json.JSONObject;

/**
 * 贷款详情数据结构
 * @author Administrator
 *
 */
public class LoanDetailInfoObject {

	private String serialNO;
	private String customerID;
	private String customerName;
	private String businessType;
	private String direction;
	private String occurType;
	private String businessSum;
	private String purpose;
	private String isReferFarming;
	private String termMonth;
	private String rateType;
	private String rateFloat;
	private String businessRate;
	private String vouchType;
	private String icType;
	private String paySource;
	private String inputOrgName;
	private String inputUserName;
	private String inputDate;
	
	public LoanDetailInfoObject() {
	}
	
	public LoanDetailInfoObject(String obj){
		//init a LoanDetailInfoObject by using the json data
//		this.businessRate = 1;
//		this.businessSum = 12930;
//		this.businessType = "type1";
//		this.customerID = "100";
//		this.customerName = "张三";
//		this.direction = "dir";
//		this.icType = "icType";
//		this.inputDate = "20130101";
//		this.inputOrgName = "org武汉";
//		this.inputUserName = "张三";
//		this.isReferFarming = "yes";
//		this.occurType = "ocType";
//		this.paySource = "work";
//		this.purpose = "purP";
//		this.rateFloat = 0.65;
//		this.rateType = "business";
//		this.serialNO = "sNO";
//		this.termMonth = 6;
//		this.vouchType = "voType";
		parseJsonData(obj);
	}
	
	private void parseJsonData(String data){
		try {
			JSONObject jsonObj = new JSONObject(data);
			this.businessRate = jsonObj.optString("BUSINESSRATE", "");
			this.businessSum = jsonObj.optString("BUSINESSSUM", "");
			this.businessType = jsonObj.optString("BUSINESSTYPE", "");
			this.customerID = jsonObj.optString("CUSTOMERID", "");
			this.customerName = jsonObj.optString("CUSTOMERNAME", "");
			this.direction = jsonObj.optString("DIRECTION", "");
			this.icType = jsonObj.optString("ICTYPE", "");
			this.inputDate = jsonObj.optString("INPUTDATE", "");
			this.inputOrgName = jsonObj.optString("INPUTORGNAME", "");
			this.inputUserName = jsonObj.optString("INPUTUSERNAME", "");
			this.isReferFarming = jsonObj.optString("ISREFERFARMING", "");
			this.occurType = jsonObj.optString("OCCURTYPE", "");
			this.paySource = jsonObj.optString("PAYSOURCE", "");
			this.purpose = jsonObj.optString("PURPOSE", "");
			this.serialNO = jsonObj.optString("SERIALNO", "");
			this.termMonth = jsonObj.optString("TERMMONTH", "");
			this.vouchType = jsonObj.optString("VOUCHTYPE", "");
			this.rateType = jsonObj.optString("RATETYPE", "");
			this.rateFloat = jsonObj.optString("RATEFLOAT", "");
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
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getOccurType() {
		return occurType;
	}
	public void setOccurType(String occurType) {
		this.occurType = occurType;
	}
	public String getBusinessSum() {
		return businessSum;
	}
	public void setBusinessSum(String businessSum) {
		this.businessSum = businessSum;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getIsReferFarming() {
		return isReferFarming;
	}
	public void setIsReferFarming(String isReferFarming) {
		this.isReferFarming = isReferFarming;
	}
	public String getTermMonth() {
		return termMonth;
	}
	public void setTermMonth(String termMonth) {
		this.termMonth = termMonth;
	}
	public String getRateType() {
		return rateType;
	}
	public void setRateType(String rateType) {
		this.rateType = rateType;
	}
	public String getRateFloat() {
		return rateFloat;
	}
	public void setRateFloat(String rateFloat) {
		this.rateFloat = rateFloat;
	}
	public String getBusinessRate() {
		return businessRate;
	}
	public void setBusinessRate(String businessRate) {
		this.businessRate = businessRate;
	}
	public String getVouchType() {
		return vouchType;
	}
	public void setVouchType(String vouchType) {
		this.vouchType = vouchType;
	}
	public String getIcType() {
		return icType;
	}
	public void setIcType(String icType) {
		this.icType = icType;
	}
	public String getPaySource() {
		return paySource;
	}
	public void setPaySource(String paySource) {
		this.paySource = paySource;
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

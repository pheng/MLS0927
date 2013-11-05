
package net.chinawuyue.mls.before_loan;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 贷款审批请求报文数据结构
 * @author Administrator
 *
 */
public class BeforeLoanRequest {
	
	public BeforeLoanRequest() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 贷款审批业务列表请求报文
	 * @author Administrator
	 *
	 */
	class LoanListRequest{
		private String CODENO = "XD0003";
		private String USERID = null;
		
		/**
		 * 业务类型
		 * 010 待审批
		 * 020 已审批
		 * 030 被退回
		 */
		private String APPROVETYPE = null;

		public LoanListRequest() {
			// TODO Auto-generated constructor stub
		}
		
		public LoanListRequest(String USERID, String APPROVETYPE){
			this.USERID = USERID;
			this.APPROVETYPE = APPROVETYPE;
		}
		
		public JSONObject jsonRequest(){
			JSONObject obj = new JSONObject();
			try {
				obj.put("CODENO", CODENO);
				obj.put("userid", USERID);
				obj.put("approveType", APPROVETYPE);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return obj;
		}
		
		public String getCODENO() {
			return CODENO;
		}
		
		public String getUSERID() {
			return USERID;
		}

		public void setUSERID(String uSERID) {
			USERID = uSERID;
		}

		public String getAPPROVETYPE() {
			return APPROVETYPE;
		}

		/**
		 * 设置业务类型
		 * @param aPPROVETYPE 010 待完成；020 已审批；030 被退回
		 */
		public void setAPPROVETYPE(String aPPROVETYPE) {
			APPROVETYPE = aPPROVETYPE;
		}
	}
	
	/**
	 * 贷款详细请求报文
	 * @author Administrator
	 *
	 */
	class LoanDetailRequest{
		private String CODENO = "XD0004";
		private String SERIALNO = null;
		
		public LoanDetailRequest() {
			// TODO Auto-generated constructor stub
		}
		
		public LoanDetailRequest(String SERIALNO) {
			// TODO Auto-generated constructor stub
			this.SERIALNO = SERIALNO;
		}
		
		public JSONObject jsonRequest(){
			JSONObject obj = new JSONObject();
			try {
				obj.put("CODENO", CODENO);
				obj.put("serialNo", SERIALNO);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return obj;
		}
		
		public String getCODENO() {
			return CODENO;
		}
		public String getSERIALNO() {
			return SERIALNO;
		}
		public void setSERIALNO(String sERIALNO) {
			SERIALNO = sERIALNO;
		}
	}
	
	/**
	 * 贷款客户详情请求报文
	 * @author Administrator
	 *
	 */
	class CustomerDetailRequest{
		private String CODENO = "XD0005";
		private String CUSTOMERID;
		
		public CustomerDetailRequest() {
		}
		
		public CustomerDetailRequest(String CUSTOMERID) {
			this.CUSTOMERID = CUSTOMERID;
		}
		
		public JSONObject jsonRequest(){
			JSONObject obj = new JSONObject();
			try {
				obj.put("CODENO", CODENO);
				obj.put("customerID", CUSTOMERID);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return obj;
		}
		
		public String getCODENO() {
			return CODENO;
		}
		public String getCUSTOMERID() {
			return CUSTOMERID;
		}
		public void setCUSTOMERID(String cUSTOMERID) {
			CUSTOMERID = cUSTOMERID;
		}
	}
	
	/**
	 * 审批意见修改请求报文
	 * @author Administrator
	 *
	 */
	class LoanSignOptionRe{
		private String CODENO = "XD0006";
		private String OPINIONTYPE = null;
		private String SERIALNO = null;
		private String FLOWNO = null;
		private String PHASENO = null;
		private String OBJECTTYPE = null;
		private Number APPROVEBUSINESSSUM = null;
		private Number APPROVERATEFLOAT = null;
		private Number APPROVEBUSINESSRATE = null;
		private Number APPROVETERMMONTH = null;
		private String PHASECHOICE = null;
		private String PHASEOPINION = null;
		private String USERID = null;
		private String ORGID = null;
		
		/**
		 * 交易结果 提供给返回报文使用
		 */
		private String RETURNCODE = null;
		
		public LoanSignOptionRe() {
		}
		
		public JSONObject jsonRequest(){
			JSONObject obj = new JSONObject();
			try {
				obj.put("CODENO", CODENO);
				obj.put("opinionTYPE", OPINIONTYPE);
				obj.put("serialNO", SERIALNO);
				obj.put("flowNO", FLOWNO);
				obj.put("phaseNO", PHASENO);
				obj.put("objectTYPE", OBJECTTYPE);
				obj.put("approveBusinessSum", APPROVEBUSINESSSUM);
				obj.put("approveRateFloat", APPROVERATEFLOAT);
				obj.put("approveBusinessRate", APPROVEBUSINESSRATE);
				obj.put("approveTermMonth", APPROVETERMMONTH);
				obj.put("phaseChoice", PHASECHOICE);
				obj.put("phaseOpinion", PHASEOPINION);
				obj.put("userid", USERID);
				obj.put("orgid", ORGID);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return obj;
		}
		
		public String getCODENO() {
			return CODENO;
		}

		public void setCODENO(String cODENO) {
			CODENO = cODENO;
		}

		public String getOPINIONTYPE() {
			return OPINIONTYPE;
		}

		public void setOPINIONTYPE(String oPINIONTYPE) {
			OPINIONTYPE = oPINIONTYPE;
		}

		public String getSERIALNO() {
			return SERIALNO;
		}

		public void setSERIALNO(String sERIALNO) {
			SERIALNO = sERIALNO;
		}

		public String getFLOWNO() {
			return FLOWNO;
		}

		public void setFLOWNO(String fLOWNO) {
			FLOWNO = fLOWNO;
		}

		public String getPHASENO() {
			return PHASENO;
		}

		public void setPHASENO(String pHASENO) {
			PHASENO = pHASENO;
		}

		public String getOBJECTTYPE() {
			return OBJECTTYPE;
		}

		public void setOBJECTTYPE(String oBJECTTYPE) {
			OBJECTTYPE = oBJECTTYPE;
		}

		public Number getAPPROVEBUSINESSSUM() {
			return APPROVEBUSINESSSUM;
		}

		public void setAPPROVEBUSINESSSUM(Number aPPROVEBUSINESSSUM) {
			APPROVEBUSINESSSUM = aPPROVEBUSINESSSUM;
		}

		public Number getAPPROVERATEFLOAT() {
			return APPROVERATEFLOAT;
		}

		public void setAPPROVERATEFLOAT(Number aPPROVERATEFLOAT) {
			APPROVERATEFLOAT = aPPROVERATEFLOAT;
		}

		public Number getAPPROVEBUSINESSRATE() {
			return APPROVEBUSINESSRATE;
		}

		public void setAPPROVEBUSINESSRATE(Number aPPROVEBUSINESSRATE) {
			APPROVEBUSINESSRATE = aPPROVEBUSINESSRATE;
		}

		public Number getAPPROVETERMMONTH() {
			return APPROVETERMMONTH;
		}

		public void setAPPROVETERMMONTH(Number aPPROVETERMMONTH) {
			APPROVETERMMONTH = aPPROVETERMMONTH;
		}

		public String getPHASECHOICE() {
			return PHASECHOICE;
		}

		public void setPHASECHOICE(String pHASECHOICE) {
			PHASECHOICE = pHASECHOICE;
		}

		public String getPHASEOPINION() {
			return PHASEOPINION;
		}

		public void setPHASEOPINION(String pHASEOPINION) {
			PHASEOPINION = pHASEOPINION;
		}

		public String getUSERID() {
			return USERID;
		}

		public void setUSERID(String uSERID) {
			USERID = uSERID;
		}

		public String getORGID() {
			return ORGID;
		}

		public void setORGID(String oRGID) {
			ORGID = oRGID;
		}

		public String getRETURNCODE() {
			return RETURNCODE;
		}

		public void setRETURNCODE(String rETURNCODE) {
			RETURNCODE = rETURNCODE;
		}
		
	}
	
	/**
	 * 下级审批人列表请求报文
	 * @author Administrator
	 *
	 */
	class NextSignPerListRe{
		private String CODENO = "XD0007";
		private String SERIALNO = null;
		private String FLOWNO = null;
		private String PHASENO = null;
		private String OBJECTTYPE = null;
		private String USERID = null;
		private String ORGID = null;
		
		public NextSignPerListRe() {
		}
		
		public JSONObject jsonRequest(){
			JSONObject obj = new JSONObject();
			try {
				obj.put("CODENO", CODENO);
				obj.put("serialNo", SERIALNO);
				obj.put("flowNo", FLOWNO);
				obj.put("phaseNo", PHASENO);
				obj.put("objectType", OBJECTTYPE);
				obj.put("userId", USERID);
				obj.put("orgId", ORGID);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return obj;
		}
		
		public String getCODENO() {
			return CODENO;
		}
		public String getSERIALNO() {
			return SERIALNO;
		}
		public void setSERIALNO(String sERIALNO) {
			SERIALNO = sERIALNO;
		}
		public String getFLOWNO() {
			return FLOWNO;
		}
		public void setFLOWNO(String fLOWNO) {
			FLOWNO = fLOWNO;
		}
		public String getPHASENO() {
			return PHASENO;
		}
		public void setPHASENO(String pHASENO) {
			PHASENO = pHASENO;
		}
		public String getOBJECTTYPE() {
			return OBJECTTYPE;
		}
		public void setOBJECTTYPE(String oBJECTTYPE) {
			OBJECTTYPE = oBJECTTYPE;
		}
		public String getUSERID() {
			return USERID;
		}
		public void setUSERID(String uSERID) {
			USERID = uSERID;
		}
		public String getORGID() {
			return ORGID;
		}
		public void setORGID(String oRGID) {
			ORGID = oRGID;
		}
	}
	
	/**
	 * 审批意见提交请求报文
	 * @author Administrator
	 *
	 */
	class LoanSubmitRe{
		private String CODENO = "XD0008";
		private String SERIALNO = null;
		private String FLOWNO = null;
		private String PHASENO = null;
		private String OBJECTTYPE = null;
		private String USERID = null;
		private String ORGID = null;
		
		/**
		 * 提交类型
		 * 010 批注
		 * 020 否决
		 * 030 退回客户经理补充资料
		 * 040提交下一审批人
		 */
		private String SUBMITTYPE = null;
		
		private String NEXTUSERID = null;
		private String NEXTORGID = null;
		
		public LoanSubmitRe() {
			// TODO Auto-generated constructor stub
		}
		
		public JSONObject jsonRequest(){
			JSONObject obj = new JSONObject();
			try {
				obj.put("CODENO", CODENO);
				obj.put("serialNo", SERIALNO);
				obj.put("flowNo", FLOWNO);
				obj.put("phaseNo", PHASENO);
				obj.put("objectType", OBJECTTYPE);
				obj.put("userId", USERID);
				obj.put("orgId", ORGID);
				obj.put("submitType", SUBMITTYPE);
				obj.put("nextUserId", NEXTUSERID);
				obj.put("nextOrgId", NEXTORGID);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return obj;
		}
		
		public String getCODENO() {
			return CODENO;
		}
		public String getSERIALNO() {
			return SERIALNO;
		}
		public void setSERIALNO(String sERIALNO) {
			SERIALNO = sERIALNO;
		}
		public String getFLOWNO() {
			return FLOWNO;
		}
		public void setFLOWNO(String fLOWNO) {
			FLOWNO = fLOWNO;
		}
		public String getPHASENO() {
			return PHASENO;
		}
		public void setPHASENO(String pHASENO) {
			PHASENO = pHASENO;
		}
		public String getOBJECTTYPE() {
			return OBJECTTYPE;
		}
		public void setOBJECTTYPE(String oBJECTTYPE) {
			OBJECTTYPE = oBJECTTYPE;
		}
		public String getUSERID() {
			return USERID;
		}
		public void setUSERID(String uSERID) {
			USERID = uSERID;
		}
		public String getORGID() {
			return ORGID;
		}
		public void setORGID(String oRGID) {
			ORGID = oRGID;
		}
		public String getSUBMITTYPE() {
			return SUBMITTYPE;
		}
		public void setSUBMITTYPE(String sUBMITTYPE) {
			SUBMITTYPE = sUBMITTYPE;
		}
		public String getNEXTUSERID() {
			return NEXTUSERID;
		}
		public void setNEXTUSERID(String nEXTUSERID) {
			NEXTUSERID = nEXTUSERID;
		}
		public String getNEXTORGID() {
			return NEXTORGID;
		}
		public void setNEXTORGID(String nEXTORGID) {
			NEXTORGID = nEXTORGID;
		}
	}
}

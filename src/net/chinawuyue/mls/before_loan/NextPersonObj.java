package net.chinawuyue.mls.before_loan;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 下级审批人数据结构
 * @author Administrator
 *
 */
public class NextPersonObj {

	private String SUBMITTYPE = null;
	private String NEXTUSERID = null;
	private String NEXTORGID = null;
	private String NEXTUSERNAME = null;
	
	public NextPersonObj() {
	}
	
	public NextPersonObj(String data) {
		jsonParseResponse(data);
	}
	
	public void jsonParseResponse(String data){
		try {
			JSONObject obj = new JSONObject(data);
			this.NEXTUSERID = obj.optString("NEXTUSERID", "");
			this.NEXTORGID = obj.optString("NEXTORGID", "");
			this.NEXTUSERNAME = obj.optString("NEXTUSERNAME", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
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

	public String getNEXTUSERNAME() {
		return NEXTUSERNAME;
	}

	public void setNEXTUSERNAME(String nEXTUSERNAME) {
		NEXTUSERNAME = nEXTUSERNAME;
	}

}

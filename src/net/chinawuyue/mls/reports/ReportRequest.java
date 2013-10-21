package net.chinawuyue.mls.reports;
/**
 * ÇëÇó±¨ÎÄ
 */
public class ReportRequest {
	private String CODENO;
	private String USERID;
	private String ORGID;
	private String REPORTNO;
	private String DATE;
	private String QUERYORGID;
	private String UNITNAME;
	public String getCODENO() {
		return CODENO;
	}
	public void setCODENO(String cODENO) {
		CODENO = cODENO;
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
	public String getREPORTNO() {
		return REPORTNO;
	}
	public void setREPORTNO(String rEPORTNO) {
		REPORTNO = rEPORTNO;
	}
	public String getDATE() {
		return DATE;
	}
	public void setDATE(String dATE) {
		DATE = dATE;
	}
	public String getQUERYORGID() {
		return QUERYORGID;
	}
	public void setQUERYORGID(String qUERYORGID) {
		QUERYORGID = qUERYORGID;
	}
	public String getUNITNAME() {
		return UNITNAME;
	}
	public void setUNITNAME(String uNITNAME) {
		UNITNAME = uNITNAME;
	}
	@Override
	public String toString(){
		return "CODENO:"+CODENO+" USERID:"+USERID+" ORGID:"+ORGID+
				" REPORTNO:"+REPORTNO+" DATE:"+DATE+" QUERYORGID:"+QUERYORGID+
				" UNITNAME:"+UNITNAME;
	}
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof ReportRequest)){
			return false;
		}
		return this.toString().equals(((ReportRequest)o).toString());
	}
}

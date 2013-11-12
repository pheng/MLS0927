
package net.chinawuyue.mls.before_loan;


import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import net.chinawuyue.mls.Constant;
import net.chinawuyue.mls.R;
import net.chinawuyue.mls.login.LoginInfo;
import net.chinawuyue.mls.util.DoFetchThread;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 贷款详情
 * @author Administrator
 *
 */
public class LoanDetailInfoActivity extends SherlockActivity{
	
	private static final String TAG = "LoanDetailInfoActivity";

	private TextView text_serialno_info = null;
	private TextView text_customerid_info = null;
	private TextView text_customername_info = null;
	private TextView text_businesstype_info = null;
	private TextView text_direction_info = null;
	private TextView text_occurtype_info = null;
	private TextView text_businessum_info = null;
	private TextView text_purpose_info = null;
	private TextView text_iseferfarming_info = null;
	private TextView text_termmonth_info = null;
	private TextView text_ratetype_info = null;
	private TextView text_ratefloat_info = null;
	private TextView text_businessrate_info = null;
	private TextView text_vouchtype_info = null;
	private TextView text_ictype_info = null;
	private TextView text_paysource_info = null;
	private TextView text_inputorgname_info = null;
	private TextView text_inputusername_info = null;
	private TextView text_inputdate_info = null;
	
	private String info;
	private String OBJECTTYPE = null;
	private String FLOWNO = null;
	private String PHASENO = null;
	private LoanDetailInfoObject obj;
	private int kind = -1;
	
	//向服务器请求
	private BeforeLoanRequest xRequest;
	private BeforeLoanRequest.CustomerDetailRequest xCusDetReq;
	private BeforeLoanRequest.LoanSignOptionRe xSignOptReq;
	private ProgressDialog progressDialog;
	
	private static final int ITEM1 = 1;
	private static final int ITEM2 = 2;
	private static final int ITEM3 = 3;
	
	private LoginInfo loginInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loan_listview_more_info);
		Intent intent = getIntent();
		kind = intent.getIntExtra("kind", -1);
		info = intent.getStringExtra("info");
		loginInfo = (LoginInfo) intent.getSerializableExtra("loginInfo");
		OBJECTTYPE = intent.getStringExtra("OBJECTTYPE");
		PHASENO = intent.getStringExtra("PHASENO");
		FLOWNO = intent.getStringExtra("FLOWNO");
		
		xRequest = new BeforeLoanRequest();
		
		//模拟数据
//		info = testData();
		
		inintView();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		SubMenu mSubMenu = menu.addSubMenu("");
		mSubMenu.add(0, ITEM1, 0, "客户详情");
		
		//已审批没有查看、签署意见
		if(kind != Constant.BeforeLoanConstan.KIND_FINISH){
			mSubMenu.add(0, ITEM2, 0, "查看、签署意见");
		}
		mSubMenu.add(0, ITEM3, 0, "返回");
		MenuItem item = mSubMenu.getItem();
		item.setIcon(R.drawable.ic_menu_view);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == Constant.BeforeLoanConstan.REQUEST_CODE 
				&& resultCode == Constant.BeforeLoanConstan.RESULT_CODE_TRUE){
			setResult(resultCode);
		    finish();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		Log.d("nOptions", "onOptionsItemSelected:" + item.getItemId());
		switch (item.getItemId()) {
		case ITEM1:
			//客户详情
			moreCustomInfo();
			break;
		case ITEM2:
			//签署意见
			signIdea();
			break;
		case ITEM3:
			LoanDetailInfoActivity.this.finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void inintView() {
		text_serialno_info = (TextView)this.findViewById(R.id.text_serialno_info);
		text_customerid_info = (TextView)this.findViewById(R.id.text_customerid_info);
		text_customername_info = (TextView)this.findViewById(R.id.text_customername_info);
		text_businesstype_info = (TextView)this.findViewById(R.id.text_businesstype_info);
		text_direction_info = (TextView)this.findViewById(R.id.text_direction_info);
		text_occurtype_info = (TextView)this.findViewById(R.id.text_occurtype_info);
		text_businessum_info = (TextView)this.findViewById(R.id.text_businessum_info);
		text_purpose_info = (TextView)this.findViewById(R.id.text_purpose_info);
		text_iseferfarming_info = (TextView)this.findViewById(R.id.text_iseferfarming_info);
		text_termmonth_info = (TextView)this.findViewById(R.id.text_termmonth_info);
		text_ratetype_info = (TextView)this.findViewById(R.id.text_ratetype_info);
		text_ratefloat_info = (TextView)this.findViewById(R.id.text_ratefloat_info);
		text_businessrate_info = (TextView)this.findViewById(R.id.text_businessrate_info);
		text_vouchtype_info = (TextView)this.findViewById(R.id.text_vouchtype_info);
		text_ictype_info = (TextView)this.findViewById(R.id.text_ictype_info);
		text_paysource_info = (TextView)this.findViewById(R.id.text_paysource_info);
		text_inputorgname_info = (TextView)this.findViewById(R.id.text_inputorgname_info);
		text_inputusername_info = (TextView)this.findViewById(R.id.text_inputusername_info);
		text_inputdate_info = (TextView)this.findViewById(R.id.text_inputdate_info);
		
		if(info != null){
//			Log.d(TAG, "detail info: " + info);
			obj = new LoanDetailInfoObject(info);
			text_serialno_info.setText(obj.getSerialNO());
			text_serialno_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_customerid_info.setText(obj.getCustomerID());
			text_customerid_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_customername_info.setText(obj.getCustomerName());
			text_customername_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_businesstype_info.setText(obj.getBusinessType());
			text_businesstype_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_direction_info.setText(obj.getDirection());
			text_direction_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_occurtype_info.setText(obj.getOccurType());
			text_occurtype_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_businessum_info.setText(obj.getBusinessSum() + "");
			text_businessum_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_purpose_info.setText(obj.getPurpose());
			text_purpose_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_iseferfarming_info.setText(obj.getIsReferFarming());
			text_iseferfarming_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_termmonth_info.setText(obj.getTermMonth() + "");
			text_termmonth_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_ratetype_info.setText(obj.getRateType());
			text_ratetype_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_ratefloat_info.setText(obj.getRateFloat() + "");
			text_ratefloat_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_businessrate_info.setText(obj.getBusinessRate() + "");
			text_businessrate_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_vouchtype_info.setText(obj.getVouchType());
			text_vouchtype_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_ictype_info.setText(obj.getIcType());
			text_ictype_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_paysource_info.setText(obj.getPaySource());
			text_paysource_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_inputorgname_info.setText(obj.getInputOrgName());
			text_inputorgname_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_inputusername_info.setText(obj.getInputUserName());
			text_inputusername_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			text_inputdate_info.setText(obj.getInputDate());
			text_inputdate_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		}else{
			Toast.makeText(getApplicationContext(), "获取详情失败，请稍后再查...", Toast.LENGTH_SHORT).show();
			LoanDetailInfoActivity.this.finish();
		}
	}
	
	private void signIdea(){
//		Log.d(TAG, "signIdea");
		//网络请求数据
		xSignOptReq = xRequest.new LoanSignOptionRe();
		//添加请求参数
		xSignOptReq.setOPINIONTYPE("010");
		xSignOptReq.setSERIALNO(obj.getSerialNO());
		xSignOptReq.setFLOWNO(FLOWNO);
		xSignOptReq.setPHASENO(PHASENO);
		xSignOptReq.setOBJECTTYPE(OBJECTTYPE);
		
		progressDialog = ProgressDialog.show(LoanDetailInfoActivity.this, "", LoanDetailInfoActivity.this.getString(R.string.wait), true, true);
		final DoFetchThread doFetch = new DoFetchThread(xSignOptReq.getCODENO(), signOptHandler, xSignOptReq.jsonRequest());
		Thread thread = new Thread(doFetch);
		thread.start();
		progressDialog.setOnCancelListener(new OnCancelListener(){
			@Override
			public void onCancel(DialogInterface dialog) {
				doFetch.stop();
			}
		});
	}
	
	@SuppressLint("HandlerLeak")
	Handler signOptHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(progressDialog != null){
				progressDialog.dismiss();
			}
			if (msg.what == -1) {
				Toast.makeText(getApplicationContext(), R.string.net_error, Toast.LENGTH_SHORT).show();
				return;
			}
			String json = msg.obj.toString();
			String RETURNCODE = "";
			try {
				JSONObject obj = new JSONObject(json);
				RETURNCODE = obj.optString("RETURNCODE");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent intent = new Intent();
			intent.putExtra("loginInfo", (Serializable)loginInfo);
			if(RETURNCODE != null && RETURNCODE.equalsIgnoreCase("N")){
//				Log.d(TAG, "start cha loginInfo:" + loginInfo.toString());
				intent.putExtra("signIdea", json);
				intent.putExtra("BUSINESSSUM", obj.getBusinessSum().toString());
			}else{
				//显示默认值
//				Log.d(TAG, "start cha loginInfo:" + loginInfo.toString());
				intent.putExtra("OBJECTTYPE", OBJECTTYPE);
				intent.putExtra("PHASENO", PHASENO);
				intent.putExtra("FLOWNO", FLOWNO);
				intent.putExtra("BUSINESSSUM", obj.getBusinessSum());
				intent.putExtra("RATEFLOAT", obj.getRateFloat());
				intent.putExtra("BUSINESSRATE", obj.getBusinessRate());
				intent.putExtra("TERMMONTH", obj.getTermMonth());
				intent.putExtra("SERIALNO", obj.getSerialNO());
			}
			intent.setClass(getApplicationContext(), ChangeIdeaActivity.class);
			startActivityForResult(intent, Constant.BeforeLoanConstan.REQUEST_CODE);
		};
	};
	
	private void moreCustomInfo(){
//		Log.d(TAG, "moreCustomInfo");
		//fetch user info from service 
		xCusDetReq = xRequest.new CustomerDetailRequest(obj.getCustomerID());
		
		progressDialog = ProgressDialog.show(LoanDetailInfoActivity.this, "", LoanDetailInfoActivity.this.getString(R.string.wait), true, true);
		final DoFetchThread doFetch = new DoFetchThread(xCusDetReq.getCODENO(), customHandler, xCusDetReq.jsonRequest());
		Thread thread = new Thread(doFetch);
		thread.start();
		progressDialog.setOnCancelListener(new OnCancelListener(){
			@Override
			public void onCancel(DialogInterface dialog) {
				doFetch.stop();
			}
		});
	}
	
	@SuppressLint("HandlerLeak")
	Handler customHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(progressDialog != null){
				progressDialog.dismiss();
			}
			if (msg.what == -1) {
				Toast.makeText(getApplicationContext(), R.string.net_error, Toast.LENGTH_SHORT).show();
				return;
			}
			String json = msg.obj.toString();
			String RETURNCODE = "";
			try {
				JSONObject obj = new JSONObject(json);
				RETURNCODE = obj.optString("RETURNCODE");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(RETURNCODE != null && RETURNCODE.equalsIgnoreCase("N")){
				Intent intent = new Intent();
				intent.putExtra("userInfo", json);
				intent.setClass(LoanDetailInfoActivity.this, CustomInfoActivity.class);
				startActivity(intent);
			}else{
				//查询报告失败，提示用户查询失败
				Toast.makeText(getApplicationContext(), "查询客户详情失败！", Toast.LENGTH_SHORT).show();
			}
		};
	};
}


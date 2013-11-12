
package net.chinawuyue.mls.before_loan;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.chinawuyue.mls.Constant;
import net.chinawuyue.mls.R;
import net.chinawuyue.mls.login.LoginInfo;
import net.chinawuyue.mls.util.DoFetchThread;
import android.annotation.SuppressLint;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;
/**
 * 签署意见
 * @author Administrator
 *
 */
public class ChangeIdeaActivity extends SherlockActivity{
	
//	private static final String TAG = "ChangeIdeaActivity";
	
	private TextView text_serialno_info = null;
	private TextView text_businesssum_info = null;
	
	private TextView edit_approvebusinesssum_info = null;
	private TextView edit_approveratefloat_info=null;
	private TextView edit_approvebusinessrate_info = null;
	private TextView edit_approvetermmonth_info = null;
	private TextView edit_phaseopinion_info = null;
	
	private RadioButton radio1 = null;
	private RadioButton radio2 = null;
	
	//请求报文是否一定需要这些参数
	private String flowNO;//流程类型
	private String phaseNO;//阶段编号
	private String objectType;//对象类型
	
	private String businessSum;//申请金额
	private String approveSum;//批准金额
	private String rateFloat;//利率增量
	private String businessRate;//执行利率
	private String termMonth;//期限(月)
	private String serialNO;//申请流水号
	private String idea = null;//意见
	private String ideadetail = null;//意见详情
	
	private List<NextPersonObj> xItems = null;
	
	/**
	 * 查看修改/签署 意见
	 * true 已经签署过意见，现在是查看修改意见或者是准备提交了
	 * false 意见未签署，显示默认值，用户需要签署意见，才能提交
	 */
	private boolean modifyFlag;
	
	private BeforeLoanRequest xRequest;
	private BeforeLoanRequest.LoanSignOptionRe xSignOptRe;
	private BeforeLoanRequest.NextSignPerListRe xSignPerListRe;
	private BeforeLoanRequest.LoanSubmitRe xSubmitRe;
	
	private ProgressDialog progressDialog;
	
	private static final int ITEM1 = 1;
	private static final int ITEM2 = 2;
	private static final int ITEM3 = 3;
	
	private ArrayList<String> submitType;
	//dialog控件
	private Button diaOk;
	private Button diaCancle;
	private RadioGroup radioGrp;
	private Dialog dia;
	private Spinner spinner;
	private ArrayList<String> personName;
	private ArrayAdapter<String> spiAdapter;
	
	private LoginInfo loginInfo;
	
	private Button subBtn;
	private Button sigBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subscribe_idea_info);
		
		xRequest = new BeforeLoanRequest();
		
		Intent intent = getIntent();
		loginInfo = (LoginInfo)intent.getSerializableExtra("loginInfo");
//		Log.d(TAG, "loginInfo: " + loginInfo.toString());
		
		businessSum = intent.getStringExtra("BUSINESSSUM");
		String signIdea = intent.getStringExtra("signIdea");
		if(signIdea != null && signIdea.length() > 0){
			modifyFlag = true;
			parseJson(signIdea);
		}else{
			modifyFlag = false;
			//从审批详细中取默认值
			flowNO = intent.getStringExtra("FLOWNO");
			phaseNO = intent.getStringExtra("PHASENO");
			objectType = intent.getStringExtra("OBJECTTYPE");
			serialNO = intent.getStringExtra("SERIALNO");
			approveSum = businessSum;
			rateFloat = intent.getStringExtra("RATEFLOAT");
			businessRate = intent.getStringExtra("BUSINESSRATE");
			termMonth = intent.getStringExtra("TERMMONTH");
		}
				
		inintView();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		SubMenu mSubMenu = menu.addSubMenu("");
		mSubMenu.add(0, ITEM1, 0, "保存意见");
		mSubMenu.add(0, ITEM2, 0, "提交");
		mSubMenu.add(0, ITEM3, 0, "返回");
		MenuItem item = mSubMenu.getItem();
		item.setIcon(R.drawable.ic_menu_view);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM1:
			//保存意见
			signIdea();
			break;
		case ITEM2:
			//提交
			//请求：获得下级审批人员列表
			getSignPersonList();
			break;
		case ITEM3:
			//返回
			ChangeIdeaActivity.this.finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void parseJson(String signIdea){
		try {
			JSONObject obj = new JSONObject(signIdea);
			this.flowNO = obj.optString("FLOWNO", "");
			this.phaseNO = obj.optString("PHASENO", "");
			this.objectType = obj.optString("OBJECTTYPE", "");
			this.serialNO = obj.optString("SERIALNO", "");
			this.approveSum = obj.optString("APPROVEBUSINESSSUM", "");
			this.rateFloat = obj.optString("APPROVERATEFLOAT", "");
			this.businessRate = obj.optString("APPROVEBUSINESSRATE", "");
			this.termMonth = obj.optString("APPROVETERMMONTH", "");
			this.idea = obj.optString("PHASECHOICE", "");
			this.ideadetail = obj.optString("PHASEOPINION", "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void inintView() {
		text_serialno_info = (TextView)this.findViewById(R.id.text_serialno_info);
		text_serialno_info.setText(serialNO);
		
		text_businesssum_info =(TextView)this.findViewById(R.id.text_businesssum_info);
		text_businesssum_info.setText(businessSum + "");
		
		edit_approvebusinesssum_info= (TextView)this.findViewById(R.id.edit_approvebusinesssum_info);
		edit_approvebusinesssum_info.setText(approveSum + "");
		edit_approveratefloat_info = (TextView)this.findViewById(R.id.edit_approveratefloat_info);
		edit_approveratefloat_info.setText(rateFloat + "");
		edit_approvebusinessrate_info = (TextView)this.findViewById(R.id.edit_approvebusinessrate_info);
		edit_approvebusinessrate_info.setText(businessRate + "");
		edit_approvetermmonth_info = (TextView)this.findViewById(R.id.edit_approvetermmonth_info);
		edit_approvetermmonth_info.setText(termMonth + "");
		
		edit_phaseopinion_info = (EditText)this.findViewById(R.id.edit_phaseopinion_info);

		radio1 = (RadioButton)this.findViewById(R.id.radio1);
		radio2 = (RadioButton)this.findViewById(R.id.radio2);
		
		if(modifyFlag){
			edit_phaseopinion_info.setText(ideadetail);
			if((idea != null) && (idea.equalsIgnoreCase("同意"))){
				radio1.setChecked(true);
				radio2.setChecked(false);
			}else if((idea != null) && (idea.equalsIgnoreCase("不同意"))){
				radio1.setChecked(false);
				radio2.setChecked(true);
			}
		}
		
		sigBtn = (Button) findViewById(R.id.sigBtn);
		sigBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				signIdea();
			}
		});
		
		subBtn = (Button) findViewById(R.id.subBtn);
		subBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getSignPersonList();
			}
		});
	}
	
	//签署意见
	private void signIdea(){
//		Log.d(TAG, "signIdea");
		ideadetail = edit_phaseopinion_info.getText().toString();
		businessSum = edit_approvebusinesssum_info.getText().toString();
		rateFloat = edit_approveratefloat_info.getText().toString();
		businessRate = edit_approvebusinessrate_info.getText().toString();
		termMonth = edit_approvetermmonth_info.getText().toString();
		
		if(radio1.isChecked()){
			idea = "同意";
		}else{
			idea = "不同意";
		}
		
		//网络请求签署意见
		xSignOptRe = xRequest.new LoanSignOptionRe();
		xSignOptRe.setOPINIONTYPE("020");
		xSignOptRe.setSERIALNO(serialNO);
		xSignOptRe.setUSERID(loginInfo.userCode);
		xSignOptRe.setORGID(loginInfo.orgId);
		xSignOptRe.setFLOWNO(flowNO);
		xSignOptRe.setPHASENO(phaseNO);
		xSignOptRe.setOBJECTTYPE(objectType);
		
		try {
			xSignOptRe.setAPPROVEBUSINESSRATE(NumberFormat.getInstance().parse(businessRate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			xSignOptRe.setAPPROVEBUSINESSSUM(NumberFormat.getInstance().parse(approveSum));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			xSignOptRe.setAPPROVERATEFLOAT(NumberFormat.getInstance().parse(rateFloat));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			xSignOptRe.setAPPROVETERMMONTH(NumberFormat.getInstance().parse(termMonth));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xSignOptRe.setPHASECHOICE(idea);
		xSignOptRe.setPHASEOPINION(ideadetail);
		
		progressDialog = ProgressDialog.show(ChangeIdeaActivity.this, "", ChangeIdeaActivity.this.getString(R.string.wait), true, true);
		final DoFetchThread doFetch = new DoFetchThread(xSignOptRe.getCODENO(), signOptHandler, xSignOptRe.jsonRequest());
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
				e.printStackTrace();
			}
			if(RETURNCODE != null && RETURNCODE.equalsIgnoreCase("N")){
				//意见签署成功
				Toast.makeText(getApplicationContext(), "意见保存成功，可以进行提交！", Toast.LENGTH_SHORT).show();
			}else{
				//请求签署意见失败,提示用户错误
				Toast.makeText(getApplicationContext(), "意见修改,请稍后重试！", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	protected void getSignPersonList() {
		//网络请求
		xSignPerListRe = xRequest.new NextSignPerListRe();
		xSignPerListRe.setFLOWNO(flowNO);
		xSignPerListRe.setOBJECTTYPE(objectType);
		xSignPerListRe.setORGID(loginInfo.orgId);
		xSignPerListRe.setPHASENO(phaseNO);
		xSignPerListRe.setSERIALNO(serialNO);
		xSignPerListRe.setUSERID(loginInfo.userCode);
		
		progressDialog = ProgressDialog.show(ChangeIdeaActivity.this, "", ChangeIdeaActivity.this.getString(R.string.wait), true, true);
		final DoFetchThread doFetch = new DoFetchThread(xSignPerListRe.getCODENO(), getSignPersonListHandler, xSignPerListRe.jsonRequest());
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
	Handler getSignPersonListHandler = new Handler() {
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
			String ERRORCODE = "";
			try {
				JSONObject obj = new JSONObject(json);
				RETURNCODE = obj.optString("RETURNCODE");
				ERRORCODE = obj.optString("ERRORCODE");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(RETURNCODE != null && RETURNCODE.equalsIgnoreCase("N")){
				//请求下一级审批人列表成功--根据不同提交类型选择不同处理？
				parsePerList(json);
				showSubmitDialog();
			}else if(ERRORCODE != null && ERRORCODE.equalsIgnoreCase("SH0003")){
				Toast.makeText(getApplicationContext(), "请先保存意见后在提交！", Toast.LENGTH_LONG).show();
			}else{
				//请求下一级审批人列表失败
				Toast.makeText(getApplicationContext(), "请求提交异常,请稍后重试！", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	private void parsePerList(String json){
		this.personName = new ArrayList<String>();
		this.xItems = new ArrayList<NextPersonObj>();
		this.submitType = new ArrayList<String>();
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray array = obj.optJSONArray("ARRAY1");
			if(array == null || array.length() < 1){
				return;
			}
			for(int i = 0; i < array.length(); i++){
				
				//获取提交类型
				String type = array.getJSONObject(i).optString("SUBMITTYPE");
				
				if(type.equalsIgnoreCase("040")){
					//提交下一审批人040
					NextPersonObj dataObj = new NextPersonObj(array.getJSONObject(i).toString());
					personName.add(dataObj.getNEXTUSERNAME());
					xItems.add(dataObj);
				}else{
					//批准010、否决020、退回030
					submitType.add(type);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(xItems.size() > 0){
			submitType.add("040");
		}
	}

	protected void showSubmitDialog() {
		dia = new Dialog(this);
		dia.setContentView(R.layout.dialog_layout);
		dia.setTitle("审批提交");
		
		diaCancle = (Button) dia.findViewById(R.id.diaBtnCancle);
		diaCancle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dia.dismiss();
			}
		});
		
		diaOk = (Button) dia.findViewById(R.id.diaBtnOk);
		diaOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(radioGrp.getCheckedRadioButtonId() < 0){
					Toast.makeText(getApplicationContext(), "请选择提交方式后再确认！", Toast.LENGTH_SHORT).show();
					return;
				}
				String subType = (String) radioGrp.getChildAt(radioGrp.getCheckedRadioButtonId()).getTag();
				xSubmitRe = xRequest.new LoanSubmitRe();
				xSubmitRe.setSERIALNO(serialNO);
				xSubmitRe.setFLOWNO(flowNO);
				xSubmitRe.setPHASENO(phaseNO);
				xSubmitRe.setOBJECTTYPE(objectType);
				xSubmitRe.setUSERID(loginInfo.userCode);
				xSubmitRe.setORGID(loginInfo.orgId);
				xSubmitRe.setSUBMITTYPE(subType);
				if(subType.equalsIgnoreCase("040")){
					NextPersonObj obj = xItems.get(spinner.getSelectedItemPosition());
					xSubmitRe.setNEXTORGID(obj.getNEXTORGID());
					xSubmitRe.setNEXTUSERID(obj.getNEXTUSERID());
				}
				
				progressDialog = ProgressDialog.show(ChangeIdeaActivity.this, "", ChangeIdeaActivity.this.getString(R.string.wait), true, true);
				final DoFetchThread doFetch = new DoFetchThread(xSubmitRe.getCODENO(), submitHandler, xSubmitRe.jsonRequest());
				Thread thread = new Thread(doFetch);
				thread.start();
				progressDialog.setOnCancelListener(new OnCancelListener(){
					@Override
					public void onCancel(DialogInterface dialog) {
						doFetch.stop();
					}
				});
				dia.dismiss();
			}
		});
		radioGrp = (RadioGroup) dia.findViewById(R.id.diaRadGrp);
		
		spiAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, personName); 
		for(int i = 0; i < submitType.size(); i++){
			RadioButton radBtn = new RadioButton(this);
			radBtn.setId(i);
			if(submitType.get(i).equalsIgnoreCase("010")){
				radBtn.setTag("010");
				radBtn.setText("批准");
				radioGrp.addView(radBtn);
			}else if(submitType.get(i).equalsIgnoreCase("020")){
				radBtn.setTag("020");
				radBtn.setText("否决");
				radioGrp.addView(radBtn);
			}else if(submitType.get(i).equalsIgnoreCase("030")){
				radBtn.setTag("030");
				radBtn.setText("退回客户经理补充资料");
				radioGrp.addView(radBtn);
			}else if(submitType.get(i).equalsIgnoreCase("040")){
				radBtn.setTag("040");
				radBtn.setText("提交下一审批人");
				radBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked){
							spinner.setVisibility(View.VISIBLE);
						}else{
							spinner.setVisibility(View.GONE);
						}
					}
				});
				radioGrp.addView(radBtn);
				spinner = (Spinner) dia.findViewById(R.id.diaSpi);
				spinner.setAdapter(spiAdapter);
				spinner.setVisibility(View.GONE);
			}
		}
		
		dia.show();
	}
	
	@SuppressLint("HandlerLeak")
	Handler submitHandler = new Handler() {
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
			String ERRORCODE = "";
			try {
				JSONObject obj = new JSONObject(json);
				RETURNCODE = obj.optString("RETURNCODE");
				ERRORCODE = obj.optString("ERRORCODE");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(RETURNCODE != null && RETURNCODE.equalsIgnoreCase("N")){
				//提交成功
				Toast.makeText(getApplicationContext(), "提交成功！", Toast.LENGTH_SHORT).show();
				setResult(Constant.BeforeLoanConstan.RESULT_CODE_TRUE);
				finish();
			}else if(ERRORCODE != null && ERRORCODE.equalsIgnoreCase("SH0001")){
				//提交失败,提示用户错误
				Toast.makeText(getApplicationContext(), "提交失败，查询用户不存在！", Toast.LENGTH_SHORT).show();
			}else if(ERRORCODE != null && ERRORCODE.equalsIgnoreCase("SH0002")){
				//提交失败,提示用户错误
				Toast.makeText(getApplicationContext(), "提交失败，交易码不存在！", Toast.LENGTH_SHORT).show();
			}else if(ERRORCODE != null && ERRORCODE.equalsIgnoreCase("SH0003")){
				//提交失败,提示用户错误
				Toast.makeText(getApplicationContext(), "提交失败，意见未保存！", Toast.LENGTH_SHORT).show();
			}else if(ERRORCODE != null && ERRORCODE.equalsIgnoreCase("SH0004")){
				//提交失败,提示用户错误
				Toast.makeText(getApplicationContext(), "提交失败，无审批权限！", Toast.LENGTH_SHORT).show();
			}else if(ERRORCODE != null && ERRORCODE.equalsIgnoreCase("SH0005")){
				//提交失败,提示用户错误
				Toast.makeText(getApplicationContext(), "提交失败，审批流程异常！", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), "提交异常！", Toast.LENGTH_SHORT).show();
			}
		};
	};
}


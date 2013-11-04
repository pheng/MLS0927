package net.chinawuyue.mls.sys;

import net.chinawuyue.mls.R;
import net.chinawuyue.mls.login.LoginInfo;
import net.chinawuyue.mls.util.DoFetchThread;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 修改密码界面
 *
 */
public class ChangePwd implements OnFocusChangeListener,OnClickListener{

	private Context context = null;
	private View layout = null;
	private EditText etOrginalPwd;
	private EditText etNewPwd1;
	private EditText etNewPwd2;
	private Button btnCancel;
	private Button btnSubmit;
	private int color;
	private LoginInfo loginInfo;
	
	public ChangePwd(Context context,LoginInfo loginInfo) {
		this.context = context;
		this.loginInfo = loginInfo;
	}

	public View getChangePwdView(){
		return layout;
	}
	
	public void setChangePwdView() {
		layout = LayoutInflater.from(context).inflate(R.layout.activity_changepwd, null);
		etOrginalPwd = (EditText)layout.findViewById(R.id.etOrginalPwd);
		etNewPwd1 = (EditText)layout.findViewById(R.id.etNewPwd1);
		etNewPwd2 = (EditText)layout.findViewById(R.id.etNewPwd2);
		btnCancel = (Button)layout.findViewById(R.id.btnCancel);
		btnSubmit = (Button)layout.findViewById(R.id.btnSubmit);
		color = etOrginalPwd.getHintTextColors().getDefaultColor();
		etOrginalPwd.setOnFocusChangeListener(this);
		etNewPwd1.setOnFocusChangeListener(this);
		etNewPwd2.setOnFocusChangeListener(this);
		btnCancel.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			((EditText)v).setHintTextColor(Color.WHITE);
		}else{
			((EditText)v).setHintTextColor(color);
		}
	}
	
	private boolean validateInput(){
		if(etOrginalPwd.getText().length()==0){
			Toast.makeText(context, "请输入原始密码", 0).show();
			return false;
		}
		if(etNewPwd1.getText().length()==0){
			Toast.makeText(context, "请输入新密码", 0).show();
			return false;
		}
		if(etNewPwd2.getText().length()==0){
			Toast.makeText(context, "请重复输入新密码", 0).show();
			return false;
		}
		if(!etNewPwd2.getText().toString().equals(etNewPwd1.getText().toString())){
			Toast.makeText(context, "两次输入密码不一致", 0).show();
			return false;
		}
		return true;
	}

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==-1||msg.obj==null){
				Toast.makeText(context, R.string.net_error, 1).show();
			}else{
				switch(Integer.parseInt(msg.obj.toString())){
				case 0:
					Toast.makeText(context, "更新失败", 1).show();
					break;
				case 1:
					Toast.makeText(context, "更新成功", 1).show();
					break;
				case -1:
					Toast.makeText(context, "用户名不存在或密码错误", 1).show();
					break;
				}
			}
		};
	};
	
	/**连接服务器进行修改*/
	private void changePwd(){
		JSONObject request = new JSONObject();
		try {
			request.put("usercode", loginInfo.userCode);
			request.put("oldpwd", etOrginalPwd.getText().toString());
			request.put("newpwd", etNewPwd1.getText().toString());
			Thread thread = new Thread(new DoFetchThread("ChangePWD",handler,request));
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void clearPwd(){
		etOrginalPwd.setText("");
		etNewPwd1.setText("");
		etNewPwd2.setText("");
	}
	
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.btnCancel:
			clearPwd();
			break;
		case R.id.btnSubmit:
			if(validateInput()){
				changePwd();
			}
			break;
		}
	}
	
	
}

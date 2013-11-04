package net.chinawuyue.mls.login;

import java.io.File;
import java.io.Serializable;

import net.chinawuyue.mls.MainActivity;
import net.chinawuyue.mls.R;
import net.chinawuyue.mls.undotask.UndoTaskService;
import net.chinawuyue.mls.util.ActivityUtil;
import net.chinawuyue.mls.util.HttpUtil;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
/**
 * 登录界面
 * */
public class LoginActivity extends SherlockActivity {

	/** 登录信息 */
	private LoginInfo loginInfo = new LoginInfo();
	/** 以下是UI */
	private EditText view_userName;
	private EditText view_password;
	private CheckBox view_rememberMe;
	private Button view_loginSubmit;

	/** 用来操作SharePreferences的标识 */
	private final String SHARE_LOGIN_TAG = "SHARE_LOGIN_TAG";

	/** 如果登录成功后,用于保存用户名到SharedPreferences,以便下次不再输入 */
	private String SHARE_LOGIN_USERNAME = "LOGIN_USERNAME";

	/** 如果登录成功后,用于保存PASSWORD到SharedPreferences,以便下次不再输入 */
	private String SHARE_LOGIN_PASSWORD = "LOGIN_PASSWORD";

	/** 如果登陆失败,这个可以给用户确切的消息显示,true是网络连接失败,false是用户名和密码错误 */
	private boolean isNetError = false, isUpdate = false;

	/** 登录loading提示框 */
	private ProgressDialog proDialog;

	/** 是否非绑定用户登录*/
	private boolean isNoBindingUserLogin;
	
	/** 是否不同用户登录*/
	public static boolean isNewUserLogin;
	private String userName;
	/** 设备号*/
	private String deviceId;
	private HttpUtil httpUtil = null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//获得设备号
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); 
		deviceId = tm.getDeviceId();
		System.out.println(deviceId);
		
		view_userName = (EditText) findViewById(R.id.login_edit_account);
		view_password = (EditText) findViewById(R.id.login_edit_pwd);
		view_rememberMe = (CheckBox) findViewById(R.id.login_cb_savepwd);
		view_loginSubmit = (Button) findViewById(R.id.login_btn_login);

		initView(false);
		// 需要去submitListener里面设置URL
		view_loginSubmit.setOnClickListener(submitListener);
		// 添加本窗体到集合中，方便一起关闭
		ActivityUtil.activityList.add(this);
		httpUtil = new HttpUtil();
	}

	/** 如果上次做了保存，则读取赋值 */
	private void initView(boolean isRememberMe) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		userName = share.getString(SHARE_LOGIN_USERNAME, "");
		if (!"".equals(userName)) {
			view_userName.setText(userName);
			view_rememberMe.setChecked(true);
		}
		share = null;
	}

	/** 登录Button Listener */
	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (checkInfo()) {
				proDialog = ProgressDialog.show(LoginActivity.this, "用户登录",
						"正在连接服务器，请稍候....", true, true);
				// 开一个线程进行登录验证,主要是用于失败,成功可以直接通过startAcitivity(Intent)转向
				Thread loginThread = new Thread(new DoLogin());
				loginThread.start();
				proDialog.setOnCancelListener(new OnCancelListener(){
					@Override
					public void onCancel(DialogInterface arg0) {
						httpUtil.abort();
					}
				});
			}
		}
	};

	/** 保存账户、密码 */
	private void saveSharePreferences() {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		share.edit()
				.putString(SHARE_LOGIN_USERNAME,
						view_userName.getText().toString()).commit();
		share = null;
	}

	/** 清除密码 */
	private void clearSharePreference() {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		share.edit().putString(SHARE_LOGIN_USERNAME, "").commit();
		share = null;
	}

	private boolean checkInfo() {
		if (view_userName.getText().toString().length() == 0) {
			view_userName.setError("账号不能为空");
			return false;
		}
		if (view_password.getText().toString().length() == 0) {
			view_password.setError("密码不能为空");
			return false;
		}
		return true;
	}


	
	private boolean validateLocalLogin(String usercode, String password) {
		// 用于标记登陆状态
		boolean loginState = false;	
		String isLocalUser = null;
		// 连接webservice
//		JSONObject jsonRes = HttpUtil.doLoginWithDeviceId(usercode, password,deviceId);
		JSONObject jsonRes = httpUtil.doLogin(usercode, password);
		if (jsonRes != null) {
			try {
				if(jsonRes.has("logininfo")){
					loginInfo.userCode = jsonRes.getJSONObject("logininfo").optString("Usercode");
					loginInfo.lastLoginDate = jsonRes.getJSONObject("logininfo").optString("Lastlogindate");
					loginInfo.versionCode = jsonRes.getJSONObject("logininfo").optString("versioncode");
					loginInfo.role = jsonRes.getJSONObject("logininfo").optString("Role");
					isLocalUser = jsonRes.getJSONObject("logininfo").optString("isLocalUser");
				}
				if(jsonRes.has("userinfo")&&"1".equals(isLocalUser)){
					loginInfo.userName = jsonRes.getJSONObject("userinfo").optString("USERNAME");
					loginInfo.count1 = jsonRes.getJSONObject("userinfo").optString("COUNT1");
					loginInfo.count2 = jsonRes.getJSONObject("userinfo").optString("COUNT2");
					loginInfo.orgId = jsonRes.getJSONObject("userinfo").optString("ORGID");
					loginInfo.orgName = jsonRes.getJSONObject("userinfo").optString("ORGNAME");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (loginInfo.userCode!=null&&!"".equals(loginInfo.userCode) && !"-1".equals(loginInfo.userCode)) {
				loginState = true;
				// 非设备绑定的用户 不能登录
				if(!"1".equals(isLocalUser)){
					isNoBindingUserLogin = true;
					loginState = false;
				}
				if (loginInfo.versionCode!=null&&
						loginInfo.versionCode.length()>0 &&
						Integer.parseInt(loginInfo.versionCode) > this.getCurrentVersionCode())
					isUpdate = true;
			} else {
				loginState = false;
				isNetError = false;
				isUpdate = false;
			}
		} else {
			loginState = false;
			isNetError = true;
			isUpdate = false;
		}

		// 登陆成功
		if (loginState) {
			if (view_rememberMe.isChecked()) {
				saveSharePreferences();
			} 
		} else {
			// 如果不是网络错误
			if (!isNetError) {
				clearSharePreference();
			}
		}
		if (!view_rememberMe.isChecked()) {
			clearSharePreference();
		}
		return loginState;
	}

	/** 登录后台通知更新UI线程,主要用于登录失败,通知UI线程更新界面 */
	Handler loginHandler = new Handler() {
		public void handleMessage(Message msg) {
			isNetError = msg.getData().getBoolean("isNetError");
			isUpdate = msg.getData().getBoolean("isUpdate");
			isNoBindingUserLogin = msg.getData().getBoolean("isNewUserLogin");
			if (proDialog != null) {
				proDialog.dismiss();
			}
			if (isUpdate) {
				AlertDialog.Builder builder = new Builder(LoginActivity.this);
				builder.setMessage("发现新版本,需要现在升级吗？");
				builder.setTitle("升级提示");
				builder.setPositiveButton("现在下载",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
								dialog.dismiss();								
								proDialog = ProgressDialog.show(
										LoginActivity.this, "应用升级",
										"正在下载新版本，请稍候....", true, true);
								Thread thread = new Thread(new Runnable(){
									@Override
									public void run() {
										httpUtil.downAPK(downHandler);
									}});
								thread.start();
							}
						});
				builder.setNegativeButton("下次再说", new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
								dialog.dismiss();
								
								//启动后台设备、任务轮询服务
								Intent intentSer = new Intent();
								intentSer.setClass(LoginActivity.this, UndoTaskService.class);
								intentSer.putExtra("loginInfo", (Serializable)loginInfo);
								startService(intentSer);
								
								// 需要传输数据到登陆后的界面
								Intent intent = new Intent();
								intent.setClass(LoginActivity.this, MainActivity.class);
								intent.putExtra("loginInfo", (Serializable)loginInfo);
								// 转向登陆后的页面
								startActivity(intent);
								if(proDialog!=null)
									proDialog.dismiss();
							}
						});
				builder.create().show();
			} else {
				if (isNoBindingUserLogin){
					Toast.makeText(LoginActivity.this,"非本设备绑定用户不能登录",
							Toast.LENGTH_SHORT).show();
					return;
				} 
				if (isNetError) {
					Toast.makeText(LoginActivity.this,
							"登录失败,请确认有可用的网络连接",
							Toast.LENGTH_SHORT).show();
				}
				// 用户名和密码错误
				else {
					Toast.makeText(LoginActivity.this, "登录失败,请输入正确的用户名和密码.",
							Toast.LENGTH_SHORT).show();
					// 清除以前的SharePreferences密码
					clearSharePreference();
				}
			}
		}
	};

	class DoLogin implements Runnable {
		@Override
		public void run() {
			boolean loginState = false;
			String userName1 = view_userName.getText().toString();
			String password = view_password.getText().toString();
			loginState = validateLocalLogin(userName1, password);
			if (loginState && !isUpdate) {
				//是否不同用户登录
				isNewUserLogin = !userName.equals(userName1);
				
				//启动后台设备、任务轮询服务
				Intent intentSer = new Intent();
				intentSer.setClass(LoginActivity.this, UndoTaskService.class);
				intentSer.putExtra("loginInfo", (Serializable)loginInfo);
				startService(intentSer);
				
				// 需要传输数据到登陆后的界面
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, MainActivity.class);
				intent.putExtra("loginInfo", (Serializable)loginInfo);
				// 转向登陆后的页面
				startActivity(intent);
				
				if(proDialog!=null){
					proDialog.dismiss();
				}
				
				LoginActivity.this.finish();
			} else{
				// 通过调用handler来通知UI主线程更新UI
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putBoolean("isNetError", isNetError);
				bundle.putBoolean("isUpdate", isUpdate);
				bundle.putBoolean("isNewUserLogin", isNoBindingUserLogin);
				message.setData(bundle);
				loginHandler.sendMessage(message);
			}
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			ActivityUtil.showExitDialog(LoginActivity.this);
			return false;
		}
		return false;
	};

	private int getCurrentVersionCode() {
		PackageManager manager = getPackageManager();
		String packageName = getPackageName();
		try {
			PackageInfo info = manager.getPackageInfo(packageName, 0);
			return info.versionCode;
		} catch (Exception e) {
			return 1;
		}
	};

	Handler downHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (proDialog != null) {
				proDialog.dismiss();
			}
			int isDown = msg.arg1;
			switch (isDown) {
			case 1:
				downHandler.post(new Runnable() {
					public void run() {
						installAPK();
					}
				});
				// Toast.makeText(LoginActivity.this,
				// "新版本下载完成,请退出重装.",Toast.LENGTH_LONG).show();
				break;
			case 0:
				Toast.makeText(LoginActivity.this, "新版本下载失败,请择时重试.",
						Toast.LENGTH_LONG).show();
				break;
			case 2:
				Toast.makeText(LoginActivity.this, "没有检测到SD卡,请插卡重试.",
						Toast.LENGTH_LONG).show();
			}
		}
	};

	private void installAPK() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "MLS.apk")),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

}

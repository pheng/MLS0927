package net.chinawuyue.mls.undotask;

import net.chinawuyue.mls.MainActivity;
import net.chinawuyue.mls.R;
import net.chinawuyue.mls.login.LoginInfo;
import net.chinawuyue.mls.util.ActivityUtil;
import net.chinawuyue.mls.util.DoFetchThread;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * 未处理任务、设备异常登录后台轮询服务
 * @author Administrator
 *
 */
public class UndoTaskService extends Service{
	
	private static final String TAG = "UndoTaskService";
	private static final int REQUEST_TIME = 1800000;

	private NotificationManager notManager;
	
	//undo loan count
	private int count1;
	private int oldCount1 = -1;
	
	//return back loan count
	private int count3;
	private int oldCount3 = -1;
	
	//unFinish loan report count 
	private int count4;
	private int oldCount4 = -1;
	
	private static final String UNDO1 = "贷款审批：";
	private static final String UNDO3 = "\n贷款审批：";
	private static final String UNDO4 = "\n贷后检查：";
	private String undoMessage = "";
	
	private Notification not;//undo task notification
	//notification ID
	private static final int NOT_ID = 1000;//undo task
	//every request form server time
	private JSONObject jsonObj;
	
	
	private Notification not_device;//different device notification
	//notification ID
	private static final int NOT_ID_DEVICE = 2000;//different device
	//every request form server time
	private JSONObject jsonObj_device;
	
	private LoginInfo loginInfo;
	
	private boolean isExit = true;
	@Override
	public void onCreate() {
		super.onCreate();
		ActivityUtil.serviceList.add(this);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		isExit = false;
		notManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		if(intent == null ||intent.getSerializableExtra("loginInfo") == null){
			this.stopSelf();
			return; 
		}
		loginInfo = (LoginInfo) intent.getSerializableExtra("loginInfo");

		//for undo task
		jsonObj = new JSONObject();
		try {
			jsonObj.put("userid", loginInfo.userCode);
			jsonObj.put("CODENO", "XD0009");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(loginInfo.role != null && loginInfo.role.equalsIgnoreCase("1")){
			//董事长级别的用户,开启未处理任务轮询
			handler.postDelayed(new Runnable() {
				public void run() {
					Thread undoThread = new Thread(new DoFetchThread("XD0009", handler, jsonObj));
					undoThread.start();
				}
			}, 200);
		}
		
		//for different device login
		jsonObj_device = new JSONObject();
		try {
			jsonObj_device.put("CODENO", "GetMessages");
			jsonObj_device.put("usercode", loginInfo.userCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		deviceHandler.postDelayed(new Runnable() {
			public void run() {
				Thread deviceThread = new Thread(new DoFetchThread("GetMessages", deviceHandler, jsonObj_device));
				deviceThread.start();
			}
		}, 200);
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(isExit){
				return;
			}
			handler.postDelayed(new Runnable() {
				public void run() {
					Thread undoThread = new Thread(new DoFetchThread("XD0009", handler, jsonObj));
					undoThread.start();
				}
			}, REQUEST_TIME);
			if (msg.what == -1) {
				//network disconnect
				return;
			}
			parseJsonData(msg.obj.toString());
			
			if(!undoMessage.equalsIgnoreCase("") && undoMessage.length() > 0){
				showNotification(undoMessage);
				
				//undoMessage after used retrieve default
				undoMessage = "";
			}
		};
	};
	
	private void parseJsonData(String json){
//		Log.d(TAG, json);
		try {  
			JSONObject obj = new JSONObject(json);
			
			String RETURNCODE = obj.optString("RETURNCODE");
			if(RETURNCODE == null || !RETURNCODE.equalsIgnoreCase("N")){
				//请求失败
				return ;
			}
			
			count1 = obj.optInt("COUNT1", oldCount1);
			count3 = obj.optInt("COUNT3", oldCount3);
			count4 = obj.optInt("COUNT4", oldCount4);
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}
//		Log.d(TAG, "service back count: " + count1 + "--" + count3 + "--" + count4);
		if((oldCount1 >= 0) && (count1 > oldCount1)){
			// have new undo loan
			int num = count1 - oldCount1;
			undoMessage += UNDO1 + num + "条";
		}
		oldCount1 = count1;
		
		if((oldCount3 >= 0) && (count3 > oldCount3)){
			// have new return back loan
			int num = count3 - oldCount3;
			undoMessage += UNDO3 + num + "条";
		}
		oldCount3 = count3;
		
		if((oldCount4 >= 0) && (count4 > oldCount4)){
			// have new undo loan report
			int num = count4 - oldCount4;
			undoMessage += UNDO4 + num + "条";
		}
		oldCount4 = count4;
	}
	
	@SuppressWarnings("deprecation")
	private void showNotification(String message){
		//dismiss the old notification
		if(not != null){
			notManager.cancel(NOT_ID);
		}
		
		not = new Notification(R.drawable.ic_notification, "您有新的未处理任务", System.currentTimeMillis());
		not.flags = Notification.FLAG_AUTO_CANCEL;
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(UndoTaskService.this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		
		//modify the unfinished things count with the new count
		loginInfo.count1 = String.valueOf(count1);
		loginInfo.count2 = String.valueOf(count4);
		
		intent.putExtra("loginInfo", loginInfo);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
		not.setLatestEventInfo(this, "移动信贷通知", "您有新的未处理任务：" + message, contentIntent);
		notManager.notify(NOT_ID, not);
	}

	@SuppressLint("HandlerLeak")
	Handler deviceHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(isExit){
				return;
			}
			deviceHandler.postDelayed(new Runnable() {
				public void run() {
					Thread deviceThread = new Thread(new DoFetchThread("GetMessages", deviceHandler, jsonObj_device));
					deviceThread.start();
				}
			}, REQUEST_TIME);
			if (msg.what == -1) {
				//network disconnect
				return;
			}
			String deviceMsg = msg.obj.toString();
			if(deviceMsg != null && deviceMsg.length() > 0){
//				Log.d(TAG, deviceMsg);
				showDeviceNot(deviceMsg);
			}
		};
	};
	
	private void showDeviceNot(String msg) {
		//dismiss the old notification
		if(not_device != null){
			notManager.cancel(NOT_ID_DEVICE);
		}
		
		not_device = new Notification(R.drawable.ic_notification, "移动信贷账号被异常登录", System.currentTimeMillis());
		not_device.flags = Notification.FLAG_AUTO_CANCEL;
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(UndoTaskService.this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		
		intent.putExtra("loginInfo", loginInfo);
		intent.putExtra("isDevice", true);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
		not_device.setLatestEventInfo(this, "移动信贷通知", msg, contentIntent);
		notManager.notify(NOT_ID_DEVICE, not_device);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isExit = true;		
		if(not_device != null){
			notManager.cancel(NOT_ID_DEVICE);
		}
		if(not != null){
			notManager.cancel(NOT_ID);
		}
	}

}

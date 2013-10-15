package net.chinawuyue.mls.undotask;

import net.chinawuyue.mls.MainActivity;
import net.chinawuyue.mls.R;
import net.chinawuyue.mls.login.LoginInfo;
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
import android.os.Message;
import android.util.Log;

public class UndoTaskService extends Service{
	
	private static final String TAG = "UndoTaskService";

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
	
	private Notification not;
	//notification ID
	private static final int NOT_ID = 1000;
	
	//every request form server time
	private static final int REQUEST_TIME = 1800000;
	private JSONObject jsonObj;
	private LoginInfo loginInfo;
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d(TAG, "onStart");
		loginInfo = (LoginInfo) intent.getSerializableExtra("loginInfo");
		jsonObj = new JSONObject();
		try {
			jsonObj.put("userid", loginInfo.userCode);
			jsonObj.put("CODENO", "XD0009");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "jsonRequst: " + jsonObj.toString());
		Thread undoThread = new Thread(new UndoThread());
		undoThread.start();
//		Thread undoThread = new Thread(new DoFetchThread("XD0009", handler, jsonObj));
//		undoThread.start();
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			handler.postDelayed(new UndoThread(), REQUEST_TIME);
//			handler.postDelayed(new DoFetchThread("XD0009", handler, jsonObj), REQUEST_TIME);
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
		Log.d(TAG, json);
		try {  
			JSONObject obj = new JSONObject(json);
			
			String RETURNCODE = obj.optString("RETURNCODE");
			if(!RETURNCODE.equalsIgnoreCase("N")){
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
		Log.d(TAG, "showNotification");
		NotificationManager notManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		//dismiss the old notification
		if(not != null){
			Log.d(TAG, "notification is not null");
			notManager.cancel(NOT_ID);
		}
		
		not = new Notification(R.drawable.ic_notification, "您有新的未处理任务", System.currentTimeMillis());
		not.flags = Notification.FLAG_AUTO_CANCEL;
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(UndoTaskService.this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.putExtra("loginInfo", loginInfo);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
		not.setLatestEventInfo(this, "移动信贷通知", "您有新的未处理任务：" + message, contentIntent);
		notManager.notify(NOT_ID, not);
	}
	
	class UndoThread implements Runnable{
		public UndoThread() {
		}
		
		@Override
		public void run() {
			Message msg = handler.obtainMessage();
			msg.what = 0;
			Number old1 = count1 + 1;
			Number old3 = count3 + 3;
			Number old4 = count4 + 1;
			JSONObject json = new JSONObject();
			try {
				json.put("RETURNCODE", "N");
				json.put("COUNT1", old1);
				json.put("COUNT3", old3);
				json.put("COUNT4", old4);
			} catch (JSONException e) {
				msg.what = -1;
				e.printStackTrace();
			}
			msg.obj = json;
			msg.sendToTarget();
		}
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}

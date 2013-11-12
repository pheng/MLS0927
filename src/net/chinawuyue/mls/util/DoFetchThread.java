package net.chinawuyue.mls.util;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
//import android.util.Log;

/** 
 * 获取服务器数据线程 
 */
public class DoFetchThread implements Runnable {
	private String fileName;
	private Handler handler;
	private JSONObject request;
	private HttpUtil httpUtil = null;
	public DoFetchThread(String fileName,Handler handler,JSONObject request){
		this.fileName = fileName;
		this.handler = handler;
		this.request = request;
		httpUtil = new HttpUtil();
//		Log.d("DoFetchThread", "json_request:" + request.toString());
	}
	@Override
	public void run() {
		Message msg = handler.obtainMessage();
		try {
			String result = httpUtil.requestStringForPost(HttpUtil.URL
					+ fileName,request);
			result = result.replace("\\", "");
			result = result.substring(1, result.length() - 1);
			msg.obj = result;
//			Log.d("DoFetch", "response" + result);
		} catch (Exception e) {
			msg.what = -1;
			e.printStackTrace();
		}
		msg.sendToTarget();
	}
	
	public void stop(){
		httpUtil.abort();
	}
};

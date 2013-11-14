package net.chinawuyue.mls.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

/**
 * 网络工具类
 */
public class HttpUtil {
	private static final String BASE_PATH = "http://27.17.37.100:8002";//
	private static final String WEB_APP = "/wcf/UserService.svc/ajaxEndpoint/";
	public static final String URL = BASE_PATH+WEB_APP;
	private static final String DOWN_PATH = "http://27.17.37.100:8002/download/MLS.apk";
	private static final String LOCAL_PATH = Environment.getExternalStorageDirectory()+"/MLS.apk";
	private static final String ENCODING = "UTF-8";
	public static final int TIMEOUT = 8000;
	private HttpPost post = null;

	/** 发送Post请求，无参数 */
	public String requestStringForPost(String url) throws Exception {
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), TIMEOUT); // Timeout
		post = new HttpPost(url);
		HttpResponse response = httpclient.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			result = EntityUtils.toString(response.getEntity());
		}
//		return new String(result.getBytes("ISO-8859-1"), ENCODING);
		try {
			return URLDecoder.decode(result, ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return URLDecoder.decode(result.replaceAll("%", "%25"), ENCODING);
			} catch (Exception e2) {
				e.printStackTrace();
				return result;
			}
		}
	}

	/** 发送Post请求，带参数 */
	public String requestStringForPost(String url,
			List<NameValuePair> params) throws Exception {
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), TIMEOUT); // Timeout
		post = new HttpPost(url);
		// 创建请求参数
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,
				HTTP.UTF_8);
		// 给Post请求添加参数
		post.setEntity(entity);
		// 执行请求
		HttpResponse response = httpclient.execute(post);
		// 返回结果
		if (response.getStatusLine().getStatusCode() == 200) {
			result = EntityUtils.toString(response.getEntity());
		}
		try {
			return URLDecoder.decode(result, ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return URLDecoder.decode(result.replaceAll("%", "%25"), ENCODING);
			} catch (Exception e2) {
				e.printStackTrace();
				return result;
			}
		}
	}

	/** 发送Post请求，带JSON参数 */
	public String requestStringForPost(String url, JSONObject jsonRequest)
			throws Exception {
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), TIMEOUT); // Timeout
		post = new HttpPost(url);
		// 创建请求参数
		ByteArrayEntity se = new ByteArrayEntity(jsonRequest.toString()
				.getBytes("UTF8"));
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				"application/json"));
		// 给Post请求添加参数
		post.setEntity(se);
		// 执行请求
		HttpResponse response = httpclient.execute(post);
		// 返回结果
		if (response.getStatusLine().getStatusCode() == 200) {
			result = EntityUtils.toString(response.getEntity());
		}
		try {
			return URLDecoder.decode(result, ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return URLDecoder.decode(result.replaceAll("%", "%25"), ENCODING);
			} catch (Exception e2) {
				e.printStackTrace();
				return result;
			}
		}
	}
	
	public void abort(){
		try {
			post.abort();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 处理用户登录操作 */
	public JSONObject doLogin(String uc, String pwd) {
		JSONObject jsonRes = null;
		String url = BASE_PATH + WEB_APP+ "DoLogin";
		JSONObject jsonReq = new JSONObject();
		try {
			jsonReq.put("usercode", uc);
			jsonReq.put("password", pwd);
			String result = requestStringForPost(url,jsonReq);
			result = result.replace("\\", "");
			result = result.substring(1, result.length() - 1);
			if (result.length() > 0)
				jsonRes = new JSONObject(result);
			else
				jsonRes = new JSONObject("{usercode:-1}");
		} catch (Exception e) {
			jsonRes = null;
			e.printStackTrace();
		}
		return jsonRes;
	}
	
	/** 处理用户登录操作 */
	public JSONObject doLoginWithDeviceId(String uc, String pwd,String deviceId) {
		JSONObject jsonRes = null;
		String url = BASE_PATH + WEB_APP+ "DoLoginWithDeviceId";
		JSONObject jsonReq = new JSONObject();
		try {
			jsonReq.put("usercode", uc);
			jsonReq.put("password", pwd);
			jsonReq.put("deviceid", deviceId);
			String result = requestStringForPost(url,jsonReq);
			result = result.replace("\\", "");
			result = result.substring(1, result.length() - 1);
			if (result.length() > 0)
				jsonRes = new JSONObject(result);
			else
				jsonRes = new JSONObject("{usercode:-1}");
		} catch (Exception e) {
			jsonRes = null;
			e.printStackTrace();
		}
		return jsonRes;
	}
	
	/** 处理用户修改密码操作 */
	public String changePWD(String usercode, String oldpwd, String newpwd) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 5000);
		HttpPost httppost = new HttpPost(BASE_PATH + WEB_APP + "/ChangePWD");
		HttpResponse response;
		JSONObject jsonReq = new JSONObject();
		String res = null;
		try {
			jsonReq.put("usercode", usercode);
			jsonReq.put("oldpwd", oldpwd);
			jsonReq.put("newpwd", newpwd);
			ByteArrayEntity se = new ByteArrayEntity(jsonReq.toString()
					.getBytes("UTF8"));
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			httppost.setEntity(se);
			response = httpclient.execute(httppost);
			if (response != null
					&& response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				res = new String(EntityUtils.toString(response.getEntity())
						.getBytes("UTF8"));
			}
		} catch (Exception e) {
			res = null;
			e.printStackTrace();
		}
		return res.substring(1, res.length() - 1);
	}
	
//	/** 应用自动升级 apk下载 */
//	public int downAPK() {
//		/** 0:下载失败，1：下载完成，2：SD卡未就绪 */
//		int downOK = 0;
//		URL url = null;
//		if (Environment.getExternalStorageState().equals(
//				android.os.Environment.MEDIA_MOUNTED)) {
//			try {
//				url = new URL(DOWN_PATH);
//				HttpURLConnection con = (HttpURLConnection) url
//						.openConnection();
////				byte[] auth = (USERNAME+":"+PASSWORD).getBytes();
////				String basic = Base64.encodeToString(auth, Base64.NO_WRAP);
////				con.setRequestProperty("Authorization", "Basic "+basic);
////				con.setConnectTimeout(TIMEOUT);
//				InputStream in = con.getInputStream();
//				File fileOut = new File(
//						Environment.getExternalStorageDirectory(), "MLS.apk");
//				FileOutputStream out = new FileOutputStream(fileOut);
//				byte[] bytes = new byte[1024];
//				int c;
//				while ((c = in.read(bytes)) != -1) {
//					out.write(bytes, 0, c);
//				}
//				in.close();
//				out.close();
//				downOK = 1;
//			} catch (Exception e) {
//				e.printStackTrace();
//				downOK = 0;
////				if(e.getMessage().contains("unexpected end of stream")){
////					downOK = 1;
////				}
//			}
//		} else
//			downOK = 2;
//		return downOK;
//	}
	
	/** 应用自动升级 apk下载 */
	public void downAPK(Handler handler) {
		Message msg = handler.obtainMessage();
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)){
			msg.arg1 = 2;
			msg.sendToTarget();
		}
		try {
			URL url = new URL(DOWN_PATH);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(TIMEOUT);
			conn.setRequestMethod("GET");
			int fileSize = conn.getContentLength();
			//创建本地文件
			File file = new File(LOCAL_PATH);
			RandomAccessFile raf = new RandomAccessFile(file,"rwd");
			//设置本地文件长度
			raf.setLength(fileSize);
			raf.close();
			conn.disconnect();
			new DownloadThread(handler,0,fileSize).start();
		} catch (Exception e) {
			e.printStackTrace();
			msg.arg1 = 0;
			msg.sendToTarget();
		}
	}
	
	//下载线程
	class DownloadThread extends Thread{
		
		private int startPos = 0;
		private int endPos = 0;
		private Handler handler = null;
		public DownloadThread(Handler handler,int startPos,int endPos){
			this.startPos = startPos;
			this.endPos = endPos;
			this.handler = handler;
		}
		
		public void run() {
			RandomAccessFile raf = null;
			InputStream input = null;
			Message msg = handler.obtainMessage();
			try {
				URL url = new URL(DOWN_PATH);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setConnectTimeout(TIMEOUT);
				conn.setRequestMethod("GET");
				//设置下载范围
				conn.setRequestProperty("Range", "bytes="+startPos+"-"+endPos);
				raf = new RandomAccessFile(new File(LOCAL_PATH),"rwd");
				//设置写入位置
				raf.seek(startPos);
				byte[] buffer = new byte[1024];
				int len;
				input = conn.getInputStream();
				while((len=(input.read(buffer)))!=-1){
					raf.write(buffer,0,len);
				}
				msg.arg1 = 1;
				msg.sendToTarget();
			} catch (Exception e) {
				e.printStackTrace();
				msg.arg1 = 0;
				msg.sendToTarget();
			} finally{
				try {
					input.close();
					raf.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
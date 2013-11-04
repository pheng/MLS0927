package net.chinawuyue.mls.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * Activty辅助类
 */
public class ActivityUtil {
	/**记录打开的Activity*/
	public static List<Activity> activityList = new ArrayList<Activity>();
	public static List<Service> serviceList = new ArrayList<Service>();
	public static int exitCount = 0;
	/**显示关闭对话框*/
	public static void showExitDialog(Context context) {
		if(++exitCount<2){
			Toast.makeText(context, "再次按返回将退出程序", 1).show();
		}else{
			for(Service ser : serviceList){
				ser.stopSelf();
			}
			
			for (Activity act : activityList) {
				act.finish();
			}
			
		}
	}
}

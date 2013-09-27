package net.chinawuyue.mls.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**Activty辅助类*/
public class ActivityUtil {
	/**记录打开的Activity*/
	public static List<Activity> activityList = new ArrayList<Activity>();
	public static View view_Todo = null;
	public static View view_BeforeLoan = null;
	public static View view_AfterLoan = null;
	public static View view_Report = null;
	public static View view_Board = null;
	public static View view_Sys = null;
	public static int exitCount = 0;
	/**显示关闭对话框*/
	public static void showExitDialog(Context context) {
		if(++exitCount<2){
			Toast.makeText(context, "再次按返回将退出程序", 1).show();
		}else{
			for (Activity act : activityList) {
				act.finish();
			}
		}
//		AlertDialog.Builder builder = new Builder(context);
//		builder.setMessage("确认退出系统吗?")
//				.setTitle("提示")
//				.setPositiveButton("确认",
//						new android.content.DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								dialog.dismiss();
//								for (Activity act : activityList) {
//									act.finish();
//								}
//							}
//						})
//				.setNegativeButton("取消",
//						new android.content.DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								dialog.dismiss();
//							}
//						}).create().show();
	}
}

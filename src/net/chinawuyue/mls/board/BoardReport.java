package net.chinawuyue.mls.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.chinawuyue.mls.R;
import net.chinawuyue.mls.login.LoginInfo;
import net.chinawuyue.mls.reports.BaseReport;
import net.chinawuyue.mls.util.DoFetchThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 公告通知类
 * 
 */
public class BoardReport extends BaseReport {

	// 报表静态缓存
	public static List<Map<String, Object>> reportItems = new ArrayList<Map<String, Object>>();
	private Context context = null;
	private ProgressDialog progressDialog = null;
	private LoginInfo loginInfo = null;			//登录信息

	public BoardReport(Context context,LoginInfo loginInfo) {
		super(context);
		this.context = context;
		super.setActivityClass(BoardActivity.class);
		super.xItems = reportItems;
		this.loginInfo = loginInfo;
	}

	//获取数据
	public void fetchData() {
		progressDialog = ProgressDialog.show(context, "", 
				context.getResources().getString(R.string.wait),true,true);
		if (reportItems.size() == 0){
			isNeedUpdate = true;
		}
		if (isNeedUpdate) {
			JSONObject request = new JSONObject();
			try {
				request.put("userid", loginInfo.userCode);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			final DoFetchThread doFetch = new DoFetchThread("XD0002", handler,request);
			Thread thread = new Thread(doFetch);
			thread.start();
			progressDialog.setOnCancelListener(new OnCancelListener(){
				@Override
				public void onCancel(DialogInterface dialog) {
					doFetch.stop();
				}
			});
		} else {
			this.setAdapter(new MyAdapter(reportItems));
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	}
	
	// 线程处理
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			if (msg.what == -1) {
				Toast.makeText(context, R.string.net_error, 1).show();
				return;
			}
			isNeedUpdate = false;
			String json = msg.obj.toString();
			setAdapter(new MyAdapter(parseJSON(json)));
		};
	};

	// 解析JSON数据
	private List<Map<String, Object>> parseJSON(String json) {
		reportItems.clear();
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray array = obj.optJSONArray("ARRAY1");
			for (int i = 0; i < array.length(); i++) {
				JSONObject data = array.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(BOARDNO, data.opt(BOARDNO));
				map.put(BOARDNAME, data.opt(BOARDNAME));
				map.put(BOARDTITLE, data.opt(BOARDTITLE));
				map.put(BOARDDESC, data.opt(BOARDDESC));
				map.put(INPUTDATE, data.opt(INPUTDATE));
				reportItems.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reportItems;
	}

	//列表适配器
	class MyAdapter extends BaseAdapter {

		List<Map<String, Object>> items = null;

		public MyAdapter(List<Map<String, Object>> items) {
			this.items = items;
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View contentView, ViewGroup vg) {
			ViewHolder holder = null;
			if (contentView == null) {
				holder = new ViewHolder();
				View layout = LayoutInflater.from(context).inflate(
						R.layout.listview_board, null);
				holder.tvBoardNo = (TextView) layout
						.findViewById(R.id.t_board_no);
//				holder.tvBoardName = (TextView) layout
//						.findViewById(R.id.t_board_name);
				holder.tvBoardTitle = (TextView) layout
						.findViewById(R.id.t_board_title);
				holder.tvInputDate = (TextView) layout
						.findViewById(R.id.t_board_time);
				layout.setTag(holder);
				contentView = layout;
			} else {
				holder = (ViewHolder) contentView.getTag();
			}
			Map<String, Object> map = items.get(position);
//			holder.tvBoardName.setText(map.get(BaseReport.BOARDNAME).toString());
			holder.tvBoardTitle.setText(map.get(BaseReport.BOARDTITLE)
					.toString());
			holder.tvBoardNo.setText(map.get(BaseReport.BOARDNO).toString());
			holder.tvInputDate
					.setText(map.get(BaseReport.INPUTDATE).toString());
			if(position%2==0){
				contentView.setBackgroundResource(R.drawable.selector2);
			}else{
				contentView.setBackgroundResource(R.drawable.selector);
			}
			return contentView;
		}


	}
	class ViewHolder {
		public TextView tvBoardNo;
		public TextView tvBoardName;
		public TextView tvBoardTitle;
		public TextView tvInputDate;
	}
	
	@Override
	public boolean isScorllLeft() {
		return true;
	}


}

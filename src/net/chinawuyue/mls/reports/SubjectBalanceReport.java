package net.chinawuyue.mls.reports;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.chinawuyue.mls.MyHScrollView;
import net.chinawuyue.mls.R;
import net.chinawuyue.mls.reports.BaseReport.OnScrollChangedListenerImp;
import net.chinawuyue.mls.reports.BaseReport.ReportType;
import net.chinawuyue.mls.reports.LoanAnalysis1Report.MyAdapter;
import net.chinawuyue.mls.reports.LoanBalanceReport.ViewHolder;
import net.chinawuyue.mls.util.DoFetchThread;
import net.chinawuyue.mls.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 科目余额表
 * 
 * @author hp
 * 
 */
public class SubjectBalanceReport extends BaseReport {

	// 报表静态缓存
	public static List<Map<String, Object>> reportItems = new ArrayList<Map<String, Object>>();
	// 保存上一次的请求
	private static ReportRequest reportRequest = new ReportRequest();
	private Context context = null;
	private ProgressDialog progressDialog;

	public SubjectBalanceReport(Context context) {
		super(context);
		this.context = context;
		this.setReportType(ReportType.SubjectBalance);
		super.xItems = reportItems;
	}

	// 线程处理
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			if (msg.what == -1) {
				Toast.makeText(context, R.string.error, 1).show();
				return;
			}
			isNeedUpdate = false;
			String json = msg.obj.toString();
			if(json.length()>0)
				setAdapter(new MyAdapter(parseJSON(json)));
			else
				Toast.makeText(context, R.string.empty, 1).show();
		};
	};

	// 解析JSON数据
	private List<Map<String, Object>> parseJSON(String json) {
		reportItems.clear();
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray array = obj.optJSONArray("ARRAY1");
			if (array == null) {
				Toast.makeText(context, R.string.empty, 1).show();
				return reportItems;
			}
			//保留4位小数
			DecimalFormat df = new DecimalFormat("0.0000");
			for (int i = 0; i < array.length(); i++) {
				JSONObject data = array.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(SUBJECTNO, data.opt(SUBJECTNO));
				map.put(SUBJECTNAME, data.opt(SUBJECTNAME));
				map.put(UNITNAME, obj.opt(UNITNAME));
				map.put(ORGNAME, obj.opt(ORGNAME));
				for (int j = 1; j <= 6; j++) {
					map.put(COUNT + j, data.opt(COUNT + j));
//					map.put(BALANCE + j, df.format(data.opt(BALANCE + j)));
					map.put(BALANCE + j, data.opt(BALANCE + j));
				}
				reportItems.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reportItems;
	}

	public void fetchData() {
		// 测试网络json数据
		progressDialog = ProgressDialog.show(context, "", context
				.getResources().getString(R.string.wait),true,true);
		if (reportItems.size() == 0) {
			isNeedUpdate = true;
		}
		if(!reportRequest.equals(request)){
			isNeedUpdate = true;
			reportRequest = request;
		}
		handler.postDelayed(new Runnable() {
			public void run() {
				if (isNeedUpdate) {
					JSONObject jsonReq = new JSONObject();
					try {
						jsonReq.put("userId", request.getUSERID());
						jsonReq.put("orgId", request.getORGID());
						jsonReq.put("date", request.getDATE());
						jsonReq.put("queryOrgId", request.getQUERYORGID());
						jsonReq.put("unitname", request.getUNITNAME());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					final DoFetchThread doFetch = new DoFetchThread("RP0005", handler,jsonReq);
					Thread thread = new Thread(doFetch);
					thread.start();
					progressDialog.setOnCancelListener(new OnCancelListener(){
						@Override
						public void onCancel(DialogInterface dialog) {
							doFetch.stop();
						}
					});
				} else {
					setAdapter(new MyAdapter(reportItems));
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
				}
			}
		}, DELAY);
	}

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
				synchronized (context) {
					holder = new ViewHolder();
					View layout = LayoutInflater.from(context).inflate(
							R.layout.listview_report_subject_balance, null);
					// 控制列表项水平滑动
					MyHScrollView scrollView1 = (MyHScrollView) layout
							.findViewById(R.id.horizontalScrollView1);
					holder.scrollView = scrollView1;
					MyHScrollView headSrcrollView = (MyHScrollView) mHead
							.findViewById(R.id.horizontalScrollView1);
					// 滑动表头时带动列表项一起滑动
					headSrcrollView
							.AddOnScrollChangedListener(new OnScrollChangedListenerImp(
									scrollView1));
					holder.tvOrgName = (TextView) layout
							.findViewById(R.id.t_superorgname);
					holder.tvSubjectNo = (TextView) layout
							.findViewById(R.id.t_subjectno);
					holder.tvSubjectName = (TextView) layout
							.findViewById(R.id.t_subjectname);
					holder.tvCount1 = (TextView) layout
							.findViewById(R.id.t_count1);
					holder.tvBalance1 = (TextView) layout
							.findViewById(R.id.t_balance1);
					holder.tvCount2 = (TextView) layout
							.findViewById(R.id.t_count2);
					holder.tvBalance2 = (TextView) layout
							.findViewById(R.id.t_balance2);
					holder.tvCount3 = (TextView) layout
							.findViewById(R.id.t_count3);
					holder.tvBalance3 = (TextView) layout
							.findViewById(R.id.t_balance3);
					holder.tvCount4 = (TextView) layout
							.findViewById(R.id.t_count4);
					holder.tvBalance4 = (TextView) layout
							.findViewById(R.id.t_balance4);
					holder.tvCount5 = (TextView) layout
							.findViewById(R.id.t_count5);
					holder.tvBalance5 = (TextView) layout
							.findViewById(R.id.t_balance5);
					holder.tvCount6 = (TextView) layout
							.findViewById(R.id.t_count6);
					holder.tvBalance6 = (TextView) layout
							.findViewById(R.id.t_balance6);
					layout.setTag(holder);
					contentView = layout;
				}
			} else {
				holder = (ViewHolder) contentView.getTag();
			}
			Map<String, Object> map = items.get(position);
			holder.tvCount1.setText(map.get(COUNT + "1").toString());
			holder.tvBalance1.setText(map.get(BALANCE + "1").toString());
			holder.tvCount2.setText(map.get(COUNT + "2").toString());
			holder.tvBalance2.setText(map.get(BALANCE + "2").toString());
			holder.tvCount3.setText(map.get(COUNT + "3").toString());
			holder.tvBalance3.setText(map.get(BALANCE + "3").toString());
			holder.tvCount4.setText(map.get(COUNT + "4").toString());
			holder.tvBalance4.setText(map.get(BALANCE + "4").toString());
			holder.tvCount5.setText(map.get(COUNT + "5").toString());
			holder.tvBalance5.setText(map.get(BALANCE + "5").toString());
			holder.tvCount6.setText(map.get(COUNT + "6").toString());
			holder.tvBalance6.setText(map.get(BALANCE + "6").toString());
			holder.tvSubjectName.setText(map.get(SUBJECTNAME).toString());
			holder.tvSubjectNo.setText(map.get(SUBJECTNO).toString());
			holder.tvOrgName.setText(map.get(ORGNAME).toString());
			if(position%2==0){
				contentView.setBackgroundResource(R.drawable.selector2);
			}else{
				contentView.setBackgroundResource(R.drawable.selector);
			}
			return contentView;
		}

	}
	class ViewHolder {
		public HorizontalScrollView scrollView;
		public TextView tvSubjectNo;
		public TextView tvSubjectName;
		public TextView tvOrgName;
		public TextView tvCount1;
		public TextView tvBalance1;
		public TextView tvCount2;
		public TextView tvBalance2;
		public TextView tvCount3;
		public TextView tvBalance3;
		public TextView tvCount4;
		public TextView tvBalance4;
		public TextView tvCount5;
		public TextView tvBalance5;
		public TextView tvCount6;
		public TextView tvBalance6;
	}
}

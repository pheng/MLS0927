package net.chinawuyue.mls.reports;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.chinawuyue.mls.MyHScrollView;
import net.chinawuyue.mls.R;
import net.chinawuyue.mls.reports.BaseReport.OnScrollChangedListenerImp;
import net.chinawuyue.mls.reports.BaseReport.ReportType;
import net.chinawuyue.mls.reports.LoanAnalysis1Report.ViewHolder;
import net.chinawuyue.mls.reports.LoanAnalysis2Report.MyAdapter;
import net.chinawuyue.mls.util.DoFetchThread;
import net.chinawuyue.mls.util.HttpUtil;
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
 * 当年到期贷款收回率情况统计表（按业务品种）
 */
public class LoanRate2Report extends BaseReport {

	// 报表静态缓存
	public static List<Map<String, Object>> reportItems = new ArrayList<Map<String, Object>>();
	private static ReportRequest reportRequest = new ReportRequest();
	private Context context;
	private ProgressDialog progressDialog;

	public LoanRate2Report(Context context) {
		super(context);
		this.context = context;
		this.setReportType(ReportType.LoanRate2);
		super.xItems = reportItems;
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
							R.layout.listview_report_loan_rate2, null);
					MyHScrollView scrollView1 = (MyHScrollView) layout
							.findViewById(R.id.horizontalScrollView1);
					holder.scrollView = scrollView1;
					MyHScrollView headSrcrollView = (MyHScrollView) mHead
							.findViewById(R.id.horizontalScrollView1);
					headSrcrollView
							.AddOnScrollChangedListener(new OnScrollChangedListenerImp(
									scrollView1));
					holder.tvBussinessName = (TextView) layout
							.findViewById(R.id.t_bussinessname);
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
					holder.tvRatio4 = (TextView) layout
							.findViewById(R.id.t_ratio4);
					layout.setTag(holder);
					contentView = layout;
				}
			} else {
				holder = (ViewHolder) contentView.getTag();
			}
			Map<String, Object> map = items.get(position);
			holder.tvBussinessName.setText(map.get(BUSSINESSNAME).toString());
			holder.tvCount1.setText(map.get(COUNT + "1").toString());
			holder.tvBalance1.setText(map.get(BALANCE + "1").toString());
			holder.tvCount2.setText(map.get(COUNT + "2").toString());
			holder.tvBalance2.setText(map.get(BALANCE + "2").toString());
			holder.tvCount3.setText(map.get(COUNT + "3").toString());
			holder.tvBalance3.setText(map.get(BALANCE + "3").toString());
			holder.tvRatio4.setText(map.get(RATIO + "4").toString());
			if (position % 2 == 0) {
				contentView.setBackgroundResource(R.drawable.selector2);
			} else {
				contentView.setBackgroundResource(R.drawable.selector);
			}
			return contentView;
		}


	}
	class ViewHolder {
		public HorizontalScrollView scrollView;
		public TextView tvBussinessName;
		public TextView tvCount1;
		public TextView tvBalance1;
		public TextView tvCount2;
		public TextView tvBalance2;
		public TextView tvCount3;
		public TextView tvBalance3;
		public TextView tvRatio4;
	}

	@Override
	public void fetchData() {
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
					final DoFetchThread doFetch = new DoFetchThread("RP0009", handler,jsonReq);
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
				map.put(BUSSINESSNAME, data.opt(BUSSINESSNAME));
				for (int j = 1; j <= 3; j++) {
					map.put(COUNT + j, data.opt(COUNT + j));
//					map.put(BALANCE + j, df.format(data.opt(BALANCE + j)));
					map.put(BALANCE + j, data.opt(BALANCE + j));
				}
				map.put(RATIO + 4, data.opt(RATIO + 4));
				reportItems.add(map);
			}
			//根据回收率降序排序
			Collections.sort(reportItems,new Comparator<Map<String,Object>>() {
				@Override
				public int compare(Map<String, Object> lhs,
						Map<String, Object> rhs) {
					Integer lhsCount = Integer.valueOf(lhs.get(BaseReport.RATIO + 4).toString());
					Integer rhsCount = Integer.valueOf(rhs.get(BaseReport.RATIO + 4).toString());
					return rhsCount.compareTo(lhsCount);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reportItems;
	}

}

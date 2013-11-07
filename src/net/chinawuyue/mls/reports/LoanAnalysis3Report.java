package net.chinawuyue.mls.reports;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
 * 贷款分析报表三
 */
public class LoanAnalysis3Report extends BaseReport {

	// 报表静态缓存
	public static List<Map<String, Object>> reportItems = new ArrayList<Map<String, Object>>();
	private static ReportRequest reportRequest = new ReportRequest();
	private Context context;
	private ProgressDialog progressDialog;

	public LoanAnalysis3Report(Context context) {
		super(context);
		this.context = context;
		this.setReportType(ReportType.LoanAnalysis3);
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
							R.layout.listview_report_loan_analysis3, null);
					// ????áD±í?????????ˉ
					MyHScrollView scrollView1 = (MyHScrollView) layout
							.findViewById(R.id.horizontalScrollView1);
					holder.scrollView = scrollView1;
					MyHScrollView headSrcrollView = (MyHScrollView) mHead
							.findViewById(R.id.horizontalScrollView1);
					// ???ˉ±íí·ê±′??ˉáD±í??ò??e???ˉ
					headSrcrollView
							.AddOnScrollChangedListener(new OnScrollChangedListenerImp(
									scrollView1));

					holder.tvSuperOrgId = (TextView) layout
							.findViewById(R.id.t_superorgid);
					holder.tvSuperOrgName = (TextView) layout
							.findViewById(R.id.t_superorgname);
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
					holder.tvCount7 = (TextView) layout
							.findViewById(R.id.t_count7);
					holder.tvBalance7 = (TextView) layout
							.findViewById(R.id.t_balance7);
					holder.tvCount8 = (TextView) layout
							.findViewById(R.id.t_count8);
					holder.tvBalance8 = (TextView) layout
							.findViewById(R.id.t_balance8);
					holder.tvCount9 = (TextView) layout
							.findViewById(R.id.t_count9);
					holder.tvBalance9 = (TextView) layout
							.findViewById(R.id.t_balance9);
					holder.tvCount10 = (TextView) layout
							.findViewById(R.id.t_count10);
					holder.tvBalance10 = (TextView) layout
							.findViewById(R.id.t_balance10);
					holder.tvSum1 = (TextView) layout.findViewById(R.id.t_sum1);
					holder.tvRatio1 = (TextView) layout
							.findViewById(R.id.t_ratio1);
					holder.tvSum2 = (TextView) layout.findViewById(R.id.t_sum2);
					holder.tvRatio2 = (TextView) layout
							.findViewById(R.id.t_ratio2);
					holder.tvSum3 = (TextView) layout.findViewById(R.id.t_sum3);
					holder.tvRatio3 = (TextView) layout
							.findViewById(R.id.t_ratio3);
					holder.tvSum4 = (TextView) layout.findViewById(R.id.t_sum4);
					holder.tvRatio4 = (TextView) layout
							.findViewById(R.id.t_ratio4);
					holder.tvSum5 = (TextView) layout.findViewById(R.id.t_sum5);
					holder.tvRatio5 = (TextView) layout
							.findViewById(R.id.t_ratio5);
					holder.tvSum6 = (TextView) layout.findViewById(R.id.t_sum6);
					holder.tvRatio6 = (TextView) layout
							.findViewById(R.id.t_ratio6);
					holder.tvSum7 = (TextView) layout.findViewById(R.id.t_sum7);
					holder.tvRatio7 = (TextView) layout
							.findViewById(R.id.t_ratio7);
					holder.tvSum8 = (TextView) layout.findViewById(R.id.t_sum8);
					holder.tvRatio8 = (TextView) layout
							.findViewById(R.id.t_ratio8);
					holder.tvSum9 = (TextView) layout.findViewById(R.id.t_sum9);
					holder.tvRatio9 = (TextView) layout
							.findViewById(R.id.t_ratio9);
					holder.tvSum10 = (TextView) layout
							.findViewById(R.id.t_sum10);
					holder.tvRatio10 = (TextView) layout
							.findViewById(R.id.t_ratio10);
					layout.setTag(holder);
					contentView = layout;
				}
			} else {
				holder = (ViewHolder) contentView.getTag();
			}
			Map<String, Object> map = items.get(position);
			holder.tvSuperOrgId.setText(map.get(SUPERORGID).toString());
			holder.tvSuperOrgName.setText(map.get(SUPERORGNAME).toString());
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
			holder.tvCount7.setText(map.get(COUNT + "7").toString());
			holder.tvBalance7.setText(map.get(BALANCE + "7").toString());
			holder.tvCount8.setText(map.get(COUNT + "8").toString());
			holder.tvBalance8.setText(map.get(BALANCE + "8").toString());
			holder.tvCount9.setText(map.get(COUNT + "9").toString());
			holder.tvBalance9.setText(map.get(BALANCE + "9").toString());
			holder.tvCount10.setText(map.get(COUNT + "10").toString());
			holder.tvBalance10.setText(map.get(BALANCE + "10").toString());
			holder.tvSum1.setText(map.get(SUM + "1").toString());
			holder.tvRatio1.setText(map.get(RATIO + "1").toString());
			holder.tvSum2.setText(map.get(SUM + "2").toString());
			holder.tvRatio2.setText(map.get(RATIO + "2").toString());
			holder.tvSum3.setText(map.get(SUM + "3").toString());
			holder.tvRatio3.setText(map.get(RATIO + "3").toString());
			holder.tvSum4.setText(map.get(SUM + "4").toString());
			holder.tvRatio4.setText(map.get(RATIO + "4").toString());
			holder.tvSum5.setText(map.get(SUM + "5").toString());
			holder.tvRatio5.setText(map.get(RATIO + "5").toString());
			holder.tvSum6.setText(map.get(SUM + "6").toString());
			holder.tvRatio6.setText(map.get(RATIO + "6").toString());
			holder.tvSum7.setText(map.get(SUM + "7").toString());
			holder.tvRatio7.setText(map.get(RATIO + "7").toString());
			holder.tvSum8.setText(map.get(SUM + "8").toString());
			holder.tvRatio8.setText(map.get(RATIO + "8").toString());
			holder.tvSum9.setText(map.get(SUM + "9").toString());
			holder.tvRatio9.setText(map.get(RATIO + "9").toString());
			holder.tvSum10.setText(map.get(SUM + "10").toString());
			holder.tvRatio10.setText(map.get(RATIO + "10").toString());
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
		public TextView tvSuperOrgId;
		public TextView tvSuperOrgName;
		public TextView tvCount1;
		public TextView tvBalance1;
		public TextView tvSum1;
		public TextView tvRatio1;
		public TextView tvCount2;
		public TextView tvBalance2;
		public TextView tvSum2;
		public TextView tvRatio2;
		public TextView tvCount3;
		public TextView tvBalance3;
		public TextView tvSum3;
		public TextView tvRatio3;
		public TextView tvCount4;
		public TextView tvBalance4;
		public TextView tvSum4;
		public TextView tvRatio4;
		public TextView tvCount5;
		public TextView tvBalance5;
		public TextView tvSum5;
		public TextView tvRatio5;
		public TextView tvCount6;
		public TextView tvBalance6;
		public TextView tvSum6;
		public TextView tvRatio6;
		public TextView tvCount7;
		public TextView tvBalance7;
		public TextView tvSum7;
		public TextView tvRatio7;
		public TextView tvCount8;
		public TextView tvBalance8;
		public TextView tvSum8;
		public TextView tvRatio8;
		public TextView tvCount9;
		public TextView tvBalance9;
		public TextView tvSum9;
		public TextView tvRatio9;
		public TextView tvCount10;
		public TextView tvBalance10;
		public TextView tvSum10;
		public TextView tvRatio10;
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
					final DoFetchThread doFetch = new DoFetchThread("RP0004", handler,jsonReq);
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
				map.put(SUPERORGID, data.opt(SUPERORGID));
				map.put(SUPERORGNAME, data.opt(SUPERORGNAME));
				map.put(UNITNAME, obj.opt(UNITNAME));
				for (int j = 1; j <= 10; j++) {
					map.put(COUNT + j, data.opt(COUNT + j));
					map.put(RATIO + j, data.opt(RATIO + j));
//					map.put(BALANCE + j, df.format(data.opt(BALANCE + j)));
//					map.put(SUM + j, df.format(data.opt(SUM + j)));
					map.put(BALANCE + j, data.opt(BALANCE + j));
					map.put(SUM + j, data.opt(SUM + j));
				}
				reportItems.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reportItems;
	}

}

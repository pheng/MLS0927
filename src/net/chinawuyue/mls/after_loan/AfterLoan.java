package net.chinawuyue.mls.after_loan;

import java.util.ArrayList;
import java.util.List;

import net.chinawuyue.mls.Constant;
import net.chinawuyue.mls.MyHScrollView;
import net.chinawuyue.mls.MyHScrollView.OnScrollChangedListener;
import net.chinawuyue.mls.R;
import net.chinawuyue.mls.XListView;
import net.chinawuyue.mls.XListView.IXListViewListener;
import net.chinawuyue.mls.login.LoginInfo;
import net.chinawuyue.mls.util.DoFetchThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Handler;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 贷后管理
 * 
 * @author Administrator
 * 
 */
@SuppressLint("ShowToast")
public class AfterLoan implements IXListViewListener {

//	private static final String TAG = "AfterLoan";

	private Context context;
	private View layout = null;
	private List<AfterLoanObject> xItems = null;
	private List<AfterLoanCommonObject> xCommonItems = null;
	private XListView afterLoanList = null;
	private Handler xHandler = null;
	private int kind = Constant.AfterLoanConstan.KIND_FIRST_UNFINISH;
	private MyAdapter xAdapter;
	private MyAdapterCommon xAdapterCommon;

	private AfterLoanRequest xRequest;
	private AfterLoanRequest.ListRequest xListRequest;
	private AfterLoanRequest.ReportRequest xReportRequest;

	private ProgressDialog progressDialog;
	private TextView mTitleText = null;

	private ColorStateList csl;

	private ViewGroup mHead;
	protected boolean isScrollLeft = false;
	private boolean isScrolled = false;

	private LoginInfo loginInfo;
	
	private String customerName;

	public AfterLoan(Context context, LoginInfo loginInfo) {
		this.context = context;
		this.loginInfo = loginInfo;
		Resources resource = (Resources) context.getResources();
		csl = (ColorStateList) resource
				.getColorStateList(R.color.list_title_color);
		xRequest = new AfterLoanRequest();
	}

	public View getAfterLoanView() {
		return layout;
	}

	public void setAfterLoanView(int kind) {
		this.kind = kind;
		xHandler = new Handler();

		if (kind < Constant.AfterLoanConstan.KIND_COMMON) {
			this.xItems = new ArrayList<AfterLoanObject>();
			layout = LayoutInflater.from(context).inflate(
					R.layout.activity_afterloan, null);
			mHead = (ViewGroup) layout.findViewById(R.id.title_list);

			setTextColor(mHead);
		} else {
			this.xCommonItems = new ArrayList<AfterLoanCommonObject>();
			layout = LayoutInflater.from(context).inflate(
					R.layout.activity_affterloan_common, null);
			mHead = (RelativeLayout) layout.findViewById(R.id.title_list);

			setTextColor(mHead);
		}

		mHead.setFocusable(true);
		mHead.setClickable(true);
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		mTitleText = (TextView) layout.findViewById(R.id.title_text);
		setTitle();
		this.afterLoanList = (XListView) layout
				.findViewById(R.id.listview_afterloan);
		this.afterLoanList
				.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		this.afterLoanList
				.setOnItemClickListener(myOnItemClickListener);
		this.afterLoanList.setXListViewListener(this);
		if (kind < Constant.AfterLoanConstan.KIND_COMMON) {
			getDataForAfterLoan("AL0001");
		} else {
			getDataForAfterLoan("AL0002");
		}

	}

	private void setTitle() {
		if (kind == Constant.AfterLoanConstan.KIND_FIRST_UNFINISH) {
			mTitleText.setText("待完成(首次)");
		} else if (kind == Constant.AfterLoanConstan.KIND_FIRST_FINISH) {
			mTitleText.setText("已完成(首次)");
		} else if (kind == Constant.AfterLoanConstan.KIND_FIRST_PAST) {
			mTitleText.setText("过期未完成(首次)");
		} else if (kind == Constant.AfterLoanConstan.KIND_COMMON_FINISH) {
			mTitleText.setText("已完成(常规)");
		} else if (kind == Constant.AfterLoanConstan.KIND_COMMON_PAST) {
			mTitleText.setText("过期未完成(常规)");
		} else if (kind == Constant.AfterLoanConstan.KIND_COMMON_UNFINISH) {
			mTitleText.setText("待完成(常规)");
		}
	}

	/**
	 * 贷后检查（首次、常规）列表查询
	 * 
	 * @return 返回数据列表
	 */
	private void getDataForAfterLoan(String codeNo) {
		// fetch data from service with the request kind
		progressDialog = ProgressDialog.show(context, "", context.getString(R.string.wait), true, true);
		xListRequest = xRequest.new ListRequest();
		xListRequest.setCODENO(codeNo);
		xListRequest.setUSERID(loginInfo.userCode);
		if (kind == Constant.AfterLoanConstan.KIND_FIRST_UNFINISH
				|| kind == Constant.AfterLoanConstan.KIND_COMMON_UNFINISH) {
			xListRequest.setINSPECTTYPE("010");
		} else if (kind == Constant.AfterLoanConstan.KIND_FIRST_FINISH
				|| kind == Constant.AfterLoanConstan.KIND_COMMON_FINISH) {
			xListRequest.setINSPECTTYPE("020");
		} else {
			xListRequest.setINSPECTTYPE("030");
		}

		final DoFetchThread doFetch = new DoFetchThread(xListRequest.getCODENO(), handler, xListRequest.jsonRequest());
		Thread thread = new Thread(doFetch);
		thread.start();
		progressDialog.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				doFetch.stop();
			}
		});
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			if (msg.what == -1) {
				Toast.makeText(context, R.string.net_error, 1).show();
				return;
			}
			String json = msg.obj.toString();
			setAdapter(json);
		};
	};

	private Boolean parseJsonDataFirst(String jsonData) {
		// fetch data from service
		try {
			JSONObject obj = new JSONObject(jsonData);

			String RETURNCODE = obj.optString("RETURNCODE");
			if ((RETURNCODE == null) || (!RETURNCODE.equalsIgnoreCase("N"))) {
				// 请求失败
				return false;
			}

			JSONArray array = obj.optJSONArray("ARRAY1");
			if (xItems != null) {
				xItems.clear();
			}
			if (array == null || array.length() < 1) {
				return true;
			}
			for (int i = 0; i < array.length(); i++) {
				AfterLoanObject loanObj = new AfterLoanObject(array
						.getJSONObject(i).toString());
				xItems.add(loanObj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			// 请求失败
			return false;
		}
		return true;
	}

	private Boolean parseJsonDataCommon(String jsonData) {
		try {
			JSONObject obj = new JSONObject(jsonData);

			String RETURNCODE = obj.optString("RETURNCODE");
			if ((RETURNCODE == null) || (!RETURNCODE.equalsIgnoreCase("N"))) {
				// 请求失败
				return false;
			}

			JSONArray array = obj.optJSONArray("ARRAY1");
			if (xCommonItems != null) {
				xCommonItems.clear();
			}
			if (array == null || array.length() < 1) {
				return true;
			}
			for (int i = 0; i < array.length(); i++) {
				AfterLoanCommonObject loanObj = new AfterLoanCommonObject(array
						.getJSONObject(i).toString());
				xCommonItems.add(loanObj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			// 请求失败
			return false;
		}
		return true;
	}

	private void onLoad() {
		this.afterLoanList.stopRefresh();
		this.afterLoanList.stopLoadMore();
	}

	protected void setAdapter(String jsonData) {
		if (kind < Constant.AfterLoanConstan.KIND_COMMON) {
			Boolean successful = parseJsonDataFirst(jsonData);
			if (!successful) {
				// 查询数据失败，UI显示
				Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
				return;
			}

//			if (this.xItems.size() > Integer.parseInt(context
//					.getString(R.string.xlistview_pullload_limit))) {
//				this.afterLoanList.setPullLoadEnable(true);
//			} else {
//				this.afterLoanList.setPullLoadEnable(false);
//			}
			this.afterLoanList.setPullLoadEnable(false);
			
			xAdapter = new MyAdapter(xItems);
			this.afterLoanList.setAdapter(xAdapter);
			xAdapter.notifyDataSetChanged();
			
			if(this.xItems.size() == 0){
				//查询结果为空
				Toast.makeText(context, R.string.empty, Toast.LENGTH_SHORT).show();
			}
		} else {
			Boolean successful = parseJsonDataCommon(jsonData);
			if (!successful) {
				// 查询数据失败，UI显示
				Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
				return;
			}
			
//			if (this.xCommonItems.size() > Integer.parseInt(context
//					.getString(R.string.xlistview_pullload_limit))) {
//				this.afterLoanList.setPullLoadEnable(true);
//			} else {
//				this.afterLoanList.setPullLoadEnable(false);
//			}
			this.afterLoanList.setPullLoadEnable(false);
			xAdapterCommon = new MyAdapterCommon(xCommonItems);
			this.afterLoanList.setAdapter(xAdapterCommon);
			xAdapterCommon.notifyDataSetChanged();
			
			if(this.xCommonItems.size() == 0){
				//查询结果为空
				Toast.makeText(context, R.string.empty, Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onRefresh() {
		xHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (kind < Constant.AfterLoanConstan.KIND_COMMON) {
					getDataForAfterLoan("AL0001");
				} else {
					getDataForAfterLoan("AL0002");
				}
				onLoad();
			}
		}, 200);
	}

	@Override
	public void onLoadMore() {
		xHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
//				Log.d(TAG, "onLoadMore");
				if (kind < Constant.AfterLoanConstan.KIND_COMMON) {
					getDataForAfterLoan("AL0001");
					// xAdapter = new MyAdapter(xItems);
					// xAdapter.notifyDataSetChanged();
				} else {
					getDataForAfterLoan("AL0002");
					// xAdapterCommon = new MyAdapterCommon(xCommonItems);
					// xAdapterCommon.notifyDataSetChanged();
				}
				onLoad();
			}
		}, 200);
	}

	class MyAdapter extends BaseAdapter {

		List<AfterLoanObject> items = null;

		public MyAdapter(List<AfterLoanObject> items) {
			this.items = items;
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				synchronized (context) {
					holder = new ViewHolder();
					View layout = LayoutInflater.from(context).inflate(
							R.layout.listview_afterloan, null);
					MyHScrollView scrollView1 = (MyHScrollView) layout
							.findViewById(R.id.horizontalScrollView1);
					holder.scrollView = scrollView1;
					MyHScrollView headSrcrollView = (MyHScrollView) mHead
							.findViewById(R.id.horizontalScrollView1);
					headSrcrollView
							.AddOnScrollChangedListener(new OnScrollChangedListenerImp(
									scrollView1));

					holder.tvCustomerID = (TextView) layout
							.findViewById(R.id.customerID);
					holder.tvSerialNO = (TextView) layout
							.findViewById(R.id.serialNO);
					holder.tvCustomerName = (TextView) layout
							.findViewById(R.id.customerName);
					holder.tvBusinessType = (TextView) layout
							.findViewById(R.id.businessType);
					holder.tvBusinessSUM = (TextView) layout
							.findViewById(R.id.businessSUM);
					holder.tvBalance = (TextView) layout
							.findViewById(R.id.balance);
					holder.tvPutoutDate = (TextView) layout
							.findViewById(R.id.putoutDate);
					holder.tvMaturity = (TextView) layout
							.findViewById(R.id.maturity);
					holder.tvInspectDateName = (TextView) layout
							.findViewById(R.id.inspectDateName);
					holder.tvInputUserName = (TextView) layout
							.findViewById(R.id.inputUserName);
					holder.tvInputOrgName = (TextView) layout
							.findViewById(R.id.inputOrgName);
					layout.setTag(holder);
					convertView = layout;
				}
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			AfterLoanObject afterLoanObject = items.get(position);
			holder.tvCustomerID.setText(afterLoanObject.getCustomerID());
			holder.tvSerialNO.setText(afterLoanObject.getSerialNO());
			holder.tvCustomerName.setText(afterLoanObject.getCustomerName());
			holder.tvBusinessType.setText(afterLoanObject.getBusinessType());
			holder.tvBusinessSUM.setText(afterLoanObject.getBusinessSUM()
					.toString());
			holder.tvBalance.setText(afterLoanObject.getBalance().toString());
			holder.tvPutoutDate.setText(afterLoanObject.getPutoutDate());
			holder.tvMaturity.setText(afterLoanObject.getMaturity());
			holder.tvInspectDateName.setText(afterLoanObject
					.getInspectDateName());
			holder.tvInputUserName.setText(afterLoanObject.getInputUserName());
			holder.tvInputOrgName.setText(afterLoanObject.getInputOrgName());
			if (position % 2 == 0) {
				convertView.setBackgroundResource(R.drawable.selector2);
			} else {
				convertView.setBackgroundResource(R.drawable.selector);
			}
			return convertView;
		}

	}

	class ViewHolder {
		private HorizontalScrollView scrollView;
		private TextView tvCustomerID;
		private TextView tvSerialNO;
		private TextView tvCustomerName;
		private TextView tvBusinessType;
		private TextView tvBusinessSUM;
		private TextView tvBalance;
		private TextView tvPutoutDate;
		private TextView tvMaturity;
		private TextView tvInspectDateName;
		private TextView tvInputUserName;
		private TextView tvInputOrgName;
	}

	class MyAdapterCommon extends BaseAdapter {

		List<AfterLoanCommonObject> items = null;

		public MyAdapterCommon(List<AfterLoanCommonObject> items) {
			this.items = items;
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolderCommon holder = null;
			if (convertView == null) {
//				Log.d(TAG, "convertView == null");
				holder = new ViewHolderCommon();
				View layout = LayoutInflater.from(context).inflate(
						R.layout.listview_afterloan_common, null);
				MyHScrollView scrollView1 = (MyHScrollView) layout
						.findViewById(R.id.horizontalScrollView1);
				holder.scrollView = scrollView1;
				MyHScrollView headSrcrollView = (MyHScrollView) mHead
						.findViewById(R.id.horizontalScrollView1);
				headSrcrollView
						.AddOnScrollChangedListener(new OnScrollChangedListenerImp(
								scrollView1));

				holder.tvCustomerID = (TextView) layout
						.findViewById(R.id.customerID);
				holder.tvCustomerName = (TextView) layout
						.findViewById(R.id.customerName);
				holder.tvInspectDate = (TextView) layout
						.findViewById(R.id.inspectDate);
				holder.tvInspectTypeName = (TextView) layout
						.findViewById(R.id.inspectTypeName);
				holder.tvInspectUserID = (TextView) layout
						.findViewById(R.id.inspectUserID);
				holder.tvUpdateDate = (TextView) layout
						.findViewById(R.id.updateDate);
				holder.tvInputUserName = (TextView) layout
						.findViewById(R.id.inputUserName);
				holder.tvInputOrgName = (TextView) layout
						.findViewById(R.id.inputOrgName);
				layout.setTag(holder);
				convertView = layout;
			} else {
				holder = (ViewHolderCommon) convertView.getTag();
			}
			AfterLoanCommonObject afterLoanCommonObject = items.get(position);
			holder.tvCustomerID.setText(afterLoanCommonObject.getCustomerID());
			holder.tvCustomerName.setText(afterLoanCommonObject
					.getCustomerName());
			holder.tvInspectDate
					.setText(afterLoanCommonObject.getInspectDate());
			holder.tvInspectTypeName.setText(afterLoanCommonObject
					.getInspectTypeName());
			holder.tvInspectUserID.setText(afterLoanCommonObject
					.getInspectUserID());
			holder.tvUpdateDate.setText(afterLoanCommonObject.getUpdateDate());
			holder.tvInputUserName.setText(afterLoanCommonObject
					.getInputUserName());
			holder.tvInputOrgName.setText(afterLoanCommonObject
					.getInputOrgName());
			if (position % 2 == 0) {
				convertView.setBackgroundResource(R.drawable.selector2);
			} else {
				convertView.setBackgroundResource(R.drawable.selector);
			}
			return convertView;
		}

	}

	class ViewHolderCommon {
		private HorizontalScrollView scrollView;
		private TextView tvCustomerID;
		private TextView tvCustomerName;
		private TextView tvUpdateDate;
		private TextView tvInspectTypeName;
		private TextView tvInspectDate;
		private TextView tvInspectUserID;
		private TextView tvInputUserName;
		private TextView tvInputOrgName;
	}

	public boolean isScorllLeft() {
		return isScrollLeft;
	}

	private OnItemClickListener myOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if (!isScrolled) {
				// progressDialog = ProgressDialog.s
				// String id;
				// if(kind < Constant.AfterLoanConstan.KIND_COMMON){
				// id = xItems.get(position-1).getCustomerID();
				// }else{
				// id = xCommonItems.get(position-1).getCustomerID();
				// }
				// fetch from service
				// 初始化请求报文
				String oBJECTNO = null;
				String oBJECTTYPE = null;
				if (kind < Constant.AfterLoanConstan.KIND_COMMON) {
					if(xItems == null || xItems.size() < 1){
						return;
					}
					oBJECTNO = xItems.get(position - 1).getIiSerialNo();
					oBJECTTYPE = "010";
					customerName = xItems.get(position - 1).getCustomerName();
				} else {
					if(xCommonItems == null || xCommonItems.size() < 1){
						return;
					}
					oBJECTNO = xCommonItems.get(position - 1).getIiSerialNo();
					oBJECTTYPE = "020";
					customerName = xCommonItems.get(position - 1).getCustomerName();
				}

				xReportRequest = xRequest.new ReportRequest();
				xReportRequest.setOBJECTTYPE(oBJECTTYPE);
				xReportRequest.setOBJECTNO(oBJECTNO);

				progressDialog = ProgressDialog.show(context, "", context.getString(R.string.wait), true, true);
				final DoFetchThread doFectch = new DoFetchThread(
						xReportRequest.getCODENO(), rePortHandler,
						xReportRequest.jsonRequest());
				Thread thread = new Thread(doFectch);
				thread.start();
				progressDialog.setOnCancelListener(new OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						doFectch.stop();
					}
				});
			}
		}
	};

	@SuppressLint("HandlerLeak")
	Handler rePortHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			if (msg.what == -1) {
				Toast.makeText(context, R.string.net_error, 1).show();
				return;
			}
			String json = msg.obj.toString();

			String phaseOption = null;
			String RETURNCODE = null;
			try {
				JSONObject jsonObj = new JSONObject(json);
				RETURNCODE = jsonObj.optString("RETURNCODE");
				phaseOption = jsonObj.optString("PHASEOPINION","");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if ((RETURNCODE != null) && (RETURNCODE.equalsIgnoreCase("N"))) {
				// 查询报告成功，跳转到贷后检查报告界面显示
				Intent intent = new Intent();
				intent.setClass(context, AfterLoanReportActivity.class);
				intent.putExtra("data", phaseOption);
				intent.putExtra("title", customerName + "的检查报告");
				
				context.startActivity(intent);
			} else {
				// 查询报告失败，提示用户查询失败
				Toast.makeText(context, "查询报告失败！", Toast.LENGTH_SHORT).show();
			}
		};
	};

	private double downX,downY,upX,upY;
	
	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			//判断是否滚动过
			if(arg1.getAction()==MotionEvent.ACTION_DOWN){
				downX = arg1.getX();
				downY = arg1.getY();
				isScrolled = false;
			}else if(arg1.getAction()==MotionEvent.ACTION_UP){
				upX = arg1.getX();
				upY = arg1.getY();
				if(Math.abs(downX-upX)>Constant.MAX_SCROLL_DISTANCE||
						Math.abs(downY-upY)>Constant.MAX_SCROLL_DISTANCE){
					isScrolled = true;
				}
			}
			HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView.onTouchEvent(arg1);
			return false;
		}
	}

	class OnScrollChangedListenerImp implements OnScrollChangedListener {
		MyHScrollView mScrollViewArg;

		public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
			mScrollViewArg = scrollViewar;
		}

		@Override
		public void onScrollChanged(int l, int t, int oldl, int oldt) {
			mScrollViewArg.smoothScrollTo(l, t);
			isScrollLeft = l <= 0;
		}
	};

	private void setTextColor(ViewGroup viewGroup) {
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			View view = viewGroup.getChildAt(i);
			if (view instanceof TextView) {
				((TextView) view).setTextColor(csl);
			} else if (view instanceof ViewGroup) {
				setTextColor((ViewGroup) view);
			}
		}
	}
}

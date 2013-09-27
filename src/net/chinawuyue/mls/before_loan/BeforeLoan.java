package net.chinawuyue.mls.before_loan;

import java.io.Serializable;
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
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;
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
 * ��ǰ���
 * 
 */
public class BeforeLoan implements IXListViewListener {

	private static final String TAG = "BeforeLoan";

	private Context context;
	private List<LoanDataObject> xItems = null;
	private View layout = null;
	private XListView loanList = null;
	private TextView mTitleText = null;
	private Handler xHandler = null;
	private MyAdapter xAdapter;
	private int kind = Constant.BeforeLoanConstan.KIND_UNFINISH;

	// for request from service
	private BeforeLoanRequest xRequest;
	// ����ҵ���б�
	private BeforeLoanRequest.LoanListRequest xListRequest;
	// ��������
	private BeforeLoanRequest.LoanDetailRequest xLoanDetReq;

	// ����ˢ�½�����
	private ProgressDialog dialog;

	private ColorStateList csl;

	private RelativeLayout mHead;
	private boolean isScrollLeft = false;
	private boolean isScrolled = false;

	private LoginInfo loginInfo;
	private String OBJECTTYPE = null;
	private String FLOWNO = null;
	private String PHASENO = null;

	public BeforeLoan(Context context, LoginInfo loginInfo) {
		this.context = context;
		this.loginInfo = loginInfo;
		xRequest = new BeforeLoanRequest();

		Resources resource = (Resources) context.getResources();
		csl = (ColorStateList) resource
				.getColorStateList(R.color.list_title_color);
	}

	public View getLoanView() {
		return layout;
	}

	// deal with loan
	/**
	 * ������������ Ĭ������Ϊδ��ɽ��� kind = Constant.BeforeLoanConstan.KIND_UNFINISH
	 */
	public void setLoanView(int kind) {
		this.kind = kind;
		layout = LayoutInflater.from(context).inflate(R.layout.activity_loan,
				null);
		mHead = (RelativeLayout) layout.findViewById(R.id.title_loanlist);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		setTextColor(mHead);

		mTitleText = (TextView) layout.findViewById(R.id.title_text);

		setTitle();
		this.xItems = new ArrayList<LoanDataObject>();

		this.loanList = (XListView) layout.findViewById(R.id.listview_loan);
		xHandler = new Handler();
		this.loanList.setOnItemClickListener(myOnItemClickListener);
		this.loanList.setXListViewListener(this);
		this.loanList
				.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		getDataForLoan(kind);
	}

	// get loan's data
	// fetch "info" from service
	private void getDataForLoan(int kind) {
		xListRequest = xRequest.new LoanListRequest();
		xListRequest.setUSERID(loginInfo.userCode);
		Log.d(TAG, " xListReqest: " + xListRequest.jsonRequest().toString());
		if (kind == Constant.BeforeLoanConstan.KIND_UNFINISH) {
			xListRequest.setAPPROVETYPE("010");
		} else if (kind == Constant.BeforeLoanConstan.KIND_FINISH) {
			xListRequest.setAPPROVETYPE("020");
		} else if (kind == Constant.BeforeLoanConstan.KIND_REJECT) {
			xListRequest.setAPPROVETYPE("030");
		}

		dialog = ProgressDialog.show(context, "", "����ˢ��...");
		Thread thread = new Thread(new DoFetchThread(xListRequest.getCODENO(),
				handler, xListRequest.jsonRequest()));
		thread.start();
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.dismiss();
			}
			if (msg.what == -1) {
				Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
				return;
			}

			setAdapter(parseJsonData(msg.obj.toString()));
		};
	};

	protected void setAdapter(Boolean successful) {
		if (!successful) {
			// ��ѯ����ʧ�ܣ�UI��ʾ
			Toast.makeText(context, "��ѯʧ�ܣ�", Toast.LENGTH_SHORT).show();
			return;
		}
		setTitle();
		if (xItems.size() > Integer.parseInt(context
				.getString(R.string.xlistview_pullload_limit)))
			this.loanList.setPullLoadEnable(true);
		else
			this.loanList.setPullLoadEnable(false);

		xAdapter = new MyAdapter(xItems);
		this.loanList.setAdapter(xAdapter);
		xAdapter.notifyDataSetChanged();
	}

	private void setTitle() {
		if (kind == Constant.BeforeLoanConstan.KIND_UNFINISH) {
			mTitleText.setText("������");
		} else if (kind == Constant.BeforeLoanConstan.KIND_REJECT) {
			mTitleText.setText("���˻�");
		} else if (kind == Constant.BeforeLoanConstan.KIND_FINISH) {
			mTitleText.setText("������");
		}
	}

	private Boolean parseJsonData(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			String RETURNCODE = obj.optString("RETURNCODE");
			if (!RETURNCODE.equalsIgnoreCase("N")) {
				// ����ʧ��
				return false;
			}

			JSONArray array = obj.optJSONArray("ARRAY1");
			if (array == null || array.length() < 1) {
				return true;
			}
			for (int i = 0; i < array.length(); i++) {
				LoanDataObject loanObj = new LoanDataObject(array
						.optJSONObject(i).toString());
				xItems.add(loanObj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			// ����ʧ��
			return false;
		}
		return true;
	}

	private void onLoad() {
		this.loanList.stopRefresh();
		this.loanList.stopLoadMore();
	}

	@Override
	public void onRefresh() {
		xHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (xItems != null) {
					xItems.clear();
				}
				// ��������
				getDataForLoan(kind);

				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		xHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// ��������
				getDataForLoan(kind);

				onLoad();
			}
		}, 2000);
	}

	class MyAdapter extends BaseAdapter {

		List<LoanDataObject> items = null;

		public MyAdapter(List<LoanDataObject> items) {
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
				holder = new ViewHolder();
				View layout = LayoutInflater.from(context).inflate(
						R.layout.listview_loan, null);

				MyHScrollView scrollView1 = (MyHScrollView) layout
						.findViewById(R.id.horizontalScrollView1);
				holder.scrollView = scrollView1;
				MyHScrollView headSrcrollView = (MyHScrollView) mHead
						.findViewById(R.id.horizontalScrollView1);

				headSrcrollView
						.AddOnScrollChangedListener(new OnScrollChangedListenerImp(
								scrollView1));

				holder.tvBusinessSUM = (TextView) layout
						.findViewById(R.id.businessSUM);
				holder.tvBusinessType = (TextView) layout
						.findViewById(R.id.businessType);
				holder.tvCustomerID = (TextView) layout
						.findViewById(R.id.customerID);
				holder.tvCustomerName = (TextView) layout
						.findViewById(R.id.customerName);
				holder.tvInputDate = (TextView) layout
						.findViewById(R.id.inputDate);
				holder.tvManageUserName = (TextView) layout
						.findViewById(R.id.manageUserName);
				holder.tvManageOrgName = (TextView) layout
						.findViewById(R.id.manageOrgName);
				holder.tvObjectType = (TextView) layout
						.findViewById(R.id.objectType);
				holder.tvSerialNO = (TextView) layout
						.findViewById(R.id.serialNO);
				layout.setTag(holder);
				convertView = layout;
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			LoanDataObject loanObject = items.get(position);
			holder.tvBusinessSUM
					.setText(loanObject.getBusinessSUM().toString());
			holder.tvBusinessType.setText(loanObject.getBusinessType());
			holder.tvCustomerID.setText(loanObject.getCustomerID());
			holder.tvCustomerName.setText(loanObject.getCustomerName());
			holder.tvInputDate.setText(loanObject.getInputDate());
			holder.tvManageUserName.setText(loanObject.getManageUserName());
			holder.tvManageOrgName.setText(loanObject.getManageOrgName());
			holder.tvObjectType.setText(loanObject.getObjectType());
			holder.tvSerialNO.setText(loanObject.getSerialNO());
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
		private TextView tvSerialNO;
		private TextView tvCustomerID;
		private TextView tvCustomerName;
		private TextView tvBusinessType;
		private TextView tvBusinessSUM;
		private TextView tvObjectType;
		private TextView tvManageUserName;
		private TextView tvManageOrgName;
		private TextView tvInputDate;
	}

	public boolean isScorllLeft() {
		return isScrollLeft;
	}

	/**
	 * ���ô�����������
	 * 
	 * @param kind
	 *            ������ʾ����������ɣ�����ɣ����˻أ�
	 */
	public void setDataKind(final int kind) {
		this.kind = kind;
		if (xItems != null) {
			xItems.clear();
		}
		xHandler.post(new Runnable() {
			@Override
			public void run() {
				// ��������
				getDataForLoan(kind);

			}
		});
	}

	private OnItemClickListener myOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if (!isScrolled) {
				String serialNO = xItems.get(position - 1).getSerialNO();
				xLoanDetReq = xRequest.new LoanDetailRequest(serialNO);
				OBJECTTYPE = xItems.get(position - 1).getObjectType();
				FLOWNO = xItems.get(position - 1).getFlowNO();
				PHASENO = xItems.get(position - 1).getPhaseNO();

				dialog = ProgressDialog.show(context, "", "����ˢ��...");
				Thread thread = new Thread(new DoFetchThread(
						xLoanDetReq.getCODENO(), loanDetHandler,
						xLoanDetReq.jsonRequest()));
				thread.start();

				// ģ������
				// String info = getDataForDetailInfo(serialNO);
				// Intent intent = new Intent();
				// intent.putExtra("kind", kind);
				// intent.putExtra("info", info);
				// intent.setClass(context, LoanDetailInfoActivity.class);
				// context.startActivity(intent);
			}
			isScrolled = false;
		}
	};

	@SuppressLint("HandlerLeak")
	Handler loanDetHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (dialog != null) {
				dialog.dismiss();
			}
			if (msg.what == -1) {
				Toast.makeText(context, "�������Ӵ���", Toast.LENGTH_SHORT).show();
				return;
			}
			String json = msg.obj.toString();

			String RETURNCODE = null;
			try {
				JSONObject jsonObj = new JSONObject(json);
				RETURNCODE = jsonObj.optString("RETURNCODE");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (RETURNCODE.equalsIgnoreCase("N")) {
				// ��ѯ����ɹ�����ת��������ϸ������ʾ
				Intent intent = new Intent();
				intent.setClass(context, LoanDetailInfoActivity.class);
				intent.putExtra("info", json);
				intent.putExtra("kind", kind);
				intent.putExtra("loginInfo", (Serializable) loginInfo);
				intent.putExtra("PHASENO", PHASENO);
				intent.putExtra("OBJECTTYPE", OBJECTTYPE);
				intent.putExtra("FLOWNO", FLOWNO);
				context.startActivity(intent);
			} else {
				// ��ѯ����ʧ�ܣ���ʾ�û���ѯʧ��
				Log.d(TAG, "report---RETURNCODE: " + RETURNCODE);
				Toast.makeText(context, "��ѯ��ϸʧ�ܣ�", Toast.LENGTH_SHORT).show();
			}
		};
	};

	// fetch data detail from service
	protected String getDataForDetailInfo(String serialNO) {
		return serialNO + "";
	}

	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
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
			if (l != oldl)
				isScrolled = true;
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
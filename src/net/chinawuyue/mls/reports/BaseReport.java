package net.chinawuyue.mls.reports;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.chinawuyue.mls.MyHScrollView;
import net.chinawuyue.mls.MyHScrollView.OnScrollChangedListener;
import net.chinawuyue.mls.Constant;
import net.chinawuyue.mls.R;
import net.chinawuyue.mls.XListView;
import net.chinawuyue.mls.board.BoardReport;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * 报表基类
 * 
 */
public abstract class BaseReport implements XListView.IXListViewListener,
		OnItemClickListener {

	// 报表类型
	public static enum ReportType {
		LoanBalance, BusinessSurvey, SubjectBalance, 
		LoanAnalysis1, LoanAnalysis2, LoanAnalysis3,
		LoanRate1, LoanRate2, LoanRate3,LoanRate4
	};

	// 报表字段
	public static final String CODENO = "CODENO";
	public static final String RETURNCODE = "RETURNCODE";
	public static final String ERRORCODE = "ERRORCODE";
	public static final String ERRORMSG = "ERRORMSG";
	public static final String USERID = "USERID";
	public static final String USERNAME = "USERNAME";
	public static final String ORGID = "ORGID";
	public static final String ORGNAME = "ORGNAME";
	public static final String REPORTNO = "REPORTNO";
	public static final String DATE = "DATE";
	public static final String UNITNAME = "UNITNAME";
	public static final String COUNT = "COUNT";
	public static final String BALANCE = "BALANCE";
	public static final String PROJECTNAME = "PROJECTNAME";
	public static final String SUPERORGID = "SUPERORGID";
	public static final String SUPERORGNAME = "SUPERORGNAME";
	public static final String BUSSINESSNAME = "BUSINESSNAME";
	public static final String VOUCHNAME = "VOUCHNAME";
	public static final String INDUSTRYNAME = "INDUSTRYNAME";
	public static final String SUM = "SUM";
	public static final String RATIO = "RATIO";
	public static final String SUBJECTNO = "SUBJECTNO";
	public static final String SUBJECTNAME = "SUBJECTNAME";
	public static final String BUSINESSTYPENAME = "BUSINESSTYPENAME";
	public static final String YESORNO = "YESORNO";
	public static final String QUERYORGID = "QUERYORGID";
	public static final String QUERYORGNAME = "QUERYORGNAME";
	public static final String BOARDNO = "BOARDNO";
	public static final String BOARDTITLE = "BOARDTITLE";
	public static final String BOARDNAME = "BOARDNAME";
	public static final String BOARDDESC = "BOARDDESC";
	public static final String ISNEW = "ISNEW";
	public static final String INPUTDATE = "INPUTDATE";

	public static final int DELAY = 200; // 延迟时间
	private Context context; // 上下文
	protected List<Map<String, Object>> xItems = null;// 报表数据集合
	private View layout = null; // 包含报表的视图
	protected XListView reportList = null; // ListView
	private Handler xHandler = null; // 线程处理
	// protected HorizontalScrollView scrollView = null; //滚动视图
	protected ReportRequest request; // 请求报文
	private ReportType reportType; // 报表类型
	private Class activityClass = ChartActivity.class;// 跳转界面类
	protected ViewGroup mHead; // 表头
	protected boolean isScrollLeft = false;
	protected boolean isScrolled = false;
	protected boolean isNeedUpdate = false;
	public ColorStateList csl;

	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public BaseReport(Context context) {
		this.context = context;
		Resources resource = (Resources) context.getResources();
		csl = (ColorStateList) resource
				.getColorStateList(R.color.list_title_color);
	}

	public void clear() {
		if (xItems != null)
			xItems.clear();
	}

	// 获得视图
	public View getReportView() {
		fetchData();
		return layout;
	}

	// 设置请求报文
	public void setReportRequest(ReportRequest request) {
		this.request = request;
	}

	// 设置报表视图
	public void setReportView(int layoutId) {
		layout = LayoutInflater.from(context).inflate(layoutId, null);
		this.reportList = (XListView) layout.findViewById(R.id.listview_report);
		mHead = (ViewGroup) layout.findViewById(R.id.head);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		setTextColor(mHead);
		// 判断如果是公告则不需要固定列头
		if (!(this instanceof BoardReport)) {
			// 固定表头
			mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
			reportList
					.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		}
		xHandler = new Handler();
		this.reportList.setOnItemClickListener(this);
		this.reportList.setXListViewListener(this);
	}

	private double downX,downY,upX,upY;
	
	/** ListView触摸监听器 */
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
			// 当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
			HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView.onTouchEvent(arg1);
			return false;
		}
	}

	// 设置表头字体颜色
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

	private void onLoad() {
		this.reportList.stopRefresh();
		this.reportList.stopLoadMore();
	}

	@Override
	public void onRefresh() {
		xHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				xItems.clear();
				isNeedUpdate = true;
				fetchData();
				onLoad();
			}
		}, DELAY);
	}

	@Override
	public void onLoadMore() {
		xHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				isNeedUpdate = true;
				fetchData();
				onLoad();
			}
		}, DELAY);
	}

	// 获得数据
	public abstract void fetchData();

	// 设置适配器
	public void setAdapter(BaseAdapter adapter) {
		// 数据如果超过规定数量则启用拉动刷新
		if (adapter.getCount() > Integer.parseInt(context
				.getString(R.string.xlistview_report_limit)))
			this.reportList.setPullLoadEnable(true);
		else
			this.reportList.setPullLoadEnable(false);
		this.reportList.setAdapter(adapter);
	}

	public ListAdapter getAdapter() {
		return reportList.getAdapter();
	}

	/**
	 * 设置跳转的Actiivty
	 * 
	 * @param activityClass
	 */
	public void setActivityClass(Class activityClass) {
		this.activityClass = activityClass;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		// 没有滑动的情况下点击
		if (!isScrolled) {
			intentTo(pos);
		}
	}

	// 跳转到其他界面
	private void intentTo(int pos) {
		Intent intent = new Intent(context, activityClass);
		Bundle bun = new Bundle();
		// 传值 当行数据和报表类型
		if(xItems == null || xItems.size() < 1){
			return;
		}
		bun.putSerializable("map", (Serializable) xItems.get(pos - 1));
		bun.putSerializable("reportType", reportType);
		intent.putExtras(bun);
		context.startActivity(intent);
	}

	// ScrollView是否滚动到最左边
	public boolean isScorllLeft() {
		return isScrollLeft;
	}

	/** 控制滑动视图的监听器 */
	class OnScrollChangedListenerImp implements OnScrollChangedListener {
		MyHScrollView mScrollViewArg;

		public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
			mScrollViewArg = scrollViewar;
		}

		@Override
		public void onScrollChanged(int l, int t, int oldl, int oldt) {
			// 滑动视图
			mScrollViewArg.smoothScrollTo(l, t);
			// 设置是否滑动到最左边
			isScrollLeft = l <= 0;
		}
	};

}

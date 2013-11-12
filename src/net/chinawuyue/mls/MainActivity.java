package net.chinawuyue.mls;

import java.util.ArrayList;
import java.util.List;

import net.chinawuyue.mls.after_loan.AfterLoan;
import net.chinawuyue.mls.before_loan.BeforeLoan;
import net.chinawuyue.mls.board.BoardReport;
import net.chinawuyue.mls.login.LoginInfo;
import net.chinawuyue.mls.reports.BaseReport;
import net.chinawuyue.mls.reports.BaseReport.ReportType;
import net.chinawuyue.mls.reports.ReportSettingDialog;
import net.chinawuyue.mls.sys.ChangePwd;
import net.chinawuyue.mls.todo.Todo;
import net.chinawuyue.mls.util.ActivityUtil;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.MenuDrawer.OnDrawerStateChangeListener;
import net.simonvt.menudrawer.Position;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
//import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

//主界面
public class MainActivity extends SherlockActivity {

//	private static final String TAG = "MainActivity";
	private static final String STATE_ACTIVE_POSITION = "net.chinawuyue.mls.MainActivity.activePosition";
	private MenuDrawer mMenuDrawer; // 滑动菜单
	private MenuAdapter mAdapter;
	private ListView mList;
	private SubMenu submenu = null; // ActionBar的菜单
	private Todo todo = null; // 工作台功能
	private BeforeLoan beforeLoan = null; // 贷前审查功能
	private BaseReport reports = null; // 报表功能
	private BoardReport boardReport = null; // 公告功能
	private ChangePwd changePwd = null; // 修改密码功能
	private AfterLoan afterLoan = null; // 贷后检查功能
	private int currentContent = -1; // 当前显示的内容
	private ReportType reportType = ReportType.LoanBalance;// 报表类型
	private ReportSettingDialog dialog = null; // 报表设置对话框
	private boolean isMenuOpened = false; // 菜单是否打开
	public String COUNT1 = ""; // 未处理的贷款申请审批笔数
	public String COUNT2 = ""; // 待完成贷后检查笔数
	private static Boolean isDevice = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.titlebar_main);

		if (savedInstanceState != null) {
			currentContent = savedInstanceState.getInt(STATE_ACTIVE_POSITION);
		}
		// 初始化MenuDrawer
		mMenuDrawer = MenuDrawer.attach(this,
				Position.fromValue(MenuDrawer.MENU_DRAG_CONTENT));
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);

		List<Object> items = new ArrayList<Object>();
		items.add(new Item(this.getString(R.string.strTodo),
				R.drawable.ic_todo));
		items.add(new Item(this.getString(R.string.strLoan),
				R.drawable.ic_loan));
		items.add(new Item(this.getString(R.string.strReport),
				R.drawable.ic_report));
		items.add(new Item(this.getString(R.string.strAfterLoan),
				R.drawable.ic_afterloan));
		items.add(new Item(this.getString(R.string.strBoard),
				R.drawable.ic_board));
		items.add(new Item(this.getString(R.string.strChangePassword),
				R.drawable.ic_changepassword));
		mList = new ListView(this);
		mAdapter = new MenuAdapter(items);
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(mItemClickListener);
		// 设置菜单
		mMenuDrawer.setMenuView(mList);
		// 设置菜单宽度
		int width = this.getWindowManager().getDefaultDisplay().getWidth();
		mMenuDrawer.setMenuSize((int) (width * 0.6));
		// 设置mMenuDrawer拖动监听
		mMenuDrawer
				.setOnInterceptMoveEventListener(new MenuDrawer.OnInterceptMoveEventListener() {
					@Override
					public boolean isViewDraggable(View v, int dx, int x, int y) {
						ActivityUtil.exitCount = 0;
						// 如果不是ListView，菜单可以拖动
						if (!(v instanceof ListView)) {
							return false;
						}
						// 如果ListView没有滚动到最左边，菜单不能拖动
						if (currentContent == 1 && !beforeLoan.isScorllLeft()) {
							return true;
						}
						if (currentContent == 2 && !reports.isScorllLeft()) {
							return true;
						}
						if (currentContent == 3 && !afterLoan.isScorllLeft()) {
							return true;
						}
						if (currentContent == 4 && !boardReport.isScorllLeft()) {
							return true;
						}
						return v instanceof SeekBar;
					}
				});
		// 给主菜单列表添加监听
		mList.setLongClickable(true);
		mList.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gd.onTouchEvent(event);// 手势处理
			}

		});
		// 获得MenuDrawer的关闭或打开状态
		mMenuDrawer
				.setOnDrawerStateChangeListener(new OnDrawerStateChangeListener() {
					@Override
					public void onDrawerStateChange(int oldState, int newState) {
						if (oldState == MenuDrawer.STATE_CLOSING
								&& newState == MenuDrawer.STATE_CLOSED) {
							isMenuOpened = false;
						}
						if (oldState == MenuDrawer.STATE_OPENING
								&& newState == MenuDrawer.STATE_OPEN) {
							isMenuOpened = true;
						}
					}

					@Override
					public void onDrawerSlide(float openRatio, int offsetPixels) {
					}
				});
		// 优化滑动关闭菜单的效果
		mMenuDrawer.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Menu打开就进行手势处理
				if (isMenuOpened)
					return gd.onTouchEvent(event);
				return false;
			}
		});

		// 获得登录信息
		LoginInfo loginInfo = (LoginInfo)this.getIntent().getSerializableExtra("loginInfo");
		isDevice = this.getIntent().getBooleanExtra("isDevice", false);
		// 初始化 工作台 数据
		COUNT1 = loginInfo.count1;
		COUNT2 = loginInfo.count2;
		todo = new Todo(this, this);
		beforeLoan = new BeforeLoan(this, loginInfo);
		afterLoan = new AfterLoan(this, loginInfo);
		boardReport = new BoardReport(this,loginInfo);
		changePwd = new ChangePwd(this,loginInfo);
		dialog = new ReportSettingDialog(this, mMenuDrawer,loginInfo);
		ActivityUtil.activityList.add(this);
		ActivityUtil.exitCount = 0;
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		// 获得登录信息
		LoginInfo loginInfo = (LoginInfo)this.getIntent().getSerializableExtra("loginInfo");
		Boolean isDevice = this.getIntent().getBooleanExtra("isDevice", false);
		// 初始化 工作台 数据
		COUNT1 = loginInfo.count1;
		COUNT2 = loginInfo.count2;
		if(isDevice){
			setSysView();
			isDevice = false;
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/** 控制主菜单滑动收起效果的手势监听器 */
	GestureDetector gd = new GestureDetector(new OnGestureListener() {
		private static final int MIN_DISTANCE = 10;

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (e1 == null || e2 == null)
				return false;
			if (e1.getX() - e2.getX() > MIN_DISTANCE) {
				if (mMenuDrawer != null) {
					mMenuDrawer.closeMenu();
				}
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1 == null || e2 == null)
				return false;
			if (e1.getX() - e2.getX() > MIN_DISTANCE) {
				if (mMenuDrawer != null) {
					mMenuDrawer.closeMenu();
				}
			}
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}
	});

	/** 主菜单选项点击效果的监听器 */
	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0:
				setTodoView();
				showMenu();
				break;
			case 1:
				setLoanView(Constant.BeforeLoanConstan.KIND_UNFINISH);
				break;
			case 2:
				setReportView();
				break;
			case 3:
				setAfterLoanView(Constant.AfterLoanConstan.KIND_FIRST_UNFINISH);
				break;
			case 4:
				setBoardView();
				break;
			case 5:
				setSysView();
				break;
			default:
				break;
			}
			currentContent = position;
			mMenuDrawer.setActiveView(view, position);
			mMenuDrawer.closeMenu();
			ActivityUtil.exitCount = 0;
		}
	};

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_ACTIVE_POSITION, currentContent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == Constant.BeforeLoanConstan.REQUEST_CODE 
				&& resultCode == Constant.BeforeLoanConstan.RESULT_CODE_TRUE){
//			Log.d(TAG, "successful, refresh!");
			beforeLoan.onRefresh();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		submenu = menu.addSubMenu("");
		if(isDevice){
			mMenuDrawer.setActiveView(mList.getChildAt(5), 5);
			setSysView();
			isDevice = false;
		}else{
			mMenuDrawer.setActiveView(mList.getChildAt(0), 0);
			setTodoView();
		}
		return super.onCreateOptionsMenu(menu);
	}

	/** 显示ActionBar菜单 */
	private void showMenu() {
		submenu.clear();
		MenuItem item = submenu.getItem();
		item.setIcon(0);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		switch (currentContent) {
		case 0:
			// 添加工作台ActionBar菜单
			break;
		case 1:
			item.setIcon(R.drawable.ic_menu_view);
			// 添加贷款审核ActionBar菜单
			submenu.add(0, Constant.BeforeLoanConstan.KIND_UNFINISH, 0, "待审批");
			submenu.add(0, Constant.BeforeLoanConstan.KIND_REJECT, 0, "被退回");
			submenu.add(0, Constant.BeforeLoanConstan.KIND_FINISH, 0, "已审批");
			break;
		case 2:
			item.setIcon(R.drawable.ic_menu_view);
			// 添加报表查询ActionBar菜单
			submenu.add(0,Constant.ReportConstants.ITEM1,0,this.getResources().getString(R.string.report_loan_balance));
			submenu.add(0,Constant.ReportConstants.ITEM2,0,this.getResources().getString(R.string.report_business_survey));
			submenu.add(0,Constant.ReportConstants.ITEM3,0,this.getResources().getString(R.string.report_subject_balance));
			submenu.add(0,Constant.ReportConstants.ITEM4,0,this.getResources().getString(R.string.report_loan_analysis1));
			submenu.add(0,Constant.ReportConstants.ITEM5,0,this.getResources().getString(R.string.report_loan_analysis2));
			submenu.add(0,Constant.ReportConstants.ITEM6,0,this.getResources().getString(R.string.report_loan_analysis3));
			submenu.add(0,Constant.ReportConstants.ITEM7,0,this.getResources().getString(R.string.report_loan_rate1));
			submenu.add(0,Constant.ReportConstants.ITEM8,0,this.getResources().getString(R.string.report_loan_rate2));
			submenu.add(0,Constant.ReportConstants.ITEM9,0,this.getResources().getString(R.string.report_loan_rate3));
			submenu.add(0,Constant.ReportConstants.ITEM10,0,this.getResources().getString(R.string.report_loan_rate4));
			submenu.add(0,Constant.ReportConstants.ITEM_SETTING,0,this.getResources().getString(R.string.report_report_setting));
			break;
		case 3:
			item.setIcon(R.drawable.ic_menu_view);
			// 添加贷后审查ActionBar菜单
			submenu.add(0, Constant.AfterLoanConstan.KIND_FIRST_UNFINISH, 0,"待完成(首次)");
			submenu.add(0, Constant.AfterLoanConstan.KIND_FIRST_FINISH, 0,"已完成(首次)");
			submenu.add(0, Constant.AfterLoanConstan.KIND_FIRST_PAST, 0,"过期未完成(首次)");
			submenu.add(0, Constant.AfterLoanConstan.KIND_COMMON_UNFINISH, 0,"待完成(常规)");
			submenu.add(0, Constant.AfterLoanConstan.KIND_COMMON_FINISH, 0,	"已完成(常规)");
			submenu.add(0, Constant.AfterLoanConstan.KIND_COMMON_PAST, 0,"过期未完成(常规)");
			break;
		case 4:
			// 添加系统公告ActionBar菜单
			break;
		default:
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Constant.AfterLoanConstan.KIND_FIRST_UNFINISH:
		case Constant.AfterLoanConstan.KIND_FIRST_FINISH:
		case Constant.AfterLoanConstan.KIND_FIRST_PAST:
		case Constant.AfterLoanConstan.KIND_COMMON_UNFINISH:
		case Constant.AfterLoanConstan.KIND_COMMON_FINISH:
		case Constant.AfterLoanConstan.KIND_COMMON_PAST:
			afterLoan.setAfterLoanView(item.getItemId());
			mMenuDrawer.setContentView(afterLoan.getAfterLoanView());
			break;
		case Constant.BeforeLoanConstan.KIND_FINISH:
		case Constant.BeforeLoanConstan.KIND_REJECT:
		case Constant.BeforeLoanConstan.KIND_UNFINISH:
			beforeLoan.setDataKind(item.getItemId());
			break;
		case android.R.id.home:
			mMenuDrawer.toggleMenu();
			return true;
		case Constant.ReportConstants.ITEM1:
			reportType = ReportType.LoanBalance;
			setReportView();
			break;
		case Constant.ReportConstants.ITEM2:
			reportType = ReportType.BusinessSurvey;
			setReportView();
			break;
		case Constant.ReportConstants.ITEM3:
			reportType = ReportType.SubjectBalance;
			setReportView();
			break;
		case Constant.ReportConstants.ITEM4:
			reportType = ReportType.LoanAnalysis1;
			setReportView();
			break;
		case Constant.ReportConstants.ITEM5:
			reportType = ReportType.LoanAnalysis2;
			setReportView();
			break;
		case Constant.ReportConstants.ITEM6:
			reportType = ReportType.LoanAnalysis3;
			setReportView();
			break;
		case Constant.ReportConstants.ITEM7:
			reportType = ReportType.LoanRate1;
			setReportView();
			break;
		case Constant.ReportConstants.ITEM8:
			reportType = ReportType.LoanRate2;
			setReportView();
			break;
		case Constant.ReportConstants.ITEM9:
			reportType = ReportType.LoanRate3;
			setReportView();
			break;
		case Constant.ReportConstants.ITEM10:
			reportType = ReportType.LoanRate4;
			setReportView();
			break;
		case Constant.ReportConstants.ITEM_SETTING:
			dialog.showDialog();
			break;
		}
		ActivityUtil.exitCount = 0;
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		int drawerState = mMenuDrawer.getDrawerState();
		// 按下返回键
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 如果主菜单关闭就返回主菜单 否则退出程序
			if (drawerState == MenuDrawer.STATE_CLOSED
					|| drawerState == MenuDrawer.STATE_CLOSING) {
				mMenuDrawer.toggleMenu();
				ActivityUtil.exitCount = 0;
			} else {
				ActivityUtil.showExitDialog(MainActivity.this);
			}
		}
		return false;
	};

	private static class Item {
		String mTitle;
		int mIconRes;

		Item(String title, int iconRes) {
			mTitle = title;
			mIconRes = iconRes;
		}
	}

	/** MenuDrawer适配器 */
	private class MenuAdapter extends BaseAdapter {

		private List<Object> mItems;

		MenuAdapter(List<Object> items) {
			mItems = items;
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			return getItem(position) instanceof Item ? 0 : 1;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public boolean isEnabled(int position) {
			return getItem(position) instanceof Item;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			Object item = getItem(position);
			if (v == null) {
				v = getLayoutInflater().inflate(R.layout.menu_row_item, parent,
						false);
			}
			TextView tv = (TextView) v;
			tv.setText(((Item) item).mTitle);
			tv.setCompoundDrawablesWithIntrinsicBounds(((Item) item).mIconRes,
					0, 0, 0);
			v.setTag(R.id.mdActiveViewPosition, position);
			if (position == currentContent) {
				mMenuDrawer.setActiveView(v, position);
			}
			return v;
		}
	}

	// 显示工作台界面
	private void setTodoView() {
		currentContent = 0;
		todo.setTodoView();
		mMenuDrawer.setContentView(todo.getTodoView());
		showMenu();
	}

	// 显示贷款审核界面
	private void setLoanView(int kind) {
		currentContent = 1;
		beforeLoan.setLoanView(kind);
		mMenuDrawer.setContentView(beforeLoan.getLoanView());
		// 通过选择操作显示不同的ActionBar菜单
		showMenu();
	}

	public void setLoanView() {
		mMenuDrawer.setActiveView(mList.getChildAt(1), 1);
		setLoanView(Constant.BeforeLoanConstan.KIND_UNFINISH);
	}

	// 显示查看报表界面
	private void setReportView() {
		currentContent = 2;
		dialog.setReport(reportType);
		// 如果不采用默认值就显示对话框，否则直接显示报表
		if (dialog.getOrgId()==null||"".equals(dialog.getOrgId())) {
			dialog.showDialog();
		} else {
			dialog.showReport();
		}
		this.reports = dialog.getReport();
		// 通过选择操作显示不同的ActionBar菜单
		showMenu();
	}

	// 显示贷后检查界面
	private void setAfterLoanView(int kind) {
		currentContent = 3;
		afterLoan.setAfterLoanView(kind);
		mMenuDrawer.setContentView(afterLoan.getAfterLoanView());
		// 通过选择操作显示不同的ActionBar菜单
		showMenu();
	}

	public void setAfterLoanView() {
		mMenuDrawer.setActiveView(mList.getChildAt(3), 3);
		setAfterLoanView(Constant.AfterLoanConstan.KIND_FIRST_UNFINISH);
	}

	// 显示公告通知界面
	private void setBoardView() {
		currentContent = 4;
		boardReport.setReportView(R.layout.activity_report_board);
		mMenuDrawer.setContentView(boardReport.getReportView());
		showMenu();
	}

	// 显示系统设置界面
	private void setSysView() {
		currentContent = 5;
		changePwd.setChangePwdView();
		mMenuDrawer.setContentView(changePwd.getChangePwdView());
		// 通过选择操作显示不同的ActionBar菜单
		showMenu();
	}

}

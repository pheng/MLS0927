package net.chinawuyue.mls;

import java.util.ArrayList;
import java.util.List;

import net.chinawuyue.mls.after_loan.AfterLoan;
import net.chinawuyue.mls.before_loan.BeforeLoan;
import net.chinawuyue.mls.board.BoardReport;
import net.chinawuyue.mls.login.LoginActivity;
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
import android.os.Bundle;
import android.util.Log;
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

//������
public class MainActivity extends SherlockActivity {

	private static final String TAG = "MainActivity";
	private static final String STATE_ACTIVE_POSITION = "net.chinawuyue.mls.MainActivity.activePosition";
	private static final int ITEM1 = 1; // ѡ��˵���id
	private static final int ITEM2 = 2;
	private static final int ITEM3 = 3;
	private static final int ITEM4 = 4;
	private static final int ITEM5 = 5;
	private static final int ITEM6 = 6;
	private static final int ITEM7 = 7;
	private MenuDrawer mMenuDrawer; // �����˵�
	private MenuAdapter mAdapter;
	private ListView mList;
	private Menu menu = null; // ActionBar�Ĳ˵�
	private Todo todo = null; // ����̨����
	private BeforeLoan beforeLoan = null; // ��ǰ��鹦��
	private BaseReport reports = null; // ��������
	private BoardReport boardReport = null; // ���湦��
	private ChangePwd changePwd = null; // �޸����빦��
	private AfterLoan afterLoan = null; // �����鹦��
	private int currentContent = -1; // ��ǰ��ʾ������
	private ReportType reportType = ReportType.LoanAnalysis1;// ��������
	private ReportSettingDialog dialog = null; // �������öԻ���
	private boolean isMenuOpened = false; // �˵��Ƿ��
	public String COUNT1 = ""; // δ�����Ĵ���������������
	public String COUNT2 = ""; // ����ɴ��������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.titlebar_main);

		if (savedInstanceState != null) {
			currentContent = savedInstanceState.getInt(STATE_ACTIVE_POSITION);
		}
		// ��ʼ��MenuDrawer
		mMenuDrawer = MenuDrawer.attach(this,
				Position.fromValue(MenuDrawer.MENU_DRAG_CONTENT));
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);

		List<Object> items = new ArrayList<Object>();
		items.add(new Item(this.getString(R.string.strTodo),
				R.drawable.ic_action_refresh_dark));
		items.add(new Item(this.getString(R.string.strLoan),
				R.drawable.ic_action_select_all_dark));
		items.add(new Item(this.getString(R.string.strReport),
				R.drawable.ic_action_refresh_dark));
		items.add(new Item(this.getString(R.string.strAfterLoan),
				R.drawable.ic_action_select_all_dark));
		items.add(new Item(this.getString(R.string.strBoard),
				R.drawable.ic_action_refresh_dark));
		items.add(new Item(this.getString(R.string.strChangePassword),
				R.drawable.ic_action_select_all_dark));
		mList = new ListView(this);
		mAdapter = new MenuAdapter(items);
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(mItemClickListener);
		// ���ò˵�
		mMenuDrawer.setMenuView(mList);
		// ���ò˵�����
		int width = this.getWindowManager().getDefaultDisplay().getWidth();
		mMenuDrawer.setMenuSize((int) (width * 0.6));
		// ����mMenuDrawer�϶�����
		mMenuDrawer
				.setOnInterceptMoveEventListener(new MenuDrawer.OnInterceptMoveEventListener() {
					@Override
					public boolean isViewDraggable(View v, int dx, int x, int y) {
						ActivityUtil.exitCount = 0;
						// �������ListView���˵������϶�
						if (!(v instanceof ListView)) {
							return false;
						}
						// ���ListViewû�й���������ߣ��˵������϶�
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
		// �����˵��б����Ӽ���
		mList.setLongClickable(true);
		mList.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gd.onTouchEvent(event);// ���ƴ���
			}

		});
		// ���MenuDrawer�Ĺرջ��״̬
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
		// �Ż������رղ˵���Ч��
		mMenuDrawer.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Menu�򿪾ͽ������ƴ���
				if (isMenuOpened)
					return gd.onTouchEvent(event);
				return false;
			}
		});

		// ��õ�¼��Ϣ
		LoginInfo loginInfo = (LoginInfo)this.getIntent().getSerializableExtra("loginInfo");
		System.out.println("logininfo:"+loginInfo.role+" userinfo:"+loginInfo.orgId);
		// ��ʼ�� ����̨ ����
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
		setTodoView();
	}

	/** �������˵���������Ч�������Ƽ����� */
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

	/** ���˵�ѡ����Ч���ļ����� */
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
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		showMenu();
		return super.onCreateOptionsMenu(menu);
	}

	/** ��ʾActionBar�˵� */
	private void showMenu() {
		menu.clear();
		SubMenu submenu = menu.addSubMenu("");
		MenuItem item = (MenuItem) submenu.getItem();
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		switch (currentContent) {
		case 0:
			// ���ӹ���̨ActionBar�˵�
			break;
		case 1:
			// ���Ӵ������ActionBar�˵�
			item.setIcon(android.R.drawable.ic_menu_view);
			submenu.add(0, Constant.BeforeLoanConstan.KIND_UNFINISH, 0, "������");
			submenu.add(0, Constant.BeforeLoanConstan.KIND_REJECT, 0, "���˻�");
			submenu.add(0, Constant.BeforeLoanConstan.KIND_FINISH, 0, "������");
			// item.setTitle("����");
			break;
		case 2:
			// ���ӱ�����ѯActionBar�˵�
			item.setIcon(android.R.drawable.ic_menu_view);
			submenu.add(0, ITEM1, 0,
					this.getResources().getString(R.string.report_loan_balance));
			submenu.add(
					0,
					ITEM2,
					0,
					this.getResources().getString(
							R.string.report_business_survey));
			submenu.add(
					0,
					ITEM3,
					0,
					this.getResources().getString(
							R.string.report_subject_balance));
			submenu.add(
					0,
					ITEM4,
					0,
					this.getResources().getString(
							R.string.report_loan_analysis1));
			submenu.add(
					0,
					ITEM5,
					0,
					this.getResources().getString(
							R.string.report_loan_analysis2));
			submenu.add(
					0,
					ITEM6,
					0,
					this.getResources().getString(
							R.string.report_loan_analysis3));
			submenu.add(
					0,
					ITEM7,
					0,
					this.getResources().getString(
							R.string.report_report_setting));
			// item.setTitle("��������");
			break;
		case 3:
			// ���Ӵ������ActionBar�˵�
			item.setIcon(android.R.drawable.ic_menu_view);
			submenu.add(0, Constant.AfterLoanConstan.KIND_FIRST_UNFINISH, 0,
					"�����(�״�)");
			submenu.add(0, Constant.AfterLoanConstan.KIND_FIRST_FINISH, 0,
					"�����(�״�)");
			submenu.add(0, Constant.AfterLoanConstan.KIND_FIRST_PAST, 0,
					"����δ���(�״�)");
			// item.setTitle("�״�");
			submenu.add(0, Constant.AfterLoanConstan.KIND_COMMON_UNFINISH, 0,
					"�����(����)");
			submenu.add(0, Constant.AfterLoanConstan.KIND_COMMON_FINISH, 0,
					"�����(����)");
			submenu.add(0, Constant.AfterLoanConstan.KIND_COMMON_PAST, 0,
					"����δ���(����)");

			// SubMenu submenu2 = menu.addSubMenu("����");
			// submenu2.add(1, Constant.AfterLoanConstan.KIND_COMMON_UNFINISH,
			// 1, "�����");
			// submenu2.add(1, Constant.AfterLoanConstan.KIND_COMMON_FINISH, 1,
			// "�����");
			// submenu2.add(1, Constant.AfterLoanConstan.KIND_COMMON_PAST, 1,
			// "����δ���");
			// MenuItem itemAfterLoan2 = (MenuItem) submenu2.getItem();
			// itemAfterLoan2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS |
			// MenuItem.SHOW_AS_ACTION_WITH_TEXT);
			break;
		case 4:
			// ����ϵͳ����ActionBar�˵�
			break;
		default:
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("MenuItem", " " + item.getItemId() + "--" + item.getTitle());
		switch (item.getItemId()) {
		case Constant.AfterLoanConstan.KIND_FIRST_UNFINISH:
		case Constant.AfterLoanConstan.KIND_FIRST_FINISH:
		case Constant.AfterLoanConstan.KIND_FIRST_PAST:
		case Constant.AfterLoanConstan.KIND_COMMON_UNFINISH:
		case Constant.AfterLoanConstan.KIND_COMMON_FINISH:
		case Constant.AfterLoanConstan.KIND_COMMON_PAST:
			Log.d(TAG, "afterloan item is click");
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
		case ITEM1:
			reportType = ReportType.LoanBalance;
			setReportView();
			break;
		case ITEM2:
			reportType = ReportType.BusinessSurvey;
			setReportView();
			break;
		case ITEM3:
			reportType = ReportType.SubjectBalance;
			setReportView();
			break;
		case ITEM4:
			reportType = ReportType.LoanAnalysis1;
			setReportView();
			break;
		case ITEM5:
			reportType = ReportType.LoanAnalysis2;
			setReportView();
			break;
		case ITEM6:
			reportType = ReportType.LoanAnalysis3;
			setReportView();
			break;
		case ITEM7:
			dialog.showDialog();
			break;
		}
		ActivityUtil.exitCount = 0;
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		int drawerState = mMenuDrawer.getDrawerState();
		// ���·��ؼ�
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// ������˵��رվͷ������˵� �����˳�����
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

	/** MenuDrawer������ */
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

	// ��ʾ����̨����
	private void setTodoView() {
		currentContent = 0;
//		if (ActivityUtil.view_Todo == null) {
//			todo.setTodoView();
//			ActivityUtil.view_Todo = todo.getTodoView();
//		}
//		mMenuDrawer.setContentView(ActivityUtil.view_Todo);
		
		todo.setTodoView();
		mMenuDrawer.setContentView(todo.getTodoView());
	}

	// ��ʾ������˽���
	private void setLoanView(int kind) {
		currentContent = 1;
//		if (ActivityUtil.view_BeforeLoan == null) {
//			beforeLoan.setLoanView(kind);
//			ActivityUtil.view_BeforeLoan = beforeLoan.getLoanView();
//		}
//		mMenuDrawer.setContentView(ActivityUtil.view_BeforeLoan);
		
		beforeLoan.setLoanView(kind);
		mMenuDrawer.setContentView(beforeLoan.getLoanView());
		// ͨ��ѡ�������ʾ��ͬ��ActionBar�˵�
		showMenu();
	}

	public void setLoanView() {
		mMenuDrawer.setActiveView(mList.getChildAt(1), 1);
		setLoanView(Constant.BeforeLoanConstan.KIND_UNFINISH);
	}

	// ��ʾ�鿴��������
	private void setReportView() {
		currentContent = 2;
		dialog.setReport(reportType);
		// ���������Ĭ��ֵ����ʾ�Ի��򣬷���ֱ����ʾ����
		if ("".equals(dialog.getOrgId())) {
			dialog.showDialog();
		} else {
			dialog.showReport();
		}
		this.reports = dialog.getReport();
		// ͨ��ѡ�������ʾ��ͬ��ActionBar�˵�
		showMenu();
	}

	// ��ʾ���������
	private void setAfterLoanView(int kind) {
		currentContent = 3;
//		if (ActivityUtil.view_AfterLoan == null) {
//			afterLoan.setAfterLoanView(kind);
//			ActivityUtil.view_AfterLoan = afterLoan.getAfterLoanView();
//		}
//		mMenuDrawer.setContentView(ActivityUtil.view_AfterLoan);
		
		afterLoan.setAfterLoanView(kind);
		mMenuDrawer.setContentView(afterLoan.getAfterLoanView());
		// ͨ��ѡ�������ʾ��ͬ��ActionBar�˵�
		showMenu();
	}

	public void setAfterLoanView() {
		mMenuDrawer.setActiveView(mList.getChildAt(3), 3);
		setAfterLoanView(Constant.AfterLoanConstan.KIND_FIRST_UNFINISH);
	}

	// ��ʾ����֪ͨ����
	private void setBoardView() {
		currentContent = 4;
//		if (ActivityUtil.view_Board == null) {
//			boardReport.setReportView(R.layout.activity_report_board);
//			ActivityUtil.view_Board = boardReport.getReportView();
//		}
//		mMenuDrawer.setContentView(ActivityUtil.view_Board);
		
		boardReport.setReportView(R.layout.activity_report_board);
		mMenuDrawer.setContentView(boardReport.getReportView());
		showMenu();
	}

	// ��ʾϵͳ���ý���
	private void setSysView() {
		currentContent = 5;
//		if (ActivityUtil.view_Sys == null) {
//			changePwd.setChangePwdView();
//			ActivityUtil.view_Sys = changePwd.getChangePwdView();
//		}
//		mMenuDrawer.setContentView(ActivityUtil.view_Sys);
		
		changePwd.setChangePwdView();
		mMenuDrawer.setContentView(changePwd.getChangePwdView());
		// ͨ��ѡ�������ʾ��ͬ��ActionBar�˵�
		showMenu();
	}

}
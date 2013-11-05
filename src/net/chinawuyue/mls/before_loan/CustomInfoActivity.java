package net.chinawuyue.mls.before_loan;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import net.chinawuyue.mls.R;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 贷款客户详情
 * @author Administrator
 *
 */
public class CustomInfoActivity extends SherlockActivity{
	private TextView text_customerid_info = null;
	private TextView text_customername_info = null;
	private TextView text_customertype_info = null;
	private TextView text_certtype_info = null;
	private TextView text_certid_info = null;
	private TextView text_add_info = null;
	private TextView text_tel_info = null;
	private TextView text_industrytype_info = null;
	private TextView text_relativetype_info = null;
	private TextView text_relativetname_info = null;
	private TextView text_manageusername_info = null;
	private TextView text_manageorgname_info = null;
	private TextView text_inputorgname_info = null;
	private TextView text_inputusername_info = null;
	private TextView text_inputdate_info = null;
	
	private String userInfo;
	private LoanCusInfoObject obj;
	
	private static final int ITEM1 = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_listview_more_info);
		
		Intent intent = getIntent();
		userInfo = intent.getStringExtra("userInfo");
		
//		//for test
//		userInfo = testData();
		
		if(userInfo != null && userInfo.length() > 0){
			obj = new LoanCusInfoObject(userInfo);
			inintView();
		}else{
			Toast.makeText(getApplicationContext(), "获取客户详情失败，请稍候再试...", Toast.LENGTH_SHORT).show();
			this.finish();
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		SubMenu mSubMenu = menu.addSubMenu("");
		mSubMenu.add(0, ITEM1, 0, "返回");
		MenuItem item = mSubMenu.getItem();
		item.setIcon(R.drawable.ic_menu_view);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM1:
			CustomInfoActivity.this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void inintView() {
		text_customerid_info = (TextView)this.findViewById(R.id.text_customerid_info);
		text_customername_info = (TextView)this.findViewById(R.id.text_customername_info);
		text_customertype_info = (TextView)this.findViewById(R.id.text_customertype_info);
		text_certtype_info = (TextView)this.findViewById(R.id.text_certtype_info);
		text_certid_info = (TextView)this.findViewById(R.id.text_certid_info);
		text_add_info = (TextView)this.findViewById(R.id.text_add_info);
		text_tel_info = (TextView)this.findViewById(R.id.text_tel_info);
		text_industrytype_info = (TextView)this.findViewById(R.id.text_industrytype_info);
		text_relativetype_info = (TextView)this.findViewById(R.id.text_relativetype_info);
		text_relativetname_info = (TextView)this.findViewById(R.id.text_relativetname_info);
		text_manageusername_info = (TextView)this.findViewById(R.id.text_manageusername_info);
		text_manageorgname_info = (TextView)this.findViewById(R.id.text_manageorgname_info);
		text_inputorgname_info = (TextView)this.findViewById(R.id.text_inputorgname_info);
		text_inputusername_info = (TextView)this.findViewById(R.id.text_inputusername_info);
		text_inputdate_info = (TextView)this.findViewById(R.id.text_inputdate_info);
		
		text_customerid_info.setText(obj.getCustomerID());
		text_customerid_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		text_customername_info.setText(obj.getCustomerName());
		text_customername_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		text_customertype_info.setText(obj.getCustomerType());
		text_customertype_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		text_certtype_info.setText(obj.getCertType());
		text_certtype_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		text_certid_info.setText(obj.getCertID());
		text_certid_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		text_add_info.setText(obj.getAdd());
		text_add_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		text_tel_info.setText(obj.getTel());
		text_tel_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		text_industrytype_info.setText(obj.getIndustryType());
		text_industrytype_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		text_relativetype_info.setText(obj.getRelativeType());
		text_relativetype_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		text_relativetname_info.setText(obj.getRelativeName());
		text_relativetname_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		text_manageusername_info.setText(obj.getManageUserName());
		text_manageusername_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		text_manageorgname_info.setText(obj.getManageOreName());
		text_manageorgname_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		text_inputorgname_info.setText(obj.getInputOrgName());
		text_inputorgname_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		text_inputusername_info.setText(obj.getInputUserName());
		text_inputusername_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		text_inputdate_info.setText(obj.getInputDate());
		text_inputdate_info.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
	
	}
}

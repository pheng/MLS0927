package net.chinawuyue.mls.after_loan;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import net.chinawuyue.mls.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AfterLoanReportActivity extends SherlockActivity{
	
	private TextView textTitle;
	private TextView textContent;
	private static final int ITEM1 = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_afterloan_report);
		Intent intent = getIntent();
		String data = intent.getStringExtra("data");
		
		textTitle = (TextView) findViewById(R.id.text_title);
		textTitle.setText(data);
		
		textContent = (TextView) findViewById(R.id.text_content);
		textContent.setText("主要是对贷款审批后至贷款支付完毕各环节进行一次检查说明。包括合同签订、抵押物登记、重要权证保管、贷款发放操作、信贷资金支付方式、信贷资料整理入档与保管等情况进行检查和说明。\n主要是对贷款审批后至贷款支付完毕各环节进行一次检查说明。包括合同签订、抵（质）押物登记（存单国债的止付）、重要权证保管、贷款发放操作、信贷资金支付方式、信贷资料整理入档与保管等情况进行检查和说明。\n "
				+"    主要是对贷款审批后至贷款支付完毕各环节进行一次检查说明。包括合同签订、抵（质）押物登记（存单国债的止付）、重要权证保管、贷款发放操作、信贷资金支付方式、信贷资料整理入档与保管等情况进行检查和说明。 \n" +
				"     主要是对贷款审批后至贷款支付完毕各环节进行一次     检查说明。\n    包括合同签订、抵（质）押物登记（存单国债的止付）、重要权证保管、贷款发放操作、信贷资金支付方式、信贷资料整理入档与保管等情况进行检查和说明。  ");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		SubMenu mSubMenu = menu.addSubMenu("");
		mSubMenu.add(0, ITEM1, 0, "返回");
		MenuItem item = mSubMenu.getItem();
		item.setIcon(android.R.drawable.ic_menu_view);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM1:
			AfterLoanReportActivity.this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}

package net.chinawuyue.mls.after_loan;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import net.chinawuyue.mls.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 贷后管理检查报告
 * @author Administrator
 *
 */
public class AfterLoanReportActivity extends SherlockActivity{
	
	private TextView textTitle;
	private TextView textContent;
	private static final int ITEM1 = 1;
	
	/**
	 * 报告内容
	 */
	private String data = null;
	
	/**
	 * 报告标题
	 */
	private String title = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_afterloan_report);
		Intent intent = getIntent();
		data = intent.getStringExtra("data");
		title = intent.getStringExtra("title");
		
		textTitle = (TextView) findViewById(R.id.text_title);
		textTitle.setText(title);
		
		textContent = (TextView) findViewById(R.id.text_content);
		textContent.setText(data);
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
			AfterLoanReportActivity.this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}

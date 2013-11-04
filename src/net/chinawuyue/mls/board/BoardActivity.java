package net.chinawuyue.mls.board;

import java.util.Map;

import net.chinawuyue.mls.R;
import net.chinawuyue.mls.reports.BaseReport;
import net.chinawuyue.mls.reports.BaseReport.ReportType;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
/**
 * 公告通知界面
 */
public class BoardActivity extends SherlockActivity {

	private static final int ITEM1 = 1;
	private TextView tvTitle = null;
	private TextView tvTime = null;
	private TextView tvDesc = null;
	private Map<String, Object> map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board_detail);
		//获得报表界面传来的数据和报表类型
		map = (Map<String, Object>) getIntent().getExtras().get("map");
		if(map!=null)
			initViews();
	}

	private void initViews(){
		tvTitle = (TextView)findViewById(R.id.tvTitle);
		tvTime = (TextView)findViewById(R.id.tvTime);
		tvDesc = (TextView)findViewById(R.id.tvDesc);
		tvTitle.setText("标题："+map.get(BaseReport.BOARDTITLE).toString());
		tvTime.setText("发布时间："+map.get(BaseReport.INPUTDATE).toString());
		tvDesc.setText(map.get(BaseReport.BOARDDESC).toString());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu subMenu1 = menu.addSubMenu("");
		//根据报表不同添加不同的菜单
		subMenu1.add(0, ITEM1, 0, R.string.back);
		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setIcon(R.drawable.ic_menu_view);
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case ITEM1:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}

package net.chinawuyue.mls.undotask;

import net.chinawuyue.mls.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailActivity extends Activity {

	private TextView txt_title;
	private TextView txt_content;
	private LinearLayout linearLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println("---detail-");
		setContentView(R.layout.activity_detail);
		
		txt_content = (TextView) findViewById(R.id.txt_content);
		txt_title = (TextView) findViewById(R.id.txt_title);
		linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
		
		@SuppressWarnings("deprecation")
		int height = getWindowManager().getDefaultDisplay().getHeight();
		LayoutParams lp =linearLayout.getLayoutParams();			
		lp.height = (int)(height * 0.5);			
		linearLayout.setLayoutParams(lp);
//		txt_content.setHeight((int)(height * 0.5));
		
		Intent intent = getIntent();
		String time = intent.getStringExtra("time");
		String content = intent.getStringExtra("content");
		txt_content.setText(content);
		txt_title.setText(time);
	}
}

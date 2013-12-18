package net.chinawuyue.mls.undotask;

import net.chinawuyue.mls.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DetailActivity extends Activity {

	private TextView txt_title;
	private TextView txt_content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println("---detail-");
		setContentView(R.layout.activity_detail);
		
		txt_content = (TextView) findViewById(R.id.txt_content);
		txt_title = (TextView) findViewById(R.id.txt_title);
		Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		String content = intent.getStringExtra("content");
		System.out.println("title: " + title);
		System.out.println("content: " + content);
		txt_title.setVisibility(View.GONE);
		txt_content.setText(content);
		txt_title.setText(title);
	}
}

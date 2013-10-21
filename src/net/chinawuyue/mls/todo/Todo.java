package net.chinawuyue.mls.todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.chinawuyue.mls.Constant;
import net.chinawuyue.mls.MainActivity;
import net.chinawuyue.mls.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
/**
 * 我的工作台
 *
 */
public class Todo{

	private Context context = null;
	private ListView todoList = null;
	private View layout = null;
	
	private MainActivity mainActivity;
	
	public Todo(Context context) {
		this.context = context;
	}
	
	public Todo(Context context, MainActivity mainActivity) {
		this.context = context;
		this.mainActivity = mainActivity;
	}
	
	public View getTodoView(){
		return layout;
	}
	
	// to do list
	public void setTodoView() {
		layout = LayoutInflater.from(context).inflate(R.layout.activity_todo, null);
		this.todoList = (ListView) layout.findViewById(R.id.listview_todo);
		SimpleAdapter adapter = new SimpleAdapter(this.context, getDataForTodo(),
				R.layout.listview_todo, new String[] { "title", "info" },
				new int[] { R.id.title, R.id.info });
		this.todoList.setAdapter(adapter);
		this.todoList.setOnItemClickListener(mItemClickListener);
	}
	
	// get todolist's data
	// fetch "info" from service
	private List<Map<String, Object>> getDataForTodo() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "待处理的贷款申请");
		map.put("info", mainActivity.COUNT1);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "待完成的贷后检查");
		map.put("info", mainActivity.COUNT2);
		list.add(map);
		return list;
	}
	
	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0:
				mainActivity.setLoanView();
				break;
			case 1:
				mainActivity.setAfterLoanView();
				break;
			default:
				break;
			}
		}
	};
}

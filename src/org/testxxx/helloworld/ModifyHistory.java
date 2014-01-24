package org.testxxx.helloworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class ModifyHistory extends CommActivity {

	// ����һ��List���ϣ�List���ϵ�Ԫ����Map
	private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	private SimpleAdapter simpleAdapter;
	private String mantis_num;

	private String[] modifys = new String[]
			{ "modify1", "modify2", "modify3", "modify4"};
	private String[] modify_persons = new String[]
			{ "person1", "person1", "person2", "person2"};
	private String[] modify_times = new String[]
			{ "2014-01-14 10:00:00", "2014-01-13 10:00:00"
				,  "2014-01-12 10:00:00",  "2014-01-11 10:00:00"};
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_history);
		Intent intt = getIntent();
		mantis_num = intt.getStringExtra("mantis_num");
		initModifyHistory(intt);
	}
	
	private void initModifyHistory(Intent intt){
		Map<String, Object> listItem = new HashMap<String, Object>();
		@SuppressWarnings("unchecked")
		ArrayList <HashMap<String, String>> list_intt = (ArrayList<HashMap<String, String>>) intt.getSerializableExtra("list");
		
		listItem.put("modify_time", "ʱ��");
		listItem.put("modify_content", "�������");
		listItem.put("modify_person", "������");
		listItems.add(listItem);
		// ����һ��SimpleAdapter
		simpleAdapter = new SimpleAdapter(this, listItems,
			R.layout.modify_item, 
			new String[] { "modify_time", "modify_content", "modify_person"},
			new int[] {  R.id.modify_time, R.id.modify_content, R.id.modify_person });
		ListView list = (ListView) findViewById(R.id.modify_list);
		// ΪListView����Adapter
		list.setAdapter(simpleAdapter);
		// ΪListView���б�����¼����¼�������
		list.setOnItemClickListener(new OnItemClickListener()
		{
			// ��position�����ʱ�����÷�����
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				System.out.println(modifys[position]
					+ "��������");
			}
		});
		list.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			// ��position�ѡ��ʱ�����÷�����
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				System.out.println(modifys[position]
						+ "��ѡ����");
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
		// ��Ӳ�������
		addTomodifyList(list_intt);
	}
	
	public void addTomodifyList(ArrayList <HashMap<String, String>>  list){

		HashMap<String, String> data_item;
		if(null != list && !list.isEmpty()){
			Iterator<HashMap<String, String>> it = list.iterator();
			while(it.hasNext()){
				data_item = it.next();
				Map<String, Object> listItem = new HashMap<String, Object>();
				listItem.put("modify_content", data_item.get("modify_detail"));
				listItem.put("modify_person", data_item.get("reporter_name"));
				listItem.put("modify_time", data_item.get("last_modified"));
				listItems.add(listItem);
			}
		}
	}
}

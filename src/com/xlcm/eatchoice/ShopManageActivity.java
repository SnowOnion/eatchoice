package com.xlcm.eatchoice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ShopManageActivity extends Activity{	
	
	private Button btnAdd;
	private EditText inputName;
	private CheckBox checkLunch,checkDinner,checkDrink;
	private ListView list;
	private SQLiteDatabase db = null;
	private HashMap<String,Integer> shops = null;
	private int selectedid = -1;
	private String selectedstr = "";
	private boolean mode_add = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_manage);
		btnAdd = (Button)findViewById(R.id.btnAdd);
		inputName = (EditText)findViewById(R.id.inputName);
		checkLunch = (CheckBox)findViewById(R.id.checklunch);
		checkDinner = (CheckBox)findViewById(R.id.checkdinner);
		checkDrink = (CheckBox)findViewById(R.id.checkdrink);
		list = (ListView)findViewById(R.id.manage_list);
		list.setOnItemClickListener(new ListItemListener());
		shops = new HashMap<String,Integer>();
		db = openOrCreateDatabase("shops", MODE_PRIVATE,null);	
		db.execSQL("create table if not exists shops ('_id' Integer primary key autoincrement,'name' varchar(20) not null ,'lunch' int,'dinner' int,'drink' int)");
		btnAdd.setOnClickListener(new AddListener());
		loadData();
	}
	
	@SuppressLint("NewApi")
	public void loadData(){
		Cursor c = db.query("shops", null, null, null, null, null,null,null);
		SimpleCursorAdapter cAdapter = new SimpleCursorAdapter(ShopManageActivity.this, R.layout.manage_list_item, c, new String[]{"name"}, new int[]{R.id.manage_shop_name}, 0);
		list.setAdapter(cAdapter);
		if(c.moveToFirst()){
			do{
				shops.put(c.getString(c.getColumnIndex("name")) , c.getInt(c.getColumnIndex("_id")));
			}while(c.moveToNext());
		}
		Iterator it1 = shops.keySet().iterator();
		Iterator it2 = shops.values().iterator();
		while(it1.hasNext() && it2.hasNext()){
			System.out.println(it1.next() + "," + it2.next());
		}
	}

	class AddListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(db != null){
				if(mode_add){
					String sName = inputName.getText().toString();
					if(sName == null || sName.equals("")){
						return;
					}
					boolean c1,c2,c3;
					c1 = checkLunch.isChecked();
					c2 = checkDinner.isChecked();
					c3 = checkDrink.isChecked();
					String sql = "insert into shops values (NULL,'" + sName + "'," + (c1 == true ? 1 : 0) + "," + (c2 == true ? 1 : 0) + "," + (c3 == true ? 1 : 0) +")";
					Log.i("sql insert",sql);
					db.execSQL(sql);
					inputName.setText("");
					loadData();
				}
				else{
					int realid = shops.get(selectedstr);
					String sql = "delete from shops where _id = " + realid;
					Log.i("sql delete",sql);
					db.execSQL(sql);
					mode_add = true;
					inputName.setEnabled(true);
					btnAdd.setText("Ìí¼Ó");
					loadData();
				}
			}					
		}		
	}
	
	class ListItemListener implements OnItemClickListener{

		@SuppressLint("NewApi")
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
			if(mode_add){
				mode_add = false;
				inputName.setEnabled(false);
				arg1.setBackground(getResources().getDrawable(R.drawable.selected));
				selectedid = arg2;			
				TextView tv = (TextView)arg1.findViewById(R.id.manage_shop_name);
				selectedstr = tv.getText().toString();
				btnAdd.setText("É¾³ý");	
			}
			else{
				if(arg2 == selectedid){
					mode_add = true;
					inputName.setEnabled(true);
					btnAdd.setText("Ìí¼Ó");
					arg0.getChildAt(selectedid).setBackground(null);
					selectedid = -1;
					selectedstr = "";					
				}
				else{
					mode_add = false;
					inputName.setEnabled(false);
					btnAdd.setText("É¾³ý");
					arg0.getChildAt(selectedid).setBackground(null);
					arg1.setBackground(getResources().getDrawable(R.drawable.selected));
					selectedid = arg2;
					TextView tv = (TextView)arg1.findViewById(R.id.manage_shop_name);
					selectedstr = tv.getText().toString();
				}
				
			}
			
			Log.i("item cliked","arg2 : "+ arg2 + " , arg3 : " + arg3);
		}
		
	}
	
	class ModeListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(!inputName.isEnabled()){
				inputName.setEnabled(true);
				btnAdd.setText("Ìí¼Ó");
			}
		}
		
	}
}

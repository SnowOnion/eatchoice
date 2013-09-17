package com.xlcm.eatchoice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.xlcm.eatchoice.ShakeDetector.OnShakeListener;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity {

	private ListView list;
	private SQLiteDatabase db = null;
	private HashMap<String,Integer> shops = null;
	private ArrayList<HashMap<String,String>> randomShops = null;
	private ShakeDetector detector = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		list = (ListView)findViewById(R.id.choicelist);
		shops = new HashMap<String,Integer>();
		detector = new ShakeDetector(MainActivity.this);
		detector.registerOnShakeListener(new ShakeListener());
		detector.start();
		db = openOrCreateDatabase("shops", MODE_PRIVATE,null);	
		db.execSQL("create table if not exists shops ('_id' Integer primary key autoincrement,'name' varchar(20) not null ,'lunch' int,'dinner' int,'drink' int)");
		loadData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		menu.removeGroup(0);
		menu.add(0,1,1,"µÍ∆Ãπ‹¿Ì");
		menu.add(0,2,2,"ÕÀ≥ˆ");
		return true;
	}
	
	@SuppressLint("NewApi")
	public void loadData(){
		Cursor c = db.query("shops", null, null, null, null, null,null,null);		
		SimpleCursorAdapter cAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.manage_list_item, c, new String[]{"name"}, new int[]{R.id.manage_shop_name}, 0);
		list.setAdapter(cAdapter);
		if(c.moveToFirst()){
			while(c.moveToNext()){
				shops.put( c.getString(c.getColumnIndex("name")) , c.getInt(c.getColumnIndex("_id")));
			}
		}
	}
	
	@SuppressLint("NewApi")
	public void loadRandomData(){
		Set<String> keys = shops.keySet();
		String[] keyArr = new String[keys.size()];
		Iterator<String> it = keys.iterator();
		int j =0 ;
		do{
			keyArr[j++] = it.next();
		}while(it.hasNext());		
		for (String s : keyArr) {
			System.out.println("inkeyarr: "+s);
		}
		int[] ran = NRandom.getSequence(keys.size() );
		for (int k : ran) {
			System.out.println("random set: "+k);
		}
		for(int i=0;i<keys.size();i++){
			HashMap<String,String> map = new HashMap<String, String>();
			map.put("name",keyArr[ran[i]]);
			randomShops.add(map);
			System.out.println("putting :" + keyArr[ran[i]]);
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == 1){
			Intent it = new Intent();
			it.setClass(MainActivity.this, ShopManageActivity.class);
			MainActivity.this.startActivity(it);
		}
		else if(item.getItemId() == 2){
			finish();
		}
		else{
			
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		detector.start();
		loadData();
	}
	
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		detector.stop();
		super.onPause();
		
	}



	class ShakeListener implements OnShakeListener{

		@Override
		public void onShake() {
			// TODO Auto-generated method stub
			System.out.println("shaked!");
			randomShops = new ArrayList<HashMap<String,String>>();
			loadRandomData();
			SimpleAdapter sAdapter = new SimpleAdapter(MainActivity.this, randomShops, R.layout.manage_list_item, new String[]{"name"}, new int[]{R.id.manage_shop_name});
			list.setAdapter(sAdapter);
		}
		
	}
	
}

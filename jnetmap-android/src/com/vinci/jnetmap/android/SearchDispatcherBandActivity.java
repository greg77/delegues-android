package com.vinci.jnetmap.android;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import com.vinci.jnetmap.android.adapter.EntryAdapter;
import com.vinci.jnetmap.android.adapter.EntryItem;
import com.vinci.jnetmap.android.adapter.Item;
import com.vinci.jnetmap.android.adapter.SectionItem;
import com.vinci.jnetmap.android.model.Dispatcher;
import com.vinci.jnetmap.android.model.Outlet;
import com.vinci.jnetmap.android.model.Vlan;
import com.vinci.jnetmap.android.rest.OnResultListener;
import com.vinci.jnetmap.android.rest.RestClient;
import com.vinci.jnetmap_android.R;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchDispatcherBandActivity extends Activity implements OnClickListener {
	ListView listView;
	Button dispatcherButton;
	Button launchSearch;
	Context mContext;
	TextView textDispatcher;
	EditText editBand;
	EntryAdapter adapter;
	List<Outlet> outlets;
	ArrayList<Item> items = new ArrayList<Item>();
	ArrayList<Dispatcher> dispatchers = new ArrayList<Dispatcher>();
	Dispatcher dispatcherSelected ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		mContext = this;
		setContentView(R.layout.activity_searchdispatcher);
		textDispatcher = (TextView) findViewById(R.id.activity_dispatcher_text_dispatcher);
		dispatcherButton = (Button) findViewById(R.id.activity_dispatcher_button_dispatcher);
		editBand = (EditText) findViewById(R.id.activity_dispatcher_edit);
		launchSearch = (Button) findViewById(R.id.activity_dispatcher_button_lauch);
		launchSearch.setOnClickListener(this);
		dispatcherButton.setOnClickListener(this);

		listView = (ListView) findViewById(R.id.activity_dispatcher_list);
		adapter = new EntryAdapter(mContext, items);        
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(mContext, outlets.get(arg2-1).getNum()+" : "+ outlets.get(arg2-1).getRoom().getName() , Toast.LENGTH_LONG).show();
				
			}
		});
		getDispatchers();
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		getMenuInflater().inflate(R.menu.activity_search, menu);		
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		if (item.getItemId() == R.id.logout){
			SharedPreferences myPrefs = this.getSharedPreferences("user", 0);
			Toast.makeText(mContext,"deco", Toast.LENGTH_LONG).show();
			SharedPreferences.Editor prefsEditor = myPrefs.edit();
			prefsEditor.clear();
			prefsEditor.commit();
			Intent intent = new Intent(mContext,MainActivity.class);
			startActivity(intent);
			finish();
		}
		return true;
	}


	@Override
	public void onClick(View v) {		
		if (v.getId() == R.id.activity_dispatcher_button_dispatcher){
			Toast.makeText(mContext, "ok", Toast.LENGTH_LONG).show();
			chooseDispatcher();
		}
		else if (v.getId() == R.id.activity_dispatcher_button_lauch) {
			String numBand = editBand.getText().toString();			
			launchRequest(dispatcherSelected.getId(),numBand);
		}
	}
	
	
	private void getDispatchers(){
		RestClient.doGet(mContext, "dispatchers", new OnResultListener() {
			
			@Override
			public void onResult(String json) {
				//Toast.makeText(mContext, json, Toast.LENGTH_LONG).show();
				Gson gson = new Gson();
				dispatchers = gson.fromJson(json, new TypeToken<List<Dispatcher>>(){}.getType());
				Toast.makeText(mContext, dispatchers.get(0).getName(), Toast.LENGTH_SHORT).show();
			}
		});
	}


	private void launchRequest(int dispatcherId, String numBand) {
		setProgressBarIndeterminateVisibility(true);
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			//imm.hideSoftInputFromWindow(editLibelleOutput.getWindowToken(), 0);
		RestClient.doGet(mContext, "outletsDisp/"+dispatcherId+"/"+numBand, new OnResultListener() {
			
			@Override
			public void onResult(String json) {
				// TODO Auto-generated method stub
				//Toast.makeText(mContext, json, Toast.LENGTH_LONG).show();
				Log.d("jsonOutletsViaDispatcher", json);
				
				if (json.equals("noOutletFound")){
					listView.setVisibility(View.INVISIBLE);
					Toast.makeText(mContext, "aucune prise trouvée", Toast.LENGTH_LONG).show();
					
				}
				else{
					listView.setVisibility(View.VISIBLE);
					items.clear();
					Gson gson = new Gson();
					outlets = gson.fromJson(json, new TypeToken<List<Outlet>>(){}.getType() );
					items.add(new SectionItem("Prises", R.drawable.outlet_icon));
					for (Outlet outlet : outlets) {
						items.add(new EntryItem("numero", outlet.getNum()));						
					}
					 adapter.notifyDataSetChanged();
				}
				
		        
		        setProgressBarIndeterminateVisibility(false);
				
			}
		});
		
	}
	
	
	private void chooseDispatcher() {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				SearchDispatcherBandActivity.this);

		builder.setTitle("Choisir un Repartiteur - Bandeau");
		
		List<String> listItems = new ArrayList<String>();
		for (Dispatcher disp : dispatchers) {
			listItems.add(disp.getName());
		}
		final CharSequence[] items = listItems
				.toArray(new CharSequence[listItems.size()]);
		builder.setSingleChoiceItems(items, 2,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int which) {
						// TODO Auto-generated method stub

						Toast.makeText(SearchDispatcherBandActivity.this,
								dispatchers.get(which).getName(),
								Toast.LENGTH_SHORT).show();

					}
				});

		builder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int which) {
						// TODO Auto-generated method stub
						Log.d("PLOP", String.valueOf(which));
						ListView lw = ((AlertDialog) dialog)
								.getListView();
						dispatcherSelected = dispatchers.get(lw
								.getCheckedItemPosition());
						textDispatcher.setText(dispatcherSelected.getName());
						

					}
				});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						// Canceled.
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
		
	}


	
	


}

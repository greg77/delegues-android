package com.vinci.jnetmap.android;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.google.gson.Gson;
import com.vinci.jnetmap.android.adapter.EntryAdapter;
import com.vinci.jnetmap.android.adapter.EntryItem;
import com.vinci.jnetmap.android.adapter.Item;
import com.vinci.jnetmap.android.adapter.SectionItem;
import com.vinci.jnetmap.android.model.Outlet;
import com.vinci.jnetmap.android.model.Vlan;
import com.vinci.jnetmap.android.rest.OnResultListener;
import com.vinci.jnetmap.android.rest.RestClient;
import com.vinci.jnetmap_android.R;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SearchActivity extends Activity implements OnClickListener {
	ListView listView;
	Button launchSearch;
	Context mContext;
	EditText editLibelleOutput;
	EntryAdapter adapter;
	ArrayList<Item> items = new ArrayList<Item>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		mContext = this;
		setContentView(R.layout.activity_search);
		launchSearch = (Button) findViewById(R.id.activity_search_button_launch);
		launchSearch.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.activity_search_list_outlet_details);
		adapter = new EntryAdapter(mContext, items);        
        listView.setAdapter(adapter);
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
		if (v.getId() == R.id.activity_search_button_launch){
			editLibelleOutput = (EditText) findViewById(R.id.activity_search_edittext);
			String valueEditText = editLibelleOutput.getText().toString();
			String[] arrayValueEditText = valueEditText.split("-");
			if (arrayValueEditText.length == 3)
				launchRequest(valueEditText);
			else
				Toast.makeText(mContext, "code incorrect", Toast.LENGTH_LONG).show();
		}
	}


	private void launchRequest(String code) {
		setProgressBarIndeterminateVisibility(true);
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editLibelleOutput.getWindowToken(), 0);
		RestClient.doGet(mContext, "code/"+code, new OnResultListener() {
			
			@Override
			public void onResult(String json) {
				// TODO Auto-generated method stub
				if (json.equals("noOutletFound")){
					listView.setVisibility(View.INVISIBLE);
					Toast.makeText(mContext, "aucune prise trouvée", Toast.LENGTH_LONG).show();
				
				}
				else{
					listView.setVisibility(View.VISIBLE);
					items.clear();
					Gson gson = new Gson();
					Outlet currentOutlet = gson.fromJson(json, Outlet.class);
					items.add(new SectionItem("Prise", R.drawable.outlet_icon));
					items.add(new EntryItem("ip", currentOutlet.getPort().getaModule().getaSwitch().getIp()));
					items.add(new EntryItem("numéro", currentOutlet.getNum()));
					items.add(new EntryItem("salle", currentOutlet.getRoom().getName()));
			        items.add(new SectionItem("connectée au Port", R.drawable.port_icon));
			        items.add(new EntryItem("numéro", currentOutlet.getPort().getNum()));
			        items.add(new EntryItem("du bandeau numéro ", currentOutlet.getBand().getNum()));
			        items.add(new EntryItem("du repartiteur ", currentOutlet.getBand().getDispatcher().getName()+" (batiment "+currentOutlet.getBand().getDispatcher().getBuilding().getName()+")"));
			        items.add(new SectionItem("Vlan Non Taggué",R.drawable.vlan_icon));
			        if (currentOutlet.getPort().getVlanUntagged() != null){
			        	items.add(new EntryItem("numéro", currentOutlet.getPort().getVlanUntagged().getNum()));
				        items.add(new EntryItem("nom", currentOutlet.getPort().getVlanUntagged().getName()));
			        }
			        else{
			        	items.add(new EntryItem("Aucun Vlan non taggué", ""));
			        }
			        items.add(new SectionItem("Vlan Taggués",R.drawable.vlan_icon));
			        if (currentOutlet.getPort().getVlansTagged().size() >0){
			        	for (Vlan vlan : currentOutlet.getPort().getVlansTagged()) {
				        	items.add(new EntryItem("numéro (nom) ", vlan.getNum()+"( "+vlan.getName()+ ")"));
						}
			        }
			        else{
			        	items.add(new EntryItem("Aucun Vlan taggué", ""));					
			        }
			        items.add(new SectionItem("connecté au Switch", R.drawable.switch_icon));
			        items.add(new EntryItem("Module", String.valueOf(currentOutlet.getPort().getaModule().getNum())));
			        items.add(new EntryItem("nom", currentOutlet.getPort().getaModule().getaSwitch().getName()));
			        items.add(new EntryItem("répartiteur", currentOutlet.getPort().getaModule().getaSwitch().getDispatcher().getName()));
			        adapter.notifyDataSetChanged();
				}
				
		        
		        setProgressBarIndeterminateVisibility(false);

			}
		});
		
	}


	
	


}

package com.vinci.jnetmap.android;

import com.vinci.jnetmap_android.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity{
	
	Button toCodeActivity;
	Button toDispatcherActivity;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		toCodeActivity = (Button) findViewById(R.id.activity_home_code_button);
		toDispatcherActivity= (Button) findViewById(R.id.activity_home_code_dispatcher);
		
		
		toCodeActivity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
				startActivity(intent);
				
			}
		});
		
		toDispatcherActivity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(HomeActivity.this, SearchDispatcherBandActivity.class);
				startActivity(intent);
				
			}
		});
	}
}

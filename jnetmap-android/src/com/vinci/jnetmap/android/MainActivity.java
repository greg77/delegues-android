package com.vinci.jnetmap.android;

import com.vinci.jnetmap.android.manager.JnetmapManager;
import com.vinci.jnetmap.android.rest.OnResultListener;
import com.vinci.jnetmap.android.rest.RestClient;
import com.vinci.jnetmap_android.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	RelativeLayout relativeLayout;
	EditText usernameField;
	EditText passwordField;
	Button authButton;
	SharedPreferences myPrefs;
	Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);
		relativeLayout = (RelativeLayout) findViewById(R.id.activity_main_content_relative_layout);
		relativeLayout.setVisibility(View.INVISIBLE);		
		mContext = this;		
		myPrefs = this.getSharedPreferences("user", 0);
        
		if (myPrefs.getString("username", null) == null){
			lauchLoginInterface();
		}
		else{
			lauchLoginRequest(myPrefs.getString("username", null), myPrefs.getString("password", null),false);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.activity_main_button_login){
			String username = usernameField.getText().toString();
			String password = passwordField.getText().toString();
			lauchLoginRequest(username,password,true);
		}
		
	}

	private void lauchLoginRequest(final String username, final String password,Boolean fromLogin) {
		setProgressBarIndeterminateVisibility(true);
		JnetmapManager.getInstance().setUsername(username);
		if (fromLogin)			
			JnetmapManager.getInstance().setPasswordFromLoginInterface(password);
		else
			JnetmapManager.getInstance().setPasswordFromRequestInterface(password);
		RestClient.doGet(mContext,"auth", new OnResultListener() {
			
			@Override
			public void onResult(String json) {
				//Toast.makeText(MainActivity.this, json, Toast.LENGTH_LONG).show();
				SharedPreferences.Editor prefsEditor = myPrefs.edit();
				if (json.equals("ok")){
					prefsEditor.putString("username", username);
			        prefsEditor.putString("password", JnetmapManager.getInstance().getPassword());
			        prefsEditor.commit();
			        
			        Intent intent = new Intent(mContext,SearchActivity.class);
			        startActivity(intent);
			        finish();
//			        lauchLoginInterface();
				}
				else if (json.equals("401") || json.equals("403")) {
					Toast.makeText(mContext, "Authentication failed.", Toast.LENGTH_LONG).show();
			        prefsEditor.clear();
					prefsEditor.commit();
					lauchLoginInterface();
				}
				setProgressBarIndeterminateVisibility(false);
		        
			}
		});
		
	}
	
	private void lauchLoginInterface(){
		
		relativeLayout.setVisibility(View.VISIBLE);
		authButton = (Button) findViewById(R.id.activity_main_button_login);
		authButton.setOnClickListener(this);
		usernameField = (EditText) findViewById(R.id.activity_main_edittext_login);
		passwordField = (EditText) findViewById(R.id.activity_main_edittext_password);
	}

}

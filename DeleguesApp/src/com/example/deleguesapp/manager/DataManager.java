package com.example.deleguesapp.manager;

import android.database.sqlite.SQLiteOpenHelper;

import com.example.deleguesapp.model.DaoMaster;
import com.example.deleguesapp.model.User;

public class DataManager {

	static DataManager instance;
	
	private User user;
	
	private DataManager(){
		
	}
	
	
	public static DataManager getInstance() {
		if (instance == null)
			instance = new DataManager();
		
		return instance;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
	

	
}

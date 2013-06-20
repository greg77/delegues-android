package com.example.deleguesapp.manager;

import java.util.ArrayList;

import com.example.deleguesapp.model.DaoMaster;
import com.example.deleguesapp.model.DaoSession;
import com.example.deleguesapp.model.User;
import com.example.deleguesapp.model.UserDao;
import com.example.deleguesapp.model.UserDao.Properties;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class DeleguesManager {
	
	static DeleguesManager instance;
	private SQLiteOpenHelper database;
	private DaoSession session;
	private Context context;
	
	private DeleguesManager(Context context) {
		database = new DaoMaster.DevOpenHelper(context, "deleguesdb", null);
		DaoMaster daomaster = new DaoMaster(database.getWritableDatabase());
		session = daomaster.newSession();
	}
	
	public static DeleguesManager getInstance(Context context){
		if (instance == null)
			instance= new DeleguesManager(context);
		return instance;
	}
	
	public void saveUser(User user){
		UserDao userDao = session.getUserDao();
		userDao.insertOrReplace(user);
		
	}
	
	public ArrayList<User> getListUser(){
		UserDao userDao = session.getUserDao();
		ArrayList<User> listUser = (ArrayList<User>) userDao.queryBuilder().orderAsc(Properties.Name).list();
		return listUser;
	}
	
	public void deleteUser(User user){
		UserDao userDao = session.getUserDao();
		userDao.delete(user);
	}

}

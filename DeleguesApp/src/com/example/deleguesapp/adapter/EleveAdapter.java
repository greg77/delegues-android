package com.example.deleguesapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.deleguesapp.R;
import com.example.deleguesapp.manager.DeleguesManager;
import com.example.deleguesapp.model.DaoMaster;
import com.example.deleguesapp.model.DaoSession;
import com.example.deleguesapp.model.User;
import com.example.deleguesapp.model.UserDao;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.QueryBuilder;

public class EleveAdapter extends BaseAdapter {
	private ArrayList<User> listEleves;
	private boolean listEmpty = true;
	private LayoutInflater inflater;
	private Activity activity;

	public int clearData() {
		int nbEleves = 0;
		for (User eleve : listEleves) {
			eleve.setMoyG(null);
			eleve.setRemarques(null);
			eleve.setAppreciation(null);
			eleve.setEtat(0);
			DeleguesManager.getInstance(activity).saveUser(eleve);
			nbEleves++;
		}
		notifyDataSetChanged();
		return nbEleves;
	}

	public void clearEleveData(User eleve) {

		eleve.setMoyG(null);
		eleve.setRemarques(null);
		eleve.setAppreciation(null);
		eleve.setEtat(0);
		DeleguesManager.getInstance(activity).saveUser(eleve);
		notifyDataSetChanged();
		
	}
	
	public void deleteEleve(User eleve) {
		
		DeleguesManager.getInstance(activity).deleteUser(eleve);
		notifyDataSetChanged();
	}

	public void loadData() {
		listEleves = DeleguesManager.getInstance(activity).getListUser();
		listEmpty = false;
		notifyDataSetChanged();
	}

	public EleveAdapter(Activity context) {
		// TODO Auto-generated constructor stub
		this.activity = context;
		this.inflater = LayoutInflater.from(activity);
		this.loadData();
	}

	public int getCount() {
		if (listEmpty)
			return 0;
		else
			return listEleves.size();
	}

	public Object getItem(int arg0) {
		
		return listEleves.get(arg0);

	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		User eleve = listEleves.get(position);

		if (convertView == null) {
			convertView = (LinearLayout) inflater.inflate(
					R.layout.list_eleves_item, null);
			viewHolder = new ViewHolder();
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.textView1);
			
				viewHolder.image = (ImageView) convertView
					.findViewById(R.id.imageView1);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();

		}
		if (viewHolder != null) {
			viewHolder.textView.setText(eleve.getForname() + " "
					+ eleve.getName());
			System.out.println("ETAT:"+eleve.getEtat());			
			viewHolder.image.setVisibility(View.INVISIBLE);
			if (eleve.getEtat() == 1){
				viewHolder.image.setVisibility(View.VISIBLE);
			}

		}
		return convertView;
	}

	private class ViewHolder {
		TextView textView;
		ImageView image;

	}
}

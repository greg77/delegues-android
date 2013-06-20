package com.example.deleguesapp.adapter;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;

import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deleguesapp.ContactRepertoireActivity;
import com.example.deleguesapp.R;

import com.example.deleguesapp.model.Contact;
import com.example.deleguesapp.model.User;

public class ContactAdapter extends BaseAdapter{
	private ArrayList<String> mItems;
	private ArrayList<Contact> listContacts;
	private boolean listEmpty = true;
	private LayoutInflater inflater;
	private Activity activity;
	

	public ContactAdapter(Activity context) {
		
		this.activity = context;
		this.inflater = LayoutInflater.from(activity);
		this.loadData();
	}
	
	public void loadData(){
		mItems = new ArrayList<String>();
		listContacts = new ArrayList<Contact>();
		ContentResolver connectApp = activity.getContentResolver();		
		Cursor cur = connectApp.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null); 
		
		while (cur.moveToNext()) {
		  if (cur.getCount() > 0) {
			    while (cur.moveToNext()) {
			    	Contact contact = new Contact();
			    	String id = cur.getString(
		                        cur.getColumnIndex(ContactsContract.Contacts._ID));
			    	contact.setId((long)Integer.parseInt(id));
			    	String name = cur.getString(
		                        cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			    	//contact.setNameForname(name);
			    	mItems.add(name);
			    	// recherche de tous les numeros d'un contact
			    	if (Integer.parseInt(cur.getString(
			                   cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
			                Cursor pCur = connectApp.query(
			 		    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
			 		    null, 
			 		    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
			 		    new String[]{id}, null);
			            int iterPhone = 0;
			 	        while (pCur.moveToNext()) {
			 		    // Do something with phones
			 	        	 String number = pCur.getString(pCur.getColumnIndex(Phone.NUMBER));
			                 Log.d("elevenum", number+ " : " + name);
			                 contact.addPhone(number);
			                 iterPhone++;
			 	        } 
			 	        if (iterPhone==1)
			 	        	contact.setPhoneToUse(contact.getPhones().get(0));
			 	        pCur.close();
			 	    }
			    
		 		listContacts.add(contact);
		            }
		 	}
		
		listEmpty = false;
		notifyDataSetChanged();}
		
	}
	public int getCount() {
		if (listEmpty)
			return 0;
		else
			return listContacts.size();
	}

	public Object getItem(int arg0) {
		
		return listContacts.get(arg0);

	}

	public long getItemId(int arg0) {
		
		return arg0;
	}

	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		Contact contact = listContacts.get(position);

		if (convertView == null) {
			convertView = (LinearLayout) inflater.inflate(
					R.layout.list_contacts_item, null);
			viewHolder = new ViewHolder();
			viewHolder.checkbox = (CheckBox) convertView
					.findViewById(R.id.checkBox1);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			
		}
		if (viewHolder != null){
			String displayName= "";
			if (contact.getForname()!=null)
				displayName += contact.getForname();
			if (contact.getName()!=null)
				displayName += contact.getName();
			viewHolder.checkbox.setText(displayName+ " tel:"+ contact.getPhoneToUse().substring(0, 5)+"XXXX");
			viewHolder.checkbox.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		return convertView;
	}

	public class ViewHolder {
		CheckBox checkbox;
		
	}
	
	
}

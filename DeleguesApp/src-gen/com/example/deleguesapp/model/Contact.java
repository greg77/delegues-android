package com.example.deleguesapp.model;

import java.util.ArrayList;

import android.R.bool;

public class Contact implements Comparable<Contact> {
	
	private long id;
	private String forname;
	private String name;
	private boolean checked = false;
	
	private ArrayList<String> phones;
	private String phoneToUse;
	private Boolean selected;
	
	public Contact(long id,String forname, String name,ArrayList<String> phones,String phoneToUse,Boolean selected ){
		this.id = id;
		this.setForname(forname);
		this.setName(name);
		this.phones = new ArrayList<String>(phones);
		this.phoneToUse = phoneToUse;
		this.selected = selected;
	}
	public Contact() {
		
		this.phones = new ArrayList<String>();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}


	public String getPhoneToUse() {
		return phoneToUse;
	}
	public void setPhoneToUse(String phoneToUse) {
		this.phoneToUse = phoneToUse;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public ArrayList<String> getPhones() {
		return phones;
	}
	public void setPhones(ArrayList<String> phones) {
		this.phones = phones;
	}
	public void addPhone(String phone){
		this.phones.add(phone);
	}
	public String getForname() {
		return forname;
	}
	public void setForname(String forname) {
		this.forname = forname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int compareTo(Contact arg0) {
		// TODO Auto-generated method stub
		
		return forname.compareToIgnoreCase(arg0.getForname());
		
	}
	
    public boolean isChecked() {
        return checked;
      }
      public void setChecked(boolean checked) {
        this.checked = checked;
      }
      public String toString() {
        return name ; 
      }
      public void toggleChecked() {
        checked = !checked ;
      }
	


}

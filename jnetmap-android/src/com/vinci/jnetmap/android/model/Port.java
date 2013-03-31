package com.vinci.jnetmap.android.model;

import java.util.ArrayList;



public class Port {

    private int id;
    
    private String num;

    private Modules aModule;
    
    private Outlet outlet;
    
    private Vlan vlanUntagged;

    private ArrayList<Vlan> vlansTagged = new ArrayList<Vlan>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public Modules getaModule() {
		return aModule;
	}

	public void setaModule(Modules aModule) {
		this.aModule = aModule;
	}

	public Outlet getOutlet() {
		return outlet;
	}

	public void setOutlet(Outlet outlet) {
		this.outlet = outlet;
	}

	public Vlan getVlanUntagged() {
		return vlanUntagged;
	}

	public void setVlanUntagged(Vlan vlanUntagged) {
		this.vlanUntagged = vlanUntagged;
	}

	public ArrayList<Vlan> getVlansTagged() {
		return vlansTagged;
	}

	public void setVlansTagged(ArrayList<Vlan> vlansTagged) {
		this.vlansTagged = vlansTagged;
	}


}

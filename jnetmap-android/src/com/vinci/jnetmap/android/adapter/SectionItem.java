package com.vinci.jnetmap.android.adapter;



public class SectionItem implements Item{

	private int imageId;
	private final String title;
	
	public SectionItem(String title, int imageId) {
		this.title = title;
		this.setImageId(imageId);
	}
	
	public String getTitle(){
		return title;
	}
	
	@Override
	public boolean isSection() {
		return true;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

}

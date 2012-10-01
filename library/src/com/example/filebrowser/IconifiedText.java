package com.example.filebrowser;

import android.graphics.drawable.Drawable;

public class IconifiedText implements Comparable<IconifiedText> {

	public String mText = "";
	public Drawable mIcon;
	public boolean mSelectable = true;
	public String mCurrentFilePath = "";
	public boolean isChecked=false;
	public String fileSize;

	public IconifiedText(String text, Drawable bullet) {
		mIcon = bullet;
		mText = text;
	}

	public IconifiedText(String currentFileName, String currentFilePath,String fileSize) {
		this.mCurrentFilePath = currentFilePath;
		mText = currentFileName.replace("/", "");
		this.fileSize = fileSize;
	}

	public boolean isSelectable() {
		return mSelectable;
	}

	public void setSelectable(boolean selectable) {
		mSelectable = selectable;
	}

	@Override
	public int compareTo(IconifiedText other) {
		if (this.mText != null)
			return this.mText.compareTo(other.mText);
		else
			throw new IllegalArgumentException();
	}
}

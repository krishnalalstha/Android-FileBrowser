package com.example.filebrowser;

import java.io.File;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;

public class IconifiedTextView extends LinearLayout {

	private TextView mFileText, mFileSize;
	private ImageView mFileIcon;
	private CheckBox checkBox;
	private Context context;

	public IconifiedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	private void findViews() {
		mFileIcon = (ImageView) findViewById(R.id.fileIcon);
		mFileText = (TextView) findViewById(R.id.fileText);
		mFileSize = (TextView) findViewById(R.id.fileSize);
		checkBox = (CheckBox) findViewById(R.id.checkbox);

	}

	public void setImage(final IconifiedText aIconifiedText,
			boolean showCheckBox) {
		findViews();

		if (aIconifiedText.fileSize != null) {
			if (mFileSize.getVisibility() == View.GONE)
				mFileSize.setVisibility(View.VISIBLE);
			mFileSize.setText(aIconifiedText.fileSize);
			if (showCheckBox) {
				checkBox.setVisibility(View.VISIBLE);
				if (aIconifiedText.isChecked) {
					checkBox.setChecked(true);

				} else {

					checkBox.setChecked(false);
				}
			}

		} else {
			checkBox.setVisibility(View.GONE);
			mFileSize.setVisibility(View.GONE);
		}
		mFileText.setText(aIconifiedText.mText);

		if (aIconifiedText.mCurrentFilePath.equals("")) {
			mFileIcon.setScaleType(ScaleType.CENTER);
			mFileIcon.setImageDrawable(context.getResources().getDrawable(
					R.drawable.folder));
		} else if (aIconifiedText.mCurrentFilePath.equals("one_level_up")) {
			mFileIcon.setScaleType(ScaleType.CENTER);
			mFileIcon.setImageDrawable(context.getResources().getDrawable(
					R.drawable.img_one_level_up));
		} else {

			AQuery aa = new AQuery(context);
			aa.id(mFileIcon).image(new File(aIconifiedText.mCurrentFilePath),
					100);

		}
	}

	public void setText(String words) {
		mFileText.setText(words);
	}

}

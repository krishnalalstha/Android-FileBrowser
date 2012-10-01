package com.chrish.filebrowser;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.filebrowser.FileBrowserActivity;

public class FilebrowserDemoActivity extends Activity {
	private Button btnFileBrowse;
	private TextView txtFilePath;
	public static int REQUEST_FILE_BROWSE = 10001;
	private ArrayList<String> selectedImagePath;
	String TAG;
	String imagefilePath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TAG = this.getClass().getSimpleName();
		btnFileBrowse = (Button) findViewById(R.id.btnFileBrowse);
		txtFilePath=(TextView)findViewById(R.id.txtPath);
		btnFileBrowse.setOnClickListener(onButtonClickListner);
	}

	private OnClickListener onButtonClickListner = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			Intent intent = new Intent(FilebrowserDemoActivity.this,
					FileBrowserActivity.class);
			intent.putExtra(FileBrowserActivity.HOW_MANY_FILE,
					FileBrowserActivity.PICK_MULTIPLE_FILE);
			startActivityForResult(intent, REQUEST_FILE_BROWSE);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK
				& requestCode == REQUEST_FILE_BROWSE) {
			imagefilePath="";
			selectedImagePath = new ArrayList<String>();
			selectedImagePath = data
					.getStringArrayListExtra(FileBrowserActivity.PICK_MULTIPLE_FILE);
			for (String imagePath : selectedImagePath) {
				Log.v(TAG, imagePath);
				imagefilePath+=imagePath+"\n";
				
			}
			txtFilePath.setText(imagefilePath);

		}

	};

}
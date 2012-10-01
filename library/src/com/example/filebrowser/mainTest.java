package com.example.filebrowser;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class mainTest extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button test = (Button) findViewById(R.id.test);
		test.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mainTest.this,
						FileBrowserActivity.class);
				intent.putExtra(FileBrowserActivity.HOW_MANY_FILE,
						FileBrowserActivity.PICK_MULTIPLE_FILE);
				mainTest.this.startActivityForResult(intent, 1);

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK) {
			ArrayList<String> alist= data.getStringArrayListExtra(FileBrowserActivity.PICK_MULTIPLE_FILE);
			for(String path:alist){
				Log.v("path", path);
			}
		}
	}

}

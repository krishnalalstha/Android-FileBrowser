package com.example.filebrowser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FileBrowserActivity extends ListActivity {

	public static String HOW_MANY_FILE;
	public static String PICK_MULTIPLE_FILE = "multi_filePathName";
	public static String PICK_SINGLE_FILE = "single_image_file";
	public ArrayList<String> selectedImageUriList;

	private enum DISPLAYMODE {
		ABSOLUTE, RELATIVE;
	}

	File directory;
	private final DISPLAYMODE displayMode = DISPLAYMODE.RELATIVE;
	private List<IconifiedText> directoryEntries = new ArrayList<IconifiedText>();
	private File currentDirectory = new File("/");
	TextView filepath;
	Button ok;
	private String currentFilePath = "";

	private String[] fileFilterArray;

	private int fileSize = 4096;
	IconifiedTextListAdapter itla;
	private String selectType = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.file_browser_layout);
		selectType = getIntent().getStringExtra(HOW_MANY_FILE) == null ? PICK_SINGLE_FILE
				: getIntent().getStringExtra(HOW_MANY_FILE);

		selectedImageUriList = new ArrayList<String>();
		fileFilterArray = getResources()
				.getStringArray(R.array.fileEndingImage);

		filepath = (TextView) findViewById(R.id.text_imagepath);
		ok = (Button) findViewById(R.id.okbtn);
		ok.setEnabled(false);

		browseToRoot();
		this.setSelection(0);
	}

	private void returnBack() {
		Log.v("sleted no", selectedImageUriList.size() + selectType);
		String file = filepath.getText().toString();

		Intent data = new Intent();
		if (selectType.equals(PICK_SINGLE_FILE)) {
			data.putExtra(PICK_SINGLE_FILE, file);

		} else {
			Log.v("sleted no", selectedImageUriList.size() + "no123");
			data.putStringArrayListExtra(PICK_MULTIPLE_FILE,
					selectedImageUriList);
		}
		setResult(RESULT_OK, data);
		finish();
		selectedImageUriList.clear();
	}

	/**
	 * This function browses to the root-directory of the file-system.
	 */
	private void browseToRoot() {
		browseTo(new File("/"));
	}

	/**
	 * This function browses up one level according to the field:
	 * currentDirectory
	 */
	private void upOneLevel() {
		ok.setEnabled(false);
		this.browseTo(this.currentDirectory.getParentFile());
	}

	private void browseTo(final File aDirectory) {
		// On relative we display the full path in the title.
		// directory=aDirectory;
		ok.setEnabled(false);
		if (this.displayMode == DISPLAYMODE.RELATIVE)
			filepath.setText(aDirectory.getAbsolutePath());
		if (aDirectory.isDirectory()) {
			this.currentDirectory = aDirectory;
			try {
				fill(aDirectory.listFiles());
			} catch (Exception e) {
				browseToRoot();
			}

		}

	}

	private void fill(File[] files) {
		this.directoryEntries.clear();

		this.directoryEntries.add(new IconifiedText(
				getString(R.string.current_dir), "", null));

		if (this.currentDirectory.getParent() != null)
			this.directoryEntries.add(new IconifiedText(
					getString(R.string.up_one_level), "one_level_up", null));

		for (File currentFile : files) {
			String fileSizeName = null;
			if (currentFile.isDirectory()) {
				currentFilePath = "";
				fileSizeName = null;
			} else {
				String fileName = currentFile.getName();
				fileSizeName = byteConversion(currentFile.length());
				if (checkEndsWithInStringArray(fileName, fileFilterArray)) {

					currentFilePath = filepath.getText().toString() + "/"
							+ currentFile.getName();

				}
			}
			switch (this.displayMode) {
			case ABSOLUTE:
				/* On absolute Mode, we show the full path */
				this.directoryEntries.add(new IconifiedText(currentFile
						.getPath(), currentFilePath, fileSizeName));
				break;
			case RELATIVE:
				/*
				 * On relative Mode, we have to cut the current-path at the
				 * beginning
				 */
				int currentPathStringLenght = this.currentDirectory
						.getAbsolutePath().length();
				if (checkEndsWithInStringArray(currentFile.getName(),
						fileFilterArray) || currentFile.isDirectory()) {
					this.directoryEntries.add(new IconifiedText(currentFile
							.getAbsolutePath().substring(
									currentPathStringLenght), currentFilePath,
							fileSizeName));
				}

				break;
			}
		}
		Collections.sort(this.directoryEntries);

		itla = new IconifiedTextListAdapter(this, selectType);
		itla.setListItems(this.directoryEntries);
		this.setListAdapter(itla);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// super.onListItemClick(l, v, position, id);
		int selectionRowID = (int) this.getSelectedItemId();
		selectionRowID = position;
		v.setSelected(true);
		String selectedFileString = this.directoryEntries.get(selectionRowID).mText;
		if (selectedFileString.equals(getString(R.string.current_dir))) {
			// Refresh
			this.browseTo(this.currentDirectory);
		} else if (selectedFileString.equals(getString(R.string.up_one_level))) {
			ok.setEnabled(false);
			this.upOneLevel();
		} else {
			File clickedFile = null;
			switch (this.displayMode) {
			case RELATIVE:
				clickedFile = new File(this.currentDirectory.getAbsolutePath()
						+ "/" + this.directoryEntries.get(selectionRowID).mText);
				break;
			case ABSOLUTE:
				clickedFile = new File(
						this.directoryEntries.get(selectionRowID).mText);
				break;
			}
			if (clickedFile != null)
				this.browseTo(clickedFile);
			if (checkEndsWithInStringArray(clickedFile.getName(),
					fileFilterArray)) {

				Boolean isEnabled = true;
				
				if (itla.mItems.get(position).isChecked) {
					itla.mItems.get(position).isChecked = false;
					if (selectedImageUriList.size() > 0) {
						String ss = itla.mItems.get(position).mCurrentFilePath;
						selectedImageUriList.remove(ss);
					}
				} else {
					selectedImageUriList
							.add(itla.mItems.get(position).mCurrentFilePath);
					itla.mItems.get(position).isChecked = true;
				}
				itla.notifyDataSetChanged();

				long clickedFileSize = clickedFile.length() / 1048576;
				isEnabled = clickedFileSize <= fileSize && clickedFile.isFile() ? true
						: false;
				ok.setEnabled(isEnabled);
				ok.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// back to previous
						returnBack();
					}
				});
			} else {
				ok.setEnabled(false);
			}
		}
	}

	/**
	 * Checks whether checkItsEnd ends with one of the Strings from fileEndings
	 */
	private boolean checkEndsWithInStringArray(String checkItsEnd,
			String[] fileEndings) {

		if (fileEndings.length > 1) {
			for (String aEnd : fileEndings) {
				if (checkItsEnd.endsWith(aEnd))
					return true;
			}
		} else {
			File file = new File(checkItsEnd);
			return !file.isDirectory();
		}
		return false;
	}

	

	public void onBackPressed() {
		if (this.currentDirectory.getParent() != null) {

			ok.setEnabled(false);
			this.browseTo(this.currentDirectory.getParentFile());
		} else
			super.onBackPressed();

	};

	public static String byteConversion(long bytes) {
		int conversionUnit = 1024;
		if (bytes < conversionUnit) {
			return "0KB";
		}
		int exp = (int) (Math.log(bytes) / Math.log(conversionUnit));
		String pre = "KMGTPE".charAt(exp - 1) + "";
		String size = String.format("%.1f %sB",
				bytes / Math.pow(conversionUnit, exp), pre);
		return size;
	}

}
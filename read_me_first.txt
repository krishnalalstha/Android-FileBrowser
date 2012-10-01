how to implemnt this project for sd card browsing

here is the procedure
1)First of all make a new project with the libr ary folder
2)then again create a new project with existing code FileBrowserDemo
3)simple call intent like this

Intent intent = new Intent(FilebrowserDemoActivity.this,
					FileBrowserActivity.class);

			intent.putExtra(FileBrowserActivity.BROWSE_MODE,
					FileBrowserActivity.MULTIPLE);

			startActivityForResult(intent, REQUEST_FILE_BROWSE);


Note specify how multiple or single file path return

for multiple file path 
intent.putExtra(FileBrowserActivity.BROWSE_MODE,
					FileBrowserActivity.MULTIPLE);
for single path return
intent.putExtra(FileBrowserActivity.BROWSE_MODE,
					FileBrowserActivity.SINGLE);

onActivityResult you take data like this



ArraList<String>selectedImagePath = new ArrayList<String>();
selectedImagePath = data.getStringArrayListExtra(FileBrowserActivity.RESULT);




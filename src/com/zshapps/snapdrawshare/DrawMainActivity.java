package com.zshapps.snapdrawshare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DrawMainActivity extends Activity {

	private static final int COLOR_ICON = 0, CHANGE_BRUSH_SIZE = 1, SHARE_BUTTON_PIC = 2;
	
	private Menu mMenu;
	private DrawingView drawView;
	private String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SnapDrawShare/";
	public Boolean fileSaved = false;
	private String filename = "temp", flag = "MainActivity";

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
        	    WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        	    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        setContentView(R.layout.activity_draw_main);
        
        drawView = (DrawingView)findViewById(R.id.drawing);
        
		
        //get the fileName of the taken picture and load it
        Intent intent = getIntent();
        flag = intent.getStringExtra("FLAG");
        
        
        if(flag.equals("SnapMainActivity") || flag.equals("ShareMainActivity")) {
        	
            filename = intent.getStringExtra("picture_name");
            Log.e("intentFilename",filename);
            //Now load this picture and set it as the    
            BitmapFactory.Options options = new BitmapFactory.Options();
    		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    		//drawView.tempBmp = BitmapFactory.decodeFile(dir+"pic.png",options);
    			
    		drawView.tempBmp = BitmapFactory.decodeFile(dir+filename+".png" , options);
    		
    		if(flag.equals("ShareMainActivity")) {
    			fileSaved = true;
    		}
    		
        }
        
        /*if(flag.equals("ShareMainActivity")) {
        	
        	filename = intent.getStringExtra("picture_name");
            Log.e("intentFilename",filename);
            //Now load this picture and set it as the    
            BitmapFactory.Options options = new BitmapFactory.Options();
    		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    		//drawView.tempBmp = BitmapFactory.decodeFile(dir+"pic.png",options);
    			
    		drawView.tempBmp = BitmapFactory.decodeFile(dir+filename+".png" , options);
    		fileSaved = true;

        }*/
        
        if(flag.equals("MainActivity") || flag.equals("ShareMainActivity_back") ) {
        	
        	SharedPreferences prefs = getBaseContext().getSharedPreferences("com.zshapps.snapdrawshare", Context.MODE_PRIVATE);
    		String whitePicIncr = "com.zshapps.snapdrawshare.whitePicIncr";
    		if (!prefs.contains(whitePicIncr)) {
    	    	prefs.edit().putInt(whitePicIncr, 0).commit();
    	    }
    		
        	filename = Integer.toString(prefs.getInt(whitePicIncr, 0));
        	drawView.tempBmp = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        	
        	AlertDialog.Builder fromMainDialog = new AlertDialog.Builder(this);
        	//final AlertDialog fromMainDialog = null;
        	//fromMainDialog.setCancelable(false);
        	
        	CharSequence[] items = {"Load a blank canvas", "Load an existing image"};
        	fromMainDialog.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 0) {
                    	dialog.dismiss();
                    }
                    
                    if(which == 1) {
                    	//Launch ShareMainActivity with the special flag
                    	Intent intent = new Intent(getBaseContext(), ShareMainActivity.class);
    		    	    
    		    	    intent.putExtra("FLAG", "DrawMainActivity_Load");
    		    	    
    		    	    startActivity(intent);
                    }
                }
        	});
        	
        	 fromMainDialog.show();
        }
    }

    private void actuallySaveFile(String path) {
    	OutputStream stream = null;
		try {
			stream = new FileOutputStream(path);
		} catch (FileNotFoundException e) {
			Toast.makeText(getApplicationContext(), "Error creating file stream :(", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		
		drawView.getDrawCache().compress(CompressFormat.PNG, 100, stream);
		drawView.setDrawingCacheEnabled(false);
		
    	Toast.makeText(getApplicationContext(), "Image saved in Pictures/SnapDrawShare", Toast.LENGTH_LONG).show();
    	fileSaved = true;
    	drawView.setChangesMade(false);
    	
    	//Increment whitePicIncr in sharedPrefernes by 1
    	if(flag.equals("MainActivity")) {
    		SharedPreferences prefs = this.getSharedPreferences("com.zshapps.snapdrawshare", Context.MODE_PRIVATE);
    		String whitePicIncr = "com.zshapps.snapdrawshare.whitePicIncr";
    		int currentWhitePicIncr = prefs.getInt(whitePicIncr, 0);
    	    prefs.edit().putInt(whitePicIncr, currentWhitePicIncr+1).commit();
    		
    	}
    	
    }
    
    private void saveFile() throws FileNotFoundException {
    	
    	final String savePath = dir + filename + ".png";
    	File file = new File(savePath);
    	if(file.exists()) {
    		AlertDialog.Builder nameCollision = new AlertDialog.Builder(this);
    		//nameCollision.setTitle("Uh oh");
    		TextView text = new TextView(this);
    		text.setText("A file with this name exists.\nReplace the existing file?");
    		text.setTextSize(18);
    		text.setGravity(Gravity.CENTER);
    		nameCollision.setView(text);
    		nameCollision.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int whichButton) {
    	        	actuallySaveFile(savePath);
    	        }
    	        
    	    });
    		nameCollision.setNegativeButton("No", new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int whichButton) {
    	        	dialog.cancel();
    	        }});
    		
    		nameCollision.show();
    	}
    	
    	else {
    		actuallySaveFile(savePath);
    	}
    }
    
    public void onSaveButton()  {
    	//open a dialog that allows the user to choose a name
    	if(!fileSaved) {
	    	final EditText input = new EditText(this);
	    	
	    	input.setInputType(InputType.TYPE_CLASS_TEXT);
	    	input.setText("My_Pic_"+filename);
	    	input.setSelectAllOnFocus(true);
	
	    	final AlertDialog.Builder saveDialogBuilder = new AlertDialog.Builder(this);
	    	saveDialogBuilder.setTitle("Save Drawing");
	    	saveDialogBuilder.setView(input);
			
	    	saveDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		        	filename = input.getText().toString();
		        	try {
		    			saveFile();
		    		} catch (FileNotFoundException e) {
		    			Toast.makeText(getApplicationContext(), "Error saving image :(", Toast.LENGTH_LONG).show();
		    			e.printStackTrace();
		    		}
		        }
	   		});
	    	
	    	saveDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		        	dialog.cancel();
	        }});
	    	
	    	
			final AlertDialog saveDialog = saveDialogBuilder.create();
			saveDialog.show();
		
			input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
	    	    @Override
	    	    public void onFocusChange(View v, boolean hasFocus) {
	    	        if (hasFocus) {//(input.requestFocus()) {
	    	        	saveDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	    	        }
	    	    }
	    	});
    	}
    	else {
    		//Just save it at dir+filename+".png"
    		actuallySaveFile(dir + filename + ".png");
    	}
    }
    
    public void onColorButton()  {
    	//launch a dialog with color
    	drawView.onErase(false);
    	Intent intent = new Intent(this, DrawColorActivity.class);
    	startActivityForResult(intent, COLOR_ICON);
    
    }
    
    
    public void onShareButton() throws IOException  {
    	
    	//First check with a bool if the currentpic is the most recent saved
    	
    	//Create a tempfile to send
    	Intent sendPic = new Intent(Intent.ACTION_SEND); 
    	sendPic.setType("image/*");
    	//sendPic.setType("*/*");
    	
    	/*File outputDir = getCacheDir(); // context being the Activity pointer
    	File outputFile = File.createTempFile("SnapDrawShare_pic", ".png", outputDir);
    	
    	
    	OutputStream stream = new FileOutputStream(outputFile);
    	
    	//drawView.setDrawingCacheEnabled(true);
    	//ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
    	//drawView.setDrawingCacheEnabled(true);
    	drawView.getDrawCache().compress(CompressFormat.PNG, 100, stream);
		
    	Log.e("temp file path", ""+outputFile.getAbsolutePath());
    	sendPic.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+outputFile.getAbsolutePath()) );
    	
    	*/
    	String savePath = dir + "SnapDrawShare.png";
    	//File file = new File(savePath);
    	OutputStream stream = new FileOutputStream(savePath);
    	
    	drawView.getDrawCache().compress(CompressFormat.PNG, 100, stream);
		drawView.setDrawingCacheEnabled(false);
		
		sendPic.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + savePath));
    	
    	//SET cacheTHING back to true/false
    	startActivityForResult(Intent.createChooser(sendPic, "Send picture"), SHARE_BUTTON_PIC);
    	//drawView.setDrawingCacheEnabled(false);
    	
    }
    
    public void onResizeBrushButton() {
    	//launch a new activity
    	Intent intent = new Intent(this, DrawResizeBrushActivity.class);
    	startActivityForResult(intent, CHANGE_BRUSH_SIZE);
    }
    
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {     
    	 // super.onActivityResult(requestCode, resultCode, data); 
    	if(requestCode == COLOR_ICON && resultCode == RESULT_OK) {
    		int colorIndex = data.getIntExtra("COLOR_INDEX", 0);
  
    		MenuItem colorButton = (MenuItem) mMenu.findItem(R.id.action_color_change);
			colorButton.setIcon(DrawColorActivity.getColor(colorIndex));
			
			int colorNum = data.getIntExtra("COLOR_NUM", 0);
			drawView.setPaintColor(colorNum);
		}
    	
    	else if(requestCode == SHARE_BUTTON_PIC /*&& resultCode == RESULT_OK*/) {
    		//Delete snapdrawshare.png from Pictures/SnapDrawShare
    		String savePath = dir +"SnapDrawShare.png";
        	File file = new File(savePath);
        	file.delete();
		}
	}


    
    @Override
    public void onBackPressed() {
    	if(drawView.getChangesMade()) {
        	
        	//Ask the user if they want to save the file
        	AlertDialog.Builder backDialog = new AlertDialog.Builder(this);
						
			TextView text = (TextView) new TextView(this);
			text.setText("Changes have been made, do you want to save your drawing?");
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER_HORIZONTAL);
			
			backDialog.setView(text);
			backDialog.setTitle("Save File");
			
			backDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		        	onSaveButton();
		        	//Calling finish() causes filesaving to not occur
		        	//finish();
		        }
		        
		    });
			
			backDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		        	
		        	finish();
		        }
	        });
			
			backDialog.show();
			
        }
        
        else {
        	if(flag.equals("ShareMainActivity_back")) {
        		Intent intent = new Intent(this, MainActivity.class);
            	startActivity(intent);
        	}
        	else
        		super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.draw_main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        switch(id) {
	        	
	        case R.id.action_save:
	        	onSaveButton();
	        	break;
	        
	        case R.id.action_color_change:
	        	onColorButton();
	        	break;
	        	
	        case R.id.action_share:
				try {
					onShareButton();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Error sharing file.", Toast.LENGTH_LONG).show();
				}
	        	break;
	        
	        case R.id.action_resize_brush:
	        	onResizeBrushButton();
	        	break;
	        
	        case R.id.action_erase:
	        	drawView.onErase(true);
	        	break;
	        
	        case R.id.action_draw:
	        	drawView.onErase(false);
	        	break;
	        
	        case R.id.action_undo:
	        	drawView.onUndo();
	        	break;
	        
	        case R.id.action_redo:
	        	drawView.onRedo();
	        	break;
	        	
	        case android.R.id.home:
	        	onBackPressed();
	        	return true;
        }
        
      return super.onOptionsItemSelected(item);
    	//return true;
    }

}

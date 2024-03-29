package com.zshapps.snapdrawshare;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SnapMainActivity extends Activity {
	
	private static final int REQUEST_TAKE_PHOTO = 1;
	private SharedPreferences prefs;
	private static String keyStoredDate = "com.zshapps.snapdrawshare.storedDate";
	private static String keyIncr = "com.zshapps.snapdrawshare.incr";
	private int incr = 10;
	private File photoFile = null;
	private String filename = "temp";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snap_main);
				
		SaveData save = new SaveData();
		save.execute();
 
	}
	
	private class SaveData extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
		    // your background code here. Don't touch any UI components
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
	        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
	        	
	            
	        	// Create the File where the photo should go        	        	
	        	try {
	        		photoFile = createImageFile();
	        	} catch (IOException ex) {
	        		Toast.makeText(getApplicationContext(), "Error saving picture from camera", Toast.LENGTH_LONG).show();
	            }
	        	
	        	// Continue only if the File was successfully created
	        	if (photoFile != null) {
	        		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
	        		startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
	        		
	        	}
	        }
			return null;
			
		}

		protected void onPostExecute(Boolean result) {
		     //This is run on the UI thread so you can do as you wish here
		     /*if(result)
		         Toast successful
		     else
		         Toast unsuccessful
		 	*/
		 }
	}
		
	private File createImageFile() throws IOException {
	    // Create an image file name
		
		String curDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		
	    prefs = getApplicationContext().getSharedPreferences("com.zshapps.snapdrawshare", Context.MODE_PRIVATE);
	    
	    if (!prefs.contains(keyStoredDate)) {
	    	prefs.edit().putString(keyStoredDate, curDate).commit();
	    }
	    
	    if (!prefs.contains(keyIncr)) {
	    	prefs.edit().putInt(keyIncr, 0).commit();
	    }
	    incr = prefs.getInt(keyIncr, 0);
	    String storedDate = prefs.getString(keyStoredDate, new SimpleDateFormat("yyyyMMdd").format(new Date()));
	    
	    if(!storedDate.equals(curDate)) {
	    	//set the counter to 0 and change store the value of currDate in the SharedPreferences 
	    	prefs.edit().putInt(keyIncr,0).commit();
	    	prefs.edit().putString(keyStoredDate,curDate).commit();
	    	incr=0;
	    }
	    
	    String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SnapDrawShare/";
    	File newdir = new File(dir);
    	if (!newdir.exists()) {
    		newdir.mkdirs();
    	}
	    
	    filename = curDate + "_" + incr;
	    
	    File image = new File(newdir, filename + ".png" );
	    return image;
	    
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == REQUEST_TAKE_PHOTO) { 
    		if(resultCode == RESULT_OK) {
	        	Toast.makeText(getApplicationContext(), "Picture saved in Pictures/SnapDrawShare", Toast.LENGTH_LONG).show();
	        	
	        	//Increase incr by 1 and save back in SharedPrefs
	    	    prefs.edit().putInt(keyIncr,incr+1).commit();
	    	    
	    	    //Just send the filename 
	    	    Intent intent = new Intent(getApplicationContext(), DrawMainActivity.class);
	    	    intent.putExtra("picture_name", filename);
	    	    intent.putExtra("picture_extension", "png");
	    	    intent.putExtra("FLAG", "SnapMainActivity");	    	    
	    	    startActivity(intent);
	    	    
	    	    finish();	    	    
	    	    
	        }  
    		else
    			/*
    			 *  If the user doesn't want to take picture, stop camera activity
    			 *	Otherwise lagging occurs
    			 */
    			finish();
        }
    }
	
	
}

package com.zshapps.snapdrawshare;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
        
	}
	
	private File createImageFile() throws IOException {
	    // Create an image file name
		
		String curDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		//Maybe put strings in values/strings.xml
		
		
	    prefs = this.getSharedPreferences("com.example.snapdrawshare", Context.MODE_PRIVATE);
	    
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
	    
	    //String imageFileName = curDate + "_" + incr;
	    filename = curDate + "_" + incr;
	    Log.e("IMGNAME", filename);
	    
	    File image = new File(newdir, filename + ".png" );
	    return image;
	    
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == REQUEST_TAKE_PHOTO) { 
    		if(resultCode == RESULT_OK) {
	        	Toast.makeText(getApplicationContext(), "Picture saved in Pictures/SnapDrawShare", Toast.LENGTH_LONG).show();
	        	
	        	//Increase incr by 1 and save back in SharedPrefs
	    	    prefs.edit().putInt(keyIncr,incr+1).commit();
	    	    Log.e("snap after finish", "het");
	    	   
	    	    /*
	    	    //Now send this picture to the draw activity. Launch the draw activity as well.
	    	    //Convert the file to a bitmap
	    	    BitmapFactory.Options options = new BitmapFactory.Options();
	    	    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	    	    Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
	    	    Log.i("photoFile path", photoFile.getAbsolutePath());
	    	    
	    	    //Convert bitmap to bytearray
	    	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    	    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
	    	    byte[] byteArray = stream.toByteArray();
	    	    
	    	    //Send byteArray to Intent
	    	    Intent intent = new Intent(this, DrawMainActivity.class);
	    	    intent.putExtra("PICTURE", byteArray);
	    	    startActivity(intent);
	    	    */
	    	    
	    	    
	    	    //Just send the filename
	    	    
	    	    
	    	    Intent intent = new Intent(this, DrawMainActivity.class);
	    	    intent.putExtra("picture_name", filename);
	    	    startActivity(intent);
	    	    
	    	    finish();
	    	    
	    	    //TODO: Launch the draw activity with this picture on the canvas
	    	    Log.e("snap after finish", "het");
	    	    
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

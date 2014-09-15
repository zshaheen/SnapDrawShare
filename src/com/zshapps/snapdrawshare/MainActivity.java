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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private TextView snapText, drawText, shareText, feedbackText;
	private Boolean runFontCalibrate = true;
	private SharedPreferences prefs;
	private static String keySnapFont = "com.zshapps.snapdrawshare.keySnapFont";
	private static String keyDrawFont = "com.zshapps.snapdrawshare.keyDrawFont";
	private static String keyShareFont = "com.zshapps.snapdrawshare.keyShareFont";
	private static String keyFeedbackFont = "com.zshapps.snapdrawshare.keyFeedbackFont";
	private static String keyTextPadding = "com.zshapps.snapdrawshare.keyTextPadding";
	//private ProgressBar spinner;

	private static final int REQUEST_TAKE_PHOTO = 1;
	private static String keyStoredDate = "com.zshapps.snapdrawshare.storedDate";
	private static String keyIncr = "com.zshapps.snapdrawshare.incr";
	private int incr = 10;
	File photoFile = null;
	private String filename = "temp";
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * Hide top bar
         * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        */
        setContentView(R.layout.activity_main);
        
        //spinner = (ProgressBar)findViewById(R.id.progressBar1);
        //spinner.setVisibility(View.GONE);
        
        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SnapDrawShare/";
    	File newdir = new File(dir);
    	if (!newdir.exists()) {
    		newdir.mkdirs();
    	}
    	
   
    	prefs = this.getSharedPreferences("com.zshapps.snapdrawshare", Context.MODE_PRIVATE);
    	
    	if(prefs.contains(keySnapFont) && prefs.contains(keyDrawFont) 
    			&& prefs.contains(keyShareFont) && prefs.contains(keyFeedbackFont) && prefs.contains(keyTextPadding)) {
    		
    		//load the sizes and dont call code on onWindowFocusChanged
    		
    		float snapSize, drawSize, shareSize, feedbackSize;
    		int textpadding;
    		snapSize = prefs.getFloat(keySnapFont, (float) 100);
    		drawSize = prefs.getFloat(keyDrawFont, (float) 100);
    		shareSize = prefs.getFloat(keyShareFont, (float) 100);
    		feedbackSize = prefs.getFloat(keyFeedbackFont, (float) 100);
    		textpadding = prefs.getInt(keyTextPadding, 20);
    		
    		snapText = (TextView) findViewById(R.id.snapText);
    		snapText.setTextSize(TypedValue.COMPLEX_UNIT_PX, snapSize);
    		snapText.setPadding(textpadding, 0, 0, 0);
    		
    		
    		drawText = (TextView) findViewById(R.id.drawText);
    		drawText.setTextSize(TypedValue.COMPLEX_UNIT_PX, drawSize);
    		drawText.setPadding(0, 0, textpadding, 0);
    		
    		shareText = (TextView) findViewById(R.id.shareText);
    		shareText.setTextSize(TypedValue.COMPLEX_UNIT_PX, shareSize);
    		snapText.setPadding(textpadding, 0, 0, 0);
    		
    		feedbackText = (TextView) findViewById(R.id.feedbackText);
    		feedbackText.setTextSize(TypedValue.COMPLEX_UNIT_PX, feedbackSize);
            
            runFontCalibrate = false;
    	}
    	else {
    		//create the prefs 
    		prefs.edit().putFloat(keySnapFont, (float) 100).commit();
    		prefs.edit().putFloat(keyDrawFont, (float) 100).commit();
    		prefs.edit().putFloat(keyShareFont, (float) 100).commit();
    		prefs.edit().putFloat(keyFeedbackFont, (float) 100).commit();
    		prefs.edit().putInt(keyTextPadding, 20).commit();
    	}
    
        
    	/*
         * BUTTONS
         * 
         */
    	RelativeLayout snapButton = (RelativeLayout) findViewById(R.id.snap_button);
    	snapButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            	onSnapButton();
            	/*
            	Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                	//cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            		startActivityForResult(cameraIntent, 1);
                	Toast.makeText(getApplicationContext(), "some message", Toast.LENGTH_LONG).show();
                }
            	*/
            }
        });
    	
    	RelativeLayout drawButton = (RelativeLayout) findViewById(R.id.draw_button);
    	drawButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            	onDrawButton();
            }
        });
    	
    	RelativeLayout shareButton = (RelativeLayout) findViewById(R.id.share_button);
    	shareButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            	onShareButton();
            }
        });
    	
    	RelativeLayout feedbackButton = (RelativeLayout) findViewById(R.id.feedback_button);
    	feedbackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            	onFeedbackButton();
            }
        });
    	
    }
    
    

    
    public void calibrateText(final TextView v, final String key) {
    	if(v.getLineCount() != 1) {
    		float currTextSize = v.getTextSize() - 1;
        	v.setTextSize(TypedValue.COMPLEX_UNIT_PX, currTextSize);
        	v.post(new Runnable() {
                 @Override
                 public void run() {
                	 calibrateText(v, key);
                 }
        	});
    	}
    	else {
    		float newSize = v.getTextSize();
    		prefs.edit().putFloat(key, newSize).commit();
    	}
    }
    
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
    	
    	if(runFontCalibrate) {
    		
    		ImageView snapIconView = (ImageView) findViewById(R.id.snap_icon);
            int snapLength = snapIconView.getHeight();
            int snapTextPadding = (int) (snapLength * 0.18);
            
            snapText = (TextView) findViewById(R.id.snapText);
            snapText.setTextSize(TypedValue.COMPLEX_UNIT_PX, snapLength/2);
        	snapText.setPadding(snapTextPadding, 0, 0, 0);
            snapText.post(new Runnable() {

                @Override
                public void run() {
                	calibrateText(snapText, keySnapFont);
                }
            });
            
            
            
            ImageView drawIconView = (ImageView) findViewById(R.id.draw_icon);
            int drawLength = drawIconView.getWidth();
            int drawTextPadding = (int) (drawLength * 0.18);
            
            drawText = (TextView) findViewById(R.id.drawText);
            drawText.setTextSize(TypedValue.COMPLEX_UNIT_PX, drawLength/2);
    		drawText.setPadding(0, 0, drawTextPadding, 0);
            drawText.post(new Runnable() {

                @Override
                public void run() {
                	calibrateText(drawText, keyDrawFont);
                }
            });
            
            
            
            ImageView shareIconView = (ImageView) findViewById(R.id.share_icon);
            int shareLength = shareIconView.getWidth();
            int shareTextPadding = (int) (shareLength * 0.18);
            
            shareText = (TextView) findViewById(R.id.shareText);
            shareText.setTextSize(TypedValue.COMPLEX_UNIT_PX, shareLength/2);
            shareText.setPadding(shareTextPadding, 0, 0, 0);
            shareText.post(new Runnable() {

                @Override
                public void run() {
                	calibrateText(shareText, keyShareFont);
                }
            });
            
            
            
            ImageView feedbackIconView = (ImageView) findViewById(R.id.feedback_icon);
            int feedbackLength = feedbackIconView.getWidth();
            
            feedbackText = (TextView) findViewById(R.id.feedbackText);
            feedbackText.setTextSize(TypedValue.COMPLEX_UNIT_PX, feedbackLength/2);
            feedbackText.post(new Runnable() {

                @Override
                public void run() {
                	calibrateText(feedbackText, keyFeedbackFont);
                }
            });
            
            runFontCalibrate = false;
    	}
    }
    
    
    /*
    @Override
    public void onBackPressed() {
    	//Always minimize back to main screen
    	Intent startMain = new Intent(Intent.ACTION_MAIN);
    	startMain.addCategory(Intent.CATEGORY_HOME);
    	startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	startActivity(startMain);

    }*/
    
    
    public void onSnapButton() {
    	//Intent intent = new Intent(this, SnapMainActivity.class);
    	//startActivity(intent);

    		
    		

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
    
    public void onDrawButton() {
		Intent intent = new Intent(this, DrawMainActivity.class);
		intent.putExtra("FLAG", "MainActivity");
    	startActivity(intent);
	}
	
    public void onShareButton() {
		Intent intent = new Intent(this, ShareMainActivity.class);
		intent.putExtra("FLAG", "MainActivity");
    	startActivity(intent);
	}
	
    public void onFeedbackButton() {
		Intent intent = new Intent(this, FeedbackMainActivity.class);
    	startActivity(intent);
    }
    
    private File createImageFile() throws IOException {
	    // Create an image file name
		
		String curDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		
	    prefs = this.getSharedPreferences("com.zshapps.snapdrawshare", Context.MODE_PRIVATE);
	    
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
	    	    Intent intent = new Intent(this, DrawMainActivity.class);
	    	    intent.putExtra("picture_name", filename);
	    	    intent.putExtra("picture_extension", "png");
	    	    intent.putExtra("FLAG", "SnapMainActivity");	    	    
	    	    startActivity(intent);
	    	    
	    	    finish();	    	    
	    	    
	        }  
    		//else
    			/*
    			 *  If the user doesn't want to take picture, stop camera activity
    			 *	Otherwise lagging occurs
    			 */
    			//finish();
        }
    }
    
    
}

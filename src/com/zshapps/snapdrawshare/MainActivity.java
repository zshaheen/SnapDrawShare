package com.zshapps.snapdrawshare;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private static final int SNAP_ACTIVITY = 0, DRAW_ACTIVITY = 1, SHARE_ACTIVITY = 2, FEEDBACK_ACTIVITY = 3;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * Hide top bar
         * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        */
        
        /* Hide the action bar
         * android:windowActionBar="false" was added to manifest
        */
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        
        setContentView(R.layout.activity_main);

        /*
         * BUTTONS
         * 
         */
        Button snapButton, drawButton, shareButton, feedbackButton;
        snapButton = (Button) this.findViewById(R.id.button_snap);
        drawButton = (Button) this.findViewById(R.id.button_draw);
        shareButton = (Button) this.findViewById(R.id.button_share);
        feedbackButton = (Button) this.findViewById(R.id.button_feedback);
        
        snapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	onSnapButton();
            }
        });
        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	onDrawButton();
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	onShareButton();
            }
        });
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	onFeedbackButton();
            }
        });
    }
    
    
    private void onSnapButton() {
    	
    }
    
	private void onDrawButton() {
	    	
	    }
	
	private void onShareButton() {
		
	}
	
	private void onFeedbackButton() {
		//Intent intent = new Intent(this, FeedbackMainActivity.class);
    	//startActivity(intent);	
    }
    
    
}

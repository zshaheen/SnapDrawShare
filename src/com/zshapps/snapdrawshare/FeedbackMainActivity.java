package com.zshapps.snapdrawshare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.ContextThemeWrapper;
import android.widget.TextView;

public class FeedbackMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback_main);
		
		AlertDialog.Builder fromMainDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.DialogTheme));

    	CharSequence[] items = {"About", "Email"};
    	fromMainDialog.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0) {
                	final AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(FeedbackMainActivity.this, R.style.DialogTheme));
                	
                    final SpannableString s = new SpannableString("Icons used from:\n\thttp://icons8.com/");
                    Linkify.addLinks(s, Linkify.ALL);
                    
                    /*
                    TextView myMsg = new TextView(FeedbackMainActivity.this);
                    myMsg.setText(s);
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    myMsg.setMovementMethod(LinkMovementMethod.getInstance());
					*/
                    
                    //aboutDialogBuilder.setView(myMsg);
                    aboutDialogBuilder.setMessage(s);
                    AlertDialog aboutDialog = aboutDialogBuilder.create(); 
                	aboutDialog.show();
                	
                	 ((TextView)aboutDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                }
                
                if(which == 1) {
                	Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
            	            "mailto","zshapps@gmail.com", null));
            		startActivity(Intent.createChooser(emailIntent, "Send Feedback"));
                }
            }
    	});
    	
    	fromMainDialog.show();
	}
}

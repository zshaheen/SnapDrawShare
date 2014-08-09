package com.zshapps.snapdrawshare;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class FeedbackMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback_main);

		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
	            "mailto","zshapps@gmail.com", null));
		startActivity(Intent.createChooser(emailIntent, "Send Feedback"));

		finish();
	}
}

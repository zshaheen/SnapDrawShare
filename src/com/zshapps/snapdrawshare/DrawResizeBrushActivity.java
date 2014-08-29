package com.zshapps.snapdrawshare;


import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class DrawResizeBrushActivity extends Activity {

	private ImageView imageview;
	private float maxDialogWidth = 0;
	private float scale = 0;
	private SeekBar sizeBar = null;
	private Paint paint;
	private int size=5;
	
	private Bitmap bmp;
	private Button buttonOk, buttonCancel;
	private Canvas c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw_resize_brush);
		
		buttonOk = (Button) findViewById(R.id.button_ok);
		buttonCancel = (Button) findViewById(R.id.button_cancel);
		
		buttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });
		
		buttonOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	DrawingView.setBrushSize(size);
            	Log.i("Brush size", ""+size);
            	Log.i("Seekbar progress", ""+sizeBar.getProgress());
            	finish();
            }
        });

		
		scale = getResources().getDisplayMetrics().density;
		
		size = DrawingView.getBrushSize();
		
		//The diameters will range from 2 to 300
		//Dialog will have a cushion of 10dp(ish?) on each side
		maxDialogWidth = (320 * scale + 0.5f);
		getWindow().setLayout((int)maxDialogWidth/*LayoutParams.WRAP_CONTENT*/ , LayoutParams.WRAP_CONTENT);
		
		imageview = (ImageView)findViewById(R.id.circleimageview);
		bmp = Bitmap.createBitmap((int)maxDialogWidth,(int)maxDialogWidth,Bitmap.Config.ARGB_8888);
	    c = new Canvas(bmp);
	    
	    paint = new Paint();
	    paint.setAntiAlias(true);
	    if(DrawingView.erase)
	    	paint.setColor(0xFF000000);
	    else
	    	paint.setColor(DrawingView.getPaintColor());
	    	
	    
	    //float radius = (sizeDP * scale + 0.5f)/2;
	    float r = (DrawingView.getBrushSize() * scale + 0.5f);
	    
	    c.drawCircle(maxDialogWidth/2, maxDialogWidth/2, (float) Math.ceil(r/2), paint);
	    if(DrawingView.erase) {
	    	paint.setColor(0xFFFFFFFF);
	    	c.drawCircle(maxDialogWidth/2, maxDialogWidth/2, (float) Math.ceil((r-(1 * scale + 0.5f))/2), paint);
	    	paint.setColor(0xFF000000);
	    }
	    imageview.setImageDrawable(new BitmapDrawable(getResources(), bmp));
	    
	    
	    sizeBar = (SeekBar) findViewById(R.id.seekbar);
  		sizeBar.setProgress(size);
  		sizeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
  		
  			public void onStopTrackingTouch(SeekBar seekBar) {}       

  		    public void onStartTrackingTouch(SeekBar seekBar) {}       

  		    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) { 
  		    	size = progress;
  		    	//First clear the bitmap
  		    	bmp.eraseColor(android.R.color.white);
  		    	//then draw a circle
  		    	float radius = (float) ((size * scale + 0.5f)/2);
  		    	//Log.i("raduis", ""+radius);
  		    	c.drawCircle(maxDialogWidth/2, maxDialogWidth/2, (float) Math.ceil(radius) , paint);
  		    	if(DrawingView.erase) {
  			    	paint.setColor(0xFFFFFFFF);
  			    	c.drawCircle(maxDialogWidth/2, maxDialogWidth/2, (float) Math.ceil((radius-(1 * scale + 0.5f))), paint);
  			    	paint.setColor(0xFF000000);
  			    }
  		    	imageview.setImageDrawable(new BitmapDrawable(getResources(), bmp));
  		      
  		    }
  			
  		});
	  		
	}

}

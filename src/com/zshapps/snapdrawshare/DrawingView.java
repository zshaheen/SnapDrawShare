package com.zshapps.snapdrawshare;



import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class DrawingView extends View {
	
	//drawing path that is what the user touches
	private static Path drawPath;
	//drawing and canvas paint
	//the user paths are drawn with drawPaint
	//this path is then drawn on the canvas by canvasPaint
	public static Paint drawPaint;
	private Paint canvasPaint;
	//initial color
	private static int paintColor = 0xFF000000;
	private static int paintBrushSize = 5;
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	public Bitmap canvasBitmap, tempBmp, resizeBmp;
	
	private static float scale = 0; 
	
	public static boolean erase = false;

	private float xOffset=0, yOffset=0;
	
	//private ArrayList<Path> paths = new ArrayList<Path>();
	//private ArrayList<Paint> drawPaints = new ArrayList<Paint>();
	
	private ArrayList<pathAndPaint> strokes = new ArrayList<pathAndPaint>();
	private ArrayList<pathAndPaint> redoStrokes = new ArrayList<pathAndPaint>();
	private Path mPath;
	
	//private static int undoRedoSize = 3;

	
	public class pathAndPaint {
		
		private Path path;
		private Paint paint;
		
		public pathAndPaint(Path p, Paint pt) {
			path = p;
			paint = pt;
		}
		
		public Path getPath() {
			return path;
		}
		
		public Paint getPaint() {
			return paint;
		}
		
	}
	
	
	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupDrawing();
	}
	
	private void setupDrawing() {
		
		
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		
		scale = getResources().getDisplayMetrics().density;
		
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth((float) (paintBrushSize * scale + 0.5f));
		//TODO look into setStyle and setStrokeJoin
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		
		canvasPaint = new Paint(Paint.DITHER_FLAG);
		
		
		//tempBmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		/*
		String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SnapDrawShare/";
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		tempBmp = BitmapFactory.decodeFile(dir+"pic.png",options);
		*/
		
		mPath = new Path();
	}
	
	
	public void onErase(boolean val) {
		erase = val;
		if(erase)
			drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		else 
			drawPaint.setXfermode(null);
					
	}
	
	public static void setPaintColor(int color) {
		paintColor = color;
		drawPaint.setColor(paintColor);
	}
	
	public static int getPaintColor() {
		return paintColor;
		
	}
	
	
	public static void setBrushSize(int size) {
		paintBrushSize = size;
		drawPaint.setStrokeWidth((float) (paintBrushSize * scale + 0.5f));
	}
	
	public static int getBrushSize() {
		return paintBrushSize;
	}
	
	public Bitmap getDrawCache(){
		setDrawingCacheEnabled(true);
		return getDrawingCache();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		/* Uncropped, but leaves a whitespace
		width = w;
		Log.i("width", ""+width);
		Log.i("pic_height", ""+tempBmp.getHeight());
		Log.i("pic_width", ""+tempBmp.getWidth());
		height = 1400;//(tempBmp.getHeight()/tempBmp.getWidth())*width;
		Log.i("height", ""+height);
		
		//canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
		resizeBmp = Bitmap.createScaledBitmap(tempBmp, width, height, false);
		*/
		float sourceW = tempBmp.getWidth(), sourceH = tempBmp.getHeight();
		
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
		
		if(sourceH >= sourceW) { 
			//portrait 
			int newWidth = (int) (h*(sourceW/sourceH));
			resizeBmp = Bitmap.createScaledBitmap(tempBmp, newWidth, h, false);
			//Now calculate how off the width (offsetX) should be
			xOffset = -(newWidth - canvasBitmap.getWidth())/2;
			
			Log.i("portrait resize HEIGHT",""+h);
			Log.i("portrait resize WIDTH",""+newWidth);
			
			//Log.i("newWidthPORTRAIT",""+newWidth);
		}
		
		else {
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			Bitmap rotatedBitmap = Bitmap.createBitmap(tempBmp , 0, 0, tempBmp.getWidth(), tempBmp.getHeight(), matrix, true);

			//Must store rotatedBitmap height and width, otherwise newWidth is 0
			float rotatedW = rotatedBitmap.getWidth(), rotatedH = rotatedBitmap.getHeight();
			int newWidth = (int) ( h*(rotatedW/rotatedH) );


			resizeBmp = Bitmap.createScaledBitmap(rotatedBitmap, newWidth, h, true);
			//Now calculate how off the width (offsetX) should be
			xOffset = -(newWidth - canvasBitmap.getWidth())/2;
		}
		
		Log.i("offsetX", ""+xOffset);
		/* orig
		centerCropBitmap(canvasBitmap, w, h);
		resizeBmp = canvasBitmap;//Bitmap.createScaledBitmap(tempBmp, w, h, false);
		centerCropBitmap(tempBmp, w, h);
		resizeBmp = tempBmp;//Bitmap.createScaledBitmap(tempBmp, w, h, false);
		*/
	
	}
	
	
	public void onUndo() {
		/*paths.get(paths.size()-1).reset();
		drawCanvas.drawPath(paths.get(paths.size()-1) , drawPaints.get(paths.size()-1));
		*/
		//Reload the canvas, clearing it of the paths
		
		if(strokes.size() > 0) {
			drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
			drawCanvas.drawBitmap(resizeBmp, xOffset, yOffset, canvasPaint);
			drawCanvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
			
			
			//Pop the last path from ArrayList paths
			redoStrokes.add(strokes.get(strokes.size()-1));
			strokes.remove(strokes.size()-1); 
			//paths.remove(paths.size()-1);
			//drawPaints.remove(drawPaints.size()-1);
			
			//Redraw the paths
			for(int i = 0; i<strokes.size(); i++) {
				drawCanvas.drawPath(strokes.get(i).getPath(), strokes.get(i).getPaint());  //paths.get(i), drawPaints.get(i));
			}
			invalidate();
		}
		else {
			Toast.makeText(getContext(), "Nothing left to undo.", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	public void onRedo() {
		if(redoStrokes.size() > 0) {
			//add last item on redoStrokes back to stokes
			strokes.add(redoStrokes.get(redoStrokes.size()-1) );
			//Remove this item from redoStrokes
			redoStrokes.remove(redoStrokes.size()-1); 
			//Force everthing to redraw
			//Redraw the paths
			for(int i = 0; i<strokes.size(); i++) {
				drawCanvas.drawPath(strokes.get(i).getPath(), strokes.get(i).getPaint());  //paths.get(i), drawPaints.get(i));
			}
			invalidate();
			
		}
		else {
			Toast.makeText(getContext(), "Nothing left to redo.", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		/*canvas.drawBitmap(resizeBmp, xOffset, yOffset, canvasPaint);
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
		setDrawingCacheEnabled(true);*/
		
		canvas.drawBitmap(resizeBmp, xOffset, yOffset, canvasPaint);
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		
		/*
		for(Path p: paths) {
			canvas.drawPath(p, drawPaints.get(paths.indexOf(p)) );
		}*/
		
		/*for(int i = 0; i<paths.size(); i++) {
			canvas.drawPath(paths.get(i), drawPaints.get(i));
		}*/
		
		
		canvas.drawPath(drawPath, drawPaint);
		setDrawingCacheEnabled(true);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float touchX = event.getX();
		float touchY = event.getY();
		
		/*switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		    drawPath.moveTo(touchX, touchY);
		    drawPath.lineTo(touchX+0.001f, touchY+0.001f);
		    //if(erase) {
		    	drawCanvas.drawPath(drawPath, drawPaint);
		    	drawPath.reset();
		    	drawPath.moveTo(touchX, touchY);
		    //}
		    break;
		case MotionEvent.ACTION_MOVE:
		    drawPath.lineTo(touchX, touchY);
		    //if(erase) {
		    	drawCanvas.drawPath(drawPath, drawPaint);
		    	drawPath.reset();
		    	drawPath.moveTo(touchX, touchY);
		   // }
		    break;
		case MotionEvent.ACTION_UP:
		    drawCanvas.drawPath(drawPath, drawPaint);
		    drawPath.reset();
		    break;
		default:
		    return false;
		}*/
		
		switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				touch_start(touchX, touchY);
				//invalidate();
			    break;
			case MotionEvent.ACTION_MOVE:
			    touch_move(touchX, touchY);
			    //invalidate();
			    break;
			case MotionEvent.ACTION_UP:
				
				//Log.i("pathsLength",""+strokes.size());
				touch_up(touchX, touchY);
				//invalidate();
			    break;
			default:
			    return false;
		}
		
		//View must be redrawn now
		invalidate();
		return true;
	}
	
	private void touch_start(float x, float y) 
	{
		redoStrokes.clear();
		mPath.reset();
		mPath.moveTo(x, y);
		mPath.lineTo(x+0.001f, y+0.001f);
		//mPath.lineTo(x, y);
	}

	private void touch_move(float x, float y) 
	{
		mPath.lineTo(x, y); 
		
		drawCanvas.drawPath(mPath, drawPaint);
		//invalidate();
	}  

	private void touch_up(float x, float y) 
	{
		//mPath.lineTo(x, y); 
	    drawCanvas.drawPath(mPath, drawPaint);
	    //bitmapCanvas.drawPath(mPath, paintLine);// commit the path to our offscreen 
	    
	    /*
	    if(paths.size() > undoRedoSize) {
	    	//pop the first path and drawPaint
	    	paths.remove(0);
			drawPaints.remove(0);
	    }*/
	    
	    strokes.add(new pathAndPaint(mPath, drawPaint));
	    //paths.add(mPath);
	    //drawPaints.add(drawPaint);
		
	    
	    
	    
	    mPath = new Path(); 
	    drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth((float) (paintBrushSize * scale + 0.5f));
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		if(erase)
			drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
	}
	
}

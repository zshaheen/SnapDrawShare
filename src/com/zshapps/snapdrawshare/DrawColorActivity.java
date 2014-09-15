package com.zshapps.snapdrawshare;




import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


public class DrawColorActivity extends Activity {

 
	private static int[] colors = 
		{	
			R.drawable.c00x00, R.drawable.c00x01, R.drawable.c00x02, R.drawable.c00x03, R.drawable.c00x04, R.drawable.c00x05,
			R.drawable.c01x00, R.drawable.c01x01, R.drawable.c01x02, R.drawable.c01x03, R.drawable.c01x04, R.drawable.c01x05,
			R.drawable.c02x00, R.drawable.c02x01, R.drawable.c02x02, R.drawable.c02x03, R.drawable.c02x04, R.drawable.c02x05,
			R.drawable.c03x00, R.drawable.c03x01, R.drawable.c03x02, R.drawable.c03x03, R.drawable.c03x04, R.drawable.c03x05,
			R.drawable.c04x00, R.drawable.c04x01, R.drawable.c04x02, R.drawable.c04x03, R.drawable.c04x04, R.drawable.c04x05,
			R.drawable.c05x00, R.drawable.c05x01, R.drawable.c05x02, R.drawable.c05x03, R.drawable.c05x04, R.drawable.c05x05,
			R.drawable.c06x00, R.drawable.c06x01, R.drawable.c06x02, R.drawable.c06x03, R.drawable.c06x04, R.drawable.c06x05,
			R.drawable.c07x00, R.drawable.c07x01, R.drawable.c07x02, R.drawable.c07x03, R.drawable.c07x04, R.drawable.c07x05,
			R.drawable.c08x00, R.drawable.c08x01, R.drawable.c08x02, R.drawable.c08x03, R.drawable.c08x04, R.drawable.c08x05,
			R.drawable.c09x00, R.drawable.c09x01, R.drawable.c09x02, R.drawable.c09x03, R.drawable.c09x04, R.drawable.c09x05,
			R.drawable.c10x00, R.drawable.c10x01, R.drawable.c10x02, R.drawable.c10x03, R.drawable.c10x04, R.drawable.c10x05,
			//R.drawable.c11x00, R.drawable.c11x01, R.drawable.c11x02, R.drawable.c11x03, R.drawable.c11x04, R.drawable.c11x05
			R.drawable.black, R.drawable.c11x01, R.drawable.c11x02, R.drawable.c11x03, R.drawable.c11x04, R.drawable.white
		};

	private static int[][] hexColors = new int[][] {
			{0xFFaa1919, 0xFFc90606, 0xFFe70f00, 0xFFec6964, 0xFFf39692, 0xFFfad4d2},
			{0xFFaa1968, 0xFFc90673, 0xFFe70082, 0xFFec649f, 0xFFf392bc, 0xFFfad2e3},
			{0xFF7b0068, 0xFF95057d, 0xFFad3696, 0xFFbd61a8, 0xFFcb84bc, 0xFFecc2dd},
			{0xFF2a1267, 0xFF45237f, 0xFF583796, 0xFF765ca9, 0xFF9d8ac4, 0xFFccbfe0},
			{0xFF00417f, 0xFF00549a, 0xFF0069ba, 0xFF3c83d0, 0xFF73a2d7, 0xFFb1cae8},
			{0xFF005a5f, 0xFF007278, 0xFF008990, 0xFF00b4be, 0xFF00cdd4, 0xFFb5e3e5},
			{0xFF006b50, 0xFF00845f, 0xFF00a778, 0xFF00be88, 0xFF00d498, 0xFFa7e2cf},
			{0xFF007f38, 0xFF00993c, 0xFF48b13c, 0xFF88c265, 0xFFbdd99e, 0xFFdeeccb},
			{0xFFe8b900, 0xFFf5cc00, 0xFFffe300, 0xFFffee67, 0xFFfff4a7, 0xFFfff8d1},
			{0xFFb8541b, 0xFFd75b1e, 0xFFef8441, 0xFFf4a97a, 0xFFf4c598, 0xFFfddfbc},
			{0xFF493410, 0xFF918168, 0xFF866327, 0xFF9e7c42, 0xFFbea16f, 0xFFe3c695},
			//{0xFF3e4547, 0xFF676965, 0xFF989a95, 0xFFc4c7c1, 0xFFdaddd7, 0xFFf1f1ef}
			{0xFF000000, 0xFF676965, 0xFF989a95, 0xFFc4c7c1, 0xFFdaddd7, 0xFFFFFFFF}
		};

	private GridView gridView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(
			    WindowManager.LayoutParams.FLAG_FULLSCREEN, 
			    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_draw_color);
		gridView = (GridView) findViewById(R.id.gridviewcolors);
        gridView.setAdapter(new ColorAdapter(this));
        
        gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				//default color is black
				int num = 0xFF000000;
				
				switch(colors[position]) {
				
            	//Try using an array of hex values
				/////////////////////////////////
	            	case R.drawable.c00x00:
	            		num = hexColors[0][0];
	            		break;
	            	case R.drawable.c00x01:
	            		num = hexColors[0][1];
	            		break;
	            	case R.drawable.c00x02:
	            		num = hexColors[0][2];
	            		break;
	            	case R.drawable.c00x03:
	            		num = hexColors[0][3];
	            		break;
	            	case R.drawable.c00x04:
	            		num = hexColors[0][4];
	            		break;
	            	case R.drawable.c00x05:
	            		num = hexColors[0][5];
	            		break;
	            	
				/////////////////////////////////
	            	
	            	case R.drawable.c01x00:
	            		num = hexColors[1][0];
	            		break;
	            	case R.drawable.c01x01:
	            		num = hexColors[1][1];
	            		break;
	            	case R.drawable.c01x02:
	            		num = hexColors[1][2];
	            		break;
	            	case R.drawable.c01x03:
	            		num = hexColors[1][3];
	            		break;
	            	case R.drawable.c01x04:
	            		num = hexColors[1][4];
	            		break;
	            	case R.drawable.c01x05:
	            		num = hexColors[1][5];
	            		break;	
	            	
				/////////////////////////////////
	            	case R.drawable.c02x00:
	            		num = hexColors[2][0];
	            		break;
	            	case R.drawable.c02x01:
	            		num = hexColors[2][1];
	            		break;
	            	case R.drawable.c02x02:
	            		num = hexColors[2][2];
	            		break;
	            	case R.drawable.c02x03:
	            		num = hexColors[2][3];
	            		break;
	            	case R.drawable.c02x04:
	            		num = hexColors[2][4];
	            		break;
	            	case R.drawable.c02x05:
	            		num = hexColors[2][5];
	            		break;
	            	
				/////////////////////////////////
	            		
	            	case R.drawable.c03x00:
	            		num = hexColors[3][0];
	            		break;
	            	case R.drawable.c03x01:
	            		num = hexColors[3][1];
	            		break;
	            	case R.drawable.c03x02:
	            		num = hexColors[3][2];
	            		break;
	            	case R.drawable.c03x03:
	            		num = hexColors[3][3];
	            		break;
	            	case R.drawable.c03x04:
	            		num = hexColors[3][4];
	            		break;
	            	case R.drawable.c03x05:
	            		num = hexColors[3][5];
	            		break;
	            		
			/////////////////////////////////

	            	case R.drawable.c04x00:
	            		num = hexColors[4][0];
	            		break;
	            	case R.drawable.c04x01:
	            		num = hexColors[4][1];
	            		break;
	            	case R.drawable.c04x02:
	            		num = hexColors[4][2];
	            		break;
	            	case R.drawable.c04x03:
	            		num = hexColors[4][3];
	            		break;
	            	case R.drawable.c04x04:
	            		num = hexColors[4][4];
	            		break;
	            	case R.drawable.c04x05:
	            		num = hexColors[4][5];
	            		break;
	            	
				/////////////////////////////////
	            		
	            	case R.drawable.c05x00:
	            		num = hexColors[5][0];
	            		break;
	            	case R.drawable.c05x01:
	            		num = hexColors[5][1];
	            		break;
	            	case R.drawable.c05x02:
	            		num = hexColors[5][2];
	            		break;
	            	case R.drawable.c05x03:
	            		num = hexColors[5][3];
	            		break;
	            	case R.drawable.c05x04:
	            		num = hexColors[5][4];
	            		break;
	            	case R.drawable.c05x05:
	            		num = hexColors[5][5];
	            		break;	
	            	
				/////////////////////////////////
	            	case R.drawable.c06x00:
	            		num = hexColors[6][0];
	            		break;
	            	case R.drawable.c06x01:
	            		num = hexColors[6][1];
	            		break;
	            	case R.drawable.c06x02:
	            		num = hexColors[6][2];
	            		break;
	            	case R.drawable.c06x03:
	            		num = hexColors[6][3];
	            		break;
	            	case R.drawable.c06x04:
	            		num = hexColors[6][4];
	            		break;
	            	case R.drawable.c06x05:
	            		num = hexColors[6][5];
	            		break;
	            	
				/////////////////////////////////
	            		
	            	case R.drawable.c07x00:
	            		num = hexColors[7][0];
	            		break;
	            	case R.drawable.c07x01:
	            		num = hexColors[7][1];
	            		break;
	            	case R.drawable.c07x02:
	            		num = hexColors[7][2];
	            		break;
	            	case R.drawable.c07x03:
	            		num = hexColors[7][3];
	            		break;
	            	case R.drawable.c07x04:
	            		num = hexColors[7][4];
	            		break;
	            	case R.drawable.c07x05:
	            		num = hexColors[7][5];
	            		break;	        
	        			/////////////////////////////////

	            	case R.drawable.c08x00:
	            		num = hexColors[8][0];
	            		break;
	            	case R.drawable.c08x01:
	            		num = hexColors[8][1];
	            		break;
	            	case R.drawable.c08x02:
	            		num = hexColors[8][2];
	            		break;
	            	case R.drawable.c08x03:
	            		num = hexColors[8][3];
	            		break;
	            	case R.drawable.c08x04:
	            		num = hexColors[8][4];
	            		break;
	            	case R.drawable.c08x05:
	            		num = hexColors[8][5];
	            		break;
	            	
				/////////////////////////////////
	            		
	            	case R.drawable.c09x00:
	            		num = hexColors[9][0];
	            		break;
	            	case R.drawable.c09x01:
	            		num = hexColors[9][1];
	            		break;
	            	case R.drawable.c09x02:
	            		num = hexColors[9][2];
	            		break;
	            	case R.drawable.c09x03:
	            		num = hexColors[9][3];
	            		break;
	            	case R.drawable.c09x04:
	            		num = hexColors[9][4];
	            		break;
	            	case R.drawable.c09x05:
	            		num = hexColors[9][5];
	            		break;	
	            	
				/////////////////////////////////
	            	case R.drawable.c10x00:
	            		num = hexColors[10][0];
	            		break;
	            	case R.drawable.c10x01:
	            		num = hexColors[10][1];
	            		break;
	            	case R.drawable.c10x02:
	            		num = hexColors[10][2];
	            		break;
	            	case R.drawable.c10x03:
	            		num = hexColors[10][3];
	            		break;
	            	case R.drawable.c10x04:
	            		num = hexColors[10][4];
	            		break;
	            	case R.drawable.c10x05:
	            		num = hexColors[10][5];
	            		break;
	            	
				/////////////////////////////////
	            		
	            	/*case R.drawable.c11x00:
	            		num = hexColors[11][0];
	            		break;*/
	            	case R.drawable.black:
	            		num = hexColors[11][0];
	            		break;
	            	case R.drawable.c11x01:
	            		num = hexColors[11][1];
	            		break;
	            	case R.drawable.c11x02:
	            		num = hexColors[11][2];
	            		break;
	            	case R.drawable.c11x03:
	            		num = hexColors[11][3];
	            		break;
	            	case R.drawable.c11x04:
	            		num = hexColors[11][4];
	            		break;
            		/*case R.drawable.c11x05:
	            		num = hexColors[11][5];
	            		break;*/
	            	case R.drawable.white:
	            		num = hexColors[11][5];
	            		break;	
	            		
	            	default:
	            		num = 0xFF000000;
	            		break;
	            	
            	}
            	Intent resultIntent = new Intent();
        		resultIntent.putExtra("COLOR_INDEX", position);
        		resultIntent.putExtra("COLOR_NUM", num);
        		setResult(Activity.RESULT_OK, resultIntent);
        		finish();
				
			}
        });
        
        gridView.setSelection(DrawingView.getCurrColorSel());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//Save the last position
		int index = gridView.getFirstVisiblePosition();
		DrawingView.setCurrColorSel(index);
	}
	
	public static int getColor(int index) {
		return colors[index];
	}
	
	public class ColorAdapter extends BaseAdapter {
    	
        private Context mContext;
        
        public ColorAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
        	return colors.length;
        }

        public Object getItem(int position) {
            return colors[position];
        }

        public long getItemId(int position) {
            return 0;
        }
        
        
        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
        	
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }
            
            imageView.setImageResource(colors[position]);
            return imageView;
        }
    }

}

package com.userinterface;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.userinterface.OpenCV;

import hcimodify.test1.CreateImgRgb;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProcessImageActivity extends Activity{
	private int touchX;
	private int touchY;
	private ImageView imageview;
	private Bitmap image;
	private Bitmap result_image;
	
	private int[] findpoint = new int[4];
	private Mat mask,ma_img;
	private Mat bgdModel, fgdModel;
	
	private static final String TAG = "Grabcut";
	
	private OpenCV opencv = new OpenCV();
	
    private ProgressDialog mProgressDialog;
    
    
    private View selected_item = null;
    private int offset_x = 0;
    private int offset_y = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.imagedisplay);
        
        /*
         * get selected picture
         */
        Bundle extras=getIntent().getExtras();
        image=(Bitmap) extras.get("image");
        imageview=(ImageView) findViewById(R.id.image);
        imageview.setImageBitmap(image);
        imageview.setDrawingCacheEnabled(true);
        
        touchX=image.getWidth()/2;
        touchY=image.getHeight()/2;
        
        /*
         * back button: click to return previous activity
         */
        ImageButton back_bt=(ImageButton)findViewById(R.id.back_bt);
        back_bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});
        
        /*
         * image tag: touch to tag object
         */
        final ImageView tag_img=(ImageView)findViewById(R.id.tag_img);
        tag_img.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getActionMasked()){
				case MotionEvent.ACTION_DOWN:
					offset_x = (int)event.getX();
                    offset_y = (int)event.getY();
                    selected_item = v;
                    break;
                default:
                	break;					
				}
				return false;
			}
		});
        
        

        final ViewGroup vg=(ViewGroup)findViewById(R.id.image_container);
        
        imageview.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getActionMasked()){
				case MotionEvent.ACTION_DOWN:
					
					offset_x = (int)event.getX();
                    offset_y = (int)event.getY();
                    selected_item=tag_img;
                    RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(
				    		new ViewGroup.MarginLayoutParams(
		                            LinearLayout.LayoutParams.WRAP_CONTENT,
		                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    
                    int x=(vg.getWidth()-image.getWidth())/2 + offset_x;
                    int y=(vg.getHeight()-image.getHeight())/2 + offset_y;
					lp.setMargins(x, y, 0, 0);
					selected_item.setLayoutParams(lp);
                    break;
                default:
                	break;					
				}
				return false;
			}
		});
        
        vg.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getActionMasked()){					
				case MotionEvent.ACTION_MOVE:
					int x = (int)event.getX();
		            int y = (int)event.getY();
		            int w = vg.getWidth();
		            int h = vg.getHeight();

					if(x<(w-image.getWidth())/2)
						x=(w-image.getWidth())/2;
					if(x>w-(w-image.getWidth())/2 - tag_img.getWidth())
						x=w-(w-image.getWidth())/2 - tag_img.getWidth();
					
					if(y<(h-image.getHeight())/2)
						y=(h-image.getHeight())/2;
					
					if(y>(h+image.getHeight())/2 - tag_img.getHeight())
						y=(h+image.getHeight())/2 - tag_img.getHeight();
				    
				    RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(
				    		new ViewGroup.MarginLayoutParams(
		                            LinearLayout.LayoutParams.WRAP_CONTENT,
		                            LinearLayout.LayoutParams.WRAP_CONTENT));
				    lp.setMargins(x, y, 0, 0);
				    selected_item.setLayoutParams(lp);
				    touchX=tag_img.getLeft()-(w-image.getWidth())/2+tag_img.getWidth()/2;
				    touchY=tag_img.getTop()-(h-image.getHeight())/2+28;
				    break;
				default:
					break;
				}
				return true;
			}
		});
        
        /*
         * OK button: click to trigger processing function
         */
        final ImageButton ok_bt=(ImageButton)findViewById(R.id.ok_bt);
        ok_bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				touchX=tag_img.getLeft()-(vg.getWidth()-image.getWidth())/2+tag_img.getWidth()/2;
			    touchY=tag_img.getTop()-(vg.getHeight()-image.getHeight())/2+28;

			    Integer[] tempParams=new Integer[2];
			    tempParams[0]= touchX;
			    tempParams[1]= touchY;

			    new ProcessImageTask().execute(tempParams);
			}
		});
       
        /*
         * surf button: click to execute surf algorithm
         */
        Button surf_bt=(Button) findViewById(R.id.surf_bt);
	      surf_bt.setOnClickListener(new View.OnClickListener(){
	    	  
	    	  public void onClick(View arg0) {
	    			
	    		  int width = image.getWidth();
					int height = image.getHeight();
					int[] pixels = new int[width * height];
					image.getPixels(pixels, 0, width, 0, 0, width, height);
					opencv.setSourceImage(pixels, width, height);
					long start = System.currentTimeMillis();
					opencv.extractSURFFeature();
					long end = System.currentTimeMillis();
					byte[] imageData = opencv.getSourceImage();
					
					//---------------------------------
					long elapse = end - start;
					//-------------------------------
					Bitmap result = BitmapFactory.decodeByteArray(imageData, 0,
							imageData.length);							
		           imageview.setImageBitmap(result);    		  
	    	  }	    	  
	      });
		    
	   }
	
	/*
	 * grab cut function, return processed image
	 */
	public Bitmap grabCut(int[] findpoint){
		int p1x= findpoint[0];
		int p1y= findpoint[1];
		int p2x= findpoint[2];
		int p2y= findpoint[3];

		Log.i(TAG, "image size"+String.valueOf(image.getHeight())+"*"+String.valueOf(image.getWidth()));
		
		ma_img = new Mat();				
		Utils.bitmapToMat(image, ma_img);
		Log.i(TAG, "bitmap input");
		Imgproc.cvtColor(ma_img, ma_img, Imgproc.COLOR_RGBA2BGR, 3);
		
		if(ma_img != null){
			Log.i(TAG, "bitmap input2");
		}
		
		Log.i(TAG, "Mat image size"+String.valueOf(ma_img.height())+"*"+String.valueOf(ma_img.width()));
		//input finishes,  main part
		
        Rect rect;
		Point p1, p2;
		
		p1 = new Point(p1x,p1y);
		p2 = new Point(p2x,p2y);
        rect=new Rect(p1,p2);
        
        mask = new Mat();
        fgdModel = new Mat();
        bgdModel = new Mat();
		
        Imgproc.grabCut(ma_img, mask, rect, bgdModel, fgdModel, 0, Imgproc.GC_INIT_WITH_RECT);       
        Imgproc.grabCut(ma_img, mask, rect, bgdModel, fgdModel, 4);
		byte buff[]=new byte[(int) (mask.total()*mask.channels())];
				
		mask.get(0,0,buff);
		
		int orignal_width = image.getWidth();
		int orignal_height = image.getHeight();
		int width=p2x-p1x;
		int height=p2y-p1y;
		
		Bitmap resultImg=Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		for(int j = 0; j < orignal_height; j++)
			for(int i = 0 ; i < orignal_width; i++)
			{
				if(buff[j*orignal_width+i]==0||buff[j*orignal_width+i]==2 )
				{
					if(i>=p1x&&i<p2x&&j>=p1y&&j<p2y){
						//Log.i(TAG, "test 2");
					    resultImg.setPixel(i-p1x, j-p1y, Color.TRANSPARENT);
					}					
				}
				else{
					if(i>=p1x&&i<p2x&&j>=p1y&&j<p2y){
						//Log.i(TAG, "test 3");
					   int s = image.getPixel(i, j);
					   resultImg.setPixel(i-p1x, j-p1y, s);
					}
				}
			}          
		
		if (mask != null)
            mask.release();
        if (ma_img != null)
            ma_img.release();
        if (fgdModel != null)
            fgdModel.release();
        if (bgdModel != null)
            bgdModel.release();
        mask = null;
        ma_img = null;
        fgdModel = null;
        bgdModel = null;

        return resultImg;
	}
	
	/*
	 * pop up processing dialog when executing processing algorithms
	 */
	private class ProcessImageTask extends AsyncTask<Integer, Void, Bitmap>{
		@Override
		protected Bitmap doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			publishProgress();
			CreateImgRgb twopoint=new CreateImgRgb();
			findpoint=twopoint.Readrgb(image,params[0],params[1]);
			result_image=grabCut(findpoint);
			return result_image;
		}
		protected void onProgressUpdate(Void... progress) {
			mProgressDialog=ProgressDialog.show(ProcessImageActivity.this, "Processing", "Please wait...");	
	    }	
		protected void onPostExecute(Bitmap result) {
			mProgressDialog.dismiss();
			Intent intent=new Intent(ProcessImageActivity.this, DragImageActivity.class);
			intent.putExtra("image", result);
			startActivityForResult(intent,0);	
	     }

	}
}

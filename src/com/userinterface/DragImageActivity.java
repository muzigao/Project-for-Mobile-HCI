package com.userinterface;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DragImageActivity extends Activity {
	private ImageView imageview;
	private Bitmap image;
	
	private View selected_item = null;
    private int offset_x = 0;
    private int offset_y = 0;
    
    private int mPosX;
    private int mPosY;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.dragimage);
        
        Bundle extras=getIntent().getExtras();
        image=(Bitmap) extras.get("image");
        imageview=(ImageView) findViewById(R.id.image);
        imageview.setImageBitmap(image);
        imageview.setDrawingCacheEnabled(true);
        
        /*
         * touch and drag picture, place it as needed
         */
        imageview.setOnTouchListener(new View.OnTouchListener() {
			
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
        vg.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getActionMasked()){
				case MotionEvent.ACTION_DOWN:
					selected_item=imageview;
					offset_x=(int) event.getX();
					offset_y=(int) event.getY();
					
					int x1=(vg.getWidth()-image.getWidth())/2 + offset_x;
					int y1=(vg.getHeight()-image.getHeight())/2 + offset_y;
					
					if(x1<0)
						x1=0;
					if(x1>vg.getWidth()-image.getWidth())
						x1=vg.getWidth()-image.getWidth();
					
					if(y1<0)
						y1=0;
					
					if(y1>vg.getHeight()-image.getHeight())
						y1=vg.getHeight()-image.getHeight();
					
					RelativeLayout.LayoutParams lp1=new RelativeLayout.LayoutParams(
				    		new ViewGroup.MarginLayoutParams(
		                            LinearLayout.LayoutParams.WRAP_CONTENT,
		                            LinearLayout.LayoutParams.WRAP_CONTENT));
				    lp1.setMargins(x1, y1, 0, 0);
				    selected_item.setLayoutParams(lp1);
				    
				case MotionEvent.ACTION_MOVE:
					int x = (int)event.getX() - offset_x;
		            int y = (int)event.getY() - offset_y;
					
		            int w = vg.getWidth();
		            int h = vg.getHeight();

					if(x<0)
						x=0;
					if(x>w-image.getWidth())
						x=w-image.getWidth();
					
					if(y<0)
						y=0;
					
					if(y>h-image.getHeight())
						y=h-image.getHeight();
				    
				    RelativeLayout.LayoutParams lp2=new RelativeLayout.LayoutParams(
				    		new ViewGroup.MarginLayoutParams(
		                            LinearLayout.LayoutParams.WRAP_CONTENT,
		                            LinearLayout.LayoutParams.WRAP_CONTENT));
				    lp2.setMargins(x, y, 0, 0);
				    selected_item.setLayoutParams(lp2);
				    mPosX=imageview.getLeft();
				    mPosY=imageview.getTop(); 

				    break;
				default:
					break;
				}
				return true;
			}
		});
        
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
         * edit button: click to draw something on picture
         */
        ImageButton edit_bt=(ImageButton)findViewById(R.id.edit_bt);
        edit_bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(DragImageActivity.this, EditImageActivity.class);
				intent.putExtra("image", image);
				intent.putExtra("offsetX", mPosX);
				intent.putExtra("offsetY", mPosY);
				startActivityForResult(intent,0);
			}
		});
	}
}

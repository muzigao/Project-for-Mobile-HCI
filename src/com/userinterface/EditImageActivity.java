package com.userinterface;

import java.io.IOException;

import com.userinterface.ColorPickerDialog.OnColorChangedListener;

import imageio.ImageIO;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class EditImageActivity extends Activity implements OnColorChangedListener {
	private Paint       mPaint;
    private MaskFilter  mEmboss;
    private MaskFilter  mBlur;
    private Bitmap  mBitmap;
    private ImageIO imageio=new ImageIO();
    private Bitmap mBackground;
    
    private int offsetX;
    private int offsetY;
    
    private RelativeLayout painter_container;
    private ImageButton painter_norm_bt;
    private ImageButton painter_blur_bt;
    private ImageButton painter_trans_bt;
    private ImageButton eraser_bt;
    
    private ImageButton stroke_s_bt;
    private ImageButton stroke_m_bt;
    private ImageButton stroke_l_bt;
    
    
    private RelativeLayout current_color;
    private ImageButton color_picker;
    
    private int stroke=6;
    

	@Override
    public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.panel);
        
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
        
        Bundle extras=getIntent().getExtras();
        Bitmap image=(Bitmap) extras.get("image");
        offsetX=extras.getInt("offsetX");
        offsetY=extras.getInt("offsetY");

        mBackground = image;
        
        /*
         * initialize painter
         */
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.rgb(51, 181, 229));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(stroke);

        mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
                                       0.4f, 6, 3.5f);

        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
        
        RelativeLayout panel_container=(RelativeLayout)findViewById(R.id.panel_container);
        panel_container.addView(new MyView(this));
        
        /*
         * save button: click to save image to sd card
         */
        ImageButton save_bt=(ImageButton) findViewById(R.id.save_bt);
        save_bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String imgPath=String.valueOf(System.currentTimeMillis())+".png";
				try {
					imageio.saveImage(mBitmap, imgPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(EditImageActivity.this);
				builder.setMessage("Image Saved")
					.setCancelable(false)
					.setNegativeButton("OK", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					});
				
				AlertDialog alert = builder.create();
				alert.show();				
			}
		});
        
        ImageButton painter_bt=(ImageButton) findViewById(R.id.painter_bt);
        painter_container=(RelativeLayout) findViewById(R.id.painter_container);
        painter_norm_bt=(ImageButton) findViewById(R.id.painter_norm);
        painter_blur_bt=(ImageButton) findViewById(R.id.painter_blur);
        painter_trans_bt=(ImageButton) findViewById(R.id.painter_trans);
        eraser_bt=(ImageButton) findViewById(R.id.eraser);
        
        painter_bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(painter_container.getVisibility()==View.INVISIBLE){
					painter_container.setVisibility(View.VISIBLE);
				}
			}
		});
        /*
         * normal painter button: click to set painter as normal type
         */
        painter_norm_bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				painter_norm_bt.setImageResource(R.drawable.painter_normal1);
				painter_blur_bt.setImageResource(R.drawable.painter_blur);
				painter_trans_bt.setImageResource(R.drawable.painter_trans);
				eraser_bt.setImageResource(R.drawable.eraser);

		        mPaint.setAlpha(0xFF);
		        mPaint.setMaskFilter(null);
		        mPaint.setXfermode(null);
			}
		});
        /*
         * blur painter button: click to set painter as blur type
         */
        painter_blur_bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				painter_norm_bt.setImageResource(R.drawable.painter_normal);
				painter_blur_bt.setImageResource(R.drawable.painter_blur1);
				painter_trans_bt.setImageResource(R.drawable.painter_trans);
				eraser_bt.setImageResource(R.drawable.eraser);
				
				
				if (mPaint.getMaskFilter() != mBlur) {
                    mPaint.setMaskFilter(mBlur);
                    mPaint.setXfermode(null);
                    mPaint.setAlpha(0xFF);
                } 
			}
		});
        /*
         * transparency painter button: click to set painter as transparency type
         */
        painter_trans_bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				painter_norm_bt.setImageResource(R.drawable.painter_normal);
				painter_blur_bt.setImageResource(R.drawable.painter_blur);
				painter_trans_bt.setImageResource(R.drawable.painter_trans1);
				eraser_bt.setImageResource(R.drawable.eraser);
				
				mPaint.setMaskFilter(null);
				mPaint.setXfermode(new PorterDuffXfermode(
                        PorterDuff.Mode.SRC_ATOP));
				mPaint.setAlpha(0x80);
			}
		});
        /*
         * eraser button: click to choose eraser
         */
        eraser_bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				painter_norm_bt.setImageResource(R.drawable.painter_normal);
				painter_blur_bt.setImageResource(R.drawable.painter_blur);
				painter_trans_bt.setImageResource(R.drawable.painter_trans);
				eraser_bt.setImageResource(R.drawable.eraser1);
				
				mPaint.setXfermode(new PorterDuffXfermode(
                        PorterDuff.Mode.CLEAR));
			}
		});
        
        stroke_s_bt=(ImageButton) findViewById(R.id.stroke_s);
        stroke_m_bt=(ImageButton) findViewById(R.id.stroke_m);
        stroke_l_bt=(ImageButton) findViewById(R.id.stroke_l);
        
        /*
         * click to set stroke as fine type
         */
        stroke_s_bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stroke_s_bt.setImageResource(R.drawable.stroke_s1);
				stroke_m_bt.setImageResource(R.drawable.stroke_m);
				stroke_l_bt.setImageResource(R.drawable.stroke_l);
				stroke=6;
				mPaint.setStrokeWidth(stroke);
			}
		});
        /*
         * click to set stroke as medium type
         */
        stroke_m_bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stroke_s_bt.setImageResource(R.drawable.stroke_s);
				stroke_m_bt.setImageResource(R.drawable.stroke_m1);
				stroke_l_bt.setImageResource(R.drawable.stroke_l);
				stroke=10;
				mPaint.setStrokeWidth(stroke);
			}
		});
        /*
         * click to set stroke as bold type
         */
        stroke_l_bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stroke_s_bt.setImageResource(R.drawable.stroke_s);
				stroke_m_bt.setImageResource(R.drawable.stroke_m);
				stroke_l_bt.setImageResource(R.drawable.stroke_l1);
				stroke=14;
				mPaint.setStrokeWidth(stroke);
			}
		});
        /*
         * click to pop up color picker dialog
         */
        current_color=(RelativeLayout) findViewById(R.id.current_color);
        color_picker=(ImageButton) findViewById(R.id.color_picker);
        color_picker.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new ColorPickerDialog(v.getContext(),EditImageActivity.this, mPaint.getColor()).show();
				current_color.setBackgroundColor(mPaint.getColor());
			}
		});

	}
	
	@Override
	public void onBackPressed(){
		AlertDialog.Builder builder_exit = new AlertDialog.Builder(EditImageActivity.this);
		builder_exit.setMessage("Are you sure to exit?")
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
		
		AlertDialog alert_exit = builder_exit.create();
		alert_exit.show();
	}
	
	public void colorChanged(int color) {
        mPaint.setColor(color);
        current_color.setBackgroundColor(mPaint.getColor());
    }
	
	/*
	 * painting canvas class
	 */
	
	/*
	 * Copyright (C) 2007 The Android Open Source Project
	 *
	 * Licensed under the Apache License, Version 2.0 (the "License");
	 * you may not use this file except in compliance with the License.
	 * You may obtain a copy of the License at
	 *
	 *      http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * See the License for the specific language governing permissions and
	 * limitations under the License.
	 */
	public class MyView extends View{
        private Canvas  mCanvas;
        private Path    mPath;
        private Paint   mBitmapPaint;
        
        public MyView(Context c) {
            super(c);

            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            
        }
 
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);        
            
            int imgW=mBackground.getWidth();
            int imgH=mBackground.getHeight();
            
            int[] pixels = new int[w*h];
            for(int j=0;j<h;j++){
            	for(int i=0;i<w;i++){
            		
            		if((i>=offsetX && i<imgW+offsetX) &&
            			(j>=offsetY && j<imgH+offsetY)){
            			pixels[j*w+i]=mBackground.getPixel(i-offsetX, j-offsetY);
            		}
            		else{
            			pixels[j*w+i]=Color.WHITE;
            		}
            	}
            }
            mBitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            
            mCanvas = new Canvas(mBitmap);
        }
        
        
        @Override
        protected void onDraw(Canvas canvas) {
        	canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;
            }
        }
        private void touch_up() {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }
	/*
	private static final int COLOR_MENU_ID = Menu.FIRST;
    private static final int EMBOSS_MENU_ID = Menu.FIRST + 1;
    private static final int BLUR_MENU_ID = Menu.FIRST + 2;
    private static final int ERASE_MENU_ID = Menu.FIRST + 3;
    private static final int SRCATOP_MENU_ID = Menu.FIRST + 4;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, COLOR_MENU_ID, 0, "Color").setShortcut('3', 'c');
        menu.add(0, EMBOSS_MENU_ID, 0, "Emboss").setShortcut('4', 's');
        menu.add(0, BLUR_MENU_ID, 0, "Blur").setShortcut('5', 'z');
        menu.add(0, ERASE_MENU_ID, 0, "Erase").setShortcut('5', 'z');
        menu.add(0, SRCATOP_MENU_ID, 0, "SrcATop").setShortcut('5', 'z');
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xFF);

        switch (item.getItemId()) {
            case COLOR_MENU_ID:
                new ColorPickerDialog(this, this, mPaint.getColor()).show();
                return true;
            case EMBOSS_MENU_ID:
                if (mPaint.getMaskFilter() != mEmboss) {
                    mPaint.setMaskFilter(mEmboss);
                } else {
                    mPaint.setMaskFilter(null);
                }
                return true;
            case BLUR_MENU_ID:
                if (mPaint.getMaskFilter() != mBlur) {
                    mPaint.setMaskFilter(mBlur);
                } else {
                    mPaint.setMaskFilter(null);
                }
                return true;
            case ERASE_MENU_ID:
                mPaint.setXfermode(new PorterDuffXfermode(
                                                        PorterDuff.Mode.CLEAR));
                return true;
            case SRCATOP_MENU_ID:
                mPaint.setXfermode(new PorterDuffXfermode(
                                                    PorterDuff.Mode.SRC_ATOP));
                mPaint.setAlpha(0x80);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */

}
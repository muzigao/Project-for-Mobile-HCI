package com.userinterface;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

public class UserInterfaceActivity extends Activity {
    /** Called when the activity is first created. */
	private Uri mImageCaptureUri;
	private Bitmap captureImage;
	
	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /*
         * gallery button: click to select picture from sd card
         */
        ImageButton gallery_bt=(ImageButton)findViewById(R.id.gallery_bt);
        gallery_bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(Intent.ACTION_PICK,
			               		android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				startActivityForResult(intent, PICK_FROM_FILE); 

			}
		});
        
        /*
         * camera button: click to take picture from camera
         */
        ImageButton camera_bt=(ImageButton)findViewById(R.id.camera_bt);
        camera_bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, PICK_FROM_CAMERA);
			}

		});
 
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	if (resultCode != RESULT_OK) 
    		return;
    	/*
    	 * pass selected picture to ProcessImageActivity, and process via two algorithms
    	 */
    	if(requestCode==PICK_FROM_CAMERA){

    		captureImage=(Bitmap) data.getExtras().get("data");

    		Intent intent=new Intent(UserInterfaceActivity.this,ProcessImageActivity.class);
    		intent.putExtra("image",captureImage);
    		startActivityForResult(intent,0);
    	}
    	else if(requestCode==PICK_FROM_FILE){
    		mImageCaptureUri=data.getData();
    		
    		String[] filePathColumn = {MediaStore.Images.Media.DATA};
    		Cursor cursor = getContentResolver().query(mImageCaptureUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            captureImage=BitmapFactory.decodeFile(filePath);
            

    		Intent intent=new Intent(UserInterfaceActivity.this,ProcessImageActivity.class);
    		intent.putExtra("image", captureImage);
			startActivityForResult(intent,0);
    	}
    }
}
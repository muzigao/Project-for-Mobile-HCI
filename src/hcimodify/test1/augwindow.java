package hcimodify.test1;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
public class augwindow {
	
	public int[][] augwindow(int [][] RGB, int x1, int y1, int x2, int y2){ //input is an RGB image in 3D array format and the top left and bottom right corner of new RGB image made from larger input RGB image 
		int augrows = y2 - y1;
		int augcols = x2 - x1;

		
		int[][] newRGB = new int[augcols][augrows]; //initialize new RGB image in 3D array format
		
		for(int j = 0; j < augrows; j++){
			
			for(int i = 0; i < augcols; i++){
				
				newRGB[i][j]=RGB[x1+i][y1+j];
			//	Color.red(newRGB[i][j]) = Color.red(RGB[x1+i][y1+j]); //transfer values from window in larger RGB input image to smaller RGB output image
			//	Color.green(newRGB[i][j]) = Color.green(RGB[x1+i][y1+j]);
			//	Color.blue(RGB[i][j]) = Color.blue(RGB[x1+i][y1+j]);
				String augrowss = String.valueOf(newRGB[i][j]);
				
				Log.v("augrows", i+"+"+j+"+"+augrowss);
				
			}
		}
		
		return (newRGB);
	}

}

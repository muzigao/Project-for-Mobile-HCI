package hcimodify.test1;


import android.graphics.Bitmap;


public class CreateImgRgb {
	// private LAB[][] LABImage;
	public int width;
	public int height;
	public int[][][] rgb;

	/*
	 * public input(){ this.LABImage=new LAB[0][0]; this.width=0; this.height=0;
	 * }
	 */

	public int getImageWidth() {
		return this.width;
	}

	public int getImageHeight() {
		return this.height;
	}

	public int[] Readrgb(Bitmap img, int x0, int y0) {

		this.width = img.getWidth();
		this.height = img.getHeight();
		int[][] rgb = new int[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {

				int color = img.getPixel(i, j);
				rgb[i][j] = color;
			}
		}
		
		
		int[] windowpoints = new int[4];
		window newwin= new window();
		windowpoints=newwin.window(rgb,x0,y0); 
		

		return windowpoints;   // Results points x1 y1 x2 y2
	}
}



/*
 * public class window {
 * 
 * public int[] window(int [][][] RGB, int x0, int y0){ //input is RGB image in
 * 3D array format and the point on the image that the user touches (x0,y0) int
 * rows = RGB.length; int cols = RGB[0].length; int x1 = x0 - (cols / 20); int
 * y1 = y0 - (rows / 20); //makes initial window using the user input point as
 * the center of the window int x2 = x0 - (cols / 20); int y2 = y0 - (rows /
 * 20); int left = 0, right = 0, top = 0, bottom = 0;
 */
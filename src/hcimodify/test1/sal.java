package hcimodify.test1;

import java.lang.Math;

import android.graphics.Color;
import android.util.Log;

public class sal {

	public double[][] sal(int [][] newRGB){ //input a new RGB image, returns a saliency map of that image
		int salcols = newRGB.length;
		int salrows = newRGB[0].length;
		Log.v ("salrows"+salrows, "salcols"+salcols);
		//Color.red(RGB[i][j]);
		
		double[][] fullhist = new double[1728][6]; //create a 2d array for histogram data for every color quantization
		
		double[][] saliencymap = new double[salcols][salrows]; //initialize saliency map to be returned
		int[][] R = new int[salcols][salrows];
		int[][] G = new int[salcols][salrows]; //extract 2D R, G, and B arrays from 3D RGB array
		int[][] B = new int[salcols][salrows];
		int fullcount = 0;
		int colorcount = 0; //initialize amount of colors
		for(double r = 10.625; r <= 255.0; r=r+21.25){
			for(double g = 10.625; g <= 255.0; g=g+21.25){ //these loops put the R,G, and B values for each color quantization in its column 
				for(double b = 10.625; b <= 255.0; b=b+21.25){
					fullhist[fullcount][0] = r;
					fullhist[fullcount][1] = g;
					fullhist[fullcount][2] = b;
					
					Log.v("salfull", ""+fullhist[fullcount][2]);
					fullcount++;
				}
			}
		}
		for(int j = 0; j < salrows; j++){
			for(int i = 0; i < salcols; i++){ //using ints as the matlab floor function to figure out which color quantization every image pixel is in
				int Rquant = (10 * Color.red(newRGB[i][j])) / 215;
				R[i][j] = Rquant + 1;
				int Gquant = (10 * Color.green(newRGB[i][j])) / 215;
				G[i][j] = Gquant + 1;
				int Bquant = (10 * Color.blue(newRGB[i][j])) / 215;
				B[i][j] = Bquant + 1;
				int countindex = (144 * (R[i][j] - 1)) + (12 * (G[i][j] - 1)) + (B[i][j] - 1);
				double count = (double)(144 * (R[i][j] - 1)) + (12 * (G[i][j] - 1)) + (B[i][j] - 1);
				fullhist[countindex][3]++;
				fullhist[countindex][4] = count; //puts a reference number for each color in its spot in the histogram
				saliencymap[i][j] = count; //puts the same reference number in the pixel's place whose color determined the reference number
		        
			
			}
		}
		
		
		for(int w = 0; w < 1728; w++){
			if(fullhist[w][3] > (1728 * 0.05)){
				colorcount++; //determines the amount of color quantizations that have more than a 5% pixel representation in the image
			}
			else{}
		}
		Log.v("salcolorcount", ""+colorcount);
		double[][] finalhist = new double[colorcount][6]; //creates final histogram array
		int colorplacement = 0;
		int ccn=0;
		for(int p = 0; p < 1728; p++){
			
			if(fullhist[p][3] > (1728.0 * 0.05)){ //places only those color's with more than 5% of imae pixels in new histogram
				finalhist[colorplacement][0] = fullhist[p][0];
				finalhist[colorplacement][1] = fullhist[p][1];
				finalhist[colorplacement][2] = fullhist[p][2];
				finalhist[colorplacement][3] = fullhist[p][3];
				finalhist[colorplacement][4] = fullhist[p][4];
				
				for(int i=0;i<6;i++){
				//Log.v("salfullhist","p"+""+p+"+"+"i"+""+i+"+"+"+"+fullhist[p][i]);
				}
				
				for(int i=0;i<6;i++){
				Log.v("salfinalhist","colorplacement"+""+colorplacement+"+"+"i"+""+i+"+"+"+"+finalhist[colorplacement][i]);
				}
				colorplacement++;
				ccn++;
				Log.v("colorcountnumber",""+ccn);
				
			}
		}
		labconv conv = new labconv(); //LAB conversion for first 3 rows of finalhist
	    double[][] finalhistlab = conv.labconv(finalhist, colorcount);
		
		
		
		salhist sal = new salhist(); //uses salhist to determine saliency values for each color and put them in that color's column in array
	    double[][] finaldata = sal.salhist(finalhistlab, colorcount, salrows, salcols);
	   
	    for(int s = 0;s<colorcount;s++){
	    	Log.v("finaldata", ""+s+"+"+""+finaldata[s][5]);
	    }
	    
	    for(int j = 0; j < salrows; j++){
	    	for(int i = 0; i < salcols; i++){
	    		int found = 0;
	    		for(int d = 0; d < colorcount; d++){ //goes through the array with reference numbers for each pixel and replaces it with saliency value for the color referenced to
	    			if(finaldata[d][4] == saliencymap[i][j]){
	    				saliencymap[i][j] = finaldata[d][5];
	    				found = 1;
	    			}
	    			else{}
	    			
	    		}
	    		if(found == 0){
	    			if(i == 0){
	    				if(j == 0){
	    					saliencymap[i][j] = 0;
	    				}
	    				else{ //if reference number not found because color had less than 5% representation, just assign that pixel the saliency of the adjacent pixel
	    					saliencymap[i][j] = saliencymap[i][j-1];
	    				}
	    			}
	    			else{
	    				saliencymap[i][j] = saliencymap[i-1][j];
	    			}
	    		}
	    	}
	    }
	    //salmap = normed salmap
        int width1 = saliencymap.length;   
        int height1 = saliencymap[0].length;  
 
        double max = saliencymap[0][0];
        double min = saliencymap[0][0];
        for(int j=0;j<height1;j++){
            for(int i=0;i<width1;i++){
                if(saliencymap[i][j] > max){
                    max = saliencymap[i][j];
                }
                else{}
                if(saliencymap[i][j] < min){
                    min = saliencymap[i][j];
                }
                else{}
            }
        }
        for(int j=0;j<height1;j++){
            for(int i=0;i<width1;i++){
                saliencymap[i][j] = ((saliencymap[i][j] - min) / (max - min));
             //   Log.v("smap", ""+i+"+"+""+j+"+"+""+saliencymap[i][j]);
            }
        }
        
	    return (saliencymap);
	
	}
}

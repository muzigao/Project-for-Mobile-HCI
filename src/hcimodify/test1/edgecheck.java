package hcimodify.test1;

import java.lang.Math;
 
public class edgecheck {
	
	public int [] edgecheck(int [][] bin){  //input a binary image
		int edgecols = bin.length;
		int edgerows = bin[0].length;
		int left = 0, right = 0, top = 0, bottom = 0;  //initialize the number of white pixels detected for each edge
		int[] edges = new int[4]; //final array that returns number of pixels detected on each edge
		for(int j = 0; j < edgerows; j++){
			for(int i = 0; i < 10; i++){ //check every pixel in 10 left most columns for white pixels
				if(bin[i][j] == 1){
					left++;
				}
				else{}
				
			}
		}
		for(int j = 0; j < edgerows; j++){
			for(int i = 0; i < 10; i++){ //check every pixel in 10 right most columns for white pixels
				if(bin[i + edgecols - 10][j] == 1){
					right++;
				}
				else{}
				
			}
		}
		for(int j = 0; j < edgecols; j++){
			for(int i = 0; i < 10; i++){ //check every pixel in 10 top most rows for white pixels
				if(bin[j][i] == 1){
					top++;
				}
				else{}
				
			}
		}
		for(int j = 0; j < edgecols; j++){
			for(int i = 0; i < 10; i++){ //check every pixel in 10 bottom most rows for white pixels
				if(bin[j][i+edgerows-10] == 1){
					bottom++;
				}
				else{}
				
			}
		}
		edges[0] = left; //put number of white pixels detected in array to be returned
		edges[1] = right;
		edges[2] = top;
		edges[3] = bottom;
		return (edges);
	}

}

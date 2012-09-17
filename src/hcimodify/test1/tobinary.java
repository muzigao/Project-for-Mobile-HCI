package hcimodify.test1;

import java.lang.Math;

public class tobinary {

	public int[][] tobinary(double [][] saliencymap, double threshold){ //input a saliency map and method returns a binary image in a 2D array
		int bincols = saliencymap.length;
		int binrows = saliencymap[0].length;
		int[][] binary = new int[bincols][binrows]; //initialize binary 2D array
		for(int j = 0; j < binrows; j++){
			for(int i = 0; i < bincols; i++){
				if(saliencymap[i][j] > threshold){ //threshold saliency map to determine values for binary image
					binary[i][j] = 1;	
				}
				else{
					binary[i][j] = 0;
				}
				
			}
		}
		return (binary);
	}
}

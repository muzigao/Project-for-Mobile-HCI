package hcimodify.test1;

import android.util.Log;



public class window {
	
	public int[] window(int [][] RGB, int x0, int y0){ //input is RGB image in 3D array format and the point on the image that the user touches (x0,y0)
		int windowcols = RGB.length;
		int windowrows = RGB[0].length;
		int x1 = x0 - (windowcols / 10);
		int y1 = y0 - (windowrows / 10); //makes initial window using the user input point as the center of the window
		int x2 = x0 + (windowcols / 10);
		int y2 = y0 + (windowrows / 10);
		int left = 1, right = 1, top = 1, bottom = 1; 
		int count=0;
		int countx1=0;
		while(left > 0 && right > 0 && top > 0 && bottom > 0){ //if there are very salient points along every edge, expand every edge of the new RGB image
			x1 = x1 - 10;
		    y1 = y1 - 10;
		    x2 = x2 + 10;
		    y2 = y2 + 10;
		    
		    if(x1 < 0){ //make sure left edge isn't expanded past left edge of original RGB image
		    	x1 = 0;
		    	break;
		    }
		    else{}
		    if(x2 > windowcols-1){
		    	x2 = windowcols-1;
		    	break;
		    }
		    else{}
		    if(y1 < 0){
		    	y1 = 0;
		    	break;
		    }
		    else{}
		    if(y2 > windowrows-1){
		    	y2 = windowrows-1;
		    	break;
		    }
		    else{}
		    
			Log.v ("x1",""+x1);
			Log.v ("y1",""+y1);
			Log.v ("x2",""+x2);
			Log.v ("y2",""+y2);
			
		  
		    augwindow aug1 = new augwindow();
		    int[][] newRGB = aug1.augwindow( RGB, x1, y1, x2, y2 );
		    
		    sal sal0 = new sal();
		    double[][] saliencymap = sal0.sal(newRGB);
		    
		    tobinary bin = new tobinary();
		    int[][] binary = bin.tobinary(saliencymap, 0.5);
		    
		    edgecheck edgecheck0 = new edgecheck();
		    int[] edges = edgecheck0.edgecheck(binary);
		    left = edges[0];
		    right = edges[1];
		    top = edges[2];
		    bottom = edges[3];
		    count++;
		    Log.v("LRTB", ""+left+"+"+""+right+"+"+""+top+"+"+""+bottom);
		    Log.v("count", ""+count);
		}
		
		
		
		
		int left1=0;
		while(left > 0){ //if very salient points along left edge then expand left edge
			x1 = x1 - 10;
		    if(x1 < 0){ //make sure left edge isn't expanded past left edge of original RGB image
		    	x1 = 1;
		    	countx1++;
		    	Log.v("countx1", ""+countx1);
		    	break;
		    }
		    else{}
		    augwindow aug1 = new augwindow(); //use augwindow to get new RGB image from new top left and bottom right points
		    int[][] newRGB = aug1.augwindow( RGB, x1, y1, x2, y2 );
		    
		    sal sal0 = new sal(); //use sal to get saliency map from new RGB image
		    double[][] saliencymap = sal0.sal(newRGB);
		    
		    tobinary bin = new tobinary(); //use tobinary to get binary image from saliency map
		    int[][] binary = bin.tobinary(saliencymap, 0.5);
		    
		    edgecheck edgecheck0 = new edgecheck(); //use edgecheck to check edges of binary images for very salient points
		    int[] edges = edgecheck0.edgecheck(binary);
		    left = edges[0];
		    right = edges[1]; //update edges array with amounts of salient points around each edge
		    top = edges[2];
		    bottom = edges[3];
		    left1++;
		    Log.v("left1",""+left1);
		}
		int right1=0;
		while(right > 0){
			x2 = x2 + 10;
		    if(x2 > windowcols-1){
		    	x2 = windowcols-1;
		    	break;
		    }
		    else{}
		    augwindow aug1 = new augwindow();
		    int[][] newRGB = aug1.augwindow( RGB, x1, y1, x2, y2 );
		    
		    sal sal0 = new sal();
		    double[][] saliencymap = sal0.sal(newRGB);
		    
		    tobinary bin = new tobinary();
		    int[][] binary = bin.tobinary(saliencymap, 0.5);
		    
		    edgecheck edgecheck0 = new edgecheck();
		    int[] edges = edgecheck0.edgecheck(binary);
		    left = edges[0];
		    right = edges[1];
		    top = edges[2];
		    bottom = edges[3];
		    right1++;
		    Log.v("right1",""+right1);
		}
		int top1=0;
		while(top > 0){
			y1 = y1 - 10;
		    if(y1 < 0){
		    	y1 = 1;
		    	break;
		    }
		    else{}
		    augwindow aug1 = new augwindow();
		    int[][] newRGB = aug1.augwindow( RGB, x1, y1, x2, y2 );
		    
		    sal sal0 = new sal();
		    double[][] saliencymap = sal0.sal(newRGB);
		    
		    tobinary bin = new tobinary();
		    int[][] binary = bin.tobinary(saliencymap, 0.5);
		    
		    edgecheck edgecheck0 = new edgecheck();
		    int[] edges = edgecheck0.edgecheck(binary);
		    left = edges[0];
		    right = edges[1];
		    top = edges[2];
		    bottom = edges[3];
		    top1++;
		    Log.v("top1",""+top1);
		}
		int bottom1=0;
		while(bottom > 0){
			y2 = y2 + 10;
		    if(y2 > windowrows-1){
		    	y2 = windowrows-1;
		    	break;
		    }
		    else{}
		    augwindow aug1 = new augwindow();
		    int[][] newRGB = aug1.augwindow( RGB, x1, y1, x2, y2 );
		    
		    sal sal0 = new sal();
		    double[][] saliencymap = sal0.sal(newRGB);
		    
		    tobinary bin = new tobinary();
		    int[][] binary = bin.tobinary(saliencymap, 0.5);
		    
		    edgecheck edgecheck0 = new edgecheck();
		    int[] edges = edgecheck0.edgecheck(binary);
		    left = edges[0];
		    right = edges[1];
		    top = edges[2];
		    bottom = edges[3];
		    bottom1++;
		    Log.v("bottom1",""+bottom1);
		}
		int left2=0;
		
		while((left > 0)&(left2<15)){
			if (left2<20){
			x1 = x1 - 10;
		    if(x1 < 0){
		    	x1 = 1;
		    	break;
		    }
		    else{}
		    augwindow aug1 = new augwindow();
		    int[][] newRGB = aug1.augwindow( RGB, x1, y1, x2, y2 );
		    
		    sal sal0 = new sal();
		    double[][] saliencymap = sal0.sal(newRGB);
		    
		    tobinary bin = new tobinary();
		    int[][] binary = bin.tobinary(saliencymap, 0.5);
		    
		    edgecheck edgecheck0 = new edgecheck();
		    int[] edges = edgecheck0.edgecheck(binary);
		    left = edges[0];
		    right = edges[1];
		    top = edges[2];
		    bottom = edges[3];
		    left2++;
		    Log.v("left2",""+left2);
		    }
			else{break;}
		}
		int right2=0;
		while((right > 0)&(right2<10)){
			x2 = x2 + 10;
		    if(x2 > windowcols-1){
		    	x2 = windowcols-1;
		    	break;
		    }
		    else{}
		    augwindow aug1 = new augwindow();
		    int[][] newRGB = aug1.augwindow( RGB, x1, y1, x2, y2 );
		    
		    sal sal0 = new sal();
		    double[][] saliencymap = sal0.sal(newRGB);
		    
		    tobinary bin = new tobinary();
		    int[][] binary = bin.tobinary(saliencymap, 0.5);
		    
		    edgecheck edgecheck0 = new edgecheck();
		    int[] edges = edgecheck0.edgecheck(binary);
		    left = edges[0];
		    right = edges[1];
		    top = edges[2];
		    bottom = edges[3];
		    right2++;
		    Log.v("right2", ""+right2);
		}   
		int top2=0;
		while((top > 0)&(top2<10)){
			y1 = y1 - 10;
		    if(y1 < 0){
		    	y1 = 1;
		    	break;
		    }
		    else{}
		    augwindow aug1 = new augwindow();
		    int[][] newRGB = aug1.augwindow( RGB, x1, y1, x2, y2 );
		    
		    sal sal0 = new sal();
		    double[][] saliencymap = sal0.sal(newRGB);
		    
		    tobinary bin = new tobinary();
		    int[][] binary = bin.tobinary(saliencymap, 0.5);
		    
		    edgecheck edgecheck0 = new edgecheck();
		    int[] edges = edgecheck0.edgecheck(binary);
		    left = edges[0];
		    right = edges[1];
		    top = edges[2];
		    bottom = edges[3];
		    top2++;
		    Log.v("top2", ""+top2);
		}
		int bottom2=0;
		while((bottom > 0)&(bottom2<10)){
			y2 = y2 + 10;
		    if(y2 > windowrows-1){
		    	y2 = windowrows-1;
		    	break;
		    }
		    else{}
		    augwindow aug1 = new augwindow();
		    int[][] newRGB = aug1.augwindow( RGB, x1, y1, x2, y2 );
		    
		    sal sal0 = new sal();
		    double[][] saliencymap = sal0.sal(newRGB);
		    
		    tobinary bin = new tobinary();
		    int[][] binary = bin.tobinary(saliencymap, 0.5);
		    
		    edgecheck edgecheck0 = new edgecheck();
		    int[] edges = edgecheck0.edgecheck(binary);
		    left = edges[0];
		    right = edges[1];
		    top = edges[2];
		    bottom = edges[3];
		    bottom2++;
		    Log.v("bottom2", ""+bottom2);
		}//*/
		int[] windowpoints = new int[4];
		windowpoints[0] = x1;
		windowpoints[1] = y1; //put final top left and bottom right x,y window points into array
		windowpoints[2] = x2;
		windowpoints[3] = y2;
		
		
		return (windowpoints);
	}

	
}
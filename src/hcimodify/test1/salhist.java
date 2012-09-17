package hcimodify.test1;

import java.lang.Math;

import android.util.Log;

public class salhist {

	public double[][] salhist(double [][] finalhist, int colorcount, int salrows, int salcols){
		for(int i = 0; i < colorcount; i++){
			for(int j = 0; j < colorcount; j++){

				double Ldist = finalhist[i][0] - finalhist[j][0];
				double Adist = finalhist[i][1] - finalhist[j][1];
				double Bdist = finalhist[i][2] - finalhist[j][2];
				double distsquared = Math.pow(Ldist, 2.0) + Math.pow(Adist, 2.0) + Math.pow(Bdist, 2.0);
				Log.v("distsquared",""+Ldist);
				
				finalhist[i][5] = finalhist[i][5] + ((finalhist[j][3] / (salrows*salcols)) * (Math.pow(distsquared, 0.5)));
				Log.v("colorcountt",""+""+i+"+"+""+j);
				
			}
		}
		//Log.v("finalhist-salhist",""+finalhist);
		return (finalhist);
	}
}

package hcimodify.test1;

import java.lang.Math;

import android.util.Log;

public class labconv {
	
	public double labcheck(double check){
		double f = 0.00;
		if(check > 0.008856){
			f = Math.pow(check, (0.3333));
		}
		else{
			f = 7.787 * check + (16.0 / 116.0);
		}
		return(f);
	}
	public double[][] labconv(double [][] finalhist, int colorcount){
		// TODO Auto-generated method stub
		double Yn = 1.00;
		double Xn = 0.9502;
		double Zn = 1.0882;
		
		for(int p = 0;p < colorcount;p++){
			double r = finalhist[p][0] / 255.0;
			double g = finalhist[p][1] / 255.0;
			double b = finalhist[p][2] / 255.0;
			double X = 0.412453 * r + 0.357580 * g + 0.180423 * b;
			double Y = 0.212671 * r + 0.715160 * g + 0.072169 * b;
			double Z = 0.019334 * r + 0.119193 * g + 0.950227 * b;
			double L = 116.0 * labcheck( Y/Yn ) - 16.0;
			double A = 500.0 * (labcheck( X/Xn ) - labcheck( Y/Yn ));
			double B = 200.0 * (labcheck( Y/Yn ) - labcheck( Z/Zn ));
			finalhist[p][0] = L;
			finalhist[p][1] = A;
			finalhist[p][2] = B;
			Log.v("finalhist",""+r);
		}
		
		return(finalhist);
	}	
	
}

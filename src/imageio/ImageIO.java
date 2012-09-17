package imageio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;

public class ImageIO {
	private final static String ALBUM_PATH
		=Environment.getExternalStorageDirectory().toString()+"/HciImages/";
		public ImageIO(){
			
		}
		public void saveImage(Bitmap img, String imgPath) throws IOException{
			File dirFile=new File(ALBUM_PATH);
			if(!dirFile.exists()){
				dirFile.mkdir();
			}
			
			File captureFile=new File(ALBUM_PATH+imgPath);
			//BufferedOutputStream bf=new BufferedOutputStream(new FileOutputStream(captureFile));
			FileOutputStream os=new FileOutputStream(captureFile);
			img.compress(Bitmap.CompressFormat.PNG, 95, os);
			os.flush();
			os.close();
			
			//bf.flush();
			//bf.close();
		}
}

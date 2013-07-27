package com.example.camera;

import android.os.Bundle;
import android.app.Activity;

import android.widget.ImageView;
import android.widget.Button;
import android.view.View;
import android.net.Uri;
import android.content.Intent;
import android.provider.MediaStore;
import java.io.File;
import android.os.Environment;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import android.widget.Toast;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MainActivity extends Activity {

	private static final String TAG = "CallCamera";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;
		
	Uri fileUri = null;
	ImageView photoImage = null;
	  
	private File getOutputPhotoFile() {
		
		  File directory = new File(Environment.getExternalStoragePublicDirectory(
		                Environment.DIRECTORY_PICTURES), getPackageName());
		  
		  if (!directory.exists()) {
		    if (!directory.mkdirs()) {
		      Log.e(TAG, "Failed to create storage directory.");
		      return null;
		    }
		  }
		  
		  String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(new Date());
		  
		  return new File(directory.getPath() + File.separator + "IMG_"  
		                    + timeStamp + ".jpg");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		photoImage = (ImageView) findViewById(R.id.photo_image);
	
		Button callCameraButton = (Button) 
		findViewById(R.id.button_callcamera);
	
		callCameraButton.setOnClickListener( new View.OnClickListener() {
			public void onClick(View view) {
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				fileUri = Uri.fromFile(getOutputPhotoFile());
				i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQ );
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
		    if (resultCode == RESULT_OK) {
		      Uri photoUri = null;
		      if (data == null) {
		        // A known bug here! The image should have saved in fileUri
		        Toast.makeText(this, "Image saved successfully", 
		                       Toast.LENGTH_LONG).show();
		        photoUri = fileUri;
		      } else {
		        photoUri = data.getData();
		        Toast.makeText(this, "Image saved successfully in: " + data.getData(), 
		                       Toast.LENGTH_LONG).show();
		      }
		      showPhoto(photoUri.getPath());
		    } else if (resultCode == RESULT_CANCELED) {
		      Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
		    } else {
		      Toast.makeText(this, "Callout for image capture failed!", 
		                     Toast.LENGTH_LONG).show();
		    }
		  }
	}
	
	private void showPhoto(String photoUri) {
		  File imageFile = new File (photoUri);
		  if (imageFile.exists()){
		     Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
		     BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
		     photoImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
		     photoImage.setImageDrawable(drawable);
		  }       
	}

}
package com.miracle.videotogif;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.ffmpeg.android.Clip;

import com.learnncode.mediachooser.fragment.ImageFragment;
import com.todddavies.components.progressbar.ProgressWheel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

public class ImageViewFragment extends Fragment {

	private View parentView;
	private ImageView imageView;
	private ImageView gifimage;
	private Button deletebtn;
	private Button cancelbtn;
	private GifAnimationDrawable gifdr;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		parentView = inflater.inflate(R.layout.imageview, container, false);

		if (MenuActivity.imageURL != null) {
		byte[] imageRaw = null;
		  try {
		     //URL url = new URL(MenuActivity.imageURL);
		     //HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

		     //InputStream in = new BufferedInputStream(urlConnection.getInputStream());
		     
		     File file = new File(MenuActivity.imageURL);
		     InputStream in = null;
		     in = new BufferedInputStream(new FileInputStream(file));
		     
		     ByteArrayOutputStream out = new ByteArrayOutputStream();

		     int c;
		     while ((c = in.read()) != -1) {
		         out.write(c);
		     }
		     out.flush();

		     imageRaw = out.toByteArray();

		     in.close();
		     out.close();
		  } catch (IOException e) {
		     // TODO Auto-generated catch block
		     e.printStackTrace();
		  }

		  String image64 = Base64.encodeToString(imageRaw, Base64.DEFAULT);

		  String urlStr   = "http://example.com/my.gif";
		  String mimeType = "text/html";
		  String encoding = null;
		  String pageData = "<img src=\"data:image/gif;base64," + image64 + "\" />";

		  WebView wv;
		  wv =  (WebView) parentView
					.findViewById(R.id.webImageView1);
		  wv.loadDataWithBaseURL(urlStr, pageData, mimeType, encoding, urlStr);
		  
		//WebView webImageView1 = (WebView) parentView
		//		.findViewById(R.id.webImageView1);
		//webImageView1.loadData("<html><img src='file:/"+MenuActivity.imageURL+"'/></html>",  "text/html", "UTF-8");
	}
		/*
		gifimage = (ImageView) parentView
				.findViewById(R.id.gifimage);
		if (MenuActivity.imageURL != null) {
			Log.e("MenuActivity.imageURL",MenuActivity.imageURL);
			//gifimage.setMovieResource(MenuActivity.imageURL);
			try {
				gifdr = new GifAnimationDrawable(new File(MenuActivity.imageURL));
				gifdr.setOneShot(false);
				gifimage.setImageDrawable(gifdr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/

		cancelbtn = (Button) parentView.findViewById(R.id.cancelbtn);

		cancelbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				imageView.getHandler().post(new Runnable() {
					public void run() {
						imageView.setVisibility(View.INVISIBLE);
					}
				});
				deletebtn.getHandler().post(new Runnable() {
					public void run() {
						deletebtn.setVisibility(View.INVISIBLE);
					}
				});
				cancelbtn.getHandler().post(new Runnable() {
					public void run() {
						cancelbtn.setVisibility(View.INVISIBLE);
					}
				});

			}
		});

		imageView = (ImageView) parentView.findViewById(R.id.imageMask1);

		deletebtn = (Button) parentView.findViewById(R.id.deletebtn);

		deletebtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (MenuActivity.imageURL != null) {
					File file = new File(MenuActivity.imageURL);
					file.delete();
				}

				imageView.setVisibility(View.INVISIBLE);
				deletebtn.setVisibility(View.INVISIBLE);
				cancelbtn.setVisibility(View.INVISIBLE);
				//return to image list page
				MenuActivity.clip=new Clip();
				MenuActivity.mContext.changeFragment(new ImageFragment(),R.string.image);
			}
		});

		parentView.findViewById(R.id.delete).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						imageView.setVisibility(View.VISIBLE);

						deletebtn.setVisibility(View.VISIBLE);

						cancelbtn.setVisibility(View.VISIBLE);

					}
				});

		// share button listener
		parentView.findViewById(R.id.share).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						Intent shareIntent = new Intent(Intent.ACTION_SEND);
						shareIntent.setType("image/gif");
						Uri uri = Uri.fromFile(new File(
								MenuActivity.imageURL));
						shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
						startActivity(Intent.createChooser(shareIntent,
								getResources().getText(R.string.share)));
					}
				});

		return parentView;

	}

}

package com.miracle.videotogif;

import java.io.File;
import java.io.InputStream;

import org.ffmpeg.android.Clip;

import com.learnncode.mediachooser.fragment.ImageFragment;
import com.todddavies.components.progressbar.ProgressWheel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class ImageViewFragment extends Fragment {

	private View parentView;
	private ImageView imageView;
	private Button deletebtn;
	private Button cancelbtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		parentView = inflater.inflate(R.layout.imageview, container, false);

		GifMovieView gifimage = (GifMovieView) parentView
				.findViewById(R.id.gifimage);
		if (MenuActivity.imageURL != null) {
			Log.e("MenuActivity.imageURL",MenuActivity.imageURL);
			gifimage.setMovieResource(MenuActivity.imageURL);
		}

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
				MenuActivity.mContext.changeFragment(new ImageFragment());
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
						shareIntent.setType("image/*");
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

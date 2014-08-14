package com.miracle.videotogif;

import java.io.File;
import java.io.InputStream;

import com.todddavies.components.progressbar.ProgressWheel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	parentView = inflater.inflate(R.layout.imageview, container, false);
         
    	GifMovieView gifimage = (GifMovieView) parentView.findViewById(R.id.gifimage);
    	if(MenuActivity.clip.outputURL!=null)
    		gifimage.setMovieResource(MenuActivity.clip.outputURL);
    	
    	
    	
    	parentView.findViewById(R.id.delete).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						imageView=(ImageView) parentView.findViewById(R.id.imageMask1);
						imageView.setVisibility(0);
						
						deletebtn=(Button) parentView.findViewById(R.id.deletebtn);
						deletebtn.setVisibility(0);
						
						cancelbtn=(Button) parentView.findViewById(R.id.cancelbtn);
						cancelbtn.setVisibility(0);
						
						cancelbtn.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								imageView.setVisibility(1);
								deletebtn.setVisibility(1);
								cancelbtn.setVisibility(1);
							}
						});
					}
				});

    	
    	//share button listener
		parentView.findViewById(R.id.share).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						 Intent shareIntent = new Intent(Intent.ACTION_SEND);
						 shareIntent.setType("image/*");
						 Uri uri = Uri.fromFile(new File(MenuActivity.clip.outputURL));
						 shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
						 startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share)));  
					}
				});
		
        return  parentView;
        
        
    }

}

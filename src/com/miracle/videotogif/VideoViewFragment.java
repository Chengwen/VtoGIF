package com.miracle.videotogif;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class VideoViewFragment extends Fragment {

    private View parentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	parentView = inflater.inflate(R.layout.videoview, container, false);
         
    	/*GifMovieView gifimage = (GifMovieView) parentView.findViewById(R.id.gifimage);
    	if(MenuActivity.clip.outputURL!=null)
    		gifimage.setMovieResource(MenuActivity.clip.outputURL);

    	
    	//share button listener
		parentView.findViewById(R.id.btn_share).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						 Intent shareIntent = new Intent(Intent.ACTION_SEND);
						 shareIntent.setType("image/*");
						 Uri uri = Uri.fromFile(new File(MenuActivity.clip.outputURL));
						 shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
						 startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share)));  
					}
				});*/
		
        return  parentView;
        
        
    }

}

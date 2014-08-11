package com.miracle.videotogif;

import java.io.InputStream;

import com.todddavies.components.progressbar.ProgressWheel;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageViewFragment extends Fragment {

    private View parentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	parentView = inflater.inflate(R.layout.imageview, container, false);
         
    	GifMovieView gifimage = (GifMovieView) parentView.findViewById(R.id.gifimage);
    	if(MenuActivity.clip.outputURL!=null)
    		gifimage.setMovieResource(MenuActivity.clip.outputURL);

        return  parentView;
        
        
    }

}

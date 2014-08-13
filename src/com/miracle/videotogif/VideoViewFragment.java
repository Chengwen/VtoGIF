package com.miracle.videotogif;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

public class VideoViewFragment extends Fragment {

    private View parentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	parentView = inflater.inflate(R.layout.videoview, container, false);

    	VideoView videoView1 = (VideoView) parentView.findViewById(R.id.videoView1);
    	if(MenuActivity.videoURL!=null)
    	{
    		Log.d("video play",MenuActivity.videoURL);
    		videoView1.setVideoPath(MenuActivity.videoURL);
    		videoView1.start();
    	}
    	
        return  parentView;
        
    }

}

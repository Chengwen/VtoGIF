package com.miracle.videotogif;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoViewFragment extends Fragment {

    private View parentView;
    private VideoView videoView1;
    private MediaController mController ;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	parentView = inflater.inflate(R.layout.videoview, container, false);

    	videoView1 = (VideoView) parentView.findViewById(R.id.videoView1);
    	if(MenuActivity.videoURL!=null)
    	{
    		Log.d("video play",MenuActivity.videoURL);
    		videoView1.setVideoPath(MenuActivity.videoURL);
    		videoView1.start();
    	}

    	MediaController mMediaController = new MediaController(MenuActivity.mContext);
    	//mController = (MediaController)parentView.findViewById(R.id.mediaController1);
    	/*FrameLayout.LayoutParams adParams = new FrameLayout.LayoutParams(
    			FrameLayout.LayoutParams.MATCH_PARENT,
    			FrameLayout.LayoutParams.WRAP_CONTENT);
    			adParams.gravity=Gravity.CENTER; 

    	mMediaController.setLayoutParams(adParams);*/
    	videoView1.setMediaController(mMediaController);
    	
        return  parentView;
        
    }
    /*
    public void onStart() {
        // Play Video
    	videoView1.setVideoURI(mUri);
    	videoView1.start();

        super.onStart();
    }

    public void onPause() {
        // Stop video when the activity is pause.
        mPositionWhenPaused = videoView1.getCurrentPosition();
        videoView1.stopPlayback();

        super.onPause();
    }

    public void onResume() {
        // Resume video player
        if(mPositionWhenPaused >= 0) {
        	videoView1.seekTo(mPositionWhenPaused);
            mPositionWhenPaused = -1;
        }

        super.onResume();
    }

    public boolean onError(MediaPlayer player, int arg1, int arg2) {
        return false;
    }

    public void onCompletion(MediaPlayer mp) {
        this.finish();
    }*/

}

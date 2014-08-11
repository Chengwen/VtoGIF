package com.miracle.videotogif;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

import org.ffmpeg.android.Clip;

import com.todddavies.components.progressbar.ProgressWheel;

public class ProgressFragment extends Fragment {

    private View parentView;

	ProgressWheel pw_spinner;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.progress, container, false);
        

        pw_spinner = (ProgressWheel) parentView.findViewById(R.id.pw_spinner);

        final Runnable r = new Runnable() {
			public void run() {
				int lastPrecent=-1;
				while(MenuActivity.clip.convertStatus<=Clip.CONVERTING) {
					if(lastPrecent!=MenuActivity.clip.convertPrecent)
					{
						lastPrecent=MenuActivity.clip.convertPrecent;
						Log.e("lastPrecent",String.valueOf(lastPrecent));
						pw_spinner.setProgress(lastPrecent);
						
						pw_spinner.incrementProgress();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				//判断出错页。
				Log.e("start ImageViewFragment","start ImageViewFragment");
				MenuActivity.mContext.changeFragment(new ImageViewFragment());
				
			}
        };

		Thread s = new Thread(r);
		s.start();
		
        return parentView;
        
    }


}

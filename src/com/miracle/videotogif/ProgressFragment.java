package com.miracle.videotogif;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todddavies.components.progressbar.ProgressWheel;

public class ProgressFragment extends Fragment {

    private View parentView;

	ProgressWheel pw_spinner;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.progress, container, false);
        

		
        return parentView;
        
    }


}

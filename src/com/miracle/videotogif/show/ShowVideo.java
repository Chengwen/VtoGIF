package com.miracle.videotogif.show;

import java.util.List;

import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.activity.BucketHomeFragmentActivity;
import com.learnncode.mediachooser.activity.HomeFragmentActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import  com.miracle.videotogif.R;

public class ShowVideo extends Activity{
	
	Button fileViewButton;
	GridView gridView;
	MediaGridViewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_video);
		fileViewButton = (Button)findViewById(R.id.fileButton);
		gridView = (GridView)findViewById(R.id.gridView);
		fileViewButton.setOnClickListener(clickListener);

		IntentFilter videoIntentFilter = new IntentFilter(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
		registerReceiver(videoBroadcastReceiver, videoIntentFilter);

		//IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
		//registerReceiver(imageBroadcastReceiver, imageIntentFilter);

	}


	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			//if(view == folderViewButton){
				//MediaChooser.setSelectionLimit(20);
				//Intent intent = new Intent(ShowVideo.this, BucketHomeFragmentActivity.class);
				//startActivity(intent);

			//}else {
				Intent intent = new Intent(ShowVideo.this, HomeFragmentActivity.class);
				startActivity(intent);
				Log.d("1", "ok");
			//}
		}
	};

	BroadcastReceiver videoBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			Toast.makeText(ShowVideo.this, "yippiee Video ", Toast.LENGTH_SHORT).show();
			Toast.makeText(ShowVideo.this, "Video SIZE :" + intent.getStringArrayListExtra("list").size(), Toast.LENGTH_SHORT).show();
			setAdapter(intent.getStringArrayListExtra("list"));
		}
	};


	BroadcastReceiver imageBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(ShowVideo.this, "yippiee Image ", Toast.LENGTH_SHORT).show();
			Toast.makeText(ShowVideo.this, "Image SIZE :" + intent.getStringArrayListExtra("list").size(), Toast.LENGTH_SHORT).show();
			setAdapter(intent.getStringArrayListExtra("list"));
		}
	};

	@Override
	protected void onDestroy() {
		unregisterReceiver(imageBroadcastReceiver);
		unregisterReceiver(videoBroadcastReceiver);
		super.onDestroy();
	}

	private void setAdapter( List<String> filePathList) {
		if(adapter == null){
			adapter = new MediaGridViewAdapter(ShowVideo.this, 0, filePathList);
			gridView.setAdapter(adapter);
		}else{
			adapter.addAll(filePathList);
			adapter.notifyDataSetChanged();
		}
	}

}

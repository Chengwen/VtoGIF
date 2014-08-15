package com.miracle.videotogif;

import org.ffmpeg.android.Clip;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.learnncode.mediachooser.fragment.ImageFragment;
import com.learnncode.mediachooser.fragment.VideoFragment;
import com.miracle.videotogif.ResideMenu.ResideMenu;
import com.miracle.videotogif.ResideMenu.ResideMenuItem;



public class MenuActivity extends FragmentActivity implements View.OnClickListener, ImageFragment.OnImageSelectedListener, 
VideoFragment.OnVideoSelectedListener{

    private ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemVideo;
    private ResideMenuItem itemImages;
    private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;
    public static MenuActivity mContext;
    public static Clip clip=new Clip();
    public static String videoURL=null;
    public static String imageURL=null;
    public static String lastPage=null;
    public static int title=0;
    

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;
        setUpMenu();
        //changeFragment(new VideoFragment());
        changeFragment(new HomeFragment());
    }
    
    @Override
    public void onStart() {
      super.onStart();
      // The rest of your onStart() code.
      EasyTracker.getInstance(this).activityStart(this);  // Add this method.
    }

    @Override
    public void onStop() {
      super.onStop();
       // The rest of your onStop() code.
      EasyTracker.getInstance(this).activityStop(this);  // Add this method.
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        //resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.drawable.icon_home,    R.string.home);
        itemVideo  = new ResideMenuItem(this, R.drawable.icon_video,  R.string.video);
        itemImages  = new ResideMenuItem(this, R.drawable.icon_image,  R.string.image);
        //itemCalendar = new ResideMenuItem(this, R.drawable.icon_calendar, "Progress");
        //itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, "Settings");

        itemHome.setOnClickListener(this);
        itemVideo.setOnClickListener(this);
        itemImages.setOnClickListener(this);
        //itemCalendar.setOnClickListener(this);
        //itemSettings.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemVideo, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemImages, ResideMenu.DIRECTION_LEFT);
        //resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_LEFT);
        //resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);

        // You can disable a direction by setting ->
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        

        // Create an ad.
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-3568858304593155/8968970224");

        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
        layout.addView(adView);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder()
            .addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE")
            .build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
      
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        if (view == itemHome){
            changeFragment(new HomeFragment(),R.string.home);
        }else if (view == itemVideo){
            //changeFragment(new ImageViewFragment());
            changeFragment(new VideoFragment(),R.string.video);
        }else if (view == itemImages){
            //changeFragment(new ImageViewFragment());
            changeFragment(new ImageFragment(),R.string.image);
        }else if (view == itemCalendar){
            changeFragment(new ProgressFragment());
        }else if (view == itemSettings){
            changeFragment(new SettingsFragment());
        }
		

        resideMenu.closeMenu();
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(MenuActivity.mContext.title==R.string.video || MenuActivity.mContext.title==R.string.image )
			{
				changeFragment(new HomeFragment(),R.string.home);
			}
			else if(MenuActivity.mContext.title==R.string.video_details)
			{
				changeFragment(new VideoFragment(),R.string.video);
			}
			else if(MenuActivity.mContext.title==R.string.image_details)
			{
				changeFragment(new ImageFragment(),R.string.image);
			}
			else if(MenuActivity.mContext.title==R.string.home)
			{
				return super.onKeyDown(keyCode, event);
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}


    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    public void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
    
    public void changeFragment(Fragment targetFragment,int title){
    	TextView t=(TextView)MenuActivity.mContext.findViewById(R.id.title_bar);
    	MenuActivity.mContext.title=title;

		t.getHandler().post(new Runnable() {
			public void run() {
		    	TextView t=(TextView)MenuActivity.mContext.findViewById(R.id.title_bar);
				 t.setText(MenuActivity.mContext.title);
			}
		});
		
    	
    	changeFragment( targetFragment);
    }

    // What good method is to access resideMen
    public ResideMenu getResideMenu(){
        return resideMenu;
    }
    

	@Override
	public void onImageSelected(int count) {

		android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
		ImageFragment imageFragment = (ImageFragment) fragmentManager.findFragmentByTag("fragment");

		if(imageFragment.getSelectedImageList() != null && imageFragment.getSelectedImageList() .size() > 0){

			Log.d("selected imageURL",imageFragment.getSelectedImageList().get(0));
			MenuActivity.imageURL=imageFragment.getSelectedImageList().get(0);
			
			changeFragment(new ImageViewFragment(),R.string.image_details);
		}
	}


	@Override
	public void onVideoSelected(int count){

		android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
		VideoFragment videoFragment = (VideoFragment) fragmentManager.findFragmentByTag("fragment");

		if(videoFragment.getSelectedVideoList() != null && videoFragment.getSelectedVideoList() .size() > 0){

			Log.d("selected Videos",videoFragment.getSelectedVideoList().get(0));
			MenuActivity.videoURL=videoFragment.getSelectedVideoList().get(0);
			changeFragment(new VideoViewFragment(),R.string.video_details);
		}
	}
}

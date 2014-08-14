package com.miracle.videotogif;

import org.ffmpeg.android.Clip;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        //resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.drawable.icon_home,     "Home");
        itemVideo  = new ResideMenuItem(this, R.drawable.icon_profile,  "Videos");
        itemImages  = new ResideMenuItem(this, R.drawable.icon_profile,  "Images");
        itemCalendar = new ResideMenuItem(this, R.drawable.icon_calendar, "Progress");
        itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, "Settings");

        itemHome.setOnClickListener(this);
        itemVideo.setOnClickListener(this);
        itemImages.setOnClickListener(this);
        itemCalendar.setOnClickListener(this);
        itemSettings.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemVideo, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemImages, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);

        // You can disable a direction by setting ->
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
    	TextView t=(TextView)findViewById(R.id.title_bar);
        if (view == itemHome){
            changeFragment(new HomeFragment());
            t.setText("HOME");
        }else if (view == itemVideo){
            t.setText("Videos");
            //changeFragment(new ImageViewFragment());
            changeFragment(new VideoFragment());
        }else if (view == itemImages){
            t.setText("Images");
            //changeFragment(new ImageViewFragment());
            changeFragment(new ImageFragment());
        }else if (view == itemCalendar){
            t.setText("Calendar");
            changeFragment(new ProgressFragment());
        }else if (view == itemSettings){
            t.setText("Setting");
            changeFragment(new SettingsFragment());
        }
		

        resideMenu.closeMenu();
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
    
    public void changeFragment(Fragment targetFragment,String title){
    	TextView t=(TextView)findViewById(R.id.title_bar);
    	 t.setText(title);
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
			
			changeFragment(new ImageViewFragment());
		}
	}


	@Override
	public void onVideoSelected(int count){

		android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
		VideoFragment videoFragment = (VideoFragment) fragmentManager.findFragmentByTag("fragment");

		if(videoFragment.getSelectedVideoList() != null && videoFragment.getSelectedVideoList() .size() > 0){

			Log.d("selected Videos",videoFragment.getSelectedVideoList().get(0));
			MenuActivity.videoURL=videoFragment.getSelectedVideoList().get(0);
			changeFragment(new VideoViewFragment());
		}
	}
}

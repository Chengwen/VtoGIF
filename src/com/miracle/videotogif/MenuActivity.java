package com.miracle.videotogif;

import org.ffmpeg.android.Clip;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;
    public static MenuActivity mContext;
    public static Clip clip=new Clip();
    

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;
        setUpMenu();
        changeFragment(new VideoFragment());
        //changeFragment(new HomeFragment());
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.drawable.icon_home,     "Home");
        itemProfile  = new ResideMenuItem(this, R.drawable.icon_profile,  "Profile");
        itemCalendar = new ResideMenuItem(this, R.drawable.icon_calendar, "Progress");
        itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, "Settings");

        itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemCalendar.setOnClickListener(this);
        itemSettings.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
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
        }else if (view == itemProfile){
            t.setText("Videos");
            //changeFragment(new ImageViewFragment());
            changeFragment(new VideoViewFragment());
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

    // What good method is to access resideMen
    public ResideMenu getResideMenu(){
        return resideMenu;
    }
    

	@Override
	public void onImageSelected(int count) {
	/*	if( mTabHost.getTabWidget().getChildAt(1) != null){
			if(count != 0){
				((TextView) mTabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title)).setText(getResources().getString(R.string.images_tab) + "  " + count);

			}else{
				((TextView)mTabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title)).setText(getResources().getString(R.string.image));
			}
		}else {
			if(count != 0){
				((TextView) mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title)).setText(getResources().getString(R.string.images_tab) + "  "  + count);

			}else{
				((TextView)mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title)).setText(getResources().getString(R.string.image));
			}
		}*/
	}


	@Override
	public void onVideoSelected(int count){
		if(count != 0){
		//	((TextView) mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title)).setText(getResources().getString(R.string.videos_tab) + "  "  + count);

		}else{
		//	((TextView)mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title)).setText(getResources().getString(R.string.video));
		}
	}
}

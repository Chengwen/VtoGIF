package com.miracle.videotogif;


import java.io.File;
import java.io.FileDescriptor;
import java.io.RandomAccessFile;

import org.ffmpeg.android.Clip;

import cn.sharesdk.onekeyshare.OnekeyShare;

import com.learnncode.mediachooser.fragment.ImageFragment;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ImageViewFragment extends Fragment {

  private View parentView;
  private ImageView imageView;
  private GifImageView gifimage;
  private Button deletebtn;
  private Button cancelbtn;
  private TextView imageinfo;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    parentView = inflater.inflate(R.layout.imageview, container, false);

    gifimage = (GifImageView) parentView.findViewById(R.id.gifimage);

    imageinfo = (TextView) parentView.findViewById(R.id.imageinfo);
    if (MenuActivity.imageURL != null) {
      Log.e("MenuActivity.imageURL", MenuActivity.imageURL);
      try {

        FileDescriptor fd = new RandomAccessFile(MenuActivity.imageURL, "r").getFD();
        GifDrawable gifFromFd = new GifDrawable(fd);
        gifimage.setImageDrawable(gifFromFd);
        File dF = new File(MenuActivity.imageURL);

        String out =
            String.format("%.3f", ((double) dF.length()) / 1024 / 1024) + " MB - "
                + gifFromFd.getIntrinsicWidth() + "x" + gifFromFd.getIntrinsicHeight();
        imageinfo.setText(out);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    cancelbtn = (Button) parentView.findViewById(R.id.cancelbtn);

    cancelbtn.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub

        imageView.getHandler().post(new Runnable() {
          public void run() {
            imageView.setVisibility(View.INVISIBLE);
          }
        });
        deletebtn.getHandler().post(new Runnable() {
          public void run() {
            deletebtn.setVisibility(View.INVISIBLE);
          }
        });
        cancelbtn.getHandler().post(new Runnable() {
          public void run() {
            cancelbtn.setVisibility(View.INVISIBLE);
          }
        });

      }
    });

    imageView = (ImageView) parentView.findViewById(R.id.imageMask1);

    deletebtn = (Button) parentView.findViewById(R.id.deletebtn);

    deletebtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (MenuActivity.imageURL != null) {
          File file = new File(MenuActivity.imageURL);
          file.delete();
        }

        imageView.setVisibility(View.INVISIBLE);
        deletebtn.setVisibility(View.INVISIBLE);
        cancelbtn.setVisibility(View.INVISIBLE);
        // return to image list page
        MenuActivity.clip = new Clip();
        MenuActivity.mContext.changeFragment(new ImageFragment(), R.string.image);
      }
    });

    parentView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        imageView.setVisibility(View.VISIBLE);
        deletebtn.setVisibility(View.VISIBLE);
        cancelbtn.setVisibility(View.VISIBLE);

      }
    });

    // share button listener
    parentView.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        Toast.makeText(
            MenuActivity.mContext.getApplicationContext(),
            MenuActivity.mContext.getString(R.string.shareTips1) + " Facebook "
                + MenuActivity.mContext.getString(R.string.shareTips2), Toast.LENGTH_LONG).show();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/gif");
        Uri uri = Uri.fromFile(new File(MenuActivity.imageURL));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share)));
      }
    });
    
    
    parentView.findViewById(R.id.share1).setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//showOnekeyshare("facebook", false);
			showOnekeyshare("twitter", false);
		}
	});
    
    return parentView;

  }
  

	public void showOnekeyshare(String platform, boolean silent) {
      OnekeyShare oks = new OnekeyShare();
      
      // 分享时Notification的图标和文字
      oks.setNotification(R.drawable.ic_launcher, 
      MenuActivity.mContext.getString(R.string.app_name));
      // address是接收人地址，仅在信息和邮件使用
      oks.setAddress("12345678901");
      // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
      oks.setTitle( MenuActivity.mContext.getString(R.string.share));
      // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
      oks.setTitleUrl("http://sharesdk.cn");
      // text是分享文本，所有平台都需要这个字段 
      oks.setText( "Share it");
      // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
      oks.setImagePath(MenuActivity.imageURL);
      // imageUrl是图片的网络路径，新浪微博、人人网、QQ空间、
      // 微信的两个平台、Linked-In支持此字段
      //oks.setImageUrl("http://www.computerhope.com/issues/pictures/winpath.jpg");
      // url仅在微信（包括好友和朋友圈）中使用
      oks.setUrl("http://sharesdk.cn");
      // appPath是待分享应用程序的本地路劲，仅在微信中使用
      //oks.setAppPath(MainActivity.TEST_IMAGE); 
      // comment是我对这条分享的评论，仅在人人网和QQ空间使用
      oks.setComment(MenuActivity.mContext.getString(R.string.share));
      // site是分享此内容的网站名称，仅在QQ空间使用
      //oks.setSite(context.getString(R.string.app_name));
      // siteUrl是分享此内容的网站地址，仅在QQ空间使用
      oks.setSiteUrl("http://sharesdk.cn");

      // latitude是维度数据，仅在新浪微博、腾讯微博和Foursquare使用
      oks.setLatitude(23.122619f);
      // longitude是经度数据，仅在新浪微博、腾讯微博和Foursquare使用
      oks.setLongitude(113.372338f);
      // 是否直接分享（true则直接分享）
      oks.setSilent(silent);
      // 指定分享平台，和slient一起使用可以直接分享到指定的平台
      if (platform != null) {
              oks.setPlatform(platform);
      }
      // 去除注释可通过OneKeyShareCallback来捕获快捷分享的处理结果
      // oks.setCallback(new OneKeyShareCallback());
      //通过OneKeyShareCallback来修改不同平台分享的内容
      //oks.setShareContentCustomizeCallback(
      //new ShareContentCustomizeDemo());
      
      oks.show(MenuActivity.mContext);
}

}

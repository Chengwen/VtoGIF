package com.miracle.videotogif;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ffmpeg.android.Clip;
import org.ffmpeg.android.FfmpegController;
import org.ffmpeg.android.ShellUtils;

import com.learnncode.mediachooser.fragment.VideoFragment;
import com.miracle.videotogif.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.todddavies.components.progressbar.ProgressWheel;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoViewFragment extends Fragment {

  private View parentView;
  private VideoView videoView1;
  private MediaController mController;
  private ProgressWheel pw_spinner;
  private double lastStartSec = 0d;
  private double lastEndSec = 0d;
  private TextView textViewStartValue;
  private TextView textViewEndValue;

  private static RangeSeekBar<Double> seekBar;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    parentView = inflater.inflate(R.layout.videoview, container, false);

    videoView1 = (VideoView) parentView.findViewById(R.id.videoView1);

    textViewStartValue = (TextView) parentView.findViewById(R.id.textViewStartValue);
    textViewEndValue = (TextView) parentView.findViewById(R.id.textViewEndValue);

    TextView textViewStart = (TextView) parentView.findViewById(R.id.textViewStart);
    textViewStart.setText(R.string.start);
    textViewStart.setText(textViewStart.getText() + ":");

    TextView textViewEnd = (TextView) parentView.findViewById(R.id.textViewEnd);
    textViewEnd.setText(R.string.end);
    textViewEnd.setText(textViewEnd.getText() + ":");


    if (MenuActivity.videoURL != null) {
      Log.d("video play", MenuActivity.videoURL);
      videoView1.setVideoPath(MenuActivity.videoURL);
      videoView1.start();

      mController = new MediaController(MenuActivity.mContext);
      videoView1.setMediaController(mController);

      MenuActivity.clip = new Clip();
      MenuActivity.clip.path = MenuActivity.videoURL;

      MenuActivity.clip.convertPrecent = 0;
      MenuActivity.clip.convertStatus = Clip.IDLE;
      lastStartSec = 0d;
      lastEndSec = 0d;

      // create RangeSeekBar as Integer range between 20 and 75
      seekBar = new RangeSeekBar<Double>(0d, 100d, MenuActivity.mContext);
      seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Double>() {
        @Override
        public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Double minValue,
            Double maxValue) {
          // handle changed range values
          Log.i("Select", "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);

          if (lastStartSec != minValue) {
            lastStartSec = minValue;
            videoView1.seekTo(Integer.parseInt(String.format("%.0f", lastStartSec * 1000)));
            Log.d("lastStartSec seekto", String.format("%.0f", lastStartSec * 1000));
          }
          if (lastEndSec != maxValue) {
            lastEndSec = maxValue;
            videoView1.seekTo(Integer.parseInt(String.format("%.0f", lastEndSec * 1000)));
            Log.d("lastEndSec seekto", String.format("%.0f", lastEndSec * 1000));
          }
          MenuActivity.clip.startTime = getTimeFromDouble(minValue);
          MenuActivity.clip.duration = maxValue - minValue;
          textViewStartValue.getHandler().post(new Runnable() {
            public void run() {
              textViewStartValue.setText(MenuActivity.clip.startTime);
            }
          });
          textViewEndValue.getHandler().post(new Runnable() {
            public void run() {
              textViewEndValue.setText(getTimeFromDouble(getTimelen(MenuActivity.clip.startTime)
                  + MenuActivity.clip.duration));
            }
          });
        }
      });

      // add RangeSeekBar to pre-defined layout
      LinearLayout layout = (LinearLayout) parentView.findViewById(R.id.moreLinearLayoutseekBar);
      layout.addView(seekBar);

      // Start a thread to convert gif
      new Thread() {
        @Override
        public void run() {

          String realurl = MenuActivity.clip.path;
          String outputPath =
              PreferenceManager.getDefaultSharedPreferences(MenuActivity.mContext).getString(
                  "outputPath",
                  MenuActivity.mContext.getExternalFilesDir(
                      Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath());

          String date =
              new java.text.SimpleDateFormat("yyyy-MM-dd_k-m-s_S").format(new java.util.Date(System
                  .currentTimeMillis()));

          String realoutput = outputPath + "/" + date + ".gif";

          MenuActivity.clip.outputURL = realoutput;

          Log.d("video url", realurl);

          String tmpPath = realoutput + ".tmp";
          Log.d("tmpPath", tmpPath);
          File fileTmp = new File(tmpPath);
          Log.d("status", "start ffmpeg");
          try {
            FfmpegController fc = new FfmpegController(MenuActivity.mContext, fileTmp);
            try {
              // fc.convertToWaveAudio
              Log.d("status", "check convert");
              MenuActivity.clip.convertStatus = Clip.CHECKING;

              fc.getVideoInfo(realurl, new ShellUtils.ShellCallback() {

                @Override
                public void shellOut(String shellLine) {

                  Log.d("shellLine", shellLine);

                  String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
                  String regexVideo = "Video: (.*?), (.*?), (.*?)[,\\s]";
                  String regexRotate = "rotate(.*?): (\\d*)";

                  Pattern p = Pattern.compile(regexDuration);
                  Matcher m = p.matcher(shellLine);
                  if (m.find()) {
                    MenuActivity.clip.length = getTimelen(m.group(1));
                    lastEndSec = MenuActivity.clip.length;
                    seekBar.setAbsoluteMaxValue(MenuActivity.clip.length);

                    textViewStartValue.getHandler().post(new Runnable() {
                      public void run() {
                        mController.show();
                      }
                    });

                    textViewStartValue.getHandler().post(new Runnable() {
                      public void run() {
                        textViewStartValue.setText(getTimeFromDouble(0));
                      }
                    });
                    textViewEndValue.getHandler().post(new Runnable() {
                      public void run() {
                        textViewEndValue.setText(getTimeFromDouble(MenuActivity.clip.length));
                      }
                    });
                    Log.d("find Duration",
                        m.group(1) + " " + String.valueOf(MenuActivity.clip.length));
                  }


                  Pattern p1 = Pattern.compile(regexVideo);
                  Matcher m1 = p1.matcher(shellLine);
                  if (m1.find()) {
                    String strs[] = m1.group(3).split("x");
                    try {
                      MenuActivity.clip.width = Integer.parseInt(strs[0]);
                      MenuActivity.clip.height = Integer.parseInt(strs[1]);
                    } catch (Exception ex) {
                      MenuActivity.clip.width = videoView1.getWidth();
                      MenuActivity.clip.height = videoView1.getHeight();
                    }

                    float ratiowidth = (float) MenuActivity.clip.width / 450.0f;
                    float ratioheight = (float) MenuActivity.clip.height / 338.0f;
                    float ratio = ratiowidth;
                    if (ratiowidth < ratioheight) {
                      ratio = ratioheight;
                    }
                    if (MenuActivity.clip.width < 450 && MenuActivity.clip.height < 338) {
                      MenuActivity.clip.newHeight = MenuActivity.clip.height;
                      MenuActivity.clip.newWidth = MenuActivity.clip.width;
                    } else {
                      MenuActivity.clip.newHeight =
                          (int) ((float) MenuActivity.clip.height / ratio);
                      MenuActivity.clip.newWidth = (int) ((float) MenuActivity.clip.width / ratio);
                    }

                    Log.d(
                        "find Video ",
                        String.valueOf(MenuActivity.clip.width) + " "
                            + String.valueOf(MenuActivity.clip.height) + " new:"
                            + String.valueOf(MenuActivity.clip.newWidth) + " "
                            + String.valueOf(MenuActivity.clip.newHeight));
                  }

                  Pattern p2 = Pattern.compile(regexRotate);
                  Matcher m2 = p2.matcher(shellLine);
                  if (m2.find()) {
                    int rotate = 0;
                    try {
                      rotate = Integer.valueOf(m2.group(2));
                    } catch (Exception ex) {
                      Log.d("rotate Integer.valueOf(m2.group(2)) error", ex.toString());
                    }
                    MenuActivity.clip.rotate = rotate;

                    Log.d(
                        "find Rotate :",
                        rotate + "  MenuActivity.clip.rotate:"
                            + String.valueOf(MenuActivity.clip.rotate));
                  }

                }

                @Override
                public void processComplete(int exitValue) {
                  MenuActivity.clip.convertStatus = Clip.CHECKED;
                  Log.d("get video info", "processComplete");

                }
              });

              Log.d("ConvertToGIF", "check OK ,rotate" + String.valueOf(MenuActivity.clip.rotate));
            } catch (Exception ex) {
              Log.d("Exception error21", ex.toString());
            }

          } catch (Exception ex) {
            Log.d("Exception error12", ex.toString());
          }

        }
      }.start();

    }

    Button convert = (Button) parentView.findViewById(R.id.convert);
    convert.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (MenuActivity.videoURL == null
            || MenuActivity.clip.convertStatus == MenuActivity.clip.CONVERTING) {
          return;
        }

        if (MenuActivity.clip.duration > 60) {
          Toast.makeText(MenuActivity.mContext.getApplicationContext(), R.string.limit60,
              Toast.LENGTH_LONG).show();
          return;
        }

        // show progress wheel
        pw_spinner = (ProgressWheel) parentView.findViewById(R.id.pw_spinner);
        pw_spinner.setVisibility(0);

        ImageView imageView = (ImageView) parentView.findViewById(R.id.imageMask);
        imageView.setVisibility(0);
        videoView1.stopPlayback();

        final Runnable r = new Runnable() {
          public void run() {
            int lastPrecent = -1;
            while (MenuActivity.clip.convertStatus <= Clip.CONVERTING) {
              if (lastPrecent != MenuActivity.clip.convertPrecent) {
                lastPrecent = MenuActivity.clip.convertPrecent;
                Log.e("lastPrecent", String.valueOf(lastPrecent));
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
            if (MenuActivity.clip.convertStatus == Clip.SUCCESSED) {
              Log.e("start ImageViewFragment", "start ImageViewFragment");
              try {
                Thread.sleep(350);
              } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
              MenuActivity.imageURL = MenuActivity.clip.outputURL;
              MenuActivity.mContext.changeFragment(new ImageViewFragment(), R.string.image_details);
            } else {
              MenuActivity.mContext.changeFragment(new VideoFragment(), R.string.video);
              // Toast.makeText(MenuActivity.mContext,
              // R.string.convert_error,
              // Toast.LENGTH_LONG).show();
            }
          }
        };

        Thread s = new Thread(r);
        s.start();

        // Start a thread to convert gif
        new Thread() {
          @Override
          public void run() {

            Log.d("status", "Start a thread to convert gif");

            MenuActivity.clip.convertStatus = Clip.CONVERTING;

            if (MenuActivity.clip.rotate == -1)
              MenuActivity.clip.rotate = 0;

            String tmpPath = MenuActivity.clip.outputURL + ".tmp1";
            File fileTmp = new File(tmpPath);
            try {
              FfmpegController fc = new FfmpegController(MenuActivity.mContext, fileTmp);
              try {
                Log.d("status", "check for convert GIF");
                // wait until rotate read OK
                while (MenuActivity.clip.rotate == -1 || MenuActivity.clip.newHeight < 0) {
                  Log.e("waiting",
                      "MenuActivity.clip.rotate == -1 || MenuActivity.clip.newHeight<0");
                  sleep(50);
                }
                Log.d("ConvertToGIF", "start ,rotate" + String.valueOf(MenuActivity.clip.rotate));
                fc.ConvertToGIF(MenuActivity.clip, 6,

                new ShellUtils.ShellCallback() {

                  @Override
                  public void shellOut(String shellLine) {
                    Log.d("shellLine2", shellLine);
                    String regexDuration = "(.*?)time=(.*?) bitrate(.*?)";
                    Pattern p = Pattern.compile(regexDuration);
                    Matcher m = p.matcher(shellLine);
                    if (m.find()) { // Find each match in
                      // turn; String can't do
                      // this.
                      double currTime = getTimelen(m.group(2));
                      int convertPrecent = 0;
                      if (MenuActivity.clip.duration > 0)
                        convertPrecent = (int) (currTime * 100 / MenuActivity.clip.duration);
                      else
                        convertPrecent = (int) (currTime * 100 / MenuActivity.clip.length);

                      if (convertPrecent >= 100)
                        MenuActivity.clip.convertPrecent = 99;
                      else
                        MenuActivity.clip.convertPrecent = convertPrecent;

                      Log.d("curr Duration", m.group(2) + "  +" + MenuActivity.clip.convertPrecent
                          + "%");
                    }
                  }

                  @Override
                  public void processComplete(int exitValue) {

                    if (exitValue != 0) {
                      Log.d("error", "concat non-zero exit2: " + exitValue);
                      MenuActivity.clip.convertPrecent = 100;
                      MenuActivity.clip.convertStatus = Clip.ERROR;
                    } else {
                      Log.d("success", "success exit1: " + exitValue);

                      try {
                        Thread.sleep(350);
                      } catch (InterruptedException e) {
                        // TODO Auto-generated catch
                        // block
                        e.printStackTrace();
                      }
                      MenuActivity.clip.convertPrecent = 100;
                      MenuActivity.clip.convertStatus = Clip.SUCCESSED;

                    }
                  }
                });
              } catch (Exception ex) {
                MenuActivity.clip.convertPrecent = 100;
                MenuActivity.clip.convertStatus = Clip.ERROR;
                Log.d("Exception error2", ex.toString());
              }

            } catch (Exception ex) {
              MenuActivity.clip.convertPrecent = 100;
              MenuActivity.clip.convertStatus = Clip.ERROR;
              Log.d("Exception error1", ex.toString());
            }

            MenuActivity.clip.convertPrecent = 0;

            Log.d("end", "end");
          }
        }.start();

      }
    });
    return parentView;

  }

  /*
   * public void onStart() { // Play Video videoView1.setVideoURI(mUri); videoView1.start();
   * 
   * super.onStart(); }
   * 
   * public void onPause() { // Stop video when the activity is pause. mPositionWhenPaused =
   * videoView1.getCurrentPosition(); videoView1.stopPlayback();
   * 
   * super.onPause(); }
   * 
   * public void onResume() { // Resume video player if(mPositionWhenPaused >= 0) {
   * videoView1.seekTo(mPositionWhenPaused); mPositionWhenPaused = -1; }
   * 
   * super.onResume(); }
   * 
   * public boolean onError(MediaPlayer player, int arg1, int arg2) { return false; }
   * 
   * public void onCompletion(MediaPlayer mp) { this.finish(); }
   */

  // input format:"00:00:10.68"
  private double getTimelen(String timelen) {
    double min = 0;
    String strs[] = timelen.split(":");
    if (strs[0].compareTo("0") > 0) {
      min += Double.parseDouble(strs[0]) * 60 * 60;
    }
    if (strs[1].compareTo("0") > 0) {
      min += Double.parseDouble(strs[1]) * 60;
    }
    if (strs[2].compareTo("0") > 0) {
      min += Double.parseDouble(strs[2]);
    }
    return min;
  }

  private String getTimeFromDouble(double timelen) {

    String out = "";

    java.text.DecimalFormat df = new java.text.DecimalFormat("00");
    java.text.DecimalFormat df2 = new java.text.DecimalFormat("00.000");

    int hours = (int) timelen / 3600;
    int mintues = (int) (timelen - hours * 3600) / 60;
    double sec = timelen % 60d;
    out = df.format(hours) + ":" + df.format(mintues) + ":" + df2.format(sec);
    return out;
  }

  public String getRealPathFromURI(Context context, Uri contentUri) {
    Cursor cursor = null;
    try {
      String[] proj = {MediaStore.Images.Media.DATA};
      cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
      int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      cursor.moveToFirst();
      return cursor.getString(column_index);
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }
}

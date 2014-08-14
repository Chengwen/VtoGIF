package com.miracle.videotogif;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ffmpeg.android.Clip;
import org.ffmpeg.android.FfmpegController;
import org.ffmpeg.android.ShellUtils;

import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.activity.BucketHomeFragmentActivity;
import com.todddavies.components.progressbar.ProgressWheel;

import android.content.Context;
import android.content.Intent;
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
import android.widget.VideoView;

public class VideoViewFragment extends Fragment {

    private View parentView;
    private VideoView videoView1;
    private MediaController mController ;
    private ProgressWheel pw_spinner;
    
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
    	
    	
    	Button convert = (Button) parentView.findViewById(R.id.convert);
    	convert.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						if(MenuActivity.videoURL==null)
							return;
						
						
						MenuActivity.clip = new Clip();
						MenuActivity.clip.path =MenuActivity.videoURL;

						MenuActivity.clip.convertPrecent = 0;
						MenuActivity.clip.convertStatus = Clip.IDLE;

						//show progress wheel
						pw_spinner = (ProgressWheel) parentView.findViewById(R.id.pw_spinner);
						pw_spinner.setVisibility(0);
						ImageView imageView=(ImageView) parentView.findViewById(R.id.imageMask);
						imageView.setVisibility(0);
						videoView1.stopPlayback();
						
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
						
						
						// Start a thread to convert gif
						new Thread() {
							@Override
							public void run() {

								String realurl = MenuActivity.clip.path;
								String outputPath = PreferenceManager
										.getDefaultSharedPreferences(
												MenuActivity.mContext)
										.getString(
												"outputPath",
												MenuActivity.mContext
														.getExternalFilesDir(
																Environment
																		.getDataDirectory()
																		.getAbsolutePath())
														.getAbsolutePath());

								String date = new java.text.SimpleDateFormat(
										"yyyy-MM-dd_k-m-s_S")
										.format(new java.util.Date(System
												.currentTimeMillis()));

								String realoutput = outputPath + "/" + date + ".gif";

								MenuActivity.clip.outputURL = realoutput;

								Log.d("video url", realurl);

								String tmpPath = realoutput + ".tmp";
								Log.d("tmpPath", tmpPath);
								File fileTmp = new File(tmpPath);
								Log.d("status", "start ffmpeg");
								try {
									FfmpegController fc = new FfmpegController(
											MenuActivity.mContext, fileTmp);
									try {
										// fc.convertToWaveAudio
										Log.d("status", "begin convert");
										MenuActivity.clip.convertStatus = Clip.CONVERTING;

										fc.getVideoInfo(realurl,
												new ShellUtils.ShellCallback() {

													@Override
													public void shellOut(
															String shellLine) {

														// Log.d("shellLine",
														// shellLine);

														String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
														String regexVideo = "Video: (.*?), (.*?), (.*?)[,\\s]";
														String regexRotate = "rotate(.*?): (\\d*)";

														Pattern p = Pattern
																.compile(regexDuration);
														Matcher m = p
																.matcher(shellLine);
														if (m.find()) { // Find each
																		// match in
																		// turn; String
																		// can't do
																		// this.
															MenuActivity.clip.length = getTimelen(m
																	.group(1));
															Log.d("find Duration",
																	m.group(1)
																			+ " "
																			+ String.valueOf(MenuActivity.clip.length)); 												}

														Pattern p1 = Pattern
																.compile(regexVideo);
														Matcher m1 = p1
																.matcher(shellLine);
														if (m1.find()) { 
															String strs[] = m1.group(3)
																	.split("x");
															MenuActivity.clip.width = Integer
																	.parseInt(strs[0]);
															MenuActivity.clip.height = Integer
																	.parseInt(strs[1]);

															float ratiowidth = (float) MenuActivity.clip.width / 640.0f;
															float ratioheight = (float) MenuActivity.clip.height / 480.0f;
															float ratio = ratiowidth;
															if (ratiowidth < ratioheight) {
																ratio = ratioheight;
															}

															MenuActivity.clip.newHeight = (int) ((float) MenuActivity.clip.height / ratio);
															MenuActivity.clip.newWidth = (int) ((float) MenuActivity.clip.width / ratio);

															Log.d("find Video ",
																	String.valueOf(MenuActivity.clip.width)
																			+ " "
																			+ String.valueOf(MenuActivity.clip.height)
																			+ " new:"
																			+ String.valueOf(MenuActivity.clip.newWidth)
																			+ " "
																			+ String.valueOf(MenuActivity.clip.newHeight)); 
														}

														Pattern p2 = Pattern
																.compile(regexRotate);
														Matcher m2 = p2
																.matcher(shellLine);
														// Log.d("shellLine",shellLine);
														if (m2.find()) {
															int rotate = 0;
															try {
																rotate = Integer.valueOf(m2
																		.group(2));
															} catch (Exception ex) {
																Log.d("rotate Integer.valueOf(m2.group(2)) error",
																		ex.toString());
															}
															MenuActivity.clip.rotate = rotate;

															Log.d("find Rotate :",
																	rotate
																			+ "  MenuActivity.clip.rotate:"
																			+ String.valueOf(MenuActivity.clip.rotate));
														}

													}

													@Override
													public void processComplete(
															int exitValue) {

														if (MenuActivity.clip.rotate == -1)
															MenuActivity.clip.rotate = 0;
														Log.d("get video info",
																"processComplete");

													}
												});
										Log.d("ConvertToGIF", "start");
										// wait until rotate read OK
										while (MenuActivity.clip.rotate == -1) {
											Log.e("waiting",
													"MenuActivity.clip.rotate==-1");
											sleep(10);
										}
										fc.ConvertToGIF(MenuActivity.clip, 10,

										new ShellUtils.ShellCallback() {

											@Override
											public void shellOut(String shellLine) {
												// Log.d("shellLine2",shellLine);
												String regexDuration = "(.*?)time=(.*?) bitrate(.*?)";
												Pattern p = Pattern
														.compile(regexDuration);
												Matcher m = p.matcher(shellLine);
												if (m.find()) { // Find each match in
																// turn; String can't do
																// this.
													double currTime = getTimelen(m
															.group(2));
													MenuActivity.clip.convertPrecent = (int)(currTime
															* 100
															/  MenuActivity.clip.length);
													if (MenuActivity.clip.convertPrecent == 100)
														MenuActivity.clip.convertPrecent = 99;

													Log.d("curr Duration",
															m.group(2)
																	+ "  +"
																	+ MenuActivity.clip.convertPrecent
																	+ "%");
												}
											}

											@Override
											public void processComplete(int exitValue) {

												if (exitValue != 0) {
													Log.d("error",
															"concat non-zero exit2: "
																	+ exitValue);
													MenuActivity.clip.convertPrecent = 100;
													MenuActivity.clip.convertStatus = Clip.ERROR;
												} else {
													Log.d("success", "success exit1: "
															+ exitValue);
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

	//input format:"00:00:10.68"
	private double getTimelen(String timelen) {
		double min = 0;
		String strs[] = timelen.split(":");
		if (strs[0].compareTo("0") > 0) {
			min += Double.parseDouble(strs[0]) * 60 * 60;
		}
		if (strs[1].compareTo("0") > 0) {
			min +=  Double.parseDouble(strs[1]) * 60;
		}
		if (strs[2].compareTo("0") > 0) {
			min += Double.parseDouble(strs[2]);
		}
		return min;
	}

	public String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
}

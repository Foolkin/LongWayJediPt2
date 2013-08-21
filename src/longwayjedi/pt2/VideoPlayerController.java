package longwayjedi.pt2;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class VideoPlayerController extends FrameLayout
{
	
	private static final String TAG = "VideoPlayerController";
	
	private MediaPlayer			 	player;
	private Context 				activity;
	private ViewGroup				videoSurface;
	private View	 				controlPanel;
	
	private ImageButton				buttonPause;
	private ImageButton				buttonMute;
	private ImageButton				buttonLoop;
	private SeekBar 				volumeLevel;
	
	private OnClickListener			pauseListener;
	private OnClickListener			muteListener;
	private OnClickListener			loopListener;
	private OnSeekBarChangeListener	volumeLvlListener;
	
	private boolean 				showing;
	private boolean 				mute;
	private boolean					looped;
	private float 					volume;
	
	private static final int		FADE_OUT = 1;
	private static final int		DEFAULT_TIMEOUT = 3000;
	
	private Handler					handler = new MessageHandler(this);
	
	public VideoPlayerController(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		
		controlPanel = null;
		activity = context;
		
		setPauseListener();
		setMuteListener();
		setLoopListener();
		setVolumeListener();
		
		Log.i(TAG, TAG);
	}
	
	public VideoPlayerController(Context context)
	{
		super(context);
		activity = context;
		
		setPauseListener();
		setMuteListener();
		setLoopListener();
		setVolumeListener();
		
		mute = false;
		looped = true;
		volume = 1f;
		
		Log.i(TAG, TAG);
	}
	
	public void setMediaPlayer(MediaPlayer player)
	{
		this.player = player;
		updatePausePlay();
		updateMute();
		updateLoop();
	}
	
	public void setVideoSurface(ViewGroup view)
	{
		videoSurface = view;
		
		FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.MATCH_PARENT
		);
		
		removeAllViews();
		
		View v = makeControllerView();
		addView(v, frameParams);
	}
	
	public View makeControllerView()
	{
		LayoutInflater inflate = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		controlPanel = inflate.inflate(R.layout.video_player_controller, null);
		
		initControllerView(controlPanel);
		
		return controlPanel;
	}
	
	public void initControllerView(View v)
	{
		buttonPause = (ImageButton) v.findViewById(R.id.pause);
		if (buttonPause != null)
		{
			buttonPause.requestFocus();
			buttonPause.setOnClickListener(pauseListener);
		}
		
		buttonMute = (ImageButton) v.findViewById(R.id.mute);
		if (buttonMute != null)
		{
			buttonMute.requestFocus();
			buttonMute.setOnClickListener(muteListener);
		}
		
		buttonLoop = (ImageButton) v.findViewById(R.id.loop);
		if (buttonLoop != null)
		{
			buttonLoop.requestFocus();
			buttonLoop.setOnClickListener(loopListener);
		}
		
		volumeLevel = (SeekBar) v.findViewById(R.id.volume_controller);
		if (volumeLevel != null)
		{
			volumeLevel.setOnSeekBarChangeListener(volumeLvlListener);
			volumeLevel.setMax(100);
			volumeLevel.setProgress(100);
		}
	}
	
	public void show(){
		show(DEFAULT_TIMEOUT);
	}
	
	public void show(int timeout)
	{
		if (!showing && videoSurface != null)
		{
			if (buttonPause != null){
				buttonPause.requestFocus();
			}
			
			disableUnsupportedButtons(); //if will be more buttons, it it will be implemented :)
			
			FrameLayout.LayoutParams controllerLayoutParams = new FrameLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 
					ViewGroup.LayoutParams.MATCH_PARENT
			);
			
			videoSurface.addView(this, controllerLayoutParams);
			showing = true;
		}
		
		updatePausePlay();
		updateMute();
		updateLoop();
		
		Message msg = handler.obtainMessage(FADE_OUT);
		if (timeout != 0) {
			handler.removeMessages(FADE_OUT);
			handler.sendMessageDelayed(msg, timeout);
		}
	}
	
	
	public boolean isShowing(){
		return showing;
	}
	
	public void hide()
	{
		if (videoSurface == null){
			return;
		}
		
		try {
			videoSurface.removeView(this);
		} catch (IllegalArgumentException e) {
			Log.w("VideoController", "already removed");
		}
		
		showing = false;
	}
	
	
	public void disableUnsupportedButtons()
	{
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		show(DEFAULT_TIMEOUT);
		return true;
	}
	
	private void doPauseResume()
	{
		if (player == null) {
			return;
		}
		
		if (player.isPlaying()) {
			player.pause();
		} else {
			player.start();
		}
		
		updatePausePlay();
	}
	
	private void updatePausePlay() 
	{
		if (controlPanel == null || buttonPause == null || player == null) {
			return;
		}
		
		if (player.isPlaying())
			buttonPause.setImageResource(R.drawable.pause_button);
		else 
			buttonPause.setImageResource(R.drawable.play_button);
	}
	
	private void doMuteUnmute() 
	{
		if (player == null) {
			return;
		}
		
		if (!mute)
		{
			player.setVolume(0f, 0f);
			mute = true;
		} else {
			player.setVolume(volume, volume);
			mute = false;
		}
		
		updateMute();
	}
	
	private void updateMute() {
		if (controlPanel == null || buttonMute == null || player == null) {
			return;
		}
		
		if (mute)
			buttonMute.setImageResource(R.drawable.mute);
		else
			buttonMute.setImageResource(R.drawable.unmute);
	}
	
	private void doLoopUnloop() {
		if (player == null) {
			return;
		}
		
		if (looped)
		{
			player.setLooping(looped);
			looped = false;
		} else {
			player.setLooping(looped);
			looped = true;
		}
		
		updateLoop();
	}
	
	private void updateLoop() {
		if (controlPanel == null || buttonLoop == null || player == null){
			return;
		}
		
		if (looped)
			buttonLoop.setImageResource(R.drawable.loop_arrow);
		else 
			buttonLoop.setImageResource(R.drawable.arrow);
	}
	
	private void setPauseListener(){
		pauseListener = new OnClickListener() {
			public void onClick(View v) {
				doPauseResume();
				show(DEFAULT_TIMEOUT);
			}
		};
	}
	
	private void setMuteListener() {
		muteListener = new OnClickListener() {
			public void onClick(View v) {
				doMuteUnmute();
				show(DEFAULT_TIMEOUT);
			}
		};
	}
	
	private void setLoopListener() {
		loopListener = new OnClickListener() {
			public void onClick(View v) {
				doLoopUnloop();
				show(DEFAULT_TIMEOUT);
			}
		};
	}
	
	private void setVolumeListener() {
		volumeLvlListener = new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
				updatePausePlay();
				show(DEFAULT_TIMEOUT);
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
				show(3600000);
			}
			
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (player == null) {
					return;
				}
				
				int position = volumeLevel.getProgress();
				volume = (float) position / 100;
				player.setVolume(volume, volume);
			}
		
		}; 
	}
	
	private static class MessageHandler extends Handler
	{
		private final WeakReference<VideoPlayerController> mView;
		
		MessageHandler(VideoPlayerController view){
			mView = new WeakReference<VideoPlayerController>(view);
		}
		
		public void handleMessage(Message msg) {
			VideoPlayerController view = mView.get(); 
			if (view.player == null) {
				return;
			}
			
			if (msg.what == FADE_OUT) 
				view.hide();
		}
	}
}
package longwayjedi.pt2;

import java.io.IOException;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

public class VideoPlayerActivity extends Activity implements 
SurfaceHolder.Callback, MediaPlayer.OnPreparedListener {

	SurfaceView videoSurface;
	MediaPlayer player;
	VideoPlayerController controller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_player_activity);
		
		videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
		SurfaceHolder videoHolder = videoSurface.getHolder();
		videoHolder.addCallback(this);
		
		player = new MediaPlayer();
		controller = new VideoPlayerController(this);
		
		try {
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.setDataSource(this, Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4#sthash.Ukv8aBbB.dpuf"));
			player.setOnPreparedListener(this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		controller.show();
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video_player, menu);
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		player.setDisplay(holder);
		player.prepareAsync();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		player.stop();
	}
	
	@Override
	public void onPrepared(MediaPlayer player)
	{
		controller.setMediaPlayer(this.player);
		controller.setVideoSurface((FrameLayout) findViewById(R.id.videoSurfaceContainer));
		player.start();
	}

}

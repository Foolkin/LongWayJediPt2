package longwayjedi.pt2;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends Activity {
	
VideoView videoView;
	
//	String url = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*videoView = (VideoView)findViewById(R.id.VideoView);
		videoView.setVideoURI(Uri.parse(url));
		videoView.setMediaController(new MediaController(this));
		videoView.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				videoView.start();
			}
		});
		videoView.requestFocus();
		videoView.start();*/
		
		startActivity(new Intent(this, VideoPlayerActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

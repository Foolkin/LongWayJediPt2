package longwayjedi.pt2;

import java.io.IOException;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends Activity {
	
	VideoView screen;
	
	String urlYT = "rtsp://v5.cache8.c.youtube.com/CiILENy73wIaGQlv7bmyjXli4RMYDSANFEgGUgZ2aWRlb3MM/0/0/0/video.3gp";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	protected void playVideo() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException  
	{
		screen = (VideoView)findViewById(R.id.screen);
		screen.setVideoURI(Uri.parse(urlYT));
		screen.setMediaController(new MediaController(this));
		screen.requestFocus();
		screen.start();
		
	}
	
	public void playClick(View view) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
	{
		playVideo();
	}

}

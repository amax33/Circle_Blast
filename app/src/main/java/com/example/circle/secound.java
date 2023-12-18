package com.example.circle;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.MediaController;
import android.widget.VideoView;

public class secound extends Activity {
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_secound);
        VideoView videoView =(VideoView)findViewById(R.id.videoView);
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        //videoView.setMediaController(mediaController);
        Uri localUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.mylogo);
        videoView.setVideoURI(localUri);
        videoView.start();
        int SPLASH_DISPLAY_LENGTH = 4500;
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(secound.this,MainActivity.class);
                secound.this.startActivity(mainIntent);
                secound.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
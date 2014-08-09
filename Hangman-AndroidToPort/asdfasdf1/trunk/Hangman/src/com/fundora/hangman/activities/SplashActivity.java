package com.fundora.hangman.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.fundora.hangman.R;

public class SplashActivity extends Activity {

	MediaPlayer mpSplash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splashscreen);

		mpSplash = MediaPlayer.create(this, R.raw.splash);
		mpSplash.start();

		Thread logoTimer = new Thread() {
			public void run() {
				try {
					int logoTimer = 0;
					while (logoTimer < 5000) {
						sleep(100);
						logoTimer = logoTimer + 100;
					}
					Intent intent = new Intent(SplashActivity.this, MainMenuActivity.class);
					startActivity(intent);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					finish();
				}
			}
		};
		logoTimer.start();
	}

	@Override
	protected void onDestroy() {
		mpSplash.release();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		mpSplash.pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mpSplash.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
}
